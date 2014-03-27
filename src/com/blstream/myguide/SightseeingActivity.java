
package com.blstream.myguide;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.blstream.myguide.gps.LocationLogger;
import com.blstream.myguide.gps.LocationUpdater;
import com.blstream.myguide.settings.Settings;
import com.blstream.myguide.zoolocations.Animal;
import com.blstream.myguide.zoolocations.Junction;
import com.blstream.myguide.zoolocations.Language;
import com.blstream.myguide.zoolocations.Node;
import com.blstream.myguide.zoolocations.Way;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class SightseeingActivity extends Activity implements
		OnCameraChangeListener {

	private static final String LOG_TAG = SightseeingActivity.class
			.getSimpleName();
	private static final float DEFAULT_MIN_ZOOM = 14.5f;
	private static final float DEFAULT_MAX_ZOOM = 19.0f;
	private static final double DEFAULT_START_LAT = 51.1050406;
	private static final double DEFAULT_START_LON = 17.074053;

	private ImageView mImgvSlidingMenu;
	private ImageView mImgvShowRoute;
	private SearchView mSearchView;
	private ImageView mSearchViewClose;
	private ActionBar mActionBar;
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	private GoogleMap mMap;
	private float mMinZoom;
	private float mMaxZoom;
	private double mStartCenterLat;
	private double mStartCenterLon;
	private boolean mAnimalsVisible;
	private ArrayList<Marker> mAnimalMarkers;

	private boolean mPathsVisible;
	private ArrayList<Polyline> mZooPaths;

	private boolean mJunctionsVisible;
	private ArrayList<Circle> mZooJunctions;

	private LocationLogger mLocationLogger;

	private void configureAndDisplayUserPosition() {
		// check if location should be hidden
		boolean visible = !((MyGuideApp) this.getApplication())
				.getSettings()
				.getValueAsBoolean(Settings.KEY_MAP_MY_POSITION_HIDDEN);
		Log.d(LOG_TAG, String.format("Displaying position: %s", visible));
		mMap.setMyLocationEnabled(visible);
	}

	/**
	 * Called when the activity is first created. Sets up ActionBar and
	 * NavigationDrawer for the Activity. Reads settings which are saved in
	 * MyGuideApp and sets up GoogleMap.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		setContentView(R.layout.activity_sightseeing);

		mActionBar = getActionBar();

		if (mActionBar != null) {
			mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
			mActionBar.setCustomView(R.layout.action_bar_sightseeing);
			mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

			View actionBarCustomView = mActionBar.getCustomView();
			setUpActionBar(actionBarCustomView);
			setUpActionBarListeners();
		}

		setUpDrawerListView();

		setUpMapSettings();
		setUpMap();
		setUpAnimalMarkers();
		setUpWays();
		setUpJunctions();

		displayAnimalMarkers(mAnimalsVisible);
		displayAllWays(mPathsVisible);
		displayAllJunctions(mJunctionsVisible);

		if (isDebugBuild()) {
			mLocationLogger = new LocationLogger(this, 3, true);
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		if (isDebugBuild()) {
			LocationUpdater.getInstance().startUpdating(mLocationLogger);
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (isDebugBuild()) {
			LocationUpdater.getInstance().stopUpdating(mLocationLogger);
		}
	}

	/**
	 * Check if build type of application is set to debug.
	 * 
	 * @return true if yes, false if no
	 */
	private boolean isDebugBuild() {
		return (0 != (getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE));
	}

	private void setUpMapSettings() {
		MyGuideApp mga = (MyGuideApp) (this.getApplication());
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
		mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();
		MapsInitializer.initialize(this);
		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
				mStartCenterLat, mStartCenterLon), mMinZoom));
		mMap.setOnCameraChangeListener(this);

		this.configureAndDisplayUserPosition();
	}

	/**
	 * Reads Ways and Nodes from ZooData and draws Polylines on the map
	 * accordingly.
	 */

	private void setUpWays() {
		MyGuideApp mga = (MyGuideApp) (this.getApplication());
		ArrayList<Way> ways = mga.getZooData().getWays();
		mZooPaths = new ArrayList<Polyline>();
		for (Way a : ways) {
			int numOfNodes = a.getNodes().size();
			for (int i = 0; i < numOfNodes - 1; i++) {
				Node current = a.getNodes().get(i);
				Node next = a.getNodes().get(i + 1);
				mZooPaths.add(mMap.addPolyline(new PolylineOptions()
						.add(new LatLng(current.getLatitude(), current
								.getLongitude()),
								new LatLng(next.getLatitude(), next
										.getLongitude())).width(2)
						.color(Color.BLACK)));
			}
		}
	}

	/** Determines whatever of not all paths are displayed on the map. */
	private void displayAllWays(boolean display) {
		for (Polyline path : mZooPaths) {
			path.setVisible(display);
		}
	}

	/**
	 * Reads Junctions from ZooData and draws Circles on the map accordingly.
	 */

	private void setUpJunctions() {
		MyGuideApp mga = (MyGuideApp) (this.getApplication());
		ArrayList<Junction> junctions = mga.getZooData().getJunctions();
		mZooJunctions = new ArrayList<Circle>();
		for (Junction a : junctions) {
			mMap.addCircle(new CircleOptions()
					.radius(10)
					.strokeWidth(2)
					.fillColor(Color.YELLOW)
					.center(new LatLng(a.getNode().getLatitude(), a.getNode()
							.getLongitude())

					));
		}
	}

	/** Determines whatever of not all junctions are displayed on the map. */

	private void displayAllJunctions(boolean display) {
		for (Circle junction : mZooJunctions) {
			junction.setVisible(display);
		}
	}

	private void setUpAnimalMarkers() {
		MyGuideApp mga = (MyGuideApp) (this.getApplication());
		ArrayList<Animal> animals = mga.getZooData().getAnimals();
		mAnimalMarkers = new ArrayList<Marker>();
		for (Animal a : animals) {
			mAnimalMarkers.add(mMap.addMarker(new MarkerOptions().position(
					new LatLng(a.getNode().getLatitude(), a.getNode()
							.getLongitude())).title(a.getName(Language.DEFAULT))));
		}
	}

	private void displayAnimalMarkers(boolean display) {
		for (Marker m : mAnimalMarkers) {
			m.setVisible(display);
		}
	}

	/** Sets up custom ActionBar. */
	private void setUpActionBar(View v) {
		mSearchView = (SearchView) v.findViewById(R.id.svSightseeing);
		mImgvShowRoute = (ImageView) v.findViewById(R.id.imgvShowRoute);
		mImgvSlidingMenu = (ImageView) v.findViewById(R.id.imgvSlidingMenu);

		mSearchView.setQueryHint(getString(R.string.search_sightseeing));
		mSearchView.setIconified(false);
		mSearchView.clearFocus();

		int searchPlateId = mSearchView.getContext().getResources()
				.getIdentifier("android:id/search_plate", null, null);
		View searchPlate = mSearchView.findViewById(searchPlateId);

		if (searchPlate != null) {
			searchPlate.setBackgroundResource(R.drawable.rounded_edittext);

			int searchTextId = searchPlate.getContext().getResources()
					.getIdentifier("android:id/search_src_text", null, null);
			TextView searchText = (TextView) searchPlate
					.findViewById(searchTextId);
			if (searchText != null) {
				searchText.setGravity(Gravity.CENTER);
				searchText.setTextColor(Color.BLACK);
			}

			int search = searchPlate.getContext().getResources()
					.getIdentifier("android:id/search_close_btn", null, null);
			mSearchViewClose = (ImageView) searchPlate.findViewById(search);
			if (mSearchViewClose != null) {
				mSearchViewClose.setVisibility(View.GONE);
			}
		}
	}

	/** Sets up listeners for ActionBar views. */
	private void setUpActionBarListeners() {
		mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
			@Override
			public boolean onClose() {
				clearSearchView();
				return true;
			}
		});

		mSearchView
				.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
					@Override
					public boolean onQueryTextSubmit(String s) {
						// TODO handle find animals
						clearSearchView();
						return false;
					}

					@Override
					public boolean onQueryTextChange(String s) {
						// TODO autocomplete text to show animals
						return false;
					}
				});

		mImgvShowRoute.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO handle route
				clearSearchView();
			}
		});

		mImgvSlidingMenu.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				clearSearchView();

				if (!mDrawerLayout.isDrawerOpen(Gravity.LEFT))
				mDrawerLayout.openDrawer(Gravity.LEFT);
				else
				mDrawerLayout.closeDrawer(Gravity.LEFT);
			}
		});
	}

	/** Sets up NavigationDrawer. */
	public void setUpDrawerListView() {
		String[] mDrawerMenuItems = getResources().getStringArray(
				R.array.nav_drawer_items);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.lvMenuSightseeing);

		mDrawerList.setAdapter(new ArrayAdapter<String>(this,
				R.layout.sliding_menu_item, mDrawerMenuItems));

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close);

		mDrawerLayout.setDrawerListener(mDrawerToggle);

		mDrawerLayout.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent motionEvent) {
				clearSearchView();
				return false;
			}
		});
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	@Override
	public void onCameraChange(CameraPosition camera) {
		if (camera.zoom < mMinZoom) {
			mMap.animateCamera(CameraUpdateFactory.zoomTo(mMinZoom));
		} else if (camera.zoom > mMaxZoom) {
			mMap.animateCamera(CameraUpdateFactory.zoomTo(mMaxZoom));
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	private void clearSearchView() {
		mSearchView.clearFocus();
		mSearchViewClose.setVisibility(View.GONE);
	}

}
