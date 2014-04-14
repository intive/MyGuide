
package com.blstream.myguide;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.blstream.myguide.dialog.PlayServicesErrorDialogFragment;
import com.blstream.myguide.gps.LocationUpdater;
import com.blstream.myguide.path.Graph;
import com.blstream.myguide.settings.Settings;
import com.blstream.myguide.settings.SettingsHelper;
import com.blstream.myguide.zoolocations.Junction;
import com.blstream.myguide.zoolocations.ParserHelper;
import com.blstream.myguide.zoolocations.Way;
import com.blstream.myguide.zoolocations.ZooLocationsData;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

/**
 * Display splash screen while loading assets in the backgroud. Splash will be
 * displayed no shorter than {@link SplashActivity#mMinDisplayMillis}
 * milliseconds.
 */
public class SplashActivity extends FragmentActivity {

	private static final String LOG_TAG = SplashActivity.class.getSimpleName();

	private static final String BGTHREAD_STATE_KEY = "/MyGuide/SplashActivity/BackgroundThreadState";
	private static final String FILE_PATH_DATA = ".myguide/data2.xml";
	private static final String FILE_PATH_CONFIG = ".myguide/config.xml";
	private static final long DEFAULT_MIN_DISPLAY_MILLIS = TimeUnit.SECONDS.toMillis(5);

	private MyGuideApp mApp;

	private boolean mBackgroundThreadRunning = false;

	protected long mMinDisplayMillis = DEFAULT_MIN_DISPLAY_MILLIS;

	private synchronized void notifyBackgroundThreadRunning() {
		mBackgroundThreadRunning = true;
	}

	private synchronized void notifyBackgroundThreadIdle() {
		mBackgroundThreadRunning = false;
	}

	private AlertDialog newErrorDialog() {
		return (new AlertDialog.Builder(SplashActivity.this))
				.setCancelable(false)
				.setTitle(R.string.splash_dialog_error_title)
				.setMessage(R.string.splash_dialog_error_message)
				.setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						finishThisActivity();
					}
				})
				.create();
	}

	private void setMinDisplayTime() {
		try {
			mMinDisplayMillis = mApp.getSettings().getValueAsInt(Settings.KEY_SPLASH_DURATION);
		} catch (NumberFormatException e) {
			mMinDisplayMillis = DEFAULT_MIN_DISPLAY_MILLIS;
		}
		Log.d(LOG_TAG, "minimal splash duration: " + mMinDisplayMillis);
	}

	private boolean isBackgroundThreadRunning(Bundle savedInstanceState) {
		return savedInstanceState != null
				&& savedInstanceState.getBoolean(BGTHREAD_STATE_KEY, false);
	}

	private void runBackgroundThread() {
		final long startTime = TimeUnit.NANOSECONDS.toMillis(System.nanoTime());
		new Thread(new Runnable() {
			@Override
			public void run() {
				Log.i(LOG_TAG, "Starting background thread");
				notifyBackgroundThreadRunning(); // thread started work

				try {
					doInBackground();

					// make sure min display time has elapsed
					boolean interuppted = false;
					final long duration = TimeUnit.NANOSECONDS.toMillis(System.nanoTime())
							- startTime;
					if (duration < mMinDisplayMillis) {
						try {
							Thread.sleep(mMinDisplayMillis - duration);
						} catch (InterruptedException e) {
							Thread.interrupted(); // clear interruption flag
							interuppted = true;
						}
					}

					if (!interuppted) {
						startNextActivity();
						finishThisActivity();
					}
				} catch (Exception e) {
					SplashActivity.this.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							newErrorDialog().show();
						}
					});
				}

				notifyBackgroundThreadIdle(); // thread finished work
			}
		}).start();
	}

	private void parseSettingsXML() {
		// TODO: this method needs UT
		Log.i(LOG_TAG, "Parsing settings XML");

		SettingsHelper sh = new SettingsHelper();
		Settings settings = null;
		try {
			try {
				// reading settings from external file
				Log.d(LOG_TAG, "settings source: external");
				settings = sh.parse(this, new File(Environment.getExternalStorageDirectory(),
						FILE_PATH_CONFIG));
			} catch (IOException e) {
				// fallback: reading from resources
				Log.d(LOG_TAG, "settings source: resources");
				settings = sh.parse(this, null);
			}

			StringBuffer sb = new StringBuffer("Settings:\n");
			for (String key : settings.keySet()) {
				sb.append("\t" + key + ": " + settings.get(key) + "\n");
			}
			Log.i(LOG_TAG, sb.toString());
		} catch (Exception e) {
			// reading settings failed, assuming defaults will be used
			Log.e(LOG_TAG, "Parsing settings failed, new instance created", e);
			settings = new Settings();
		}

		mApp.setSettings(settings);
		setMinDisplayTime();

		Log.i(LOG_TAG, "settings XML parsed");
	}

	private void parseDataXML() {
		// TODO: this method needs UT
		Log.i(LOG_TAG, "Parsing data XML");

		ParserHelper ph = new ParserHelper();
		ZooLocationsData data = null;
		try {
			try {
				// reading data from external file
				Log.d(LOG_TAG, "data source: external");
				data = ph.parse(this, new File(Environment.getExternalStorageDirectory(),
						FILE_PATH_DATA));
			} catch (IOException e) {
				// fallback: reading from resources
				Log.d(LOG_TAG, "data source: resources");
				data = ph.parse(this, null);
			}

			Log.i(LOG_TAG, "animals: " + data.getAnimals().size());
			Log.i(LOG_TAG, "ways: " + data.getWays().size());
			Log.i(LOG_TAG, "junctions: " + data.getJunctions().size());
			Log.i(LOG_TAG, "restaurant: " + data.getRestaurant().size());
		} catch (Exception e) {
			// data should always be valid
			// controlled application shutdown
			Log.wtf(LOG_TAG, e.toString());
			throw new DataNotParsedException();
		}

		mApp.setZooData(data);

		Log.i(LOG_TAG, "data XML parsed");
	}

	private void prepareGraph() {
		Log.i(LOG_TAG, "preparing graph structure");

		ArrayList<Way> ways = mApp.getZooData().getWays();
		ArrayList<Junction> junctions = mApp.getZooData().getJunctions();
		if (ways == null || junctions == null) {
			Log.e(LOG_TAG, "can't prepare Graph: no ways or junctions");
			throw new NullPointerException();
		}

		Graph graph = new Graph();
		graph.createGraph(ways, junctions);

		mApp.setGraph(graph);

		Log.i(LOG_TAG, "graph created");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mApp = (MyGuideApp) this.getApplication();

		setContentView(R.layout.activity_splash);

		/*
		 * Activity may have been restarted (eg. via orientation change) so need
		 * to make sure if a background thread has been started before. If not
		 * then it is really the first startup or previous thread had finished
		 * its work.
		 */
		if (isBackgroundThreadRunning(savedInstanceState)) {
			notifyBackgroundThreadRunning();
		} else {
			notifyBackgroundThreadIdle();
		}
		Log.d(LOG_TAG,
				String.format("background thread already running: %s", mBackgroundThreadRunning));
		if (!mBackgroundThreadRunning) {
			if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(this) == ConnectionResult.SUCCESS) {
				/*
				 * Create instance of LocationUpdater to attempt to connect to
				 * location service. It can be done simultaneously with parsing
				 * xml data, so just after leaving splashscreen LocationUpdater
				 * will be connected and ready to use. Method clear() ensure,
				 * that singleton is not storing old data, if instance of it
				 * survived in "not killed" application process.
				 */
				LocationUpdater.getInstance().clear();
				runBackgroundThread();
			} else {
				Log.w(LOG_TAG, "Google Play Services is not available on this device.");
				PlayServicesErrorDialogFragment dialog = new PlayServicesErrorDialogFragment();
				dialog.show(getSupportFragmentManager(),
						PlayServicesErrorDialogFragment.class.getSimpleName());
			}
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		outState.putBoolean(BGTHREAD_STATE_KEY, mBackgroundThreadRunning);
	}

	/**
	 * Operations to be performed on background thread.
     * All uncatched exceptions will trigger an error dialog to appear and eventually kill the
     * Activity.
	 */
	protected void doInBackground() {
		parseSettingsXML();
		parseDataXML();
		prepareGraph();
	}

	protected void startNextActivity() {
		Intent intent = new Intent(this, StartActivity.class);
		startActivity(intent);
	}

	protected void finishThisActivity() {
		finish();
	}

}

class DataNotParsedException extends NullPointerException {
}
