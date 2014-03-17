
package com.blstream.myguide;

import android.app.Application;

import com.blstream.myguide.zoolocations.ZooLocationsData;
import com.blstream.myguide.settings.Settings;

/* Rafal's idea (instead of singleton).*/
public class MyGuideApp extends Application {

	private Settings mSettings = null;
	private ZooLocationsData mZooData = null;

	protected void setData(ZooLocationsData data) {
		mZooData = data;
	}

	protected void setSettings(Settings settings) {
		mSettings = settings;
	}

	public ZooLocationsData getData() {
		return mZooData;
	}

	public Settings getSettings() {
		return mSettings;
	}

}
