package com.blstream.myguide;

import java.util.ArrayList;

import android.app.ActionBar;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.blstream.myguide.adapters.NearestAnimalsAdapter;
import com.blstream.myguide.gps.LocationUpdater;
import com.blstream.myguide.gps.LocationUser;
import com.blstream.myguide.zoolocations.AnimalDistance;

/**
 * Fragment showing the list of nearest animals. Uses user's current location,
 * AnimalFinderHelper {@link com.blstream.myguide.AnimalFinderHelper} and
 * NearestAnimalsAdapter
 * {@link com.blstream.myguide.adapters.NearestAnimalsAdapter} to create and
 * display the list of nearest animals.
 * 
 * @author Agnieszka
 */

public class NearestAnimalsListFragment extends Fragment implements
		LocationUser {

	private ListView mAnimalListView;
	private NearestAnimalsAdapter mListAdapter;

	private ArrayList<AnimalDistance> mAnimalsAndDistances;
	private LocationUpdater mLocationUpdater;

	private AnimalFinderHelper mAnimalFinder;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_nearest_animals,
				container, false);

		setActionBar();
		mLocationUpdater = LocationUpdater.getInstance();
		mLocationUpdater.startUpdating(this);

		mAnimalListView = (ListView) view
				.findViewById(R.id.lvNearestAnimalList);

		mAnimalFinder = new AnimalFinderHelper(mLocationUpdater.getLocation(),
				(MyGuideApp) getActivity().getApplication());
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		mAnimalsAndDistances = mAnimalFinder.allAnimalsWithDistances();
		mListAdapter = new NearestAnimalsAdapter(
				getActivity().getBaseContext(), mAnimalsAndDistances);

		mAnimalListView.setAdapter(mListAdapter);
	};

	private void setActionBar() {
		ActionBar actionBar = getActivity().getActionBar();
		actionBar.setTitle(R.string.nearest_animals_title);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
	}

	@Override
	public void onLocationUpdate(Location location) {
	}

	@Override
	public void onGpsAvailable() {
	}

	@Override
	public void onGpsUnavailable() {
	}

}
