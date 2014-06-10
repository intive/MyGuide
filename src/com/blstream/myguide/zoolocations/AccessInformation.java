
package com.blstream.myguide.zoolocations;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class AccessInformation {

	private String mTrams = new String();
	private String mBuses = new String();
	private HashMap<String, String> mParkingInfo = new HashMap<String, String>();

	private List<String> parseCommaSeparatedValuesRow(String values) {
		LinkedList<String> list = new LinkedList<String>();
		for (String str : values.split(","))
			list.add(str.trim());
		return list;
	}

	protected void setTrams(String trams) {
		mTrams = trams;
	}
	
	protected void setBuses(String buses){
		mBuses = buses;
	}

	protected void setParkingInformation(Map<String, String> map) {
		mParkingInfo = new HashMap<String, String>(map);
	}

	public String getTrams() {
		return new String(mTrams);
	}

	public List<String> splitTrams() {
		return parseCommaSeparatedValuesRow(mTrams);
	}
	
	public List<String> splitBuses() {
		return parseCommaSeparatedValuesRow(mBuses);
	}

	public Map<String, String> getAllParkingInformation() {
		return new HashMap<String, String>(mParkingInfo);
	}

	public String getParkingInformation(String lang) {
		String value = mParkingInfo.get(lang);
		if (value == null) value = mParkingInfo.get(Language.DEFAULT);

		return value;
	}

}
