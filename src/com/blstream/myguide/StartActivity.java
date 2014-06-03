
package com.blstream.myguide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.app.ActionBar;
import android.app.Activity;
import android.content.res.Configuration;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.blstream.myguide.database.DbDataManager;
import com.blstream.myguide.dialog.ConfirmExitDialogFragment;
import com.blstream.myguide.dialog.EnableGpsDialogFragment;
import com.blstream.myguide.dialog.FarFromZooDialog.NavigationConfirmation;
import com.blstream.myguide.fragments.FragmentHelper;
import com.blstream.myguide.gps.DistanceFromZooGuard;
import com.blstream.myguide.gps.LocationUpdater;
import com.blstream.myguide.gps.LocationUser;
import com.blstream.myguide.settings.Settings;
import com.blstream.myguide.zoolocations.Animal;
import com.blstream.myguide.zoolocations.Track;
import com.google.android.gms.maps.SupportMapFragment;

/**
 * Created by Piotrek on 2014-04-01. Fixed by Angieszka (fragment swap) on
 * 2014-04-04.Piotrek add method to update visted Animal on 2014-05-23.
 */
public class StartActivity extends FragmentActivity implements NavigationConfirmation, LocationUser {

	/**
	 * The track user is following. NULL if user isn't following any.
	 */
	private static Track sExploredTrack = null;

	private FragmentManager mFragmentManager;
	private ActionBar mActionBar;

	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;
	private String[] mDrawerMenuItems;
	private ListView mDrawerList;

	private TrackAdapter mTrackListAdapter;
	private ListView mTrackListView;
	private ArrayList<Track> mTrackList;

	private SearchView mSearchView;

	private boolean mFarFromZooDialogWasShown;
	private boolean mDistanceFromZooGuardIsBinding;
	private DistanceFromZooGuard mDistanceFromZooGuard;

	private double mDistanceFromAnimal;
	private ArrayList<Animal> mAnimals;

	private DbDataManager mDbManager;
	private MenuItem mItemClearTrack;

	private Fragment createInformationFragment() {
		return FragmentTabManager.newInstance(
				R.array.information_tabs_name,
				new Fragment[] {
						TicketsFragment.newInstance(),
						AccessFragment.newInstance(),
						ContactFragment.newInstance(),
				},
				getString(R.string.information_ab_title));
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);

		mDbManager = DbDataManager.getInstance(this);

		StartActivity.setExploredTrack(null);
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

		createTrackList();
		setUpTrackList();
		updateVisited();

		if (!mFarFromZooDialogWasShown) {
			mDistanceFromZooGuard = new DistanceFromZooGuard(getSupportFragmentManager(),
					((MyGuideApp) getApplication()).getSettings());
		}

		mAnimals = ((MyGuideApp) this.getApplication()).getZooData().getAnimals();
		mDistanceFromAnimal = ((MyGuideApp) this.getApplication())
				.getSettings().getValueAsDouble(Settings.KEY_EXTER_RADIOUS);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_main, menu);
		MenuItem searchViewMenuItem = menu.findItem(R.id.action_search);
		mItemClearTrack = menu.findItem(R.id.action_clear);

		mItemClearTrack.setVisible(false);

		mItemClearTrack.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem menuItem) {
				mDbManager.resetAllVistedAnimals();
				for (Animal a : mAnimals) {
					a.setVisited(false);
				}
				SightseeingFragment fragment = (SightseeingFragment) getSupportFragmentManager()
						.findFragmentByTag(BundleConstants.FRAGMENT_SIGHTSEEING);
				fragment.updateAnimalMarker();
				updateVisited();
				return false;
			}
		});

		mSearchView = (SearchView) searchViewMenuItem.getActionView();

		mSearchView.setOnSearchClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mItemClearTrack.setVisible(false);
				mDrawerLayout.closeDrawer(mTrackListView);
				mDrawerLayout.closeDrawer(mDrawerList);
			}
		});

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
	protected void onStart() {
		super.onStart();
		LocationUpdater.getInstance().startUpdating(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		LocationUpdater.getInstance().stopUpdating(mDistanceFromZooGuard);
		LocationUpdater.getInstance().stopUpdating(this);
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
		MenuAdapter menuAdapter = new MenuAdapter(StartActivity.this, R.layout.sliding_menu_item,
				mDrawerMenuItems);
		mDrawerList.setAdapter(menuAdapter);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_navigation_drawer, R.string.drawer_open,
				R.string.drawer_close) {
			@Override
			public void onDrawerClosed(View drawerView) {
				super.onDrawerClosed(drawerView);
				if (!mDrawerLayout.isDrawerVisible(mTrackListView)) {
					if (mItemClearTrack != null) mItemClearTrack.setVisible(false);
				}
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				if (mDrawerLayout.isDrawerVisible(mTrackListView)) {
					if (mItemClearTrack != null) {
						mItemClearTrack.setVisible(true);
						updateVisited();
					}
				}
			}
		};

		mDrawerLayout.setDrawerListener(mDrawerToggle);
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
	}

	/**
	 * This method create List with all track ( include header - exploration )
	 */
	private void createTrackList() {
		mTrackList = new ArrayList<Track>();
		mTrackList.add(createHeaderListTrack());

		for (Track track : ((MyGuideApp) this.getApplication()).getZooData().getTracks()) {
			mTrackList.add(track);
		}

		Track noTrack = new Track();
		HashMap<String, String> names = new HashMap<String, String>();

		names.put("en", "No track");
		names.put("pl", "Bez trasy");
		noTrack.setNames(names);
		noTrack.setVisited(-1);

		mTrackList.add(noTrack);
	}

	/**
	 * This method create Exploration track ( All track information in this one)
	 * 
	 * @return Exploration Track
	 */
	private Track createHeaderListTrack() {
		Track exploration = new Track();
		HashMap<String, String> names = new HashMap<String, String>();
		ArrayList<Animal> animals = new ArrayList<Animal>();

		names.put("en", "Exploration");
		names.put("pl", "Eksploracja");
		exploration.setNames(names);

		for (Animal animal : ((MyGuideApp) this.getApplication()).getZooData().getAnimals()) {
			animals.add(animal);
		}

		exploration.setAnimals(animals);

		return exploration;
	}

	/**
	 * This method check visited animals and update Track
	 */
	private void updateVisited() {
		int i = 0;
		for (final Track track : mTrackList) {
			if (i != mTrackList.size() - 1) {
				track.setVisited(0);
				for (Animal animal : track.getAnimals()) {
					if (animal.getVisited()) track.setVisited(track.getVisited() + 1);
				}
			}
			i++;
		}
		mTrackListAdapter.refill();
	}

	/** Sets up track list (right drawer). */
	public void setUpTrackList() {

		mTrackListView = (ListView) findViewById(R.id.lvTracks);
		mTrackListAdapter = new TrackAdapter(this, R.layout.right_drawer_item, mTrackList);
		mTrackListView.setAdapter(mTrackListAdapter);
		mTrackListView.setOnItemClickListener(new ListView.OnItemClickListener() {
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
								mDrawerLayout.closeDrawer(mTrackListView);
							}
						});
					}
				}.start();

				if (position == mTrackList.size() - 1 && getExploredTrack() != null) {
					StartActivity.setExploredTrack(null);
					SupportMapFragment f = (SupportMapFragment) getSupportFragmentManager()
							.findFragmentById(R.id.map);
					if (f != null) getSupportFragmentManager().beginTransaction().remove(f)
							.commit();
					navigateToSightseeing();
				} else {
					setNextFragment(
							FragmentTrackDetails.newInstance(((MyGuideApp) getApplication())
									.getZooData().getTracks()
									.get(position - 1)), "track");
				}
			}
		});

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
				mDrawerLayout.openDrawer(mTrackListView);
				break;
			case 2:
				newFragment = new AnimalListFragment();
				tag = BundleConstants.FRAGMENT_ANIMAL_LIST;
				break;
			case 3:
				newFragment = new EventsFragment();
				tag = BundleConstants.FRAGMENT_EVENTS;
				break;
			case 4:
				newFragment = createInformationFragment();
				tag = BundleConstants.FRAGMENT_INFORMATION;
				break;
			case 5:
				newFragment = new HistoryFragment();
				tag = BundleConstants.FRAGMENT_HISTORY;
				break;
			case 6:
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
			clearSearchView();
			mDrawerLayout.closeDrawer(mTrackListView);
			return true;
		}
		if (item.getItemId() == R.id.action_filter) {
			mDrawerLayout.closeDrawer(mDrawerList);
			if (mDrawerLayout.isDrawerVisible(mTrackListView)) {
				mDrawerLayout.closeDrawer(mTrackListView);
			}
			else {
				mDrawerLayout.openDrawer(mTrackListView);
			}
		}
		return super.onOptionsItemSelected(item);
	}

	private void clearSearchView() {
		mSearchView.setQuery(null, false);
		mSearchView.setQueryHint(getString(R.string.search_sightseeing));
		mSearchView.clearFocus();
		mSearchView.onActionViewCollapsed();
	}

	private void setNextFragment(Fragment fragment, String tag) {
		SupportMapFragment f = (SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map);
		if (f != null) getSupportFragmentManager().beginTransaction().remove(f).commit();
		FragmentHelper.swapFragment(R.id.flFragmentHolder, fragment, getSupportFragmentManager(),
				tag);
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

	/**
	 * Invoke, when user made a choice which screen should be shown when he is
	 * far away from zoo: Sightseeing or How to get.
	 */
	@Override
	public void onNavigationConfirm(boolean confirm) {
		int position;
		if (confirm) {
			Fragment current = getSupportFragmentManager().findFragmentById(R.id.flFragmentHolder);
			if (!current.getTag().equals(BundleConstants.FRAGMENT_SIGHTSEEING)) {
				getSupportFragmentManager().popBackStack(null,
						FragmentManager.POP_BACK_STACK_INCLUSIVE);
			}
			position = 0;
		} else {
			getSupportFragmentManager().popBackStack();
			// TODO navigate to "How to get" tabs when it'll be done
			setNextFragment(createInformationFragment(), BundleConstants.FRAGMENT_INFORMATION);
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

	/**
	 * Checks if user is nearby any of the animals, to mark animal as visited.
	 * 
	 * @param location User's location
	 */
	private void checkAnimalProximity(Location location) {
		double lat = location.getLatitude();
		double lng = location.getLongitude();

		for (Animal a : mAnimals) {
			if (MathHelper.distanceBetween(a.getNode(), lat, lng) < mDistanceFromAnimal) {

				if (!a.getVisited()) {
					Toast.makeText(
							getApplicationContext(),
							getString(R.string.visiting_animal_toast)
									+ a.getName(Locale.getDefault()
											.getLanguage()), Toast.LENGTH_SHORT)
							.show();
				}

				// Update animal in database
				mDbManager.updateAnimalInDb(a.getId(), true);
				a.setVisited(true);
				// update track listview mAnimaladapter
				updateVisited();
				// update animal marker color
				SightseeingFragment fragment = (SightseeingFragment) getSupportFragmentManager()
						.findFragmentByTag(BundleConstants.FRAGMENT_SIGHTSEEING);
				fragment.updateAnimalVisitedMarker(a.getId());
			}
		}
	}

	@Override
	public void onLocationUpdate(Location location) {
		if (((MyGuideApp) this.getApplication()).isInTrackingMode()) {
			checkAnimalProximity(location);
		}
	}

	public static void setExploredTrack(Track track) {
		StartActivity.sExploredTrack = track;
	}

	public Track getExploredTrack() {
		return StartActivity.sExploredTrack;
	}

	@Override
	public void onGpsAvailable() {
		// TODO Auto-generated method stub
	}

	@Override
	public void onGpsUnavailable() {
		// TODO Auto-generated method stub
	}

	private static class MenuAdapter extends ArrayAdapter<String> {

		private Activity mContext;
		private String[] mMenuItem;
		private int mLayoutResourceId;

		private static int mMenuIcons[] = {
				R.drawable.menu_icon_map,
				R.drawable.menu_icon_navigation,
				R.drawable.menu_icon_animal,
				R.drawable.menu_icon_event,
				R.drawable.menu_icon_information,
				R.drawable.menu_icon_history,
				R.drawable.menu_icon_gastronomy
		};

		public MenuAdapter(Activity context, int layoutResourceId,
				String[] items) {
			super(context, layoutResourceId, items);
			mContext = context;
			mMenuItem = items;
			mLayoutResourceId = layoutResourceId;
		}

		static class ViewHolder {
			public TextView mTxtvName;
			public ImageView mImgvIcon;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;

			if (convertView == null) {
				LayoutInflater inflater = mContext.getLayoutInflater();
				convertView = inflater.inflate(mLayoutResourceId, parent, false);

				viewHolder = new ViewHolder();
				viewHolder.mTxtvName = (TextView) convertView
						.findViewById(R.id.txtvSlidingMenuTextView);
				viewHolder.mImgvIcon = (ImageView) convertView
						.findViewById(R.id.imgvDrawerIcon);

				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			String s = mMenuItem[position];

			viewHolder.mTxtvName.setText(s + "");
			viewHolder.mImgvIcon.setImageResource(mMenuIcons[position]);

			return convertView;
		}
	}

	/**
	 * This Adapter is use to show Track Menu Drawer in SightseeingFragment.
	 */
	private static class TrackAdapter extends ArrayAdapter<Track> {

		private Activity mContext;
		private List<Track> mTracks;
		private int mLayoutResourceId;
		private Handler uiHandler = new Handler();

		public TrackAdapter(Activity context, int layoutResourceId,
				List<Track> tracks) {
			super(context, layoutResourceId, tracks);
			mContext = context;
			mTracks = tracks;
			mLayoutResourceId = layoutResourceId;
		}

		static class ViewHolder {
			public TextView mTxtvName;
			public TextView mTxtvProgress;
			public ProgressBar mProgressBar;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder viewHolder;

			if (convertView == null) {
				LayoutInflater inflater = mContext.getLayoutInflater();
				convertView = inflater.inflate(mLayoutResourceId, parent, false);

				viewHolder = new ViewHolder();
				if (convertView != null) {
					viewHolder.mTxtvName = (TextView) convertView.findViewById(R.id.txtvTrackName);
					viewHolder.mTxtvProgress = (TextView) convertView
							.findViewById(R.id.txtvProgressText);
					viewHolder.mProgressBar = (ProgressBar) convertView
							.findViewById(R.id.pbProgressBar);

					convertView.setTag(viewHolder);
				}
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			Track track = mTracks.get(position);
			if (track.getVisited() == -1) {
				viewHolder.mTxtvName.setText(track.getName() + "");
				viewHolder.mTxtvProgress.setVisibility(View.GONE);
				viewHolder.mProgressBar.setVisibility(View.GONE);
			} else {
				viewHolder.mTxtvName.setText(track.getName() + "");
				viewHolder.mTxtvProgress.setText(track.getVisited() + "/"
						+ track.getAnimals().size());
				viewHolder.mProgressBar.setMax(track.getAnimals().size());
				viewHolder.mProgressBar.setProgress(track.getVisited());
			}

			return convertView;
		}

		public void refill() {
			uiHandler.post(new Runnable() {
				@Override
				public void run() {
					notifyDataSetChanged();
				}
			});
		}

	}

}
