
package com.blstream.myguide.zoolocations;

import java.io.Serializable;

/**
 * This class contains two coordinates (latitude and longitude) of some point in
 * the zoo.
 */
public class Node implements Serializable {

	private double mLatitude;
	private double mLongitude;

	public Node(double latitude, double longitude) {
		mLatitude = latitude;
		mLongitude = longitude;
	}

	public void setLatitude(double latitude) {
		mLatitude = latitude;
	}

	public void setLongitude(double longitude) {
		mLongitude = longitude;
	}

	public double getLatitude() {
		return mLatitude;
	}

	public double getLongitude() {
		return mLongitude;
	}
}
