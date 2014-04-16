
package com.blstream.myguide.path;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

import com.blstream.myguide.zoolocations.Junction;
import com.blstream.myguide.zoolocations.Node;
import com.blstream.myguide.zoolocations.Way;

public class Graph {

	protected static final double INFINITY = -1; // should be a negative number
	private static final double ZOO_LATITUDE = 51.10503;
	private static final double LON_COEFFICIENT = Math.cos(Math.toRadians(ZOO_LATITUDE));
	private static final double METERS_COEFFICIENT = 6378410*Math.PI/180.0;

	protected ArrayList<Vertex> mVertices;
	protected ArrayList<Edge> mEdges;
	private ArrayList<Vertex> mToRemove;

	public Graph() {
		mVertices = new ArrayList<Vertex>();
		mEdges = new ArrayList<Edge>();
	}

	/* Approximate distance between two points. */
	private double distanceApproximate(Node n1, Node n2) {
		double deltaLat = (n1.getLatitude() - n2.getLatitude());
		double deltaLon = (n1.getLongitude() - n2.getLongitude()) * LON_COEFFICIENT;
		return Math.sqrt(deltaLat * deltaLat + deltaLon * deltaLon);
	}

	/** Find distance in meters from point start to point end.*/
	public double findDistance(Node start, Node end) {
		mToRemove = new ArrayList<Vertex>();
		Vertex startVertex = findNearVertex(start);
		Vertex endVertex = findNearVertex(end);
		List<Node> path = new ArrayList<Node>();
		ArrayList<Vertex> vertices = new ArrayList<Vertex>();
		double  result = INFINITY;
		
		if (startVertex != null && endVertex != null) {
			vertices = findPathBetweenVertices(startVertex, endVertex);
			if (vertices.size() > 0) {
				result = METERS_COEFFICIENT * endVertex.getWeight();
				result += METERS_COEFFICIENT * distanceApproximate(end, endVertex.getPosition());
				result += METERS_COEFFICIENT * distanceApproximate(start, startVertex.getPosition());
			}
		}

		removeTemporaryVertices();
		return result;
	}

	private void removeTemporaryVertices() {
		if (mToRemove == null) {
			return;
		}
		for (Vertex v : mToRemove) {
			Edge e1 = v.getEdges().get(0);
			Edge e2 = v.getEdges().get(1);
			Vertex v1 = e1.getVertex2();
			Vertex v2 = e2.getVertex2();
			mVertices.remove(v);
			v1.getEdges().remove(e1);
			v2.getEdges().remove(e2);
		}
		mToRemove = null;		
	}
	/*
	 * Method finds vertex or edge which is nearest to point n. If it is an edge
	 * then method add new vertex to graph. New vertex is added to list
	 * 'mToRemove'.
	 */
	private Vertex findNearVertex(Node n) {
		double minLength = INFINITY;
		Vertex nearVertex = null;
		Edge nearEdge = null;
		// looking for a nearest vertex
		for (Vertex v : mVertices) {
			double l = distanceApproximate(n, v.getPosition());
			if (l < minLength || minLength == INFINITY) {
				nearVertex = v;
				minLength = l;
			}
		}
		// looking for a nearest edge
		for (Edge e : mEdges) {
			double x0 = n.getLatitude();
			double y0 = n.getLongitude() * LON_COEFFICIENT;
			double x1 = e.getVertex1().getPosition().getLatitude();
			double y1 = e.getVertex1().getPosition().getLongitude() * LON_COEFFICIENT;
			double x2 = e.getVertex2().getPosition().getLatitude();
			double y2 = e.getVertex2().getPosition().getLongitude() * LON_COEFFICIENT;
			double t = ((x0 - x2) * (x1 - x2) + (y0 - y2) * (y1 - y2))
					/ ((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
			if (t > 0 && t < 1) {
				double x3 = x2 + (x1 - x2) * t;
				double y3 = (y2 + (y1 - y2) * t) / LON_COEFFICIENT;
				double l = distanceApproximate(n, new Node(x3, y3));
				if (l < minLength) {
					minLength = l;
					nearVertex = new Vertex();
					nearVertex.setPosition(new Node(x3, y3));
					nearEdge = e;
				}
			}
		}
		// adding vertex laying on edge to graph (temporary)
		if (nearEdge != null) {
			mVertices.add(nearVertex);
			mToRemove.add(nearVertex);
			Vertex v1 = nearEdge.getVertex1();
			Vertex v2 = nearEdge.getVertex2();
			Edge e1 = new Edge(nearVertex, v1, distanceApproximate(nearVertex.getPosition(),
					v1.getPosition()));
			Edge e2 = new Edge(nearVertex, v2, distanceApproximate(nearVertex.getPosition(),
					v2.getPosition()));
			v1.addEdge(e1);
			v2.addEdge(e2);
			nearVertex.addEdge(e1);
			nearVertex.addEdge(e2);
		}
		return nearVertex;
	}

	private Vertex findVertexInGraph(Node n) {
		for (Vertex v : mVertices) {
			if (v.getPosition().getLatitude() == n.getLatitude()
			&& v.getPosition().getLongitude() == n.getLongitude() ) {
				return v;
			}
		}
		return null;
	}
	/**
	 * Creates graph using ways and junctions. Additionally it search for way's
	 * connections not included in xml.
	 */
	public void createGraph(ArrayList<Way> ways, ArrayList<Junction> junctions) {
		mVertices = new ArrayList<Vertex>();
		mEdges = new ArrayList<Edge>();
		HashMap<Way, ArrayList<Vertex>> verticesInWays = new HashMap<Way, ArrayList<Vertex>>();

		for (Way w : ways) {
			ArrayList<Vertex> verticesInWay = new ArrayList<Vertex>();
			Vertex last = null;
			for (Node n : w.getNodes()) {
				Vertex v = findVertexInGraph(n);
				if (v == null) {
					v = new Vertex();
					v.setPosition(new Node(n.getLatitude(), n.getLongitude()) );
					mVertices.add(v);
				}
				verticesInWay.add(v);
				if (last != null) {
					Edge e = new Edge(last, v, distanceApproximate(last.getPosition(),
							v.getPosition()));
					mEdges.add(e);
					v.addEdge(e);
					last.addEdge(e);
				}
				last = v;
			}
			verticesInWays.put(w, verticesInWay);
		}

		for (Junction j : junctions) {
			Vertex v = findVertexInGraph(j.getNode());
			if (v == null) {
				v = new Vertex();
				v.setPosition(new Node(j.getNode().getLatitude(), j.getNode().getLongitude()));
				mVertices.add(v);
			}
			for (Way w : j.getWays()) {
				double length = INFINITY;
				Vertex near = null;
				for (Vertex v2 : verticesInWays.get(w)) {
					double distance = distanceApproximate(v.getPosition(), v2.getPosition());
					if (length == INFINITY || length > distance) {
						near = v2;
						length = distance;
					}
				}
				if (near != null) {
					Edge e = new Edge(v, near, length);
					if (v != near && !near.getEdges().contains(e)) {
						mEdges.add(e);
						near.addEdge(e);
						v.addEdge(e);
					}
				}
			}
		}
	}

	/**
	 * Find path in graph between two points (these points are not necessary in
	 * graph).
	 */
	public List<Node> findPath(Node start, Node end) {

		mToRemove = new ArrayList<Vertex>();
		Vertex startVertex = findNearVertex(start);
		Vertex endVertex = findNearVertex(end);
		List<Node> path = new ArrayList<Node>();
		ArrayList<Vertex> vertices = new ArrayList<Vertex>();

		if (startVertex != null && endVertex != null) {
			vertices = findPathBetweenVertices(startVertex, endVertex);
			for (int i = vertices.size() - 1; i >= 0; i--) {
				path.add(vertices.get(i).getPosition());
			}
		}

		removeTemporaryVertices();
		return path;
	}

	/** Find path between two vertices in graph. */
	protected ArrayList<Vertex> findPathBetweenVertices(Vertex start, Vertex end) {

		// Dijskra's algorithm
		for (Vertex v : mVertices) {
			v.setWeight(INFINITY);
		}
		start.setWeight(0);
		start.setPredecessor(null);

		PriorityQueue<Vertex> queue = new PriorityQueue<Vertex>();
		queue.add(start);

		while (true) {
			Vertex u = queue.poll();
			if (u == null || u == end) {
				break;
			}
			for (Edge e : u.getEdges()) {
				Vertex v = e.getVertex1();
				if (v == u) {
					v = e.getVertex2();
				}
				if (u.getWeight() + e.getLength() < v.getWeight()
						|| v.getWeight() == Graph.INFINITY) {
					v.setWeight(u.getWeight() + e.getLength());
					v.setPredecessor(u);
					queue.add(v);
				}
			}
		}

		// creating result
		ArrayList<Vertex> path = new ArrayList<Vertex>();
		if (end.getPredecessor() == null && end != start) { return path; }
		path.add(end);
		while (end.getPredecessor() != null) {
			path.add(end.getPredecessor());
			end = end.getPredecessor();
		}
		return path;
	}

}
