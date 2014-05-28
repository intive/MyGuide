
package com.blstream.myguide;

import android.app.ProgressDialog;
import android.location.Location;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blstream.myguide.fragments.FragmentHelper;
import com.blstream.myguide.gps.LocationUpdater;
import com.blstream.myguide.gps.LocationUser;
import com.blstream.myguide.path.Graph;
import com.blstream.myguide.settings.Settings;
import com.blstream.myguide.zoolocations.Animal;
import com.blstream.myguide.zoolocations.AnimalDistance;
import com.blstream.myguide.zoolocations.Node;
import com.blstream.myguide.zoolocations.Way;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Fragment implementing a view of animal's position and current user's location
 * and a path between them. It uses {@link com.blstream.myguide.path.Graph} for
 * finding the shortest path. This event is trigger by a location update (but
 * with limited interval).
 * 
 * @author Rafal
 */
// TODO: Robotium tests (moving through tabs and back to the main screen)
public class AnimalDetailsMapFragment extends Fragment implements Parcelable {

	private static final String LOG_TAG = AnimalDetailsMapFragment.class.getSimpleName();

	private static final float SHORTEST_PATH_WIDTH = 8.5f;
	private static final float PATH_WIDTH = 7.5f;
	private static final int PATH_ZINDEX = 3;

	// taken from SightseeingFragment
	private static final float DEFAULT_MIN_ZOOM = 14.5f;
	private static final double DEFAULT_START_LAT = 51.1050406;
	private static final double DEFAULT_START_LON = 17.074053;

	private View mRootView;
	private SupportMapFragment mMapFragment;
	private ProgressDialog mProgressDialog;
	private GoogleMap mMap;

	private Animal mAnimal;
	private ArrayList<Animal> mAnimalsOnMap;
	private HashMap<String, Animal> mMarkerIDsAnimals;
	private Graph mGraph;

	private LocationObserver mLocationObserver;
	private boolean mLocationServiceBounded = false;
	
	private boolean markClosestAnimals = false;

	// this part handles Location Service updates
	// Fragment itself may implement this interface but it's a nicely separated
	// piece of code
	private static class LocationObserver
			implements LocationUser {

		// minimal interval (in milliseconds) for Location updates
		// must not be set too low or too high
		private static final long MINIMAL_IDLE_TIME = TimeUnit.SECONDS.toMillis(10);

		private long mLastUpdateTime = 0;
		private boolean mFirstUpdate = true;

		private AnimalDetailsMapFragment mFragment;

		public LocationObserver(AnimalDetailsMapFragment fragment) {
			mFragment = fragment;
		}

		protected void onUpdateAccepted(Location location) {
			LatLng animalLoc = new LatLng(mFragment.mAnimal.getNode().getLatitude(),
					mFragment.mAnimal.getNode().getLongitude());
			// LatLng sourceLoc = new LatLng(51.1046625, 17.0771680); // good
			// spot for testing purposes
			LatLng sourceLoc = new LatLng(location.getLatitude(), location.getLongitude());

			mFragment.mMap.clear();
			mFragment.drawAllWays(); //Logic of drawing has been changed to handle close animals
			mFragment.markAnimals(location);
			LatLngBounds bounds = mFragment.findAndDrawShortestPath(animalLoc, sourceLoc).build();
			if (mFirstUpdate) {
				mFragment.mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 16));
			}
		}

		@Override
		public void onLocationUpdate(Location location) {
			mFragment.dismissProgressDialog();

			// since every update causes path finding it may be a good idea to
			// limit
			// how often should the location be updated; set interval should be
			// a little more
			// than time needed to find the shortest path
			long time = TimeUnit.NANOSECONDS.toMillis(System.nanoTime());
			if (mFirstUpdate || time - mLastUpdateTime > MINIMAL_IDLE_TIME) {
				Log.d(LOG_TAG, "location update accepted");
				onUpdateAccepted(location);

				mLastUpdateTime = time;
				if (mFirstUpdate) mFirstUpdate = false;
			}
		}

		@Override
		public void onGpsAvailable() {
			Log.d(LOG_TAG, "onGpsAvailable");
		}

		@Override
		public void onGpsUnavailable() {
			Log.d(LOG_TAG, "onGpsUnavailable");
			mFragment.dismissProgressDialog();
		}

	}

	public static AnimalDetailsMapFragment newInstance(Animal animal) {
		Bundle bundle = new Bundle();
		bundle.putSerializable(BundleConstants.SELECTED_ANIMAL, animal);

		AnimalDetailsMapFragment f = new AnimalDetailsMapFragment();
		f.setArguments(bundle);

		return f;
	}

	private void parseArguments() {
		Bundle arguments = getArguments();
		if (arguments == null) {
			Log.w(LOG_TAG, "No arguments given");
			return;
		}

		mAnimal = (Animal) arguments.getSerializable(BundleConstants.SELECTED_ANIMAL);
		
		/* Added by Agnieszka 13-05-2014 to handle new boolean. */ 
		markClosestAnimals = arguments
				.containsKey(BundleConstants.SHOW_CLOSE_ANIMALS_ON_MAP) ? arguments.getBoolean(
						BundleConstants.SHOW_CLOSE_ANIMALS_ON_MAP) : false;
		if (mAnimal == null)
			Log.w(LOG_TAG, "NULL is not an Animal");
	}

	private void bindLocationService() {
		if (mLocationObserver == null) throw new IllegalStateException("LocationObserver not ready");
		if (mLocationServiceBounded) throw new IllegalStateException(
				"LocationObserver already bounded");

		LocationUpdater.getInstance().startUpdating(mLocationObserver);
		mLocationServiceBounded = true;
	}

	private void unbindLocationService() {
		if (mLocationObserver == null) throw new IllegalStateException("LocationObserver not ready");
		if (!mLocationServiceBounded) throw new IllegalStateException(
				"LocationObserver not bounded");

		LocationUpdater.getInstance().stopUpdating(mLocationObserver);
		mLocationServiceBounded = false;
	}

	/**
	 * Added by Agnieszka 13-05-2014.
	 * Marks animal based on it's position and 
	 * saves pair (Marker.ID, Animal) to HashMap.
	 * @param animal Animal to be marked
	 */
	private void markPosition(Animal animal) {
		LatLng position = new LatLng(animal.getNode().getLatitude(), animal
				.getNode().getLongitude());
		
		Marker marker = mMap.addMarker(new MarkerOptions()
				.position(position)
				.title(animal.getName())
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.ic_animal)));
		mMarkerIDsAnimals.put(marker.getId(), animal);
	}

	private void drawAllWays() {
		// check whether paths should be drawn
		MyGuideApp app = ((MyGuideApp) getActivity().getApplication());
		boolean visible = app.getSettings().getValueAsBoolean(Settings.KEY_PATHS_VISIBLE);

		// draw paths
		for (Way way : app.getZooData().getWays()) {
			PolylineOptions plo = new PolylineOptions()
					.width(PATH_WIDTH)
					.color(getResources().getColor(R.color.paths));
			for (Node node : way.getNodes()) {
				plo.add(new LatLng(node.getLatitude(), node.getLongitude()));
			}
			mMap.addPolyline(plo).setVisible(visible);
		}
	}
	
	/**
	 * Added by Agnieszka for marking close animals. Reads user's position,
	 * checks closest Animals using {@link AnimalFinderHelper} and marks them on
	 * the map using
	 * {@link AnimalDetailsMapFragment#markPosition(Animal)}
	 * 
	 * @param location
	 *            Location of application user
	 */
	private void markAnimals(Location location) {
		mAnimalsOnMap = new ArrayList<Animal>();
		mMarkerIDsAnimals = new HashMap<String, Animal>();
		mAnimalsOnMap.add(mAnimal);

		if (markClosestAnimals) {
			AnimalFinderHelper animalFinder = new AnimalFinderHelper(location,
					(MyGuideApp) this.getActivity().getApplication(), this
							.getActivity().getApplicationContext());
			ArrayList<AnimalDistance> allAnimals = animalFinder
					.allAnimalsWithDistances();

			for (int i = 0; i < 4; i++) {
				Animal current = allAnimals.get(i).getAnimal();
				if (!current.equals(mAnimal))
					mAnimalsOnMap.add(current);
			}
		}

		for (Animal closest : mAnimalsOnMap) {
			markPosition(closest);
		}
	}
	
	/**
	 * Code is changed copy from {@link SightseeingFragment#setUpMapListeners}
	 */
	private void setUpMapListeners() {
		mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
			@Override
			public void onInfoWindowClick(Marker marker) {
				Animal animal = mMarkerIDsAnimals.get(marker.getId());

				SupportMapFragment f = (SupportMapFragment) getActivity()
						.getSupportFragmentManager().findFragmentById(R.id.map);
				if (f != null)
					getFragmentManager().beginTransaction().remove(f).commit();

				Fragment[] fragments = {
						AnimalDescriptionTab.newInstance(
								R.drawable.placeholder_adult, R.string.text),
						AnimalDetailsMapFragment.newInstance(animal) };
				Fragment newFragment = FragmentTabManager.newInstance(
						R.array.animal_desc_tabs_name, fragments, animal);

				FragmentHelper.swapFragment(R.id.flFragmentHolder, newFragment,
						getFragmentManager(),
						BundleConstants.FRAGMENT_ANIMAL_DETAIL);
			}
		});
	}

	protected void configureAndShowProgressDialog() {
		mProgressDialog.setMessage(
				this.getActivity().getResources().getString(R.string.gps_loading_position));
		mProgressDialog.setIndeterminate(true);
		mProgressDialog.setCancelable(false);
		mProgressDialog.show();
	}

	protected void dismissProgressDialog() {
		if (mProgressDialog.isShowing()) mProgressDialog.dismiss();
	}

	/**
	 * Finds and draws the shortest path between two points on the map. Path
	 * finding is performed by a {@link com.blstream.myguide.path.Graph} class
	 * and drawing is done by {@link com.google.android.gms.maps.GoogleMap} API.
	 * This method returns a
	 * {@link com.google.android.gms.maps.model.LatLngBounds.Builder} object
	 * which is filled with all nodes included in the shortest path (including
	 * source and destination). This object can be used for eg. keeping entire
	 * path within map boundaries.
	 * 
	 * @param src starting position, eg. user's current position
	 * @param dst destination, eq. animal's position
	 * @return pre-filled
	 *         {@link com.google.android.gms.maps.model.LatLngBounds.Builder}
	 */
	protected LatLngBounds.Builder findAndDrawShortestPath(LatLng src, LatLng dst) {
		LatLngBounds.Builder builder = new LatLngBounds.Builder();

		// finding shortest path
		List<Node> nodes = mGraph.findPath(
				new Node(src.latitude, src.longitude),
				new Node(dst.latitude, dst.longitude));
		Log.d(LOG_TAG, "shortest path nodes: " + nodes.size());

		// drawing shortest path
		// found nodes are also added to the LatLngBounds.Builder
		PolylineOptions plo = new PolylineOptions()
				.color(getResources().getColor(R.color.paths_on_track))
				.zIndex(PATH_ZINDEX)
				.width(SHORTEST_PATH_WIDTH)
				.add(src);
		builder.include(src);
		for (Node node : nodes) {
			LatLng ll = new LatLng(node.getLatitude(), node.getLongitude());
			plo.add(ll);
			builder.include(ll);
		}
		plo.add(dst);
		builder.include(dst);
		mMap.addPolyline(plo).setVisible(true);

		return builder;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int i) {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.d(LOG_TAG, "onCreateView");

		parseArguments();

		// check if layout is already created
		// if true it should be removed
		if (mRootView != null) {
			ViewGroup parent = (ViewGroup) mRootView.getParent();
			if (parent != null) parent.removeView(mRootView);
		}

		// inflate XML
		mRootView = inflater.inflate(R.layout.fragment_animal_details_map, container, false);

		// check if MapFragment is included in this Fragment
		// if not replace a View with new instance of MapFragment
		FragmentManager fm = getChildFragmentManager();
		mMapFragment = (SupportMapFragment) fm.findFragmentById(R.id.animal_details_map);
		if (mMapFragment == null) {
			mMapFragment = SupportMapFragment.newInstance();
			fm.beginTransaction()
					.replace(R.id.animal_details_map, mMapFragment)
					.commit();
			fm.executePendingTransactions();
		}
		// please note that GoogleMap object will not be available at the spot
		// so GoogleMap object will be requested in the next Fragment's
		// lifecycle method
		
		if(mAnimal != null)
			getActivity().getActionBar().setTitle(mAnimal.getName());
		
		return mRootView;
	}

	@Override
	public void onStart() {
		Log.d(LOG_TAG, "onStart");
		super.onStart();

		mProgressDialog = new ProgressDialog(getActivity());
		mMap = mMapFragment.getMap(); // should now be ready
		mGraph = ((MyGuideApp) getActivity().getApplication()).getGraph();
		mLocationObserver = new LocationObserver(this);

		mMap.setMyLocationEnabled(true);
		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
				new LatLng(DEFAULT_START_LAT, DEFAULT_START_LON),
				DEFAULT_MIN_ZOOM));
		mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
			@Override
			public void onMapLoaded() {
				// as soon as the map is loaded start listening to location
				// updates
				configureAndShowProgressDialog();
				bindLocationService();
			}
		});
		
		if (markClosestAnimals)
			setUpMapListeners();
	}

	@Override
	public void onPause() {
		Log.d(LOG_TAG, "onPause");
		super.onPause();

		// no need for location updates anymore
		try {
			unbindLocationService();
		} catch (IllegalStateException e) {
			Log.d(LOG_TAG, "While unbinding location: " + e.toString());
		}
	}

}