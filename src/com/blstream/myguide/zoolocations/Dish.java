
package com.blstream.myguide.zoolocations;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Piotrek on 2014-04-07.
 */
public class Dish implements Serializable {

	private HashMap<String, String> mNames;
	private float mPrice;

	public Dish() {
		mNames = new HashMap<String, String>();
	}

	public void setPrice(float price) {
		mPrice = price;
	}

	public float getPrice() {
		return mPrice;
	}

	public void setNames(HashMap<String, String> names) {
		mNames = names;
	}

	public void addName(String lang, String name) {
		mNames.put(lang, name);
	}

	public String getName() {
		return mNames.get(Language.DEFAULT);
	}

	public String getName(String language) {
		String name = mNames.get(language);
		if (name == null) { return this.getName(); }
		return name;
	}

}
