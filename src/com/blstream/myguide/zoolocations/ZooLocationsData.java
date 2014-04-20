
package com.blstream.myguide.zoolocations;

import java.util.ArrayList;
import java.util.Collections;

/**
 * This class contains informations about animals, ways and ways' junctions and
 * theirs locations in the zoo parsed from xml file.
 */
public class ZooLocationsData {

	private ArrayList<Animal> mAnimals;
	private ArrayList<Way> mWays;
	private ArrayList<Junction> mJunctions;
	private ArrayList<Track> mTracks;
	private ArrayList<Restaurant> mRestaurants;
	private AccessInformation mAccessInfo = new AccessInformation();

	public ZooLocationsData() {
		mAnimals = new ArrayList<Animal>();
		mWays = new ArrayList<Way>();
		mJunctions = new ArrayList<Junction>();
		mTracks = new ArrayList<Track>();
		mRestaurants = new ArrayList<Restaurant>();
	}

	public ArrayList<Animal> getAnimals() {
		Collections.sort(mAnimals);
		return mAnimals;
	}

	public ArrayList<Way> getWays() {
		return mWays;
	}

	public ArrayList<Junction> getJunctions() {
		return mJunctions;
	}

	public ArrayList<Track> getTracks() {
		return mTracks;
	}

	public ArrayList<Restaurant> getRestaurant() {
		return mRestaurants;
	}

	public AccessInformation getAccessInformation() {
		return mAccessInfo;
	}

	public void setAnimals(ArrayList<Animal> animals) {
		mAnimals = animals;
	}

	public void setWays(ArrayList<Way> ways) {
		mWays = ways;
	}

	public void setJunctions(ArrayList<Junction> junctions) {
		mJunctions = junctions;
	}

	public void setTracks(ArrayList<Track> tracks) {
		mTracks = tracks;
	}

	public void setRestaurants(ArrayList<Restaurant> restaurants) {
		mRestaurants = restaurants;
	}

	public void setAccessInformation(AccessInformation accessInformation) {
		mAccessInfo = accessInformation;
	}

}
