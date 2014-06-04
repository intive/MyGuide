
package com.blstream.myguide.zoolocations;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Piotrek on 2014-04-07.
 */
public class Restaurant extends XmlObject implements Serializable {

	private HashMap<String, String> mNames;
	private Node mNode;
	private String mOpen;
	private ArrayList<Dish> mDishes;
	private int mDistance;

	public Restaurant() {
		mNames = new HashMap<String, String>();
	}

	@Override
	public void addName(String lang, String name) {
		mNames.put(lang, name);
	}

	public void setNode(Node node) {
		mNode = node;
	}

	@Override
	public Node getNode() {
		return mNode;
	}

	public void setDishes(ArrayList<Dish> dishes) {
		mDishes = dishes;
	}

	public ArrayList<Dish> getDishes() {
		return mDishes;
	}

	@Override
	public String getName() {
		return mNames.get(Language.DEFAULT);
	}

	public void setOpen(String open) {
		mOpen = open;
	}

	public String getOpen() {
		return mOpen;
	}

	public void setNames(HashMap<String, String> names) {
		mNames = names;
	}

	public String getName(String language) {
		String name = mNames.get(language);
		if (name == null) { return this.getName(); }
		return name;
	}

	public void setDistance(int distance) {
		mDistance = distance;
	}

	public int getDistance() {
		return mDistance;
	}
}
