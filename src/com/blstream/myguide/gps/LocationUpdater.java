
package com.blstream.myguide.gps;

import java.util.ArrayList;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

/**
 * Singleton which connects with location provider and gets coordinations
 * updates frequently. Each time it receive update it invokes a callback methods
 * of {@link LocationUser} which are currently binded to it. When connection
 * with provider is interrupted all binded {@link LocationUser} are informed.
 */
public class LocationUpdater {

	private static LocationUpdater mLocationUpdater;

	private LocationClient mLocationClient;
	private LocationRequest mLocationRequest;
	// List of listeners that bind to LocationUpdater
	private ArrayList<LocationUser> mLocationUsers;
	// True, when at least one listener is bind to LocationUpdater
	private boolean mRrequestingForUpdates;

	private LocationUpdater(Context context) {
		mRrequestingForUpdates = false;
		setLocationRequestFromSettings();
		mLocationUsers = new ArrayList<LocationUser>();
		mLocationClient = new LocationClient(context, mConnectionCallbacks,
				mOnConnectionFailedListener);
		mLocationClient.connect();
	}

	public static LocationUpdater getInstance(Context context) {
		if (mLocationUpdater == null) {
			mLocationUpdater = new LocationUpdater(context.getApplicationContext());
		}
		return mLocationUpdater;
	}

	/**
	 * Bind to {@link LocationUpdater} for location update.
	 * 
	 * @param user callback interface which should be bind to
	 *            {@link LocationUpdater}
	 */
	public void startUpdating(LocationUser user) {
		mLocationUsers.add(user);
		if (!mRrequestingForUpdates) {
			requestUpdates();
		}
	}

	/**
	 * When a location updates are not needed anymore, this method should always
	 * be called.
	 * 
	 * @param user callback interface which should be unbind from
	 *            {@link LocationUpdater}
	 */
	public void stopUpdating(LocationUser user) {
		mLocationUsers.remove(user);
		if (mLocationUsers.isEmpty()) {
			mLocationClient.removeLocationUpdates(mLocationListener);
			mRrequestingForUpdates = false;
		}
	}

	private void requestUpdates() {
		if (!mRrequestingForUpdates && !mLocationUsers.isEmpty()) {
			mLocationClient.requestLocationUpdates(mLocationRequest, mLocationListener);
			mRrequestingForUpdates = true;
		}
	}

	private void setLocationRequestFromSettings() {
		// TODO updates settings from options using option parser
		mLocationRequest = null;

		// temporary it always provide default settings
		if (mLocationRequest == null) {
			mLocationRequest = LocationRequest.create();
			mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY).setInterval(1000)
					.setFastestInterval(500);
		}
	}

	private final LocationListener mLocationListener = new LocationListener() {
		@Override
		public void onLocationChanged(Location location) {
			for (LocationUser e : mLocationUsers) {
				e.onLocationUpdate(location);
			}
		}
	};

	private final ConnectionCallbacks mConnectionCallbacks = new ConnectionCallbacks() {
		@Override
		public void onConnected(Bundle arg0) {
			requestUpdates();
			Log.d(LocationUpdater.class.getSimpleName(), "Connected to location services.");
		}

		@Override
		public void onDisconnected() {
			for (LocationUser e : mLocationUsers) {
				e.onGpsUnavailable();
			}
			Log.d(LocationUpdater.class.getSimpleName(), "Disconnect from location services.");
		}
	};

	private final OnConnectionFailedListener mOnConnectionFailedListener = new OnConnectionFailedListener() {
		@Override
		public void onConnectionFailed(ConnectionResult arg0) {
			for (LocationUser e : mLocationUsers) {
				e.onGpsUnavailable();
			}
			Log.d(LocationUpdater.class.getSimpleName(),
					"An error occure while connecting to location service.");
		}
	};
}
