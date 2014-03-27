package com.blstream.myguide;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import com.blstream.myguide.zoolocations.Animal;
import com.blstream.myguide.zoolocations.ZooLocationsData;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class AnimalListActivity extends Activity {

	/**
	 * Activity which displays list of all animals in the Zoo.
	 * @author Agnieszka
	 */

	private ActionBar mActionBar;

	private ListView mListOfAnimals;
	private String[] mAnimalsStrings;
	private ZooLocationsData mZooData;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		setContentView(R.layout.activity_animal_list);

		MyGuideApp mga = (MyGuideApp) (this.getApplication());
		mZooData = mga.getZooData();
		setUpAnimalsList();

		mActionBar = getActionBar();

		if (mActionBar != null) {
			mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
			mActionBar.setCustomView(R.layout.action_bar_animal_list);
			mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		}

	}

	/**
	 * Method responsible for creating ListView and setting ArrayAdapter.
	 */
	private void setUpAnimalsList() {

		mListOfAnimals = (ListView) findViewById(R.id.listViewAnimalList);
		mAnimalsStrings = getAnimalNames();

		ArrayAdapter<String> animalsArrayAdapter = new ArrayAdapter<String>(
				this, R.layout.animal_kingdom_item, mAnimalsStrings);
		mListOfAnimals.setAdapter(animalsArrayAdapter);

	}

	/**
	 * Reads names of animals from ZooLocationsData.
	 * @return array of strings with animal names
	 */
	private String[] getAnimalNames() {

		ArrayList<Animal> animals = mZooData.getAnimals();
		String[] animalNames = new String[animals.size()];

		for (int i = 0; i < animalNames.length; i++) {
			animalNames[i] = animals.get(i).getName();
			// TODO other languages and fallback to english (data in XML file first)
		}

		Arrays.sort(animalNames);
		return animalNames;
	}
	
	/**
	 * Checks if device's language is Polish.
	 * @return
	 */
	private boolean isPolish() {
		return Locale.getDefault().getLanguage().equals("pl");
	}
}
