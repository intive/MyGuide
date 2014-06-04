
package com.blstream.myguide;

import java.util.ArrayList;
import java.util.Collections;

import android.content.Context;
import android.location.Location;
import android.os.Handler;
import com.blstream.myguide.path.Graph;
import com.blstream.myguide.zoolocations.Animal;
import com.blstream.myguide.zoolocations.XmlObjectDistance;
import com.blstream.myguide.zoolocations.Node;
import com.blstream.myguide.zoolocations.XmlObject;
import com.blstream.myguide.zoolocations.Restaurant;

/**
 * Class which calculates the distance between user and all xmlObject in the
 * ZOO. Returns the closest animal and/or the list of all animals sorted by
 * distance from given location.
 * 
 * @author Agnieszka
 */

public class XmlObjectFinderHelper {

	private static final double FURTHEST_NORTH = 51.107912;
	private static final double FURTHEST_SOUTH = 51.101359;
	private static final double FURTHEST_EAST = 17.078696;
	private static final double FURTHEST_WEST = 17.068376;

	private static final String LOG_TAG = XmlObjectFinderHelper.class
			.getSimpleName();
	Location mLocation;
	ArrayList<XmlObject> mAllctXmlObjects = new ArrayList<XmlObject>();
	Graph mGraph;
	Handler mHandler;
	Context mContext;

	public XmlObjectFinderHelper(Location location, MyGuideApp app, Context context,
			XmlObject object) {
		mLocation = location;
		if (object.getClass().equals(Animal.class)) {
			mAllctXmlObjects.addAll(app.getZooData().getAnimals());
		} else if (object.getClass().equals(Restaurant.class)) {
			mAllctXmlObjects.addAll(app.getZooData().getRestaurant());
		}
		mGraph = app.getGraph();
		mContext = context;
		mHandler = new Handler();
	}

	/**
	 * Creates Node from user's position for mGraph method findDistance
	 * {@linkplain com.blstream.myguide.zoolocations.Graph#findDistance}.
	 * Currently unused due to replacing {@link Graph#findDistance(Node, Node)}
	 * with {@link MathHelper#distanceBetween(Node, double, double)}
	 */
	private Node myPosition() {
		return new Node(mLocation.getLatitude(), mLocation.getLongitude());
	}

	/**
	 * Calculates distances of all animals and returns the array of results.
	 * 
	 * @return double array containing distances to every animal
	 */

	private double[] distancesToAllXmlObjects() {
		final double[] distances = new double[mAllctXmlObjects.size()];

		for (int i = 0; i < distances.length; i++) {
			distances[i] = MathHelper.distanceBetween(mAllctXmlObjects.get(i)
					.getNode(), mLocation.getLatitude(), mLocation
					.getLongitude());
		}

		return distances;
	}

	/**
	 * Returns list of pairs (Animal, distance) based on list of Animals and
	 * list of distances
	 * {@link com.blstream.myguide.NearestAnimalsListFragment#distancesToAllAnimals()}
	 */
	public ArrayList<XmlObjectDistance> allXmlObjectsWithDistances() {
		ArrayList<XmlObjectDistance> result = new ArrayList<XmlObjectDistance>();
		double[] distances = distancesToAllXmlObjects();
		for (int i = 0; i < distances.length; i++) {
			result.add(new XmlObjectDistance(mAllctXmlObjects.get(i), distances[i]));
		}

		Collections.sort(result);
		return result;
	}

	/**
	 * Returns pair (Animal, distance) for the animal which is the closest to
	 * User; if user's localization is unknown or not within bounds of the Zoo,
	 * returns null.
	 */
	public XmlObjectDistance closestXmlObject() {
		if (mLocation != null && withinBoundsOfZoo(mLocation)) { return allXmlObjectsWithDistances()
				.get(0); }
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
