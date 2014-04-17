package com.blstream.myguide;

import java.util.ArrayList;
import java.util.Collections;

import android.location.Location;
import android.util.Log;

import com.blstream.myguide.path.Graph;
import com.blstream.myguide.zoolocations.*;

/**
 * Class which calculates the distance between user and all animals in the ZOO.
 * Returns the closest animal and/or the list of all animals sorted by distance
 * from given location.
 * 
 * @author Agnieszka
 */

public class AnimalFinderHelper {

	private static final String LOG_TAG = AnimalFinderHelper.class
			.getSimpleName();
	Location mLocation;
	ArrayList<Animal> mAllAnimals;
	Graph mGraph;

	public AnimalFinderHelper(Location location, MyGuideApp app) {
		mLocation = location;
		mAllAnimals = app.getZooData().getAnimals();
		mGraph = app.getGraph();
	}

	/**
	 * Creates Node from user's position for mGraph method findDistance
	 * {@linkplain com.blstream.myguide.zoolocations.Graph#findDistance}.
	 * */
	private Node myPosition() {
		return new Node(mLocation.getLatitude(), mLocation.getLongitude());
	}

	/**
	 * Calculates distances of all animals (using new thread) and returns the
	 * array of results.
	 * 
	 * @return double array containing distances to every animal
	 */
	private double[] distancesToAllAnimals() {
		final double[] distances = new double[mAllAnimals.size()];
		final Node position = myPosition();
		Thread calcuateDistances = new Thread(new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < distances.length; i++) {
					distances[i] = mGraph.findDistance(position, mAllAnimals
							.get(i).getNode());
				}
			}
		});

		calcuateDistances.start();
		try {
			calcuateDistances.join();
		} catch (InterruptedException e) {
			Log.e(LOG_TAG, e.getMessage());
		}
		return distances;
	}

	/**
	 * Returns list of pairs (Animal, distance) based on list of Animals and
	 * list of distances
	 * {@link com.blstream.myguide.NearestAnimalsListFragment#distancesToAllAnimals()}
	 */
	public ArrayList<AnimalDistance> allAnimalsWithDistances() {
		ArrayList<AnimalDistance> result = new ArrayList<AnimalDistance>();
		double[] distances = distancesToAllAnimals();
		for (int i = 0; i < distances.length; i++) {
			result.add(new AnimalDistance(mAllAnimals.get(i), distances[i]));
		}

		Collections.sort(result);
		return result;
	}

	/**
	 * Returns pair (Animal, distance) for the animal which is the closest to
	 * User.
	 */
	public AnimalDistance closestAnimal() {
		return allAnimalsWithDistances().get(0);
	}

}
