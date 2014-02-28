
package com.blstream.myguide.zoolocations;

import java.util.ArrayList;

/**
 * This class contains informations about animals, ways and ways' junctions and
 * theirs locations in the zoo parsed from xml file.
 */
public class ZooLocationsData {

	private ArrayList<Animal> mAnimals;
	private ArrayList<Way> mWays;
	private ArrayList<Junction> mJunctions;

	public ZooLocationsData(ArrayList<Animal> animals, ArrayList<Way> ways,
			ArrayList<Junction> junctions) {
		mAnimals = animals;
		mWays = ways;
		mJunctions = junctions;
	}

	public ArrayList<Animal> getAnimals() {
		return mAnimals;
	}

	public ArrayList<Way> getWays() {
		return mWays;
	}

	public ArrayList<Junction> getJunctions() {
		return mJunctions;
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

}
