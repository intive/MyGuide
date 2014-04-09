
package com.blstream.myguide;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.blstream.myguide.fragments.FragmentHelper;
import com.blstream.myguide.zoolocations.*;

import java.util.List;

/**
 * Created by Piotrek on 2014-04-06.
 *
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
		List<Restaurant> mRestaurants = mZooData.getRestaurant();

		mListView = (ListView) view.findViewById(R.id.lvListItem);
		GastronomyAdapter mGastronomyAdapter = new GastronomyAdapter(getActivity(),
				R.layout.gastronomy_list_item, mRestaurants);
		mListView.setAdapter(mGastronomyAdapter);
		setUpListeners();

		return view;
	}

	private void setActionBar() {
		if (getActivity().getActionBar() != null) {
			getActivity().getActionBar().setTitle("Gastronomy");
			getActivity().getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		}
	}

	private void setUpListeners() {
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

}
