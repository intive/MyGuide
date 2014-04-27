
package com.blstream.myguide.zoolocations;

import java.util.HashMap;
import java.util.Map;

public class Ticket {

	public static enum Type {

		INDIVIDUAL,
		GROUP,

	}

	private HashMap<String, String> mDescriptionDict;
	private int mPrice = 0;
	private Type mTicketType;

	public Ticket(Type type) {
		mTicketType = type;
		mDescriptionDict = new HashMap<String, String>();
	}

	protected Ticket setPrice(int price) {
		mPrice = price;
		return this;
	}

	protected Ticket setDescriptionDictionary(Map<String, String> dict) {
		mDescriptionDict = new HashMap<String, String>(dict);
		return this;
	}

	public Type getType() {
		return mTicketType;
	}

	public int getPrice() {
		return mPrice;
	}

	public String getDescription(String lang) {
		String value = mDescriptionDict.get(lang);
		if (value == null) value = mDescriptionDict.get(Language.DEFAULT);
		return value;
	}

}
