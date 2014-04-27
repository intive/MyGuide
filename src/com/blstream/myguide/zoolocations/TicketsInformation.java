
package com.blstream.myguide.zoolocations;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TicketsInformation {

	private LinkedList<Ticket> mTickets = new LinkedList<Ticket>();
	private HashMap<String, String> mInformation = new HashMap<String, String>();

	protected TicketsInformation setTickets(List<Ticket> tickets) {
		mTickets = new LinkedList<Ticket>(tickets);
		return this;
	}

	protected TicketsInformation setInformation(Map<String, String> map) {
		mInformation = new HashMap<String, String>(map);
		return this;
	}

	public List<Ticket> getTickets() {
		return new LinkedList<Ticket>(mTickets);
	}

	public String getInformation(String lang) {
		String value = mInformation.get(lang);
		if (value == null) value = mInformation.get(Language.DEFAULT);

		return value;
	}

	public Map<String, String> getAllInformation() {
		return new HashMap<String, String>(mInformation);
	}

}
