
package com.blstream.myguide.zoolocations;

import java.util.ArrayList;

/**
 * This class contains location of junction and list of ways which cross there.
 */
public class Junction {

	private Node mNode;
	private ArrayList<Way> mWays;

	public Junction(Node node, ArrayList<Way> ways) {
		mNode = node;
		mWays = ways;
	}

	public void setWays(ArrayList<Way> ways) {
		mWays = ways;
	}

	public void setNode(Node node) {
		mNode = node;
	}

	public ArrayList<Way> getWays() {
		return mWays;
	}

	public Node getNode() {
		return mNode;
	}
}
