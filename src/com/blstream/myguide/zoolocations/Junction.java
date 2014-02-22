package com.blstream.myguide.zoolocations;

import java.util.ArrayList;

/** This class contains location of junction and list of ways which cross there.
 * */
public class Junction {
	
	private Node node;
	private ArrayList<Way> ways;
	
	public Junction(Node node, ArrayList<Way> ways) {
		this.node = node;
		this.ways = ways;
	}
	
	public void setWays(ArrayList<Way> ways) {
		this.ways = ways;
	}
	
	public void setNode(Node node) {
		this.node = node;
	}

	public ArrayList<Way> getWays() {
		return ways;
	}
		
	public Node getNode() {
		return node;
	}
}
