
package com.blstream.myguide;

import android.app.ActionBar;
import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
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
import android.widget.TextView;

import com.blstream.myguide.fragments.FragmentHelper;
import com.blstream.myguide.gps.LocationUpdater;
import com.blstream.myguide.gps.LocationUser;
import com.blstream.myguide.zoolocations.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Piotrek on 2014-04-06. This class is used to show Restaurant List
 * which are read from xml Each restaurant is shown in label and contain
 * header,description and image
 */
public class GastronomyListFragment extends Fragment implements LocationUser {

	private ListView mListView;
	private ZooLocationsData mZooData;
	private LocationUpdater mLocationUpdater;
	private List<Restaurant> mRestaurants;
	private GastronomyAdapter mGastronomyAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_list_view, container,
				false);

		setHasOptionsMenu(true);
		setActionBar();
		mLocationUpdater = LocationUpdater.getInstance();
		mLocationUpdater.startUpdating(this);

		mZooData = ((MyGuideApp) this.getActivity().getApplication())
				.getZooData();
		mRestaurants = mZooData.getRestaurant();

		if (view != null) {
			mListView = (ListView) view.findViewById(R.id.lvListItem);
			mGastronomyAdapter = new GastronomyAdapter(getActivity(),
					R.layout.gastronomy_list_item, mRestaurants);
			mListView.setAdapter(mGastronomyAdapter);
			setUpListeners();
		}

		return view;
	}

	@Override
	public void onPause() {
		mLocationUpdater.stopUpdating(this);
		super.onPause();
	}

	private void setActionBar() {
		if (getActivity().getActionBar() != null) {
			getActivity().getActionBar().setTitle(getString(R.string.gastronomy_list_title));
			getActivity().getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		}
	}

	private void setUpListeners() {
		// Add options click to restaurant item. Click open new Fragment with
		// restaurant description
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
				Restaurant restaurant = mZooData.getRestaurant().get(position);

				Fragment[] fragments = {
						// MenuFragment
						GastronomyMenuTab
								.newInstance(restaurant),
						// Fragment with map, where is marker with restaurant
						XmlObjectDetailsMapFragment.newInstance(restaurant)

				};
				Fragment newFragment = FragmentTabManager.newInstance(
						R.array.gastronomy_desc_tabs_name, fragments, restaurant);

				FragmentHelper.swapFragment(R.id.flFragmentHolder, newFragment,
						getFragmentManager(), BundleConstants.FRAGMENT_RESTAURANT);
			}
		});
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		MenuItem itemSearch = menu.findItem(R.id.action_search);
		MenuItem itemFilter = menu.findItem(R.id.action_filter);

		if (itemSearch != null) itemSearch.setVisible(false);
		if (itemFilter != null) itemFilter.setVisible(false);

		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public void onLocationUpdate(Location location) {
		setUpRestaurantDistance();
	}

	@Override
	public void onGpsAvailable() {
		setUpRestaurantDistance();
	}

	@Override
	public void onGpsUnavailable() {
		if (mGastronomyAdapter != null) {
			mGastronomyAdapter.setGPS(false);
		}
	}

	private void setUpRestaurantDistance() {
		if (mGastronomyAdapter != null) {
			mGastronomyAdapter.setGPS(true);
		}
		XmlObjectFinderHelper xmlObjectFinderHelper = new XmlObjectFinderHelper(
				mLocationUpdater.getLocation(), (MyGuideApp) getActivity()
						.getApplication(), getActivity(), new Restaurant());

		ArrayList<XmlObjectDistance> xmlObjectDistance = xmlObjectFinderHelper
				.allXmlObjectsWithDistances();

		if (xmlObjectDistance != null) {
			mRestaurants.clear();
			for (XmlObjectDistance xmlObject : xmlObjectDistance) {
				Restaurant restaurant = (Restaurant) xmlObject.getXmlObject();
				restaurant.setDistance(xmlObject.getDistance());
				mRestaurants.add(restaurant);
			}
			if (mGastronomyAdapter != null) {
				mGastronomyAdapter.refill();
			}
		}
	}

	/**
	 * This Adapter is used to show Restaurants in GastronomyListFragment.
	 */
	private static class GastronomyAdapter extends ArrayAdapter<Restaurant> {

		private Activity mContext;
		private List<Restaurant> mRestaurants;
		private int mLayoutResourceId;
		private Handler uiHandler = new Handler();
		private boolean mIsGPS;

		private static int mRestaurantIcons[] = {
				R.drawable.lv_item_res_letnia,
				R.drawable.lv_item_res_papugami,
				R.drawable.lv_item_bar_zoo,
				R.drawable.lv_item_karczma_koala,
				R.drawable.lv_item_karczma_lwa
		};

		public GastronomyAdapter(Activity context, int layoutResourceId,
				List<Restaurant> restaurants) {
			super(context, layoutResourceId, restaurants);
			mContext = context;
			mRestaurants = restaurants;
			mLayoutResourceId = layoutResourceId;
		}

		public void setGPS(boolean gps) {
			mIsGPS = gps;
		}

		static class ViewHolder {
			public TextView mTxtvRestaurantName;
			public ImageView mImgvRestaurant;
			public TextView mTxtvMeter;
			public TextView mTxtvOpenTime;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;

			if (convertView == null) {
				LayoutInflater inflater = mContext.getLayoutInflater();
				convertView = inflater.inflate(mLayoutResourceId, parent, false);

				viewHolder = new ViewHolder();
				viewHolder.mTxtvRestaurantName = (TextView) convertView
						.findViewById(R.id.txtvGastronomyName);
				viewHolder.mImgvRestaurant = (ImageView) convertView
						.findViewById(R.id.imgvRestaurant);
				viewHolder.mTxtvMeter = (TextView) convertView
						.findViewById(R.id.txtvGastronomyDistance);
				viewHolder.mTxtvOpenTime = (TextView) convertView.findViewById(R.id.txtvOpen);

				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			Restaurant restaurant = mRestaurants.get(position);

			viewHolder.mTxtvRestaurantName.setText(restaurant.getName() + "");
			viewHolder.mTxtvOpenTime.setText(restaurant.getOpen() + "");
			viewHolder.mImgvRestaurant.setImageResource(mRestaurantIcons[position]);
			viewHolder.mTxtvMeter.setVisibility(View.INVISIBLE);
			if (mIsGPS) {
				if (restaurant.getDistance() != 0) {
					viewHolder.mTxtvMeter.setVisibility(View.VISIBLE);
					viewHolder.mTxtvMeter.setText(restaurant.getDistance() + " m");
				}
			}

			return convertView;
		}

		// create method refill to notyfiydatachange
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
