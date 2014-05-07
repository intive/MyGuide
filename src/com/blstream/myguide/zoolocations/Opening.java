
package com.blstream.myguide.zoolocations;

import java.util.HashMap;
import java.util.Map;

public class Opening {

	public static class Hours {

		private String mFrom;
		private String mTo;

		public Hours(String from, String to) {
			mFrom = from;
			mTo = to;
		}

		public String from() {
			return mFrom;
		}

		public String to() {
			return mTo;
		}

	}

	public static enum When {

		WEEKDAYS,
		WEEKENDS;

		public static When parseWhen(String str) {
			if ("weekdays".equals(str)) return WEEKDAYS;
			else if ("weekends".equals(str)) return WEEKENDS;
			return null;
		}

	}

	private Map<String, String> mDescription;
	private Map<When, Hours> mHours = new HashMap<When, Hours>();

	protected Opening setDescription(Map<String, String> map) {
		mDescription = new HashMap<String, String>(map);
		return this;
	}

	protected Opening setHours(When when, Hours hours) {
		mHours.put(when, hours);
		return this;
	}

	public String getDescription(String lang) {
		String value = mDescription.get(lang);
		if (value == null) value = mDescription.get(Language.DEFAULT);
		return value;
	}

	public Hours getHours(When when) {
		return mHours.get(when);
	}

}
