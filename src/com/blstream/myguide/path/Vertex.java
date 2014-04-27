
package com.blstream.myguide.path;

import java.util.ArrayList;

import com.blstream.myguide.zoolocations.Node;

public class Vertex implements Comparable<Vertex> {

	private double mWeight;
	private ArrayList<Edge> mEdges;
	private Vertex mPredecessor;
	private Node mPosition;
	private int mHeapIndex;

	public Vertex() {
		mEdges = new ArrayList<Edge>();
	}

	public void setWeight(double weight) {
		mWeight = weight;
	}

	public void setPredecessor(Vertex predecessor) {
		mPredecessor = predecessor;
	}

	public void setPosition(Node position) {
		mPosition = position;
	}

	public double getWeight() {
		return mWeight;
	}

	public ArrayList<Edge> getEdges() {
		return mEdges;
	}

	public Vertex getPredecessor() {
		return mPredecessor;
	}

	public Node getPosition() {
		return mPosition;
	}

	public void addEdge(Edge e) {
		mEdges.add(e);
	}

	@Override
	public int compareTo(Vertex v2) {
		Double weight1 = this.getWeight();
		Double weight2 = v2.getWeight();
		if (weight1 == Graph.INFINITY) { return 1; }
		if (weight2 == Graph.INFINITY) { return -1; }
		return weight1.compareTo(weight2);
	}

	public void setHeapIndex(int index) {
		mHeapIndex = index;
	}
	
	public int getHeapIndex() {
		return mHeapIndex;
	}
}
