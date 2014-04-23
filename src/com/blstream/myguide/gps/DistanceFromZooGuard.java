package com.blstream.myguide.gps;

import android.location.Location;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.blstream.myguide.BundleConstants;
import com.blstream.myguide.R;
import com.blstream.myguide.dialog.FarFromZooDialog;
import com.blstream.myguide.settings.Settings;

/**
 * Control distance between user and Zoo and show proper dialog if user is far
 * away.
 */
public class DistanceFromZooGuard implements LocationUser {

	private static final String LOG_TAG = DistanceFromZooGuard.class.getSimpleName();

	private FragmentManager mFragmentManager;
	private int mMinDistance;
	private Location mZooLocation;
	private boolean mUpdateLocationAvailable;

	public DistanceFromZooGuard(FragmentManager fm, Settings settings) {
		mFragmentManager = fm;
		mMinDistance = settings.getValueAsInt(Settings.KEY_DISTANCE_FROM_ZOO);

		mZooLocation = new Location(LOG_TAG);
		mZooLocation.setLongitude(settings.getValueAsDouble(Settings.KEY_START_LON));
		mZooLocation.setLatitude(settings.getValueAsDouble(Settings.KEY_START_LAT));
	}

	public boolean isFarFromZoo() {
		if (mUpdateLocationAvailable) {
			Location location = LocationUpdater.getInstance().getLocation();
			if (location == null) { return false; }
			return (location.distanceTo(mZooLocation) > mMinDistance) ? true : false;
		} else {
			return false;
		}
	}

	public void showDialog() {
		FragmentTransaction ft = mFragmentManager.beginTransaction();
		Fragment prev = mFragmentManager.findFragmentByTag(FarFromZooDialog.class.getSimpleName());
		if (prev != null) {
			ft.remove(prev);
		}
		// must be added to BackStack to allow proper navigation to Sightseeing
		ft.addToBackStack(null);
		new FarFromZooDialog().show(ft, FarFromZooDialog.class.getSimpleName());
	}

	@Override
	public void onLocationUpdate(Location location) {
		mUpdateLocationAvailable = true;
		// no more then one update is needed - unbind from LoctionUser
		LocationUpdater.getInstance().stopUpdating(this);
		if (mFragmentManager.findFragmentById(R.id.flFragmentHolder).getTag()
				.equals(BundleConstants.FRAGMENT_SIGHTSEEING)
				&& location.distanceTo(mZooLocation) > mMinDistance) {
			showDialog();
		}
	}

	@Override
	public void onGpsAvailable() {
	}

	@Override
	public void onGpsUnavailable() {
	}

}
