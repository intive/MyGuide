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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.blstream.myguide.fragments.FragmentHelper;
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
	private ActionBar mActionBar;

	private ArrayList<AnimalDistance> mAnimalsAndDistances;
	private LocationUpdater mLocationUpdater;

	private AnimalFinderHelper mAnimalFinder;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_list_view, container,
				false);

		setActionBar();
		mLocationUpdater = LocationUpdater.getInstance();
		mLocationUpdater.startUpdating(this);

		mAnimalListView = (ListView) view.findViewById(R.id.lvListItem);

		mAnimalFinder = new AnimalFinderHelper(mLocationUpdater.getLocation(),
				(MyGuideApp) getActivity().getApplication(), getActivity());
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		mAnimalsAndDistances = mAnimalFinder.allAnimalsWithDistances();
		NearestAnimalsAdapter mListAdapter = new NearestAnimalsListFragment.NearestAnimalsAdapter(
				getActivity(), R.layout.custom_animal_row, mAnimalsAndDistances);

		mAnimalListView.setAdapter(mListAdapter);
		mAnimalListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				AnimalDetailsMapFragment wayToAnimal = new AnimalDetailsMapFragment();

				Bundle arguments = new Bundle();
				arguments.putSerializable(BundleConstants.SELECTED_ANIMAL,
						mAnimalsAndDistances.get(position).getAnimal());
				arguments.putSerializable(
						BundleConstants.SHOW_CLOSE_ANIMALS_ON_MAP, true);
				wayToAnimal.setArguments(arguments);

				FragmentHelper.swapFragment(R.id.flFragmentHolder, wayToAnimal,
						getFragmentManager(),
						BundleConstants.FRAGMENT_CLOSEST_ANIMALS_MAP);

				mActionBar.setTitle(mAnimalsAndDistances.get(position)
						.getAnimal().getName());
			}
		});
	};

	private void setActionBar() {
		mActionBar = getActivity().getActionBar();
		mActionBar.setTitle(R.string.nearest_animals_title);
		mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
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

		public NearestAnimalsAdapter(Context context, int resource,
				ArrayList<AnimalDistance> objects) {
			super(context, resource, objects);
		}

		private class ViewHolder {
			ImageView animalImage;
			TextView animalName;
			TextView animalDistance;
			TextView animalFact;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder viewHolder;

			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) getContext()
						.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
				convertView = inflater
						.inflate(R.layout.custom_animal_row, null);

				viewHolder = new ViewHolder();
				viewHolder.animalImage = (ImageView) convertView
						.findViewById(R.id.imgvAnimalIcon);
				viewHolder.animalName = (TextView) convertView
						.findViewById(R.id.txtvAnimalName);
				viewHolder.animalDistance = (TextView) convertView
						.findViewById(R.id.txtvAnimalDistance);
				viewHolder.animalFact = (TextView) convertView
						.findViewById(R.id.txtvAnimalFunFact);

				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			Animal animal = ((AnimalDistance) getItem(position)).getAnimal();
			int distance = ((AnimalDistance) getItem(position)).getDistance();

			if (animal != null) {
				viewHolder.animalDistance.setText(Integer.toString(distance)
						+ "m");
				viewHolder.animalName.setText(animal.getName());
				viewHolder.animalFact.setText(animal.getDescriptionAdult()
						.getText());
			}

			return convertView;
		}

	}

}
