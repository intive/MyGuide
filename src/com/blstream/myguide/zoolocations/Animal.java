
package com.blstream.myguide.zoolocations;

import java.io.Serializable;

/** This class contains animal name and its location in the zoo. */
public class Animal implements Serializable{

	private String mName;
	private Node mNode;

	public Animal(String name, Node node) {
		mName = name;
		mNode = node;
	}

	public void setName(String name) {
		mName = name;
	}

	public void setNode(Node node) {
		mNode = node;
	}

	public String getName() {
		return mName;
	}

	public Node getNode() {
		return mNode;
	}
}
