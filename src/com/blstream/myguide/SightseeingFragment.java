
package com.blstream.myguide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import android.app.ActionBar;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.blstream.myguide.database.DbDataManager;
import com.blstream.myguide.TrackNavigation.NavigationHolder;
import com.blstream.myguide.fragments.FragmentHelper;
import com.blstream.myguide.gps.LocationLogger;
import com.blstream.myguide.gps.LocationUpdater;
import com.blstream.myguide.gps.LocationUser;
import com.blstream.myguide.path.Graph;
import com.blstream.myguide.settings.Settings;
import com.blstream.myguide.zoolocations.Animal;
import com.blstream.myguide.zoolocations.XmlObjectDistance;
import com.blstream.myguide.zoolocations.Junction;
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

public class SightseeingFragment extends Fragment implements LocationUser, NavigationHolder {

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
	private HashMap<Marker, Animal> mAnimalMarkersMap;
	private ToggleButton mNavigationToggleButton;
	private Marker mNavigationMarker;

	private Track mTrack;
	private SearchView mSearchView;

	private boolean mPathsVisible;
	private ArrayList<Polyline> mZooPaths;

	private boolean mJunctionsVisible;
	private ArrayList<Circle> mZooJunctions;
	private ArrayList<Animal> mAnimalsList;
	private TrackDrawer mTrackDrawer;
	private TrackNavigation mTrackNavigation;

	private LocationLogger mLocationLogger;
	private LocationUpdater mLocationUpdater;

	private boolean mLocationLogVisible;
	private BottomAnimalFragment mBottomAnimalFragment;
	private XmlObjectDistance mLastAnimalDistance;

	private MenuItem mItemSearch;
	private MenuItem mItemFilter;
	private MenuItem mItemCheck;

	private Marker mCheckMarker;
	private DbDataManager mDbManager;

	public static SightseeingFragment newInstance() {
		return new SightseeingFragment();
	}

	private void getTrack() {
		mTrack = ((StartActivity) getActivity()).getExploredTrack();
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
		getTrack();
		View rootView = inflater.inflate(R.layout.fragment_sightseeing,
				container, false);
		mDbManager = DbDataManager.getInstance(getActivity());
		mNavigationToggleButton = (ToggleButton) rootView.findViewById(
				R.id.tgbtn_navigationOnOff);

        setUpBottomAnimalFragment();

		getActivity().getActionBar().setTitle("");
		getActivity().getActionBar().setNavigationMode(
				ActionBar.NAVIGATION_MODE_STANDARD);

		((StartActivity) getActivity()).getDrawerLayout().setDrawerLockMode(
				DrawerLayout.LOCK_MODE_UNLOCKED);

		setHasOptionsMenu(true);
		setUpMapSettings();
		setUpMap();
		setUpAnimalMarkers();
		setUpWays();
		setUpJunctions();
		setUpLocationLogger();

		displayAnimalMarkers(mAnimalsVisible);
		displayAllWays(mPathsVisible);
		displayAllJunctions(mJunctionsVisible);

		setUpNavigation();

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
		if (mTrackNavigation != null) {
			mTrackNavigation.stopNavigate();
			mNavigationToggleButton.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		final MenuItem searchViewMenuItem = menu.findItem(R.id.action_search);
		mItemSearch = menu.findItem(R.id.action_search);
		mItemFilter = menu.findItem(R.id.action_filter);
		mItemCheck = menu.add(getString(R.string.select_animal));

		mItemCheck.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);
		mItemCheck.setVisible(false);
		setUpMenuItemListeners();

		mSearchView = (SearchView) searchViewMenuItem.getActionView();

		if (mSearchView != null) {
			setUpSearchView();
			setUpSearchViewListeners();
		}
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_filter) {
			clearSearchView();
		}
		return super.onOptionsItemSelected(item);
	}

	private void setUpMenuItemListeners() {
		mItemCheck.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem menuItem) {
				if (mAnimalMarkersMap.get(mCheckMarker).getVisited()) {
					mDbManager.updateAnimalInDb(mAnimalMarkersMap.get(mCheckMarker).getId(), false);
					mAnimalMarkersMap.get(mCheckMarker).setVisited(false);
					if (mTrack != null) {
						if (mTrack.getAnimals().contains(mAnimalMarkersMap.get(mCheckMarker))) {
							changeAnimalMarkerIcon(R.drawable.ic_animal_on_track);
						} else {
							changeAnimalMarkerIcon(R.drawable.ic_animal);
						}
					} else {
						changeAnimalMarkerIcon(R.drawable.ic_animal);
					}
				} else {
					mDbManager.updateAnimalInDb(mAnimalMarkersMap.get(mCheckMarker).getId(), true);
					mAnimalMarkersMap.get(mCheckMarker).setVisited(true);
					changeAnimalMarkerIcon(R.drawable.ic_animal_visited);
				}
				closeCheckButton(false);

				return false;
			}
		});
	}

	private void setUpSearchView() {
		int searchIconId = mSearchView.getContext().getResources().
				getIdentifier("android:id/search_button", null, null);

		ImageView searchIcon = (ImageView) mSearchView.findViewById(searchIconId);
		searchIcon.setImageResource(R.drawable.ic_action_search_icon_myguide);
		mSearchView.setQueryHint(getString(R.string.search_sightseeing));

		int searchPlateId = mSearchView.getContext().getResources()
				.getIdentifier("android:id/search_plate", null, null);
		View searchPlate = mSearchView.findViewById(searchPlateId);

		if (searchPlate != null) {
			searchPlate.setBackgroundResource(R.drawable.rounded_edittext);
			int searchTextId = searchPlate.getContext().getResources()
					.getIdentifier("android:id/search_src_text", null, null);
			TextView searchText = (TextView) searchPlate.findViewById(searchTextId);
			if (searchText != null) {
				searchText.setGravity(Gravity.CENTER);
			}
		}
	}

	/**
	 * Method change marker color where animal.id == id
	 *
	 * @param id animal id to change marker color
	 */
	public void updateAnimalVisitedMarker(int id) {
		for (Marker m : mAnimalMarkersMap.keySet()) {
			if (mAnimalMarkersMap.get(m).getId() == id) m.setIcon(BitmapDescriptorFactory
					.fromResource(R.drawable.ic_animal_visited));
		}
	}

	public void updateAnimalMarker() {
		for (Marker m : mAnimalMarkersMap.keySet()) {
			if (mAnimalMarkersMap.get(m).getVisited()) m.setIcon(BitmapDescriptorFactory
					.fromResource(R.drawable.ic_animal_visited));
			else m.setIcon(BitmapDescriptorFactory
					.fromResource(R.drawable.ic_animal));

			if (mTrack != null) {
				if (mTrack.getAnimals().contains(mAnimalMarkersMap.get(m))) m
						.setIcon(BitmapDescriptorFactory
								.fromResource(R.drawable.ic_animal_on_track));
			}
		}
	}

	private void setUpSearchViewListeners() {
		mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String s) {
				boolean findAnimal = false;

				for (Marker marker : mAnimalMarkersMap.keySet()) {

					if (replacePolishChar(marker.getTitle().toLowerCase()).contains(
							replacePolishChar(s.toLowerCase())))
					{
						mCheckMarker = marker;
						setUpAnimalCamera();

						marker.showInfoWindow();
						findAnimal = true;
					}
				}

				if (findAnimal) {
					clearSearchView();
				} else {
					mSearchView.setQuery(null, false);
					mSearchView.setQueryHint(getString(R.string.search_sightseeing_not));
				}

				return false;
			}

			@Override
			public boolean onQueryTextChange(String s) {
				return false;
			}
		});
	}

	private String replacePolishChar(String s) {
		return s.replace("ą", "a").replace("ć", "c").replace("ę", "e").replace("ł", "l")
				.replace("ń", "n").replace("ó", "o").replace("ś", "s").replace("ż", "z")
				.replace("ź", "z");
	}

	private void clearSearchView() {
		mSearchView.setQuery(null, false);
		mSearchView.setQueryHint(getString(R.string.search_sightseeing));
		mSearchView.clearFocus();
		mSearchView.onActionViewCollapsed();
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
	 * {@link XmlObjectFinderHelper }
	 */
	private void setUpClosestAnimal() {
		if (mBottomAnimalFragment == null) mBottomAnimalFragment = new BottomAnimalFragment();

        if (getActivity() != null) {
            XmlObjectFinderHelper animalFinderHelper = new XmlObjectFinderHelper(
                    mLocationUpdater.getLocation(), (MyGuideApp) getActivity()
                    .getApplication(), getActivity(), new Animal()
            );

            XmlObjectDistance closestAnimal = animalFinderHelper.closestXmlObject();

            if (closestAnimal != null) {

                if (sameAnimalNewDistance(closestAnimal)) {
                    mBottomAnimalFragment.setDistance(closestAnimal.getDistance());
                } else if (!sameAsLastAnimal(closestAnimal)) {
                    Bundle data = new Bundle();
                    data.putSerializable(BundleConstants.CLOSEST_ANIMAL, closestAnimal);
                    mBottomAnimalFragment = new BottomAnimalFragment();
                    mBottomAnimalFragment.setArguments(data);
                    FragmentManager manager = getChildFragmentManager();
                    FragmentHelper.swapFragment(R.id.closestAnimal,
                            mBottomAnimalFragment, manager,
                            BundleConstants.FRAGMENT_BOTTOM_ANIMAL);
                    mLastAnimalDistance = closestAnimal;
                }
            }
        }
	}

	private boolean sameAnimalNewDistance(XmlObjectDistance closest) {
		return mLastAnimalDistance != null &&
				closest.getXmlObject().equals(mLastAnimalDistance.getXmlObject()) &&
				(closest.getDistance() != mLastAnimalDistance.getDistance());
	}

	private boolean sameAsLastAnimal(XmlObjectDistance closest) {
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
		mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
			@Override
			public void onMapClick(LatLng latLng) {
				clearSearchView();
				setUpCamera();
				closeCheckButton(true);
			}
		});
		mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
			@Override
			public boolean onMarkerClick(Marker marker) {
				if (mNavigationMarker == null || !marker.getId().equals(mNavigationMarker.getId())) {
					mCheckMarker = marker;
					setUpAnimalCamera();
					openCheckedButton();
					marker.showInfoWindow();
					clearSearchView();
				}
				return true;
			}
		});
		mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
			@Override
			public View getInfoWindow(Marker marker) {
				View infoWindow = getActivity().getLayoutInflater().inflate(
						R.layout.info_window_animal, null);
				TextView txtvHeader = (TextView) infoWindow.findViewById(R.id.txtvHeaderInfoWindow);
				txtvHeader.setText(marker.getTitle());
                ImageView image = (ImageView) infoWindow.findViewById(R.id.imgvInfoWindowAnimal);
                String[] name = mAnimalMarkersMap.get(marker).getDescriptionAdult().getImageName().substring(4).split("\\.");
                int id = getResources().getIdentifier(name[0], "drawable",
                        getActivity().getPackageName());
                image.setImageResource(id);

				return infoWindow;
			}

			@Override
			public View getInfoContents(Marker marker) {
				return null;
			}
		});
		/**
		 * This listener is on Info window click ( Top of Marker ) After click
		 * animal description fragment is opened
		 */
		mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
			@Override
			public void onInfoWindowClick(Marker marker) {
				Animal animal = mAnimalMarkersMap.get(marker);

				SupportMapFragment f = (SupportMapFragment) getActivity()
						.getSupportFragmentManager().findFragmentById(R.id.map);
				if (f != null)
				getFragmentManager().beginTransaction().remove(f).commit();

				Fragment[] fragments = {
						AnimalDescriptionTab.newInstance(animal),
						XmlObjectDetailsMapFragment.newInstance(animal)
				};
				Fragment newFragment = FragmentTabManager.newInstance(
						R.array.animal_desc_tabs_name, fragments, animal);

				FragmentHelper.swapFragment(R.id.flFragmentHolder, newFragment,
						getFragmentManager(),
						BundleConstants.FRAGMENT_ANIMAL_DETAIL);
			}
		});
	}

	private void openCheckedButton() {
		if (mItemSearch != null) mItemSearch.setVisible(false);
		if (mItemFilter != null) mItemFilter.setVisible(false);

		if (mAnimalMarkersMap.get(mCheckMarker).getVisited()) mItemCheck
				.setTitle(getString(R.string.unselect_animal));
		else mItemCheck.setTitle(getString(R.string.select_animal));

		mItemCheck.setVisible(true);
	}

	private void closeCheckButton(boolean visibility) {
		if (mItemSearch != null) mItemSearch.setVisible(true);
		if (mItemFilter != null) mItemFilter.setVisible(true);

		mItemCheck.setVisible(false);
		if (!visibility) mCheckMarker.showInfoWindow();
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
		mAnimalMarkersMap = new HashMap<Marker, Animal>();
		for (Animal a : mAnimalsList) {
			mAnimalMarkersMap.put(mMap.addMarker(new MarkerOptions()
					.position(
							new LatLng(a.getNode().getLatitude(), a.getNode()
									.getLongitude()))
					.title(a.getName(Locale.getDefault().getLanguage()))
					.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_animal))), a);
		}
	}

	private void displayAnimalMarkers(final boolean display) {
		for (final Marker m : mAnimalMarkersMap.keySet()) {
			if (mAnimalMarkersMap.get(m).getVisited()) {
				m.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_animal_visited));
			}
			m.setVisible(display);
		}
	}

	private void changeAnimalMarkerIcon(int icon) {
		mCheckMarker.setIcon(BitmapDescriptorFactory
				.fromResource(icon));
	}

	private void setUpAnimalCamera() {
		mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mCheckMarker.getPosition(), 17));
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

	private void setUpNavigation() {
		if (mTrack != null) {
			Graph graph = ((MyGuideApp) getActivity().getApplication()).getGraph();
			mTrackDrawer = new TrackDrawer(graph, mMap, mAnimalMarkersMap);
			drawTrack();
			mTrackNavigation = new TrackNavigation(mMap, graph, mTrack, this,
					((MyGuideApp) getActivity().getApplication()).getSettings());

			// Reference to navigation marker is needed to prevent from showing
			// InfoWindows, which does not exist for navigation marker
			mNavigationMarker = mTrackNavigation.getNavigationMarker();

			mNavigationToggleButton.setVisibility(View.VISIBLE);
			mNavigationToggleButton
					.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
						public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
							if (isChecked) {
								mTrackNavigation.setCameraAutoCenterOn();
							} else {
								mTrackNavigation.setCameraAutoCenterOff();
							}
						}
					});
		}
	}

	private void destroyBottomFragment() {
		if (mBottomAnimalFragment != null && mBottomAnimalFragment.isVisible()) {
			FragmentManager fm = getChildFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			ft.remove(mBottomAnimalFragment);
			ft.commit();
		}
	}

    private void setUpBottomAnimalFragment() {
        mLocationUpdater = LocationUpdater.getInstance();
        mLocationUpdater.startUpdating(this);
        mLastAnimalDistance = null;
        mBottomAnimalFragment = new BottomAnimalFragment();
    }

	@Override
	public void onPause() {
		destroyBottomFragment();
		mLocationUpdater.stopUpdating(this);
		if (mTrackNavigation != null) {
			mTrackNavigation.stopNavigate();
			mNavigationToggleButton.setVisibility(View.INVISIBLE);
		}
		super.onPause();
	}

	@Override
	public void onResume() {
		if (mTrackNavigation != null && mTrack != null) {
			mTrackNavigation.startNavigate();
			mNavigationToggleButton.setVisibility(View.VISIBLE);
		}
		super.onResume();
        setUpBottomAnimalFragment();
	}

	@Override
	public void onLocationUpdate(Location location) {
		setUpClosestAnimal();
	}

	@Override
	public void onGpsAvailable() {
		setUpClosestAnimal();
	}

	@Override
	public void onGpsUnavailable() {
		destroyBottomFragment();
	}

	public void drawTrack() {
		mTrackDrawer
				.drawTrack(mTrack, getResources().getColor(R.color.paths_on_track), mPathsVisible);
	}

	public void cleanTrack() {
		mTrackDrawer.cleanTrack();
	}

	@Override
	public void onCameraCenterStateChange(boolean cameraAutoCenterIsOn) {
		if (mNavigationToggleButton != null) mNavigationToggleButton
				.setChecked(cameraAutoCenterIsOn);

	}

	@Override
	public void onDestinationChange(Animal newDestination) {
		Toast.makeText(getActivity(), getString(R.string.navigate_to) + newDestination.getName(Locale.getDefault().getLanguage()),
				Toast.LENGTH_SHORT).show();

	}
}
