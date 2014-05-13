
package com.blstream.myguide.zoolocations;

import java.util.HashMap;

public class History {

	private HashMap<String, String> mInformation;
	private String mDate;
	private String mImagePath;

	public History() {
		mInformation = new HashMap<String, String>();
	}

	public void addInformation(String lang, String info) {
		mInformation.put(lang, info);
	}

	public void setInformation(HashMap<String, String> info) {
		mInformation = info;
	}

	public String getInformation() {
		return mInformation.get(Language.DEFAULT);
	}

	public String getName(String language) {
		String info = mInformation.get(language);
		if (info == null) { return this.getInformation(); }
		return info;
	}

	public void setDate(String time) {
		mDate = time;
	}

	public String getDate() {
		return mDate;
	}

	public void setImagePath(String imagePath) {
		mImagePath = imagePath;
	}

	public String getImagePath() {
		return mImagePath;
	}

}
