
package com.blstream.myguide.gps;

import android.content.Context;
import android.location.Location;
import android.text.format.Time;
import android.util.Log;
import android.widget.Toast;

/**
 * Listener used for log and toast information about new coordinations. To avoid
 * logging to many information it logs coordinates only if the location has been
 * changed and no more then one every 3 sec.
 */
public class LocationLogger implements LocationUser {

	private Context mContext;
	private Time mLastLogTime;
	private final int INTERVAL = 3000;
	private Location mLastLocation;

	public LocationLogger(Context context) {
		mContext = context;
		mLastLogTime = new Time();
		mLastLogTime.setToNow();
	}

	@Override
	public void onLocationUpdate(Location location) {
		if (isTimeBetweenLogsExpired() && isLocationDifferent(location)) {
			String msg = "Location changed: " + Double.toString(location.getLatitude())
					+ "," + Double.toString(location.getLongitude());
			Log.i(LocationUpdater.class.getSimpleName(), msg);
			Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
			mLastLogTime.setToNow();
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
			mLastLocation = location;
			return true;
		} else if (location.getLatitude() != mLastLocation.getLatitude()
				|| location.getLongitude() != mLastLocation.getLongitude()) {
			mLastLocation = location;
			return true;
		}
		return false;
	}

	private boolean isTimeBetweenLogsExpired() {
		Time temp = new Time();
		temp.setToNow();
		if (temp.toMillis(false) - mLastLogTime.toMillis(false) > INTERVAL) { return true; }
		return false;
	}
}
