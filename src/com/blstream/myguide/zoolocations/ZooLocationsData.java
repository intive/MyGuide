
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
	private ArrayList<Restaurant> mRestaurants;

	public ZooLocationsData(ArrayList<Animal> animals, ArrayList<Way> ways,
			ArrayList<Junction> junctions, ArrayList<Restaurant> restaurants) {
		mAnimals = animals;
		mWays = ways;
		mJunctions = junctions;
		mRestaurants = restaurants;
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

	public ArrayList<Restaurant> getRestaurant() {
		return mRestaurants;
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

	public void setRestaurants(ArrayList<Restaurant> restaurants) {
		mRestaurants = restaurants;
	}
}
