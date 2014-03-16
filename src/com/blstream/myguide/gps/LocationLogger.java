
package com.blstream.myguide.gps;

import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

/**
 * Listener used for log and toast information about new coordinations. To avoid
 * logging to many information it logs coordinates only if the location has been
 * changed and no more then one every 3 sec.
 */
public class LocationLogger implements LocationUser {

	private Context mContext;
	private static final long INTERVAL_NS = TimeUnit.SECONDS.toNanos(3);
	private long mLastLogTimeInNanoseconds;
	private Location mLastLocation;

	public LocationLogger(Context context) {
		mContext = context;
		mLastLogTimeInNanoseconds = 0;
	}

	@Override
	public void onLocationUpdate(Location location) {
		if (isTimeBetweenLogsExpired() && isLocationDifferent(location)) {
			String msg = "Location changed: " + Double.toString(location.getLatitude())
					+ "," + Double.toString(location.getLongitude());
			Log.i(LocationUpdater.class.getSimpleName(), msg);
			Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
			updateLastLogInfo(location);
		}
	}

	@Override
	public void onGpsAvailable() {
	}

	@Override
	public void onGpsUnavailable() {
	}

	private boolean isLocationDifferent(Location location) {
		if (location == null) {
			return false;
		} else if (mLastLocation == null) {
			return true;
		} else if (location.getLatitude() != mLastLocation.getLatitude()
				|| location.getLongitude() != mLastLocation.getLongitude()) { return true; }
		return false;
	}

	private boolean isTimeBetweenLogsExpired() {
		if (System.nanoTime() - mLastLogTimeInNanoseconds > INTERVAL_NS) { return true; }
		return false;
	}

	private void updateLastLogInfo(Location location) {
		mLastLocation = location;
		mLastLogTimeInNanoseconds = System.nanoTime();
	}
}
