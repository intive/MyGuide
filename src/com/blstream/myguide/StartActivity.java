
package com.blstream.myguide;

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

import com.blstream.myguide.dialog.EnableGpsDialogFragment;
import com.blstream.myguide.fragments.FragmentHelper;
import com.blstream.myguide.gps.LocationUpdater;
import com.google.android.gms.maps.SupportMapFragment;

/**
 * Created by Piotrek on 2014-04-01. Fixed by Angieszka (fragment swap) on
 * 2014-04-04.
 */
public class StartActivity extends FragmentActivity {

	private FragmentManager mFragmentManager;
	private ActionBar mActionBar;

	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;
	private String[] mDrawerMenuItems;
	private ListView mDrawerList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);

		if (savedInstanceState == null) {
			mFragmentManager = getSupportFragmentManager();
			// The main Fragment
			Fragment fragment = new SightseeingFragment();

			FragmentHelper.initFragment(R.id.flFragmentHolder, fragment,
					getSupportFragmentManager(), BundleConstants.FRAGMENT_SIGHTSEEING);
		}

		mActionBar = getActionBar();
		if (mActionBar != null) {
			mActionBar.setHomeButtonEnabled(true);
			mActionBar.setTitle("");
			mActionBar.setIcon(android.R.color.transparent);
			mActionBar.setDisplayHomeAsUpEnabled(true);
		}
		setUpDrawerListView();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_main, menu);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	protected void onStart() {
		super.onStart();
		showEnableGpsDialogIfNeeded();
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
					getSupportFragmentManager().popBackStack(null,
							FragmentManager.POP_BACK_STACK_INCLUSIVE);
				}
				break;
			case 1:
				newFragment = new AnimalListFragment();
				tag = BundleConstants.FRAGMENT_ANIMAL_LIST;
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

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) { return true; }
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

}
