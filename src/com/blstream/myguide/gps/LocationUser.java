
package com.blstream.myguide.gps;

import android.location.Location;

/**
 * Interface that is use for communication with {@link LocationUpdater}.
 */
public interface LocationUser {
	/**
	 * Invoke after {@link LocationUpdater} receive new location from device
	 * services.
	 * 
	 * @param location the last known location for the provider, or null
	 */
	public void onLocationUpdate(Location location);

	/**
	 * Invoke after gps turned on. All location base UI should be enable within
	 * this method.
	 */
	public void onGpsAvailable();

	/**
	 * Invoke after gps turned off. All location base UI should be disable
	 * within this method.
	 */
	public void onGpsUnavailable();
}
