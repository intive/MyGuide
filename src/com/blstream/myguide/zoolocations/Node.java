package com.blstream.myguide.zoolocations;

public class Node {

	private double latitude;	
	private double longitude;
	
	public Node(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}
		
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}	
}
