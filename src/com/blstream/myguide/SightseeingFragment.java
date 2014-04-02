
package com.blstream.myguide;

import java.util.ArrayList;

import android.app.ActionBar;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class SightseeingFragment extends Fragment implements
		OnCameraChangeListener {

	private static final String LOG_TAG = SightseeingFragment.class
			.getSimpleName();
	private static final float DEFAULT_MIN_ZOOM = 14.5f;
	private static final float DEFAULT_MAX_ZOOM = 19.0f;
	private static final double DEFAULT_START_LAT = 51.1050406;
	private static final double DEFAULT_START_LON = 17.074053;

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
	ArrayList<Animal> animals;

	private LocationLogger mLocationLogger;

	private void configureAndDisplayUserPosition() {
		// check if location should be hidden
		boolean visible = !((MyGuideApp) getActivity().getApplication())
				.getSettings()
				.getValueAsBoolean(Settings.KEY_MAP_MY_POSITION_HIDDEN);
		Log.d(LOG_TAG, String.format("Displaying position: %s", visible));
		mMap.setMyLocationEnabled(visible);
	}

	public SightseeingFragment() {

	}

	public View mRootView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.activity_sightseeing, container, false);

		getActivity().getActionBar().setTitle("");
		getActivity().getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);

		setUpMapSettings();
		setUpMap();
		setUpAnimalMarkers();
		setUpWays();
		setUpJunctions();

		displayAnimalMarkers(mAnimalsVisible);
		displayAllWays(mPathsVisible);
		displayAllJunctions(mJunctionsVisible);

		if (isDebugBuild()) {
			mLocationLogger = new LocationLogger(getActivity(), 3, true);
		}
		return mRootView;
	}

	@Override
	public void onStart() {
		super.onStart();
		if (isDebugBuild()) {
			LocationUpdater.getInstance().startUpdating(mLocationLogger);
		}
	}

	@Override
	public void onStop() {
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
		return (0 != (getActivity().getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE));
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
		mMap = ((SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(
				R.id.map))
				.getMap();
		MapsInitializer.initialize(getActivity());
		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
				mStartCenterLat, mStartCenterLon), mMinZoom));
		mMap.setOnCameraChangeListener(this);

		mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
			@Override
			public void onInfoWindowClick(Marker marker) {
				Animal animal = animals.get(Integer.parseInt(marker.getId().substring(1)));

				Fragment newFragment = new AnimalDescriptionFragment(animal);
				FragmentTransaction transaction = getActivity().getSupportFragmentManager()
						.beginTransaction();
				transaction.setCustomAnimations(R.anim.right_in, R.anim.left_out);
				transaction.replace(R.id.flFragmentHolder, newFragment, "Details");
				transaction.addToBackStack("addDetail");
				transaction.commit();
			}
		});

		this.configureAndDisplayUserPosition();
	}

	/**
	 * Reads Ways and Nodes from ZooData and draws Polylines on the map
	 * accordingly.
	 */
	private void setUpWays() {
		MyGuideApp mga = (MyGuideApp) (getActivity().getApplication());
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
										.getLongitude())
						).width(2)
						.color(Color.BLACK)));
			}
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
							.getLongitude())
					));
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
		animals = mga.getZooData().getAnimals();
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

	@Override
	public void onCameraChange(CameraPosition camera) {
		if (camera.zoom < mMinZoom) {
			mMap.animateCamera(CameraUpdateFactory.zoomTo(mMinZoom));
		} else if (camera.zoom > mMaxZoom) {
			mMap.animateCamera(CameraUpdateFactory.zoomTo(mMaxZoom));
		}
	}

}
