
package com.blstream.myguide.zoolocations;

import java.io.Serializable;
import java.util.HashMap;

public class Event implements Serializable {

	private HashMap<String, String> mNames;
	private String mTime;
	private String mImagePath;
	private String mTimeWeekends;
	private String mTimeHolidays;
	private String mStartDate;

	public Event() {
		mNames = new HashMap<String, String>();
	}

	public void addName(String lang, String name) {
		mNames.put(lang, name);
	}

	public void setNames(HashMap<String, String> names) {
		mNames = names;
	}

	public String getName() {
		return mNames.get(Language.DEFAULT);
	}

	public String getName(String language) {
		String name = mNames.get(language);
		if (name == null) { return this.getName(); }
		return name;
	}

	public void setTime(String time) {
		mTime = time;
	}

	public String getTime() {
		return mTime;
	}

	public void setImagePath(String imagePath) {
		mImagePath = imagePath;
	}

	public String getImagePath() {
		return mImagePath;
	}

	public void setTimeWeekends(String timeWeekends) {
		mTimeWeekends = timeWeekends;
	}

	public String getTimeWeekends() {
		return mTimeWeekends;
	}

	public void setTimeHolidays(String timeHolidays) {
		mTimeHolidays = timeHolidays;
	}

	public String getTimeHolidays() {
		return mTimeHolidays;
	}

	public void setStartDate(String startDate) {
		mStartDate = startDate;
	}

	public String getStartDate() {
		return mStartDate;
	}

}
