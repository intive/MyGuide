
package com.blstream.myguide.path;

import java.util.ArrayList;

import android.test.AndroidTestCase;

import com.blstream.myguide.zoolocations.*;

/** Class containing tests for ZooLocationsDataParser. */
public class GraphTestCase extends AndroidTestCase {

	public void testDijkstraAlgorithm() {
		// given
		Graph g = new Graph();
		Vertex v1 = new Vertex();
		Vertex v2 = new Vertex();
		Vertex v3 = new Vertex();
		Vertex v4 = new Vertex();
		Vertex v5 = new Vertex();
		Vertex v6 = new Vertex();
		Vertex v7 = new Vertex();
		Vertex v8 = new Vertex();
		g.mVertices.add(v1);
		g.mVertices.add(v2);
		g.mVertices.add(v3);
		g.mVertices.add(v4);
		g.mVertices.add(v5);
		g.mVertices.add(v6);
		g.mVertices.add(v7);
		g.mVertices.add(v8);
		Edge e1 = new Edge(v2, v3, 2);
		g.mEdges.add(e1);
		v2.addEdge(e1);
		v3.addEdge(e1);
		Edge e2 = new Edge(v5, v1, 2);
		g.mEdges.add(e2);
		v5.addEdge(e2);
		v1.addEdge(e2);
		Edge e3 = new Edge(v1, v4, 4);
		g.mEdges.add(e3);
		v1.addEdge(e3);
		v4.addEdge(e3);
		Edge e4 = new Edge(v1, v2, 10);
		g.mEdges.add(e4);
		v1.addEdge(e4);
		v2.addEdge(e4);
		Edge e5 = new Edge(v3, v4, 3);
		g.mEdges.add(e5);
		v3.addEdge(e5);
		v4.addEdge(e5);
		Edge e6 = new Edge(v6, v4, 5);
		g.mEdges.add(e6);
		v6.addEdge(e6);
		v4.addEdge(e6);
		Edge e7 = new Edge(v5, v6, 3);
		g.mEdges.add(e7);
		v5.addEdge(e7);
		v6.addEdge(e7);
		Edge e8 = new Edge(v4, v5, 1);
		g.mEdges.add(e8);
		v4.addEdge(e8);
		v5.addEdge(e8);
		Edge e9 = new Edge(v7, v8, 2);
		g.mEdges.add(e9);
		v7.addEdge(e9);
		v8.addEdge(e9);
		// when
		ArrayList<Vertex> path1 = g.findPathBetweenVertices(v1, v2);
		ArrayList<Vertex> path2 = g.findPathBetweenVertices(v2, v1);
		ArrayList<Vertex> path3 = g.findPathBetweenVertices(v1, v7);
		ArrayList<Vertex> path4 = g.findPathBetweenVertices(v1, v1);

		// then
		assertEquals(path1.size(), 5);
		assertEquals(path1.get(0), v2);
		assertEquals(path1.get(1), v3);
		assertEquals(path1.get(2), v4);
		assertEquals(path1.get(3), v5);
		assertEquals(path1.get(4), v1);

		assertEquals(path2.size(), 5);
		assertEquals(path2.get(4), v2);
		assertEquals(path2.get(3), v3);
		assertEquals(path2.get(2), v4);
		assertEquals(path2.get(1), v5);
		assertEquals(path2.get(0), v1);

		assertEquals(path3.size(), 0);

		assertEquals(path4.size(), 1);
		assertEquals(path4.get(0), v1);
	}

	public void testCreatingGraph() {
		// given
		ArrayList<Way> ways = new ArrayList<Way>();
		ArrayList<Junction> junctions = new ArrayList<Junction>();

		Node n1 = new Node(0, 0);
		Node n2 = new Node(1, 1);
		Node n3 = new Node(2, 1);

		ArrayList<Node> nodes1 = new ArrayList<Node>();
		nodes1.add(n1);
		nodes1.add(n2);
		nodes1.add(n3);

		Node n4 = new Node(0, 0); // = n1
		Node n5 = new Node(1, 0);
		Node n6 = new Node(2, 1); // = n2

		ArrayList<Node> nodes2 = new ArrayList<Node>();
		nodes2.add(n4);
		nodes2.add(n5);
		nodes2.add(n6);

		Way w1 = new Way(1, nodes1);
		Way w2 = new Way(2, nodes2);

		ways.add(w1);
		ways.add(w2);
		junctions.add(new Junction(new Node(0, 0), ways));

		// when
		Graph g = new Graph();
		g.createGraph(ways, junctions);

		// then
		assertEquals(g.mVertices.size(), 4);
		assertEquals(g.mEdges.size(), 4);
	}
}
