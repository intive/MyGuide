
package com.blstream.myguide;

import android.os.Bundle;
import android.app.Activity;

import com.blstream.myguide.gps.LocationLogger;
import com.blstream.myguide.gps.LocationUpdater;

public class HowToGetActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_how_to_get);
	}

	/*
	 * LocationLogger as well as onStart()/onStop() was added to this activity
	 * to allow QA to test application behavior on gps coordination changes.
	 * While in the nearest future no work is plan to be done within this
	 * activity, it seems to be a good place for temporary tests.
	 */
	private final LocationLogger mLocationLogger = new LocationLogger(this);

	@Override
	protected void onStart() {
		super.onStart();
		LocationUpdater.getInstance(this).startUpdating(mLocationLogger);
	}

	@Override
	protected void onStop() {
		super.onStop();
		LocationUpdater.getInstance(this).stopUpdating(mLocationLogger);
	}
}
