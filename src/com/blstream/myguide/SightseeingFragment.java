package com.blstream.myguide;

import java.util.ArrayList;

import android.app.ActionBar;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blstream.myguide.fragments.FragmentHelper;
import com.blstream.myguide.gps.LocationLogger;
import com.blstream.myguide.gps.LocationUpdater;
import com.blstream.myguide.gps.LocationUser;
import com.blstream.myguide.settings.Settings;
import com.blstream.myguide.zoolocations.Animal;
import com.blstream.myguide.zoolocations.AnimalDistance;
import com.blstream.myguide.zoolocations.Junction;
import com.blstream.myguide.zoolocations.Language;
import com.blstream.myguide.zoolocations.Node;
import com.blstream.myguide.zoolocations.Track;
import com.blstream.myguide.zoolocations.Way;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

/**
 * Main fragment of application
 */

public class SightseeingFragment extends Fragment implements LocationUser {

	private static final String LOG_TAG = SightseeingFragment.class
			.getSimpleName();
	private static final float DEFAULT_MIN_ZOOM = 14.5f;
	private static final float DEFAULT_MAX_ZOOM = 19.0f;
	private static final double DEFAULT_START_LAT = 51.1050406;
	private static final double DEFAULT_START_LON = 17.074053;
	private static final float WAYS_WIDTH_DEFAULT = 7.5f;

	private GoogleMap mMap;
	private float mMinZoom;
	private float mMaxZoom;
	private double mStartCenterLat;
	private double mStartCenterLon;
	private boolean mAnimalsVisible;
	private ArrayList<Marker> mAnimalMarkers;

	private Track mTrack;

	private boolean mPathsVisible;
	private ArrayList<Polyline> mZooPaths;

	private boolean mJunctionsVisible;
	private ArrayList<Circle> mZooJunctions;
	private ArrayList<Animal> mAnimalsList;
	private TrackDrawer mTrackDrawer;

	private LocationLogger mLocationLogger;
	private LocationUpdater mLocationUpdater;

	private boolean mLocationLogVisible;
	private BottomAnimalFragment mBottomAnimalFragment;
	private AnimalDistance mLastAnimalDistance;

	public static SightseeingFragment newInstance() {
		return new SightseeingFragment();
	}

	public static SightseeingFragment newInstance(Track track) {
		SightseeingFragment fragment = new SightseeingFragment();

		Bundle bundle = new Bundle();
		bundle.putSerializable(BundleConstants.SELECTED_TRACK, track);
		fragment.setArguments(bundle);

		return fragment;
	}

	private void getArgs() {
		Bundle args = getArguments();
		if (args != null) {
			mTrack = (Track) args.getSerializable(BundleConstants.SELECTED_TRACK);
		}
	}

	private void configureAndDisplayUserPosition() {
		// check if location should be hidden
		boolean visible = !((MyGuideApp) getActivity().getApplication())
				.getSettings().getValueAsBoolean(
						Settings.KEY_MAP_MY_POSITION_HIDDEN)
				&& LocationUpdater.getInstance().isGpsEnable();
		Log.d(LOG_TAG, String.format("Displaying position: %s", visible));
		mMap.setMyLocationEnabled(visible);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_sightseeing,
				container, false);

		mLocationUpdater = LocationUpdater.getInstance();
		mLocationUpdater.startUpdating(this);
		mLastAnimalDistance = null;

		getActivity().getActionBar().setTitle("");
		getActivity().getActionBar().setNavigationMode(
				ActionBar.NAVIGATION_MODE_STANDARD);

		((StartActivity) getActivity()).getDrawerLayout().setDrawerLockMode(
				DrawerLayout.LOCK_MODE_UNLOCKED);

		setUpMapSettings();
		setUpMap();
		setUpAnimalMarkers();
		setUpWays();
		setUpJunctions();
		setUpLocationLogger();

		displayAnimalMarkers(mAnimalsVisible);
		displayAllWays(mPathsVisible);
		displayAllJunctions(mJunctionsVisible);

		setUpClosestAnimal();

		mTrackDrawer = new TrackDrawer(
				((MyGuideApp) getActivity().getApplication()).getGraph(), mMap, mAnimalMarkers);

		if (mTrack != null) drawTrack();

		return rootView;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		((StartActivity) getActivity()).getDrawerLayout().setDrawerLockMode(
				DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.RIGHT);
	}

	@Override
	public void onStart() {
		super.onStart();
		configureAndDisplayUserPosition();
		if (mLocationLogVisible) {
			LocationUpdater.getInstance().startUpdating(mLocationLogger);
		}
	}

	@Override
	public void onStop() {
		super.onStop();
		if (mLocationLogVisible) {
			LocationUpdater.getInstance().stopUpdating(mLocationLogger);
		}
	}

	private void setUpMapSettings() {
		MyGuideApp mga = (MyGuideApp) (getActivity().getApplication());
		Settings settings = mga.getSettings();

		mAnimalsVisible = settings
				.getValueAsBoolean(Settings.KEY_ANIMALS_VISIBLE);
		mPathsVisible = settings.getValueAsBoolean(Settings.KEY_PATHS_VISIBLE);
		mJunctionsVisible = settings
				.getValueAsBoolean(Settings.KEY_JUNCTIONS_VISIBLE);

		try {
			mStartCenterLat = settings.getValueAsDouble(Settings.KEY_START_LAT);
		} catch (NumberFormatException e) {
			Log.w(LOG_TAG, Settings.KEY_START_LAT + " " + e);
			mStartCenterLat = DEFAULT_START_LAT;
			mStartCenterLon = DEFAULT_START_LON;
		}
		try {
			mStartCenterLon = settings.getValueAsDouble(Settings.KEY_START_LON);
		} catch (NumberFormatException e) {
			Log.w(LOG_TAG, Settings.KEY_START_LON + " " + e);
			mStartCenterLat = DEFAULT_START_LAT;
			mStartCenterLon = DEFAULT_START_LON;
		}
		try {
			mMinZoom = settings.getValueAsFloat(Settings.KEY_MIN_ZOOM);
		} catch (NumberFormatException e) {
			Log.w(LOG_TAG, Settings.KEY_MIN_ZOOM + " " + e);
			mMinZoom = DEFAULT_MIN_ZOOM;
			mMaxZoom = DEFAULT_MAX_ZOOM;
		}
		try {
			mMaxZoom = settings.getValueAsFloat(Settings.KEY_MAX_ZOOM);
		} catch (NumberFormatException e) {
			Log.w(LOG_TAG, Settings.KEY_MIN_ZOOM + " " + e);
			mMinZoom = DEFAULT_MIN_ZOOM;
			mMaxZoom = DEFAULT_MAX_ZOOM;
		}
	}

	private void setUpMap() {
		mMap = ((SupportMapFragment) getActivity().getSupportFragmentManager()
				.findFragmentById(R.id.map)).getMap();
		MapsInitializer.initialize(getActivity());
		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
				mStartCenterLat, mStartCenterLon), mMinZoom));
		setUpCamera();
		setUpMapListeners();
	}

	/**
	 * Sets up the closest animal showing at the bottom of the screen. Uses
	 * {@link com.blstream.myguide.AnimalFinderHelper }
	 */
	private void setUpClosestAnimal() {
		mBottomAnimalFragment = new BottomAnimalFragment();

		AnimalFinderHelper animalFinderHelper = new AnimalFinderHelper(
				mLocationUpdater.getLocation(), (MyGuideApp) getActivity()
						.getApplication(), getActivity());

		AnimalDistance closestAnimal = animalFinderHelper.closestAnimal();
		if (closestAnimal != null && !sameAsLastAnimal(closestAnimal)) {
			Bundle data = new Bundle();
			data.putSerializable(BundleConstants.CLOSEST_ANIMAL, closestAnimal);
			mBottomAnimalFragment.setArguments(data);
			FragmentManager manager = getChildFragmentManager();
			FragmentHelper.swapFragment(R.id.closestAnimal,
					mBottomAnimalFragment, manager,
					BundleConstants.FRAGMENT_BOTTOM_ANIMAL);
			mLastAnimalDistance = closestAnimal;
		}

	}

	private boolean sameAsLastAnimal(AnimalDistance closest) {
		return mLastAnimalDistance != null
				&& closest.equals(mLastAnimalDistance);
	}

	private void setUpMapListeners() {
		mMap.setOnCameraChangeListener(new OnCameraChangeListener() {
			@Override
			public void onCameraChange(CameraPosition cameraPosition) {
				if (cameraPosition.zoom < mMinZoom) {
					mMap.animateCamera(CameraUpdateFactory.zoomTo(mMinZoom));
				} else if (cameraPosition.zoom > mMaxZoom) {
					mMap.animateCamera(CameraUpdateFactory.zoomTo(mMaxZoom));
				}
			}
		});
		/**
		 * This listener is on Info window click ( Top of Marker ) After click
		 * animal description fragment is opened
		 */
		mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
			@Override
			public void onInfoWindowClick(Marker marker) {
				Animal animal = mAnimalsList.get(Integer.parseInt(marker
						.getId().substring(1)));

				SupportMapFragment f = (SupportMapFragment) getActivity()
						.getSupportFragmentManager().findFragmentById(R.id.map);
				if (f != null)
					getFragmentManager().beginTransaction().remove(f).commit();

				Fragment[] fragments = {
						AnimalDescriptionTab.newInstance(
								R.drawable.placeholder_adult, R.string.text),
						AnimalDescriptionTab.newInstance(
								R.drawable.placeholder_child, R.string.text),
						AnimalDetailsMapFragment.newInstance(animal) };
				Fragment newFragment = FragmentTabManager.newInstance(
						R.array.animal_desc_tabs_name, fragments, animal);

				FragmentHelper.swapFragment(R.id.flFragmentHolder, newFragment,
						getFragmentManager(),
						BundleConstants.FRAGMENT_ANIMAL_DETAIL);
			}
		});
	}

	/**
	 * Reads Ways and Nodes from ZooData and draws Polylines on the map
	 * accordingly.
	 */
	private void setUpWays() {
		MyGuideApp mga = (MyGuideApp) (getActivity().getApplication());
		ArrayList<Way> ways = mga.getZooData().getWays();
		mZooPaths = new ArrayList<Polyline>();

		for (Way way : ways) {
			PolylineOptions plo = new PolylineOptions()
					.width(WAYS_WIDTH_DEFAULT)
					.color(getResources().getColor(R.color.paths));
			for (Node node : way.getNodes()) {
				plo.add(new LatLng(node.getLatitude(), node.getLongitude()));
			}
			mZooPaths.add(mMap.addPolyline(plo));
		}

	}

	/**
	 * Determines whatever of not all paths are displayed on the map.
	 */
	private void displayAllWays(boolean display) {
		for (Polyline path : mZooPaths) {
			path.setVisible(display);
		}
	}

	/**
	 * Reads Junctions from ZooData and draws Circles on the map accordingly.
	 */
	private void setUpJunctions() {
		MyGuideApp mga = (MyGuideApp) (getActivity().getApplication());
		ArrayList<Junction> junctions = mga.getZooData().getJunctions();
		mZooJunctions = new ArrayList<Circle>();
		for (Junction a : junctions) {
			mMap.addCircle(new CircleOptions()
					.radius(10)
					.strokeWidth(2)
					.fillColor(Color.YELLOW)
					.center(new LatLng(a.getNode().getLatitude(), a.getNode()
							.getLongitude())).visible(mJunctionsVisible));
		}
	}

	/**
	 * Determines whatever of not all junctions are displayed on the map.
	 */
	private void displayAllJunctions(boolean display) {
		for (Circle junction : mZooJunctions) {
			junction.setVisible(display);
		}
	}

	private void setUpAnimalMarkers() {
		MyGuideApp mga = (MyGuideApp) (getActivity().getApplication());
		mAnimalsList = mga.getZooData().getAnimals();
		mAnimalMarkers = new ArrayList<Marker>();
		for (Animal a : mAnimalsList) {
			mAnimalMarkers.add(mMap.addMarker(new MarkerOptions()
					.position(
							new LatLng(a.getNode().getLatitude(), a.getNode()
									.getLongitude()))
					.title(a.getName(Language.DEFAULT))
					.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_animal))));
		}
	}

	private void displayAnimalMarkers(boolean display) {
		for (Marker m : mAnimalMarkers) {
			m.setVisible(display);
		}
	}

	private void setUpCamera() {
		mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
				mStartCenterLat, mStartCenterLon), 15));
	}

	private void setUpLocationLogger() {
		mLocationLogVisible = ((MyGuideApp) (getActivity().getApplication()))
				.getSettings().getValueAsBoolean(Settings.KEY_GPS_LOGGING);
		if (mLocationLogVisible) {
			mLocationLogger = new LocationLogger(getActivity(), 3, true);
		}
	}


	@Override
	public void onLocationUpdate(Location location) {
		setUpClosestAnimal();

	}

	@Override
	public void onGpsAvailable() {
		Log.i("","avaible");
	}

	@Override
	public void onGpsUnavailable() {
		Log.i("","unavaible");
	}

	public void drawTrack() {
		mTrackDrawer
				.drawTrack(mTrack, getResources().getColor(R.color.paths_on_track), mPathsVisible);
	}

	public void cleanTrack() {
		mTrackDrawer.cleanTrack();
	}

}
