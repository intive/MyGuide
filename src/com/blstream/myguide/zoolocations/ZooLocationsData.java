
package com.blstream.myguide.zoolocations;

import java.util.ArrayList;
import java.util.Collections;

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
	private ArrayList<Event> mEvents;
	private TicketsInformation mTicketInformation = new TicketsInformation();
	private AccessInformation mAccessInfo = new AccessInformation();
	private ContactInformation mContactInfo;
	private ArrayList<History> mHistory;

	public ZooLocationsData() {
		mAnimals = new ArrayList<Animal>();
		mWays = new ArrayList<Way>();
		mJunctions = new ArrayList<Junction>();
		mTracks = new ArrayList<Track>();
		mRestaurants = new ArrayList<Restaurant>();
		mEvents = new ArrayList<Event>();
		mHistory = new ArrayList<History>();
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

	public ArrayList<Event> getEvent() {
		return mEvents;
	}

	public TicketsInformation getTicketInformation() {
		return mTicketInformation;
	}

	public AccessInformation getAccessInformation() {
		return mAccessInfo;
	}

	public ContactInformation getContactInformation() {
		return mContactInfo;
	}

	public ArrayList<History> getHistory() {
		return mHistory;
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

	public void setEvents(ArrayList<Event> events) {
		mEvents = events;
	}

	public void setTicketInformation(TicketsInformation ticketInformation) {
		mTicketInformation = ticketInformation;
	}

	public int sumOfAnimalsOnTracks() {
		int sum = 0;
		for (Track t : mTracks) {
			sum += t.getAnimals().size();
		}
		return sum;
	}

	public void setAccessInformation(AccessInformation accessInformation) {
		mAccessInfo = accessInformation;
	}

	public void setContactInformation(ContactInformation contactInformation) {
		mContactInfo = contactInformation;
	}

	public void setHistory(ArrayList<History> history) {
		mHistory = history;
	}

}
