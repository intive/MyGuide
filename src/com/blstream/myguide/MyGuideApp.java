
package com.blstream.myguide;

import android.app.Application;

import com.blstream.myguide.settings.Settings;
import com.blstream.myguide.zoolocations.ZooLocationsData;

/**
 * @author Rafal Class designed for keeping data which would be avaible from
 *         every class in Application containing context.
 */
public class MyGuideApp extends Application {

	private Settings mSettings = null;
	private ZooLocationsData mZooData = null;

	protected synchronized void setZooData(ZooLocationsData data) {
		mZooData = data;
	}

	protected synchronized void setSettings(Settings settings) {
		mSettings = settings;
	}

	public synchronized ZooLocationsData getZooData() {
		return mZooData;
	}

	public synchronized Settings getSettings() {
		return mSettings;
	}

}
