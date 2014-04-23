package com.blstream.myguide;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.blstream.myguide.gps.LocationUpdater;
import com.blstream.myguide.gps.LocationUser;
import com.blstream.myguide.zoolocations.Animal;
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
				(MyGuideApp) getActivity().getApplication(), getActivity());
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		mAnimalsAndDistances = mAnimalFinder.allAnimalsWithDistances();
		mListAdapter = new NearestAnimalsListFragment.NearestAnimalsAdapter(getActivity(),
				R.layout.custom_animal_row, mAnimalsAndDistances);

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

	private static class NearestAnimalsAdapter extends
			ArrayAdapter<AnimalDistance> {

		private final int NUM_OF_CHARS_IN_LINE = 18;

		public NearestAnimalsAdapter(Context context, int resource,
				ArrayList<AnimalDistance> objects) {
			super(context, resource, objects);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			LayoutInflater inflater = (LayoutInflater) getContext()
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

			convertView = inflater.inflate(R.layout.custom_animal_row, null);

			ImageView animalImage = (ImageView) convertView
					.findViewById(R.id.imgvAnimalIcon);
			TextView animalName = (TextView) convertView
					.findViewById(R.id.txtvAnimalName);
			TextView animalDistance = (TextView) convertView
					.findViewById(R.id.txtvAnimalDistance);
			TextView animalFact = (TextView) convertView
					.findViewById(R.id.txtvAnimalFunFact);

			Animal animal = ((AnimalDistance) getItem(position)).getAnimal();
			int distance = ((AnimalDistance) getItem(position)).getDistance();

			animalName.setText(prepareName(animal.getName(),
					NUM_OF_CHARS_IN_LINE));
			animalFact.setText(animal.getDescriptionAdult().getText());
			animalDistance.setText(Integer.toString(distance) + "m");
			// TODO image

			return convertView;
		}

		public static String prepareName(String name, int characterNumber) {
			if (name.contains(" ") && name.length() > characterNumber)
				name = name.replace(' ', '\n');
			return name;
		}

	}

}
