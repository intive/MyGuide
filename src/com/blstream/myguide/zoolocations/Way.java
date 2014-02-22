package com.blstream.myguide.zoolocations;

import java.util.ArrayList;

/** This class contains way's id and locations of its nodes.
 * */
public class Way {
	
	private int id;
	private ArrayList<Node> nodes;
	
	public Way(int id, ArrayList<Node> nodes) {
		this.id = id;
		this.nodes = nodes;
	}
	
	public int getId() {
		return id;
	}
	
	public ArrayList<Node> getNodes() {
		return nodes;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public void setNodes(ArrayList<Node> nodes) {
		this.nodes = nodes;
	}
}
