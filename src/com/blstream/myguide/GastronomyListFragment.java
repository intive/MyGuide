
package com.blstream.myguide;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
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
import com.blstream.myguide.zoolocations.*;

import java.util.List;

/**
 * Created by Piotrek on 2014-04-06.
 * This class is used to show Restaurant List which are read
 * from xml Each restaurant is shown in label and contain header,description and image
 */
public class GastronomyListFragment extends Fragment {

	private ListView mListView;
	private ZooLocationsData mZooData;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_list_view, container,
				false);

		setHasOptionsMenu(true);
		setActionBar();

		mZooData = ((MyGuideApp) this.getActivity().getApplication())
				.getZooData();
		List<Restaurant> restaurants = mZooData.getRestaurant();

		if (view != null) {
			mListView = (ListView) view.findViewById(R.id.lvListItem);
			GastronomyAdapter gastronomyAdapter = new GastronomyAdapter(getActivity(),
					R.layout.gastronomy_list_item, restaurants);
			mListView.setAdapter(gastronomyAdapter);
			setUpListeners();
		}

		return view;
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
						AnimalDescriptionTab
								.newInstance(R.drawable.placeholder_adult, R.string.text),
						GastronomyMenuTab
								.newInstance(restaurant)
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

	/**
	 * This Adapter is used to show Restaurants in GastronomyListFragment.
	 */
	private static class GastronomyAdapter extends ArrayAdapter<Restaurant> {

		private Activity mContext;
		private List<Restaurant> mRestaurants;
		private int mLayoutResourceId;

		public GastronomyAdapter(Activity context, int layoutResourceId,
				List<Restaurant> restaurants) {
			super(context, layoutResourceId, restaurants);
			mContext = context;
			mRestaurants = restaurants;
			mLayoutResourceId = layoutResourceId;
		}

		static class ViewHolder {
			public TextView mTxtvRestaurantName;
			public ImageView mImgvRestaurant;
			public TextView mTxtvTimeTo;
			public TextView mTxtvMeter;
			public TextView mTxtvOpenTime;
			public TextView mTxtvMenu;
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
					viewHolder.mTxtvTimeTo = (TextView) convertView.findViewById(R.id.txtvTimeTo);
					viewHolder.mTxtvMeter = (TextView) convertView
							.findViewById(R.id.txtvGastronomyDistance);
					viewHolder.mTxtvOpenTime = (TextView) convertView.findViewById(R.id.txtvOpen);
					viewHolder.mTxtvMenu = (TextView) convertView.findViewById(R.id.txtvMenu);

					convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			Restaurant restaurant = mRestaurants.get(position);

			viewHolder.mTxtvRestaurantName.setText(restaurant.getName() + "");
			viewHolder.mTxtvOpenTime.setText(restaurant.getOpen() + "");

			return convertView;
		}
	}

}
