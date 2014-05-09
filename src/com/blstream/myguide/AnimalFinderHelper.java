package com.blstream.myguide;

import java.util.ArrayList;
import java.util.Collections;

import android.content.Context;
import android.location.Location;
import android.os.Handler;
import android.widget.Toast;

import com.blstream.myguide.path.Graph;
import com.blstream.myguide.zoolocations.Animal;
import com.blstream.myguide.zoolocations.AnimalDistance;
import com.blstream.myguide.zoolocations.Node;

/**
 * Class which calculates the distance between user and all animals in the ZOO.
 * Returns the closest animal and/or the list of all animals sorted by distance
 * from given location.
 * 
 * @author Agnieszka
 */

public class AnimalFinderHelper {

	private static final double FURTHEST_NORTH = 51.107912;
	private static final double FURTHEST_SOUTH = 51.101359;
	private static final double FURTHEST_EAST = 17.078696;
	private static final double FURTHEST_WEST = 17.068376;

	private static final String LOG_TAG = AnimalFinderHelper.class
			.getSimpleName();
	Location mLocation;
	ArrayList<Animal> mAllAnimals;
	Graph mGraph;
	Handler mHandler;
	Context mContext;

	public AnimalFinderHelper(Location location, MyGuideApp app, Context context) {
		mLocation = location;
		mAllAnimals = app.getZooData().getAnimals();
		mGraph = app.getGraph();
		mContext = context;
		mHandler = new Handler();
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
		
		Toast.makeText(mContext,
				R.string.nearest_animals_waiting_toast,
				Toast.LENGTH_SHORT).show();
		
		//TODO MOVE CALCULATIONS INTO ANOTHER THREAD
		for (int i = 0; i < distances.length; i++) {
			distances[i] = mGraph.findDistance(position, mAllAnimals
					.get(i).getNode());
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
	 * User; if user's localization is unknown or not within bounds of the Zoo,
	 * returns null.
	 */
	public AnimalDistance closestAnimal() {
		if (mLocation != null && withinBoundsOfZoo(mLocation)) {
			return allAnimalsWithDistances().get(0);
		}
		return null;
	}

	/**
	 * Checks if user's position is within bounds of the Zoo.
	 */
	private boolean withinBoundsOfZoo(Location location) {
		return (FURTHEST_NORTH > location.getLatitude())
				&& (location.getLatitude() > FURTHEST_SOUTH)
				&& (FURTHEST_EAST > location.getLongitude())
				&& (location.getLongitude() > FURTHEST_WEST);
	}

}
