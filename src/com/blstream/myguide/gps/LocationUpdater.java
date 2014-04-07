
package com.blstream.myguide.gps;

import java.util.ArrayList;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.blstream.myguide.settings.Settings;
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

	private static final String LOG_TAG = LocationUpdater.class.getSimpleName();

	private static Context mAppContext;
	private static Settings mSettings;
	private LocationClient mLocationClient;
	private LocationRequest mLocationRequest;
	// List of listeners that bind to LocationUpdater
	private ArrayList<LocationUser> mLocationUsers;
	// True, when at least one listener is bind to LocationUpdater
	private boolean mRrequestingForUpdates;
	private boolean mGpsPopupWasShown;

	private LocationUpdater() {
		mRrequestingForUpdates = false;
		mGpsPopupWasShown = false;
		mLocationUsers = new ArrayList<LocationUser>();
		mLocationClient = new LocationClient(mAppContext, mConnectionCallbacks,
				mOnConnectionFailedListener);
		mLocationClient.connect();
	}

	public static LocationUpdater getInstance() {
		if (mLocationUpdater == null) {
			mLocationUpdater = new LocationUpdater();
		}
		return mLocationUpdater;
	}

	public boolean isGpsEnable() {
		return ((LocationManager) mAppContext
				.getSystemService(Context.LOCATION_SERVICE))
				.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}

	public void markGpsEnableDialogAsShown() {
		mGpsPopupWasShown = true;
	}

	public boolean isEnableGpsDialogNeeded() {
		return (!isGpsEnable() && !mGpsPopupWasShown);
	}

	/**
	 * Must be invoke before creating a singleton. Used to inject
	 * ApplicationContext.
	 * 
	 * @param context of application
	 */
	public static void setAppContext(Context context) {
		if (mAppContext == null) {
			mAppContext = context.getApplicationContext();
		}
	}

	/**
	 * Must be invoke before creating a singleton. Used to inject settings.
	 * 
	 * @param settings used in application
	 */
	public static void setSettings(Settings settings) {
		if (mSettings == null) {
			mSettings = settings;
		}
	}

	/**
	 * Restart LocationUpdater state.
	 */
	public void clear() {
		mGpsPopupWasShown = false;
		mRrequestingForUpdates = false;
		mLocationRequest = null;
		mLocationUsers.clear();
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

	/**
	 * Returns the best most recent location currently available. If a location
	 * is not available, which should happen very rarely, null will be returned.
	 * 
	 * @return current location or null if a location is not available
	 */
	public Location getLocation() {
		if (mLocationClient.isConnected()) {
			return mLocationClient.getLastLocation();
		} else {
			return null;
		}
	}

	private void requestUpdates() {
		if (!mRrequestingForUpdates) {
			if (mLocationRequest == null) {
				setLocationRequestFromSettings();
			}
			if (mLocationClient.isConnected() && isGpsEnable()) {
				if (isGpsEnable()) {
					mLocationClient.requestLocationUpdates(mLocationRequest, mLocationListener);
					mRrequestingForUpdates = true;
					mGpsPopupWasShown = false;
					for (LocationUser e : mLocationUsers) {
						e.onGpsAvailable();
					}
				} else {
					notifyBinderAboutConnectionProblem();
				}
			} else {
				notifyBinderAboutConnectionProblem();
				if (!mLocationClient.isConnecting() && !mLocationClient.isConnected()) {
					mLocationClient.connect();
				}
			}
		}
	}

	private void setLocationRequestFromSettings() {
		mLocationRequest = LocationRequest.create();
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
				.setInterval(mSettings.getValueAsInt(Settings.KEY_GPS_INTERVAL))
				.setFastestInterval(mSettings.getValueAsInt(Settings.KEY_MIN_GPS_INTERVAL));
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
			Log.d(LOG_TAG, "Connected to location services.");
		}

		@Override
		public void onDisconnected() {
			notifyBinderAboutConnectionProblem();
			mRrequestingForUpdates = false;
			Log.d(LOG_TAG, "Disconnect from location services.");
		}
	};

	private final OnConnectionFailedListener mOnConnectionFailedListener = new OnConnectionFailedListener() {
		@Override
		public void onConnectionFailed(ConnectionResult arg0) {
			notifyBinderAboutConnectionProblem();
			mRrequestingForUpdates = false;
			Log.d(LOG_TAG, "An error occure while connecting to location service.");
		}
	};

	private void notifyBinderAboutConnectionProblem() {
		for (LocationUser e : mLocationUsers) {
			e.onGpsUnavailable();
		}
	}
}
