
package com.blstream.myguide.zoolocations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * This class contains informations about animals, ways and ways' junctions and
 * theirs locations in the zoo parsed from xml file.
 */
public class ZooLocationsData {

	private ArrayList<Animal> mAnimals;
	private ArrayList<Way> mWays;
	private ArrayList<Junction> mJunctions;
	private ArrayList<Track> mTracks;
	private ArrayList<Restaurant> mRestaurants;
	private ArrayList<Ticket> mTickets;
	private HashMap<String, String> mTicketInformation = new HashMap<String, String>();

	public ZooLocationsData() {
		mAnimals = new ArrayList<Animal>();
		mWays = new ArrayList<Way>();
		mJunctions = new ArrayList<Junction>();
		mTracks = new ArrayList<Track>();
		mRestaurants = new ArrayList<Restaurant>();
		mTickets = new ArrayList<Ticket>();
	}

	public ArrayList<Animal> getAnimals() {
		Collections.sort(mAnimals);
		return mAnimals;
	}

	public ArrayList<Way> getWays() {
		return mWays;
	}

	public ArrayList<Junction> getJunctions() {
		return mJunctions;
	}

	public ArrayList<Track> getTracks() {
		return mTracks;
	}

	public ArrayList<Restaurant> getRestaurant() {
		return mRestaurants;
	}

	public List<Ticket> getTickets() {
		return new LinkedList<Ticket>(mTickets);
	}

	public Map<String, String> getTicketInformation() {
		return mTicketInformation;
	}

	public void setAnimals(ArrayList<Animal> animals) {
		mAnimals = animals;
	}

	public void setWays(ArrayList<Way> ways) {
		mWays = ways;
	}

	public void setJunctions(ArrayList<Junction> junctions) {
		mJunctions = junctions;
	}

	public void setTracks(ArrayList<Track> tracks) {
		mTracks = tracks;
	}

	public void setRestaurants(ArrayList<Restaurant> restaurants) {
		mRestaurants = restaurants;
	}

	public void setTickets(ArrayList<Ticket> tickets) {
		mTickets = tickets;
	}

	public void setTicketInformation(Map<String, String> ticketInformation) {
		mTicketInformation.putAll(ticketInformation);
	}

}
