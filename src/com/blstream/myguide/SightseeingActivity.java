
package com.blstream.myguide;

import android.app.ActionBar;
import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.blstream.myguide.settings.Settings;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

public class SightseeingActivity extends Activity implements OnCameraChangeListener {

	private static final String LOG_TAG = ParseXmlTask.class.getSimpleName();
	private static final float DEFAULT_MIN_ZOOM = 14.5f;
	private static final float DEFAULT_MAX_ZOOM = 19.0f;
	private static final double DEFAULT_START_LAT = 51.1050406;
	private static final double DEFAULT_START_LON = 17.074053;

	private ImageView mImgvSlidingMenu;
	private ImageView mImgvShowRoute;
	private SearchView mSearchView;
	private ImageView mSearchViewClose;
	private ActionBar mActionBar;
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	private GoogleMap mMap;
	private float mMinZoom;
	private float mMaxZoom;
	private double mStartCenterLat;
	private double mStartCenterLon;

	/**
	 * Called when the activity is first created. Sets up ActionBar and
	 * NavigationDrawer for the Activity. Reads settings which are saved in
	 * MyGuideApp and sets up GoogleMap.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		setContentView(R.layout.activity_sightseeing);

		mActionBar = getActionBar();

		if (mActionBar != null) {
			mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
			mActionBar.setCustomView(R.layout.action_bar_sightseeing);
			mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

			View actionBarCustomView = mActionBar.getCustomView();
			setUpActionBar(actionBarCustomView);
			setUpActionBarListeners();
		}

		setUpDrawerListView();

		MyGuideApp mga = (MyGuideApp) (this.getApplication());
		Settings settings = mga.getSettings();
		try {
			mStartCenterLat = Double.parseDouble(settings.getValueAsString(Settings.KEY_START_LAT));
		} catch (NumberFormatException e) {
			Log.w(LOG_TAG, Settings.KEY_START_LAT + " " + e);
			mStartCenterLat = DEFAULT_START_LAT;
			mStartCenterLon = DEFAULT_START_LON;
		}
		try {
			mStartCenterLon = Double.parseDouble(settings.getValueAsString(Settings.KEY_START_LON));
		} catch (NumberFormatException e) {
			Log.w(LOG_TAG, Settings.KEY_START_LON + " " + e);
			mStartCenterLat = DEFAULT_START_LAT;
			mStartCenterLon = DEFAULT_START_LON;
		}
		try {
			mMinZoom = settings.getValueAsFloat(Settings.KEY_MIN_ZOOM);
		} catch (NumberFormatException e) {
			Log.w(LOG_TAG, Settings.KEY_MIN_ZOOM + " " + e);
			mMinZoom = DEFAULT_MIN_ZOOM;
			mMaxZoom = DEFAULT_MAX_ZOOM;
		}
		try {
			mMaxZoom = settings.getValueAsFloat(Settings.KEY_MAX_ZOOM);
		} catch (NumberFormatException e) {
			Log.w(LOG_TAG, Settings.KEY_MIN_ZOOM + " " + e);
			mMinZoom = DEFAULT_MIN_ZOOM;
			mMaxZoom = DEFAULT_MAX_ZOOM;
		}

		mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mStartCenterLat,
				mStartCenterLon), mMinZoom));
		mMap.setOnCameraChangeListener(this);
	}

	/** Sets up custom ActionBar. */
	private void setUpActionBar(View v) {
		mSearchView = (SearchView) v.findViewById(R.id.svSightseeing);
		mImgvShowRoute = (ImageView) v.findViewById(R.id.imgvShowRoute);
		mImgvSlidingMenu = (ImageView) v.findViewById(R.id.imgvSlidingMenu);

		mSearchView.setQueryHint(getString(R.string.search_sightseeing));
		mSearchView.setIconified(false);
		mSearchView.clearFocus();

		int searchPlateId = mSearchView.getContext().getResources()
				.getIdentifier("android:id/search_plate", null, null);
		View searchPlate = mSearchView.findViewById(searchPlateId);

		if (searchPlate != null) {
			searchPlate.setBackgroundResource(R.drawable.rounded_edittext);

			int searchTextId = searchPlate.getContext().getResources()
					.getIdentifier("android:id/search_src_text", null, null);
			TextView searchText = (TextView) searchPlate.findViewById(searchTextId);
			if (searchText != null) {
				searchText.setGravity(Gravity.CENTER);
				searchText.setTextColor(Color.BLACK);

			}

			int search = searchPlate.getContext().getResources()
					.getIdentifier("android:id/search_close_btn", null, null);
			mSearchViewClose = (ImageView) searchPlate.findViewById(search);
			if (mSearchViewClose != null) {
				mSearchViewClose.setVisibility(View.GONE);
			}
		}
	}

	/** Sets up listeners for ActionBar views. */
	private void setUpActionBarListeners() {
		mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
			@Override
			public boolean onClose() {
				mSearchView.clearFocus();
				mSearchViewClose.setVisibility(View.GONE);
				return true;
			}
		});

		mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String s) {
				mSearchView.clearFocus();
				return false;
			}

			@Override
			public boolean onQueryTextChange(String s) {

				return false;
			}
		});

		mImgvShowRoute.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mSearchView.clearFocus();
				mSearchViewClose.setVisibility(View.GONE);
			}
		});

		mImgvSlidingMenu.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mSearchView.clearFocus();
				mSearchViewClose.setVisibility(View.GONE);

				if (!mDrawerLayout.isDrawerOpen(Gravity.LEFT)) mDrawerLayout
						.openDrawer(Gravity.LEFT);
				else mDrawerLayout.closeDrawer(Gravity.LEFT);
			}
		});
	}

	/** Sets up NavigationDrawer. */
	public void setUpDrawerListView() {
		String[] mDrawerMenuItems = getResources().getStringArray(R.array.nav_drawer_items);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.lvMenuSightseeing);

		mDrawerList.setAdapter(new ArrayAdapter<String>(this,
				R.layout.sliding_menu_item, mDrawerMenuItems));

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close);

		mDrawerLayout.setDrawerListener(mDrawerToggle);

		mDrawerLayout.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent motionEvent) {
				mSearchView.clearFocus();
				mSearchViewClose.setVisibility(View.GONE);
				return false;
			}
		});
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	@Override
	public void onCameraChange(CameraPosition camera) {
		if (camera.zoom < mMinZoom) {
			mMap.animateCamera(CameraUpdateFactory.zoomTo(mMinZoom));
		}
		else if (camera.zoom > mMaxZoom) {
			mMap.animateCamera(CameraUpdateFactory.zoomTo(mMaxZoom));
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

}
