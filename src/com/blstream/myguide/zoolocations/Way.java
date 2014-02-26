
package com.blstream.myguide.zoolocations;

import java.util.ArrayList;

/**
 * This class contains way's id and locations of its nodes.
 */
public class Way {

	private int mId;
	private ArrayList<Node> mNodes;

	public Way(int id, ArrayList<Node> nodes) {
		mId = id;
		mNodes = nodes;
	}

	public int getId() {
		return mId;
	}

	public ArrayList<Node> getNodes() {
		return mNodes;
	}

	public void setId(int id) {
		mId = id;
	}

	public void setNodes(ArrayList<Node> nodes) {
		mNodes = nodes;
	}
}
