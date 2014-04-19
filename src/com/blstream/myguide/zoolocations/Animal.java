
package com.blstream.myguide.zoolocations;

import java.io.Serializable;
import java.util.HashMap;

/**
 * This class contains animal names (in various languages), descriptions for
 * adult and child and animal's location in the zoo.
 */
public class Animal extends XmlObject implements Serializable, Comparable<Animal> {

	private int mId;
	private HashMap<String, String> mNames;
	private Node mNode;
	private Description mDescriptionAdult;
	private Description mDescriptionChild;

	public Animal() {
		mNames = new HashMap<String, String>();
	}

	@Override
	public void addName(String lang, String name) {
		mNames.put(lang, name);
	}

	public void setNames(HashMap<String, String> names) {
		mNames = names;
	}

	public void setNode(Node node) {
		mNode = node;
	}

	public void setDescriptionChild(Description description) {
		mDescriptionChild = description;
	}

	public void setDescriptionAdult(Description description) {
		mDescriptionAdult = description;
	}

	public void setId(int id) {
		mId = id;
	}

	@Override
	public String getName() {
		return mNames.get(Language.DEFAULT);
	}

	public String getName(String language) {
		String name = mNames.get(language);
		if (name == null) { return this.getName(); }
		return name;
	}

	public Node getNode() {
		return mNode;
	}

	public Description getDescriptionChild() {
		return mDescriptionChild;
	}

	public Description getDescriptionAdult() {
		return mDescriptionAdult;
	}

	public int getId() {
		return mId;
	}

	@Override
	public int compareTo(Animal another) {
		return -(another.getName().compareTo(getName()));
	}
}
