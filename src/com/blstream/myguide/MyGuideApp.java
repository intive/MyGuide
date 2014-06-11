
package com.blstream.myguide;

import android.app.Activity;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.blstream.myguide.gps.LocationUpdater;
import com.blstream.myguide.path.Graph;
import com.blstream.myguide.settings.Settings;
import com.blstream.myguide.zoolocations.ZooLocationsData;

/**
 * Class designed for keeping data which would be avaible from every class in
 * Application containing context.
 * 
 * @author Rafal
 */
public class MyGuideApp extends Application {

	private static final int NOTIFICATION_ID = 0x1;

	private Settings mSettings = null;
	private ZooLocationsData mZooData = null;
	private Graph mGraph;
	/**
	 * Remembers if application is in tracking mode
	 * (user is going along the track).
	 */
	private boolean mIsInTrackingMode = false;

	private void showNotification() {
		// display notification
		NotificationManager notificationManager = ((NotificationManager) getSystemService(NOTIFICATION_SERVICE));
        Notification notification = createNotification();

        Intent notificationIntent = new Intent(this, StartActivity.class);

        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent intent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        notification.setLatestEventInfo(this, getString(R.string.app_name), getString(R.string.notif_text), intent);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(NOTIFICATION_ID, notification);
	}

	private void hideNotification() {
		// remove notification
		((NotificationManager) getSystemService(NOTIFICATION_SERVICE))
				.cancel(NOTIFICATION_ID);
	}

	protected Notification createNotification() {
		return new NotificationCompat.Builder(this)
				.setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle(getString(R.string.app_name))
				.setContentText(getString(R.string.notif_text))
				.setOngoing(true)   // cannot be cancelled
				.setAutoCancel(true)
				.build();
	}

	@Override
	public void onCreate() {
		super.onCreate();

		LocationUpdater.setAppContext(this);
		registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
			@Override
			public void onActivityCreated(Activity activity, Bundle bundle) {
				if (!(activity instanceof StartActivity)) return;

				hideNotification();
				showNotification();
			}

			@Override
			public void onActivityStarted(Activity activity) {

			}

			@Override
			public void onActivityResumed(Activity activity) {
                if (!(activity instanceof StartActivity)) return;

                hideNotification();
                showNotification();
			}

			@Override
			public void onActivityPaused(Activity activity) {

			}

			@Override
			public void onActivityStopped(Activity activity) {

			}

			@Override
			public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

			}

			@Override
			public void onActivityDestroyed(Activity activity) {
				if (!(activity instanceof StartActivity)) return;

				hideNotification();
			}
		});
	}

	protected synchronized void setZooData(ZooLocationsData data) {
		mZooData = data;
	}

	protected synchronized void setSettings(Settings settings) {
		mSettings = settings;
		LocationUpdater.setSettings(mSettings);
	}

	protected synchronized void setGraph(Graph graph) {
		mGraph = graph;
	}

	public synchronized ZooLocationsData getZooData() {
		return mZooData;
	}

	public synchronized Settings getSettings() {
		return mSettings;
	}

	public synchronized Graph getGraph() {
		return mGraph;
	}
	
	public void setTrackingModeOn() {
		mIsInTrackingMode = true;
	}
	
	public void setTrackingModeOff() {
		mIsInTrackingMode = false;
	}
	
	public boolean isInTrackingMode(){
		return mIsInTrackingMode;
	}

}
