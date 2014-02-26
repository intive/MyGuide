
package com.blstream.myguide.zoolocations;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.TreeMap;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

/** Class containing function parsing xml file and messages for exceptions. */
public class ZooLocationsDataParser {

	public static class WayNotFoundException extends XmlPullParserException {

		public WayNotFoundException(String mes) {
			super(mes);
		}
	}

	private static final String ENCODING = "UTF-8";

	private ArrayList<Animal> mAnimals;
	private ArrayList<Way> mWays;
	private ArrayList<Junction> mJunctions;
	private TreeMap<Integer, Way> mWaysMap;

	/**
	 * This function parses chosen xml file and returns data saved in
	 * {@link ZooLocationData} object. If there are ways in junction tag which
	 * are not defined in ways tag, function throws exception.
	 * 
	 * @param is InputStream of xml file
	 * @return parsed data from xml file
	 */
	public ZooLocationsData parse(InputStream in) throws XmlPullParserException, IOException {
		mAnimals = new ArrayList<Animal>();
		mWays = new ArrayList<Way>();
		mJunctions = new ArrayList<Junction>();
		mWaysMap = new TreeMap<Integer, Way>();

		XmlPullParser parser = Xml.newPullParser();
		parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
		parser.setInput(in, ENCODING);
		parser.nextTag();
		readRoot(parser);

		return new ZooLocationsData(mAnimals, mWays, mJunctions);
	}

	private void readRoot(XmlPullParser parser) throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, null, "root");

		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			if ("animals".equals(name)) {
				readAnimals(parser);
			} else if ("ways".equals(name)) {
				readWays(parser);
			} else if ("junctions".equals(name)) {
				readJunctions(parser);
			} else {
				skip(parser);
			}
		}

		ArrayList<Junction> junctionsWithWays = new ArrayList<Junction>();
		for (Junction j : mJunctions) {
			ArrayList<Way> waysInJunction = new ArrayList<Way>();
			for (Way w : j.getWays()) {
				Way way = mWaysMap.get(w.getId());
				if (way != null) {
					waysInJunction.add(way);
				} else {
					throw new WayNotFoundException("Way has not found in parsed xml");
				}
			}
			junctionsWithWays.add(new Junction(j.getNode(), waysInJunction));
		}
		mJunctions = junctionsWithWays;
	}

	private void readAnimals(XmlPullParser parser) throws XmlPullParserException, IOException {

		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			if ("animal".equals(name)) {
				mAnimals.add(readAnimal(parser));
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

	private Node readAtributesLatLon(XmlPullParser parser) throws XmlPullParserException,
			IOException {
		String latitudeStr = parser.getAttributeValue(null, "lat");
		String longitudeStr = parser.getAttributeValue(null, "lon");
		double latitude = Double.parseDouble(latitudeStr);
		double longitude = Double.parseDouble(longitudeStr);
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
			if ("way".equals(name)) {
				Way way = readWay(parser);
				mWays.add(way);
				mWaysMap.put(way.getId(), way);
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
			if ("node".equals(name)) {
				nodes.add(readNode(parser));
			} else {
				skip(parser);
			}
		}
		return new Way(id, nodes);
	}

	private int readAttributeId(XmlPullParser parser) throws XmlPullParserException, IOException {
		String idStr = parser.getAttributeValue(null, "id");
		int id = Integer.parseInt(idStr);
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
			if ("junction".equals(name)) {
				mJunctions.add(readJunction(parser));
			} else {
				skip(parser);
			}
		}
	}

	private Junction readJunction(XmlPullParser parser) throws XmlPullParserException, IOException {

		Node node = readAtributesLatLon(parser);
		ArrayList<Way> waysInJunction = new ArrayList<Way>();

		while (parser.nextTag() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			if ("way".equals(name)) {
				waysInJunction.add(readWay(parser));
			} else {
				skip(parser);
			}
		}

		return new Junction(node, waysInJunction);
	}

	private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
		if (parser.getEventType() != XmlPullParser.START_TAG) { throw new IllegalStateException(); }
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
