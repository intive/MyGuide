
package com.blstream.myguide.gps;

import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

/**
 * Listener used for log and toast information about new coordinations. To avoid
 * logging to many information it logs coordinates only if the location has been
 * changed.
 */
public class LocationLogger implements LocationUser {

	private final static String LOG_TAG = LocationLogger.class.getSimpleName();

	private Context mContext;
	private long mIntervalInNanoseconds;
	private long mLastLogTimeInNanoseconds;
	private Location mLastLocation;
	private boolean mShowToast;

	/**
	 * @param context
	 * @param intervalInSec number of seconds between logs/toast
	 * @param showToast if true toast will appears, if false LocationLogger will
	 *            use only LogCat
	 */
	public LocationLogger(Context context, int intervalInSec, boolean showToast) {
		mContext = context;
		mIntervalInNanoseconds = TimeUnit.SECONDS.toNanos(intervalInSec);
		mShowToast = showToast;
		mLastLogTimeInNanoseconds = 0;
	}

	@Override
	public void onLocationUpdate(Location location) {
		if (isTimeBetweenLogsExpired() && isLocationDifferent(location)) {
			String msg = "Location changed: " + Double.toString(location.getLatitude())
					+ "," + Double.toString(location.getLongitude());
			Log.i(LOG_TAG, msg);
			if (mShowToast) {
				Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
			}
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
		if (System.nanoTime() - mLastLogTimeInNanoseconds > mIntervalInNanoseconds) { return true; }
		return false;
	}

	private void updateLastLogInfo(Location location) {
		mLastLocation = location;
		mLastLogTimeInNanoseconds = System.nanoTime();
	}
}
