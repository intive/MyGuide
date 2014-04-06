package com.blstream.myguide.path;
 
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

import android.util.Log;

import com.blstream.myguide.zoolocations.*;

public class Graph {
	
	public static final double INFINITY = -1;	// should be a negative number
	private static final double ZOO_LAT = 51.10503;
	private static final double LON_COEFFICIENT = Math.cos(ZOO_LAT);

	private ArrayList<Vertex> mVertices;
	private ArrayList<Edge> mEdges;
	private ArrayList<Vertex> mToRemove;
	 

	public Graph() {
		mVertices = new ArrayList<Vertex>();
		mEdges = new ArrayList<Edge>();
	}

	/* Approximate distance between two points.*/
	private double distanceApproximate(Node n1, Node n2) {		
		double deltaLat = (n1.getLatitude() - n2.getLatitude());
		double deltaLon = (n1.getLongitude() - n2.getLongitude()) * LON_COEFFICIENT;
		return Math.sqrt(deltaLat*deltaLat + deltaLon*deltaLon);
	}
	
	/* Method finds vertex or edge which is nearest to point n. If it is an edge then
	 * method add new vertex to graph. New vertex is added to list 'mToRemove'.*/
	private Vertex findNearVertex(Node n) {
		double minLength = INFINITY;
		Vertex nearVertex = null;
		Edge nearEdge = null;
		
		for (Vertex v : mVertices) {
			double l = distanceApproximate(n, v.getPosition());			
			if (l < minLength || minLength == INFINITY) {
				nearVertex = v;
				minLength = l;
			}
		}
		
		for (Edge e : mEdges) {
			double x0 = n.getLatitude();
			double y0 = n.getLongitude() * LON_COEFFICIENT;
			double x1 = e.getVertex1().getPosition().getLatitude();
			double y1 = e.getVertex1().getPosition().getLongitude() * LON_COEFFICIENT;
			double x2 = e.getVertex2().getPosition().getLatitude();
			double y2 = e.getVertex2().getPosition().getLongitude() * LON_COEFFICIENT;	
			double t = ((x0-x2)*(x1-x2) + (y0-y2)*(y1-y2)) / ( (x1-x2)*(x1-x2) + (y1-y2)*(y1-y2) );
			if (t > 0 && t < 1) {
				double x3 = x2 + (x1-x2)*t;
				double y3 = (y2 + (y1-y2)*t) / LON_COEFFICIENT;
				double l = distanceApproximate(n, new Node(x3,y3));
				if (l < minLength) {
					minLength = l;
					nearVertex = new Vertex();
					nearVertex.setPosition(new Node(x3, y3));
					nearEdge = e;
				}
			}
		}
		if (nearEdge != null) {
			mVertices.add(nearVertex);
			mToRemove.add(nearVertex);
			Vertex v1 = nearEdge.getVertex1();
			Vertex v2 = nearEdge.getVertex2();
			Edge e1 = new Edge(nearVertex, v1, distanceApproximate(nearVertex.getPosition(), v1.getPosition()));
			Edge e2 = new Edge(nearVertex, v2, distanceApproximate(nearVertex.getPosition(), v2.getPosition()));
		}
		return nearVertex;
	}
	
	/** Creates graph usuing ways and junctions. Additionally it search for way's connections
	 * not included in xml.*/
	public void createGraph(ArrayList<Way> ways, ArrayList<Junction> junctions) {

		HashMap<Node, Vertex> vertices = new HashMap<Node, Vertex>();
		HashMap<Way, ArrayList<Vertex>> verticesInWays = new HashMap<Way, ArrayList<Vertex>>();
		
		for (Way w: ways) {
			ArrayList<Vertex> verticesInWay = new ArrayList<Vertex>();
			Vertex last = null;
			for (Node n : w.getNodes()) {
				Double lat = n.getLatitude();
				Double lon = n.getLongitude();
				Vertex v;
				try {
					v = vertices.get(n);
				} catch (NullPointerException e) {
					v = null;
				}
				if (v == null) {				
					v = new Vertex();
					vertices.put(new Node(lat, lon), v);
					v.setPosition(new Node(lat, lon));
					mVertices.add(v);
					verticesInWay.add(v);
				}
				if (last != null) {
					Edge e = new Edge(last, v, distanceApproximate(last.getPosition(), v.getPosition()) );
					mEdges.add(e);
				}
				last = v;
			} 
			verticesInWays.put(w, verticesInWay);
		}
		
		for (Junction j : junctions) {
			Vertex v;
			Double lat = j.getNode().getLatitude();
			Double lon = j.getNode().getLongitude();
			try {
				v = vertices.get(j.getNode());
			} catch (NullPointerException e) {
				v = null;
			}
			if (v == null) {	
				v = new Vertex();			
				v.setPosition(new Node(lat, lon));
				mVertices.add(v);	
			}
			for (Way w : j.getWays()) {
				double length = INFINITY;
				Vertex near = null;
				for (Vertex v2 : verticesInWays.get(w)) {
					double distance =  distanceApproximate(v.getPosition(), v2.getPosition());
					if (length == INFINITY || length > distance) {
						near = v2;
						length = distance;
					}
				}
				if (near != null) {
					Edge e = new Edge(v, near, length);
				}
			}
		}		
	}

	/** Find path in graph between two points (these points are not necessary in graph).
	 * Path is reversed.*/
	public List<Node> findPath(Node start, Node end) {
		
		mToRemove = new ArrayList<Vertex>();
		Vertex startVertex = findNearVertex(start);
		Vertex endVertex = findNearVertex(end);		
		List<Node> path = new ArrayList<Node>();
		
		if (startVertex != null && endVertex != null) {
			path = findPathBetweenVertices(startVertex, endVertex);
		}
		
		//removing added vertices		
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
		
		return path;
	}
		
	/** Find path between two vertices in graph.*/
	public List<Node> findPathBetweenVertices(Vertex start, Vertex end) {
		
		// Dijskra algorithm
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
				if (u.getWeight() + e.getLength() < v.getWeight() || v.getWeight() == Graph.INFINITY) {
					v.setWeight(u.getWeight() + e.getLength());
					v.setPredecessor(u);
					queue.add(v);
				}
			}
		}
	
		// creating result
		List<Node> path = new ArrayList<Node>();
		path.add(end.getPosition());
		while (end.getPredecessor() != null) {
			path.add(end.getPredecessor().getPosition());
			end = end.getPredecessor();
		}
		return path;	
	}
	
}
