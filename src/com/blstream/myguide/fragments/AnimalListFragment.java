package com.blstream.myguide.fragments;

import java.util.ArrayList;
import java.util.Locale;

import com.blstream.myguide.MyGuideApp;
import com.blstream.myguide.R;
import com.blstream.myguide.zoolocations.Animal;
import com.blstream.myguide.zoolocations.ZooLocationsData;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class AnimalListFragment extends ListFragment {

	/**
	 * Fragment which displays list of all animals in the Zoo.
	 * @author Agnieszka
	 */

	private ZooLocationsData mZooData;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		mZooData = ((MyGuideApp) getActivity().getApplication()).getZooData();
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				inflater.getContext(), R.layout.animal_kingdom_item,
				getAnimalNames());
		setListAdapter(adapter);

		return super.onCreateView(inflater, container, savedInstanceState);
	}

	/**
	 * Reads names of animals from ZooLocationsData.
	 * @return array of strings with animal names
	 */
	private String[] getAnimalNames() {

		ArrayList<Animal> animals = mZooData.getAnimals();
		String[] animalNames = new String[animals.size()];

		for (int i = 0; i < animalNames.length; i++) {
			animalNames[i] = animals.get(i).getName(
					Locale.getDefault().getDisplayLanguage());
		}

		return animalNames;
	}

}
