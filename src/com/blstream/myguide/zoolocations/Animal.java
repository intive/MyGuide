
package com.blstream.myguide.zoolocations;

import java.util.TreeMap;

/**
 * This class contains animal names (in various languages), descriptions for
 * adult and child and animal's location in the zoo.
 */
public class Animal {

	private TreeMap<String, String> mName;
	private Node mNode;
	private Description mDescriptionAdult;
	private Description mDescriptionChild;

	public void setName(TreeMap<String, String> name) {
		mName = name;
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

	public String getName() {
		return mName.get(Language.DEFAULT);
	}

	public String getName(String language) {
		String name = mName.get(language);
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
}
