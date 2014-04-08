
package com.blstream.myguide.path;

public class Edge {

	private double mLength;
	private Vertex mVertex1;
	private Vertex mVertex2;

	public Edge(Vertex v1, Vertex v2, double l) {
		mVertex1 = v1;
		mVertex2 = v2;
		mLength = l;
	}

	public void setVertex1(Vertex vertex1) {
		mVertex1 = vertex1;
	}

	public void setVertex2(Vertex vertex2) {
		mVertex2 = vertex2;
	}

	public void setLength(double length) {
		mLength = length;
	}

	public Vertex getVertex1() {
		return mVertex1;
	}

	public Vertex getVertex2() {
		return mVertex2;
	}

	public double getLength() {
		return mLength;
	}

	@Override
	public boolean equals(Object o) {
		Edge edge = (Edge) o;
		if (mVertex1.equals(edge.getVertex1())
				&& mVertex2.equals(edge.getVertex2())) { return true; }
		if (mVertex1.equals(edge.getVertex2())
				&& mVertex2.equals(edge.getVertex1())) { return true; }
		return false;
	}

}
