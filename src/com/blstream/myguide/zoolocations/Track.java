
package com.blstream.myguide.zoolocations;

import java.io.Serializable;
import java.util.HashMap;
import java.util.ArrayList;

public class Track implements Serializable {

	private String mImage;
	private HashMap<String, String> mNames;
	private HashMap<String, String> mDescriptions;
	private ArrayList<Animal> mAnimals;
    private int mVisted;

	public Track() {
		mNames = new HashMap<String, String>();
		mDescriptions = new HashMap<String, String>();
		mAnimals = new ArrayList<Animal>();
	}

	public void setImage(String image) {
		mImage = image;
	}

	public void setNames(HashMap<String, String> names) {
		mNames = names;
	}

	public void setDescriptions(HashMap<String, String> descriptions) {
		mDescriptions = descriptions;
	}

	public void setAnimals(ArrayList<Animal> animals) {
		mAnimals = animals;
	}

	public String getImage() {
		return mImage;
	}

	public String getName() {
		return mNames.get(Language.DEFAULT);
	}

	public String getName(String language) {
		String name = mNames.get(language);
		if (name == null) { return this.getName(); }
		return name;
	}

	public String getDescription() {
		return mDescriptions.get(Language.DEFAULT);
	}

	public String getDescription(String language) {
		String description = mDescriptions.get(language);
		if (description == null) { return this.getDescription(); }
		return description;
	}

	public ArrayList<Animal> getAnimals() {
		return mAnimals;
	}

    public void setVisited(int visited) {
        mVisted = visited;
    }

    public int getVisited() {
        return mVisted;
    }

}
