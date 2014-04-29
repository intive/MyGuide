
package com.blstream.myguide;

import java.util.ArrayList;

import android.app.ActionBar;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.blstream.myguide.dialog.ConfirmExitDialogFragment;
import com.blstream.myguide.dialog.EnableGpsDialogFragment;
import com.blstream.myguide.dialog.FarFromZooDialog.NavigationConfirmation;
import com.blstream.myguide.fragments.FragmentHelper;
import com.blstream.myguide.gps.DistanceFromZooGuard;
import com.blstream.myguide.gps.LocationUpdater;
import com.blstream.myguide.zoolocations.Track;
import com.google.android.gms.maps.SupportMapFragment;

import com.blstream.myguide.zoolocations.*;
import java.util.ArrayList;

/**
 * Created by Piotrek on 2014-04-01. Fixed by Angieszka (fragment swap) on
 * 2014-04-04.
 */
public class StartActivity extends FragmentActivity implements NavigationConfirmation {

	private FragmentManager mFragmentManager;
	private ActionBar mActionBar;

	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;
	private String[] mDrawerMenuItems;
	private ListView mDrawerList;

	private TrackListAdapter mTrackListAdapter;
	private ListView mTrackList;
	private View mTrackHeader;

	private boolean mFarFromZooDialogWasShown;
	private boolean mDistanceFromZooGuardIsBinding;
	private DistanceFromZooGuard mDistanceFromZooGuard;


    private Fragment createInformationFragment() {
		Fragment fragments[] = new Fragment[] {
				TicketsFragment.newInstance(),
				AccessFragment.newInstance(),
				DummyFragment.newInstance("kota"),
		};

		return FragmentTabManager.newInstance(
				R.array.information_tabs_name,
				fragments,
				getResources().getStringArray(R.array.nav_drawer_items)[2]);    // this needs adjusting if strings.xml is changed
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);

		if (savedInstanceState == null) {
			mFragmentManager = getSupportFragmentManager();
			// The main Fragment
			Fragment fragment = SightseeingFragment.newInstance();

			FragmentHelper.initFragment(R.id.flFragmentHolder, fragment,
					getSupportFragmentManager(), BundleConstants.FRAGMENT_SIGHTSEEING);
		} else {
			mFarFromZooDialogWasShown = savedInstanceState.getBoolean(BundleConstants.FAR_FROM_ZOO);
		}

		mActionBar = getActionBar();
		if (mActionBar != null) {
			mActionBar.setHomeButtonEnabled(true);
			mActionBar.setTitle("");
			mActionBar.setIcon(android.R.color.transparent);
			mActionBar.setDisplayHomeAsUpEnabled(true);
		}
		setUpDrawerListView();
		setUpTrackList();

		if (!mFarFromZooDialogWasShown) {
			mDistanceFromZooGuard = new DistanceFromZooGuard(getSupportFragmentManager(),
					((MyGuideApp) getApplication()).getSettings());
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_main, menu);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus) {
			showEnableGpsDialogIfNeeded();
			LocationUpdater.getInstance().refreshGpsStatus();
			if (LocationUpdater.getInstance().isGpsEnable()) {
				LocationUpdater.getInstance().markGpsEnableDialogAsUnshown();
				if (!mFarFromZooDialogWasShown && !mDistanceFromZooGuardIsBinding) {
					mDistanceFromZooGuardIsBinding = true;
					LocationUpdater.getInstance().startUpdating(mDistanceFromZooGuard);
				}
			}
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		LocationUpdater.getInstance().stopUpdating(mDistanceFromZooGuard);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean(BundleConstants.FAR_FROM_ZOO, mFarFromZooDialogWasShown);
	}

	/** Sets up NavigationDrawer. */
	public void setUpDrawerListView() {

		mDrawerMenuItems = getResources().getStringArray(
				R.array.nav_drawer_items);
		mDrawerList = (ListView) findViewById(R.id.lvMenu);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
		mDrawerList.setAdapter(new ArrayAdapter<String>(this,
				R.layout.sliding_menu_item, mDrawerMenuItems));

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_navigation_drawer, R.string.drawer_open,
				R.string.drawer_close);

		mDrawerLayout.setDrawerListener(mDrawerToggle);
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
	}

	/** Sets up track list (right drawer). */
	public void setUpTrackList() {
		mTrackList = (ListView) findViewById(R.id.lvTracks);
		ArrayList<Track> tracks = new ArrayList<Track>();
		mTrackListAdapter = new TrackListAdapter(this, R.layout.right_drawer_item, tracks);
		mTrackHeader = getLayoutInflater().inflate(R.layout.right_drawer_item, null);
		mTrackList.addHeaderView(mTrackHeader);

		mTrackList.setAdapter(mTrackListAdapter);
		mTrackListAdapter.clear();
		mTrackListAdapter.addAll(((MyGuideApp) getApplication()).getZooData().getTracks());
		mTrackList.setOnItemClickListener(new ListView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (position == 0) {
				return;
				}
				new Thread() {
					@Override
					public void run() {
						StartActivity.this.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								mDrawerLayout.closeDrawer(mTrackList);
							}
						});
					}
				}.start();

				setNextFragment(
						FragmentTrackDetails.newInstance(((MyGuideApp) getApplication())
								.getZooData().getTracks()
								.get(position - 1)), "track");
			}
		});

		setUpTrackHeader();
	}

	private class DrawerItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			selectItem(position);
		}
	}

	/** Swaps fragments in the main content view */
	private void selectItem(int position) {

		Fragment current = getSupportFragmentManager().findFragmentById(R.id.flFragmentHolder);
		Fragment newFragment = null;
		String tag = null;

		new Thread() {
			@Override
			public void run() {
				StartActivity.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						mDrawerLayout.closeDrawer(mDrawerList);
					}
				});
			}
		}.start();

		switch (position) {
			case 0:
				if (!current.getTag().equals(BundleConstants.FRAGMENT_SIGHTSEEING)) {
					navigateToSightseeing();
				}
				break;

			case 1:
				newFragment = new AnimalListFragment();
				tag = BundleConstants.FRAGMENT_ANIMAL_LIST;
				break;

			case 2:
				newFragment = createInformationFragment();
				tag = BundleConstants.FRAGMENT_INFORMATION;
				break;

			case 4:
				newFragment = new GastronomyListFragment();
				tag = BundleConstants.FRAGMENT_GASTRONOMY;
				break;

			default:
				break;
		}

		if (newFragment != null && !current.getTag().equals(tag)) {
			if (!tag.equals(BundleConstants.FRAGMENT_SIGHTSEEING)) setNextFragment(newFragment, tag);
		}

		mDrawerList.setItemChecked(position, true);
		setTitle(mDrawerMenuItems[position]);
	}

	private void navigateToSightseeing() {
		if (!mFarFromZooDialogWasShown && mDistanceFromZooGuard.isFarFromZoo()) {
			mDistanceFromZooGuard.showDialog();
		} else {
			getSupportFragmentManager()
					.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			mDrawerLayout.closeDrawer(mTrackList);
			return true;
		}
		if (item.getItemId() == R.id.action_filter) {
			mDrawerLayout.closeDrawer(mDrawerList);
			if (mDrawerLayout.isDrawerVisible(mTrackList)) {
				mDrawerLayout.closeDrawer(mTrackList);
			}
			else {
				mDrawerLayout.openDrawer(mTrackList);
			}
		}
		return super.onOptionsItemSelected(item);
	}

	private void setNextFragment(Fragment fragment, String tag) {
		SupportMapFragment f = (SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map);
		if (f != null) getSupportFragmentManager().beginTransaction().remove(f).commit();
		FragmentHelper.swapFragment(R.id.flFragmentHolder, fragment, getSupportFragmentManager(),
				tag);
	}

	/*
	 * This method show use animation between fragments, when we back.
	 */
	private void setPreviousFragment(Fragment fragment) {
		FragmentTransaction ft = mFragmentManager.beginTransaction();
		ft.setCustomAnimations(R.anim.left_in, R.anim.right_out);
		ft.replace(R.id.flFragmentHolder, fragment);
		ft.commit();
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	private void showEnableGpsDialogIfNeeded() {
		if (LocationUpdater.getInstance().isEnableGpsDialogNeeded()) {
			new EnableGpsDialogFragment().show(getSupportFragmentManager(),
					EnableGpsDialogFragment.class.getSimpleName());
		}
	}

	@Override
	public void onBackPressed() {
		if (getSupportFragmentManager().findFragmentById(R.id.flFragmentHolder)
				.getTag() == BundleConstants.FRAGMENT_SIGHTSEEING) {
			new ConfirmExitDialogFragment().show(getSupportFragmentManager(),
					ConfirmExitDialogFragment.class.getSimpleName());
		} else {
			super.onBackPressed();
		}
	}

	public DrawerLayout getDrawerLayout() {
		return mDrawerLayout;
	}

	private void setUpTrackHeader() {
		TextView text = (TextView) mTrackHeader.findViewById(R.id.txtvTrackName);
		TextView progressText = (TextView) mTrackHeader.findViewById(R.id.txtvProgressText);
		ProgressBar progressBar = (ProgressBar) mTrackHeader.findViewById(R.id.pbProgressBar);
		int animals = ((MyGuideApp) getApplication()).getZooData().sumOfAnimalsOnTracks();
		text.setText(R.string.exploration);
		// TODO set real progress
		progressText.setText(1 + "/" + animals);
		progressBar.setMax(animals);
		progressBar.setProgress(1);
	}

	/**
	 * Invoke, when user made a choice which screen should be shown when he is
	 * far away from zoo: Sightseeing or How to get.
	 */
	@Override
	public void onNavigationConfirm(boolean confirm) {
		int position;
		if (confirm) {
			getSupportFragmentManager().popBackStack(null,
					FragmentManager.POP_BACK_STACK_INCLUSIVE);
			position = 0;
		} else {
			getSupportFragmentManager().popBackStack();
			// TODO navigate to "How to get" tabs when it'll be done
			setNextFragment(new InformationFragment(), BundleConstants.FRAGMENT_INFORMATION);
			position = 2;
		}
		mDrawerList.setItemChecked(position, true);
		setTitle(mDrawerMenuItems[position]);
		mDrawerLayout.closeDrawer(mDrawerList);
	}

	@Override
	public void markDialogAsShown() {
		mFarFromZooDialogWasShown = true;
	}
}
