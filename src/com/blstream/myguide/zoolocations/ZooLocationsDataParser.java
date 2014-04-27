
package com.blstream.myguide.zoolocations;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;
import android.util.Xml;

/** Class containing function parsing xml file and messages for exceptions. */
public class ZooLocationsDataParser {

	private static final String LOG_TAG = ZooLocationsDataParser.class.getSimpleName();

	public static class WayNotFoundException extends XmlPullParserException {

		public WayNotFoundException(String mes) {
			super(mes);
		}
	}

	public static class AnimalNotFoundException extends XmlPullParserException {

		public AnimalNotFoundException(String mes) {
			super(mes);
		}
	}

	private static final String ENCODING = "UTF-8";

	private ArrayList<Animal> mAnimals;
	private ArrayList<Way> mWays;
	private ArrayList<Junction> mJunctions;
	private ArrayList<Track> mTracks;
	private ArrayList<Restaurant> mRestaurants;
	private TicketsInformation mTicketInformation;
	private AccessInformation mAccessInfo;

	private HashMap<Integer, Way> mWaysMap;
	private HashMap<Integer, Animal> mAnimalsMap;

	/**
	 * This function parses chosen xml file and returns data saved in object. If
	 * there are ways in junction tag which are not defined in ways tag,
	 * function throws exception.
	 * 
	 * @param in InputStream of xml file
	 * @return parsed data from xml file
	 */
	public ZooLocationsData parse(InputStream in) throws XmlPullParserException, IOException {
		mAnimals = new ArrayList<Animal>();
		mWays = new ArrayList<Way>();
		mJunctions = new ArrayList<Junction>();
		mTracks = new ArrayList<Track>();
		mRestaurants = new ArrayList<Restaurant>();
		mTicketInformation = new TicketsInformation();
		mWaysMap = new HashMap<Integer, Way>();
		mAnimalsMap = new HashMap<Integer, Animal>();
		mAccessInfo = new AccessInformation();

		XmlPullParser parser = Xml.newPullParser();
		parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
		parser.setInput(in, ENCODING);
		parser.nextTag();
		readRoot(parser);

		ZooLocationsData data = new ZooLocationsData();
		data.setAnimals(mAnimals);
		data.setWays(mWays);
		data.setJunctions(mJunctions);
		data.setTracks(mTracks);
		data.setRestaurants(mRestaurants);
		data.setTicketInformation(mTicketInformation);
		data.setAccessInformation(mAccessInfo);

		return data;
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
			} else if ("visiting_tracks".equals(name)) {
				readTracks(parser);
			} else if ("gastronomy".equals(name)) {
				readGastronomy(parser);
			} else if ("tickets_information".equals(name)) {
				readTicketsInformation(parser);
			} else if ("access_information".equals(name)) {
				readAccessInformation(parser);
			} else {
				skip(parser);
			}
		}

		for (Junction j : mJunctions) {
			ArrayList<Way> waysInJunction = new ArrayList<Way>();
			for (Way w : j.getWays()) {
				Way way = mWaysMap.get(w.getId());
				if (way != null) {
					waysInJunction.add(way);
				} else {
					throw new WayNotFoundException("id: " + w.getId());
				}
			}
			j.setWays(waysInJunction);
		}

		for (Track t : mTracks) {
			ArrayList<Animal> animalsOnTrack = new ArrayList<Animal>();
			for (Animal a : t.getAnimals()) {
				Animal animal = mAnimalsMap.get(a.getId());
				if (animal != null) {
					animalsOnTrack.add(animal);
				} else {
					throw new AnimalNotFoundException("id: " + a.getId());
				}
			}
			t.setAnimals(animalsOnTrack);
		}
	}

	private void readGastronomy(XmlPullParser parser) throws XmlPullParserException, IOException {
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			if ("restaurant".equals(name)) {
				mRestaurants.add(readRestaurant(parser));
			} else {
				skip(parser);
			}
		}
	}

	private Restaurant readRestaurant(XmlPullParser parser) throws XmlPullParserException,
			IOException {
		Restaurant restaurant = new Restaurant();
		Node node = readAtributesLatLon(parser);
		restaurant.setNode(node);

		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			if ("name".equals(name)) {
				restaurant.setNames(readDictionary(parser));
			} else if ("open".equals(name)) {
				restaurant.setOpen(readText(parser));
			} else if ("dishes".equals(name)) {
				restaurant.setDishes(readDishes(parser));
			} else {
				skip(parser);
			}
		}

		return restaurant;
	}

	private ArrayList<Dish> readDishes(XmlPullParser parser) throws XmlPullParserException,
			IOException {
		ArrayList<Dish> dishes = new ArrayList<Dish>();
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			if ("dish".equals(name)) {
				dishes.add(readDish(parser));
			} else {
				skip(parser);
			}
		}
		return dishes;
	}

	private Dish readDish(XmlPullParser parser) throws XmlPullParserException, IOException {
		Dish dish = new Dish();

		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			if ("price".equals(name)) {
				dish.setPrice(Float.parseFloat(readText(parser)));
			} else if ("name".equals(name)) {
				dish.setNames(readDictionary(parser));
			} else {
				skip(parser);
			}
		}

		return dish;
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
		Animal animal = new Animal();
		Node node = readAtributesLatLon(parser);
		animal.setNode(node);
		animal.setId(readAttributeId(parser));

		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			if ("name".equals(name)) {
				animal.setNames(readDictionary(parser));
			} else if ("description_adult".equals(name)) {
				animal.setDescriptionAdult(readDescription(parser));
			} else if ("description_child".equals(name)) {
				animal.setDescriptionChild(readDescription(parser));
			} else {
				skip(parser);
			}
		}
		mAnimalsMap.put(animal.getId(), animal);
		return animal;
	}

	private Description readDescription(XmlPullParser parser) throws XmlPullParserException,
			IOException {
		Description description = new Description();
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			if ("image".equals(name)) {
				description.setImageName(readText(parser));
			} else {
				String language = parser.getName();
				String text = readText(parser);
				description.addText(language, text);
			}
		}
		return description;
	}

	private HashMap<String, String> readDictionary(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		HashMap<String, String> names = new HashMap<String, String>();

		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String language = parser.getName();
			String name = readText(parser);
			names.put(language, name);
		}
		return names;
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

	private void readTracks(XmlPullParser parser) throws XmlPullParserException, IOException {

		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			if ("track".equals(name)) {
				mTracks.add(readTrack(parser));
			} else {
				skip(parser);
			}
		}
	}

	private Track readTrack(XmlPullParser parser) throws XmlPullParserException, IOException {
		Track track = new Track();

		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			if ("name".equals(name)) {
				track.setNames(readDictionary(parser));
			} else if ("description".equals(name)) {
				track.setDescriptions(readDictionary(parser));
			} else if ("image".equals(name)) {
				track.setImage(readText(parser));
			} else if ("animals".equals(name)) {
				track.setAnimals(readAnimalsOnTrack(parser));
			} else {
				skip(parser);
			}
		}
		return track;
	}

	private ArrayList<Animal> readAnimalsOnTrack(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		ArrayList<Animal> animals = new ArrayList<Animal>();
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			if ("animal_id".equals(name)) {
				Animal animal = new Animal();
				animal.setId(Integer.parseInt(readText(parser)));
				animals.add(animal);
			} else {
				skip(parser);
			}
		}
		return animals;
	}

	private void readAccessInformation(XmlPullParser parser) throws XmlPullParserException, IOException {
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}

			String name = parser.getName();
			if ("trams".equals(name)) {
				mAccessInfo.setTrams(readText(parser).trim());
			} else if ("parkings_information".equals(name)) {
				mAccessInfo.setParkingInformation(readDictionary(parser));
			} else {
				skip(parser);
			}
		}
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

	private void readTicketsInformation(XmlPullParser parser) throws XmlPullParserException,
			IOException {
		ArrayList<Ticket> tickets = new ArrayList<Ticket>();
		HashMap<String, String> information = new HashMap<String, String>();

		// using mTickets
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) continue;

			String name = parser.getName();
			if ("individual".equals(name)) {
				tickets.addAll(readTicketSet(Ticket.Type.INDIVIDUAL, parser));
			} else if ("group".equals(name)) {
				tickets.addAll(readTicketSet(Ticket.Type.GROUP, parser));
			} else if ("information".equals(name)) {
				information = readDictionary(parser);
			} else {
				skip(parser);
			}
		}

		mTicketInformation
				.setTickets(tickets)
				.setInformation(information);
	}

	private ArrayList<Ticket> readTicketSet(Ticket.Type ticketType, XmlPullParser parser)
			throws XmlPullParserException, IOException {
		ArrayList<Ticket> tickets = new ArrayList<Ticket>();

		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) continue;

			String name = parser.getName();
			if ("ticket".equals(name)) {
				tickets.add(readTicket(ticketType, parser));
			} else {
				skip(parser);
			}
		}

		return tickets;
	}

	private Ticket readTicket(Ticket.Type ticketType, XmlPullParser parser)
			throws XmlPullParserException, IOException {
		Ticket ticket = new Ticket(ticketType);

		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) continue;

			String name = parser.getName();
			if ("description".equals(name)) {
				HashMap<String, String> dict = readDictionary(parser);
				ticket.setDescriptionDictionary(dict);
			} else if ("price".equals(name)) {
				String text = readText(parser);
				ticket.setPrice(Integer.parseInt(text));
			} else {
				skip(parser);
			}
		}

		return ticket;
	}

}
