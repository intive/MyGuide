
package com.blstream.myguide.zoolocations;

/** This class contains animal name and its location in the zoo. */
public class Animal {

	private String name;
	private Node node;

	public Animal(String name, Node node) {
		this.name = name;
		this.node = node;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	public String getName() {
		return name;
	}

	public Node getNode() {
		return node;
	}

}
