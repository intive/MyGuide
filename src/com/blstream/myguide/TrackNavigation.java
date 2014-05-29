
package com.blstream.myguide;

import java.util.ArrayList;

import android.location.Location;
import android.util.Log;

import com.blstream.myguide.gps.LocationUpdater;
import com.blstream.myguide.gps.LocationUser;
import com.blstream.myguide.path.Graph;
import com.blstream.myguide.settings.Settings;
import com.blstream.myguide.zoolocations.Animal;
import com.blstream.myguide.zoolocations.Node;
import com.blstream.myguide.zoolocations.Track;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class TrackNavigation implements LocationUser {

	/**
	 * Used for update GUI state
	 */
	public static interface NavigationHolder {
		public void onCameraCenterStateChange(boolean cameraAutoCenterIsOn);

		public void onDestinationChange(Animal newDestination);
	};

	private static final String LOG_TAG = TrackNavigation.class.getSimpleName();

	private static final double MAX_DISTANCE_TO_PROJECT = 20.0;
	private static final double MAX_DISTANCE_TO_AUTO_CENTER = 600.0;
	private static final double JUNCTION_EXIT_RADIUS_DEFAULT = 2.5;
	private static final double JUNCTION_ENTER_RADIUS_DEFAULT = 2.0;

	private NavigationHolder mNavigationHolder;

	private Animal mDestination;
	private Location mLastKnownLocation;

	private GoogleMap mMap;
	private Marker mMarker;
	private Graph mGraph;
	private Track mTrack;

	private boolean mAutoCenter = false;
	private boolean mCameraMovedByNavigation = false;
	private boolean mUserIsWithinJunction = false;

	private double mJunctionEnterRadius;
	private double mJunctionExitRadius;

	public TrackNavigation(GoogleMap map, Graph graph, Track track,
			NavigationHolder navigationHolder, Settings settings) {
		mMap = map;
		mGraph = graph;
		mTrack = track;
		mMarker = mMap.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_navigation))
				.position(new LatLng(0, 0))
				.anchor(0.5f, 0.5f));
		mDestination = track.getAnimals().get(0);
		mNavigationHolder = navigationHolder;
		try {
			mJunctionEnterRadius = settings.getValueAsDouble(Settings.JUNCTION_ENTER_RADIUS);
		} catch (NumberFormatException e) {
			Log.w(LOG_TAG, Settings.JUNCTION_ENTER_RADIUS + " " + e);
			mJunctionEnterRadius = JUNCTION_ENTER_RADIUS_DEFAULT;
		}
		try {
			mJunctionExitRadius = settings.getValueAsDouble(Settings.JUNCTION_EXIT_RADIUS);
		} catch (NumberFormatException e) {
			Log.w(LOG_TAG, Settings.JUNCTION_EXIT_RADIUS + " " + e);
			mJunctionExitRadius = JUNCTION_EXIT_RADIUS_DEFAULT;
		}
	}

	public void startNavigate() {
		mMap.setMyLocationEnabled(false);
		LocationUpdater.getInstance().startUpdating(this);
	}

	public void stopNavigate() {
		LocationUpdater.getInstance().stopUpdating(this);
		if (mMarker != null) mMarker.remove();
	}

	@Override
	public void onLocationUpdate(Location currentLocation) {
		mLastKnownLocation = currentLocation;
		updateDestination();
		if (mDestination == null) return;
		ArrayList<Node> nodeList = (ArrayList<Node>) mGraph.findPath(
				new Node(currentLocation.getLatitude(), currentLocation.getLongitude()),
						mDestination.getNode());
		moveAndRotateArrow(currentLocation, nodeList);
		// center camera if necessary
		if (mAutoCenter
				&& MathHelper.distanceBetween(nodeList.get(0), currentLocation.getLatitude(),
						currentLocation.getLongitude()) < MAX_DISTANCE_TO_AUTO_CENTER) {
			mMap.animateCamera(CameraUpdateFactory.newLatLng(mMarker.getPosition()));
			mCameraMovedByNavigation = true;
		}
	}

	/*
	 * set position and rotation of marker based on current location and path to
	 * destination point
	 */
	private void moveAndRotateArrow(Location location, ArrayList<Node> nodeList) {
		if (nodeList.size() < 2) return;
		// angles are in radians and positive in the counter-clockwise direction
		double direction = 0;
		double realLat = location.getLatitude();
		double realLng = location.getLongitude();
		double projectedLat = 0;
		double projectedLng = 0;
		/*
		 * two different junction radius will pretend arrow to move dynamically
		 * on the junctions radius edge
		 */
		double JunctionRange = (mUserIsWithinJunction) ? mJunctionExitRadius : mJunctionEnterRadius;
		if (nodeList.size() > 2
				&& MathHelper.distanceBetween(nodeList.get(1), realLat, realLng) < JunctionRange) {
			// project to junction
			projectedLat = nodeList.get(1).getLatitude();
			projectedLng = nodeList.get(1).getLongitude();
			direction = MathHelper.vectorToAngle(nodeList.get(1), nodeList.get(2));
			mUserIsWithinJunction = true;
		} else if (MathHelper.distanceBetween(nodeList.get(0), realLat, realLng) < MAX_DISTANCE_TO_PROJECT) {
			// project to way
			projectedLat = nodeList.get(0).getLatitude();
			projectedLng = nodeList.get(0).getLongitude();
			direction = MathHelper.vectorToAngle(nodeList.get(0), nodeList.get(1));
			mUserIsWithinJunction = false;
		} else {
			// too far form way/junction - no projection
			projectedLat = realLat;
			projectedLng = realLng;
			direction = MathHelper.vectorToAngle(new Node(realLat, realLng), nodeList.get(0));
			mUserIsWithinJunction = false;
		}

		mMarker.setPosition(new LatLng(projectedLat, projectedLng));
		mMarker.setRotation((float) Math.toDegrees(direction));
	}

	/**
	 * Set destination point to nearest unvisited animal from track.
	 *
	 * @return node of closest animal, or null when there is no unvisited animal
	 *         on track left
	 */
	private Animal updateDestination() {
		if (mLastKnownLocation != null) {
			Animal closestAnimalOnTrack = null;
			double minDistance = Double.MAX_VALUE;
			double tempDistance;

			for (Animal a : mTrack.getAnimals()) {
				if (!a.getVisited()) {
					tempDistance = mGraph.findDistance(a.getNode(),
							new Node(mLastKnownLocation.getLatitude(),
									mLastKnownLocation.getLongitude()));
					if (minDistance > tempDistance) {
						minDistance = tempDistance;
						closestAnimalOnTrack = a;
					}
				}
			}
			if (closestAnimalOnTrack != null && mDestination != null) {
				if (!closestAnimalOnTrack.equals(mDestination)) {
					mDestination = closestAnimalOnTrack;
					Log.i(LOG_TAG, "Navigation destination: " + closestAnimalOnTrack.getName());
					mNavigationHolder.onDestinationChange(mDestination);
				}
			} else {
				mDestination = null;
			}
		}
		return mDestination;
	}

	@Override
	public void onGpsAvailable() {
		mMarker.setVisible(true);
		mMap.setMyLocationEnabled(false);
	}

	@Override
	public void onGpsUnavailable() {
		mMarker.setVisible(false);
		mUserIsWithinJunction = false;
	}

	public Marker getNavigationMarker() {
		return mMarker;
	}

	/**
	 * After calling this method camera will move every time user position is
	 * changed, to keep user location marker in the middle of the map
	 */
	public void setCameraAutoCenterOn() {
		mAutoCenter = true;
		if (mLastKnownLocation != null
				&& mDestination.getNode() != null
				&& MathHelper.distanceBetween(mDestination.getNode(),
						mLastKnownLocation.getLatitude(),
						mLastKnownLocation.getLongitude()) < MAX_DISTANCE_TO_AUTO_CENTER) {
			mMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(
					mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude())));
			mCameraMovedByNavigation = true;
		}

		/*
		 * if user change camera position manually, camera center mode will be
		 * turned off
		 */
		mMap.setOnCameraChangeListener(new OnCameraChangeListener() {
			@Override
			public void onCameraChange(CameraPosition cameraPosition) {
				if (!mCameraMovedByNavigation) {
					setCameraAutoCenterOff();
					mNavigationHolder.onCameraCenterStateChange(false);
				}
				mCameraMovedByNavigation = false;
			}
		});
	}

	/**
	 * After calling this method camera will no longer moved when user position
	 * is changed
	 */
	public void setCameraAutoCenterOff() {
		mAutoCenter = false;
		mMap.setOnCameraChangeListener(null);
	}
}
