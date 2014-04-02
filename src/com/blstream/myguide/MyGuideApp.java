
package com.blstream.myguide;

import android.app.Application;

import com.blstream.myguide.gps.LocationUpdater;
import com.blstream.myguide.settings.Settings;
import com.blstream.myguide.zoolocations.ZooLocationsData;

/**
 * Class designed for keeping data which would be avaible from every class in
 * Application containing context.
 * 
 * @author Rafal
 */
public class MyGuideApp extends Application {

	private Settings mSettings = null;
	private ZooLocationsData mZooData = null;

	@Override
	public void onCreate() {
		super.onCreate();
		LocationUpdater.setAppContext(this);
	}

	protected synchronized void setZooData(ZooLocationsData data) {
		mZooData = data;
	}

	protected synchronized void setSettings(Settings settings) {
		mSettings = settings;
		LocationUpdater.setSettings(mSettings);
	}

	public synchronized ZooLocationsData getZooData() {
		return mZooData;
	}

	public synchronized Settings getSettings() {
		return mSettings;
	}

}
