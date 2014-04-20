
package com.blstream.myguide.zoolocations;

import java.util.HashMap;
import java.util.Map;

public class AccessInformation {

	private String mTrams = new String();
	private HashMap<String, String> mParkingInfo = new HashMap<String, String>();

	protected void setTrams(String trams) {
		mTrams = trams;
	}

	protected void setParkingInformation(Map<String, String> map) {
		mParkingInfo = new HashMap<String, String>(map);
	}

	public String getTrams() {
		return new String(mTrams);
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
