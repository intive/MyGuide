
package com.blstream.myguide;

import com.blstream.myguide.zoolocations.Node;

public class MathHelper {

	private static final double EARTH_RADIUS = 6371;

	/**
	 * Calculated by the Euclidean method (straight line), returns distance in
	 * meters.
	 */
	public static double distanceBetween(Node node, double lat, double lng) {
		double latitude = Math.toRadians(node.getLatitude() - lat);
		double longitude = Math.toRadians(node.getLongitude() - lng);

		double x = longitude * Math.cos(0.5 * (node.getLatitude() + lat));
		double sqrt = Math.sqrt(Math.pow(x, 2) + Math.pow(latitude, 2));

		return (EARTH_RADIUS * sqrt) * 1000;
	}

	/**
	 * Convert given vector to angle. Angle is in radians and it is positive in
	 * the clockwise direction.
	 * 
	 * @param firstPoint is start of vector
	 * @param SecondPoint is end of vector
	 * @return angle of given vector
	 */
	public static double vectorToAngle(Node firstPoint, Node SecondPoint) {
		double x = SecondPoint.getLongitude() - firstPoint.getLongitude();
		double y = SecondPoint.getLatitude() - firstPoint.getLatitude();
		if (x >= 0) {
			// 1st quarter
			if (y >= 0) return Math.atan2(x, y);
			// 2nd quarter
			else return Math.PI / 2 + Math.atan2(-y, x);
		} else {
			// 3rd quarter
			if (y <= 0) return Math.PI + Math.atan2(-x, -y);
			// 4th quarter
			else return 3 * Math.PI / 2 + Math.atan2(y, -x);
		}
	}
}
