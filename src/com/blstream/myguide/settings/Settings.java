
package com.blstream.myguide.settings;

import java.util.HashMap;

public class Settings extends HashMap<String, String> {

	public static final String KEY_LANGUAGE = "lang_fallback";
	public static final String KEY_INNER_RADIOUS = "internal_object_radius";
	public static final String KEY_EXTER_RADIOUS = "external_object_radius";
	public static final String KEY_MAX_ZOOM = "max_zoom";
	public static final String KEY_MIN_ZOOM = "min_zoom";
	public static final String KEY_START_LAT = "start_lat";
	public static final String KEY_START_LON = "start_lon";
	public static final String KEY_ANIMALS_VISIBLE = "animals_visible";
	public static final String KEY_SPLASH_DURATION = "splash_min_display_time_ms";
	public static final String KEY_PATHS_VISIBLE = "paths_visible";
	public static final String KEY_JUNCTIONS_VISIBLE = "junctions_visible";
	public static final String KEY_GPS_INTERVAL = "gps_interval";
	public static final String KEY_MIN_GPS_INTERVAL = "min_gps_interval";
	public static final String KEY_GPS_LOGGING = "gps_logging";
	public static final String KEY_MAP_MY_POSITION_HIDDEN = "map_my_position_hidden";
	public static final String KEY_DISTANCE_FROM_ZOO = "distance_from_zoo";
	public static final String KEY_ZOO_ENTRANCE_LAT = "zoo_entrance_lat";
	public static final String KEY_ZOO_ENTRANCE_LNG = "zoo_entrance_lng";

	private static final long serialVersionUID = 1L;

	private String mValue;
	private int mValueInt;
	private float mValueFloat;

	public String getValueAsString(String key) {
		mValue = super.get(key);
		return mValue;
	}

	public int getValueAsInt(String key) {
		mValueInt = Integer.parseInt(super.get(key));
		return mValueInt;
	}

	public float getValueAsFloat(String key) {
		mValueFloat = Float.parseFloat(super.get(key));
		return mValueFloat;
	}

	public double getValueAsDouble(String key) {
		return Double.parseDouble(super.get(key));
	}

	public boolean getValueAsBoolean(String key) {
		return Boolean.parseBoolean(super.get(key));
	}

}
