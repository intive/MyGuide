package com.blstream.myguide.zoolocations;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.TreeMap;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;
import android.util.Xml;

public class ZooLocationsDataParser {
	
	private final static String LOG_TAG = "zoolocations"; 
	
	private ArrayList<Animal> animals;
	private ArrayList<Way> ways;
	private ArrayList<Junction> junctions;
	private TreeMap<Integer, Way> waysMap;
	
	public ZooLocationsData parse(InputStream in) throws XmlPullParserException, IOException {	
		animals = new ArrayList<Animal>();
		ways = new ArrayList<Way>();
		junctions = new ArrayList<Junction>();
		waysMap = new TreeMap<Integer, Way>();
		
		XmlPullParser parser = Xml.newPullParser();
		parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
		parser.setInput(in, "UTF-8");
		parser.nextTag();
		readRoot(parser);
	
		return new ZooLocationsData(animals, ways, junctions);
	}
			
	private void readRoot(XmlPullParser parser) throws XmlPullParserException, IOException {

		parser.require(XmlPullParser.START_TAG, null, "root");

		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			if (name.equals("animals")) {
				readAnimals(parser);
			} else if (name.equals("ways")) {
				readWays(parser);				
			} else if (name.equals("junctions")) {
				readJunctions(parser);				
			} else {
				skip(parser);
			}
		}
	}
		
	private void readAnimals(XmlPullParser parser) throws XmlPullParserException, IOException {

		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			if (name.equals("animal")) {
				animals.add(readAnimal(parser));
			} else {
				skip(parser);
			}
		}
	}

	private Animal readAnimal(XmlPullParser parser) throws XmlPullParserException, IOException {
		Node node = readAtributesLatLon(parser);
		String name = readText(parser);
		return new Animal(name, node);
	}

	private Node readAtributesLatLon(XmlPullParser parser) throws XmlPullParserException, IOException {
		String latitudeStr = parser.getAttributeValue(null, "lat");
		String longitudeStr = parser.getAttributeValue(null, "lon");
		Double latitude, longitude;
		if (latitudeStr == null || longitudeStr == null) {
			throw new XmlPullParserException("At least one of coordinates is missing.");
		}
		try {
			latitude = Double.parseDouble(latitudeStr);
		} catch (NumberFormatException e) {
			throw new XmlPullParserException("Atrribute lat is not a Double");
		}
		try {
			longitude = Double.parseDouble(longitudeStr);
		} catch (NumberFormatException e) {
			throw new XmlPullParserException("Atrribute lon is not a Double");
		}
		return new Node(latitude, longitude);
	}

	private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
		String result = "";
		if (parser.next() == XmlPullParser.TEXT) {
			result = parser.getText();
			parser.nextTag();
		}
		return result;
	}

	private void readWays(XmlPullParser parser) throws XmlPullParserException, IOException {

		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			if (name.equals("way")) {
				Way way = readWay(parser);
				ways.add(way);
				waysMap.put(way.getId(), way);
			} else {
				skip(parser);
			}
		}
	}

	private Way readWay(XmlPullParser parser) throws XmlPullParserException, IOException {
		ArrayList<Node> nodes = new ArrayList<Node>();
		int id = readAttributeId(parser);
		
		if (parser.isEmptyElementTag()) {
			parser.nextTag();
			return new Way(id, nodes);
		}
		while (parser.nextTag() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			if (name.equals("node")) {
				nodes.add(readNode(parser));
			} else {
				skip(parser);
			}		
		}
		return new Way(id, nodes);
	}

	private int readAttributeId(XmlPullParser parser) throws XmlPullParserException, IOException {
		String idStr = parser.getAttributeValue(null, "id");
		int id;
		if (idStr == null) {
			throw new XmlPullParserException("Attribute id is missing.");
		}
		try {
			id = Integer.parseInt(idStr);
		} catch (NumberFormatException e) {
			throw new XmlPullParserException("Atrribute id is not a Integer");
		}
		return id;
	}
	
	private Node readNode(XmlPullParser parser) throws XmlPullParserException, IOException {
		Node node = readAtributesLatLon(parser);
		parser.nextTag();
		return node;
	}

	private void readJunctions(XmlPullParser parser) throws XmlPullParserException, IOException {

		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			if (name.equals("junction")) {
				junctions.add(readJunction(parser));
			} else {
				skip(parser);
			}
		}
	}
		
	private Junction readJunction(XmlPullParser parser) throws XmlPullParserException, IOException {

		Node node = readAtributesLatLon(parser);
		ArrayList<Way> ways = new ArrayList<Way>();
		
		while (parser.nextTag() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			if (name.equals("way")) {
				int wayId = readWay(parser).getId();
				Way way = waysMap.get(wayId);
				if (way != null) {
					ways.add(way);
				}
				else {
					Log.wtf(LOG_TAG,"Way of id "+ wayId+" does not exist in parsed xml");
				}
			} else {
				skip(parser);
			}
		}
		
		return new Junction(node, ways);		
	}
	
	private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
		if (parser.getEventType() != XmlPullParser.START_TAG) {
			throw new IllegalStateException();
		}
		int depth = 1;
		while (depth != 0) {
			switch (parser.next()) {
			case XmlPullParser.END_TAG:
				depth--;
				break;
			case XmlPullParser.START_TAG:
				depth++;
				break;
			}
		}
	}
	
}
