
package com.blstream.myguide;

import java.util.ArrayList;
import java.util.Locale;

import com.blstream.myguide.fragments.FragmentHelper;
import com.blstream.myguide.zoolocations.Animal;
import com.blstream.myguide.zoolocations.ZooLocationsData;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

/**
 * Fragment showing list of animals in the Zoo.
 * 
 * @author Agnieszka
 */

public class AnimalListFragment extends Fragment {

	private ZooLocationsData mZooData;
	private SearchView mSearchView;
    private ArrayAdapter<String> mAnimaladapter;
    private ListView mAnimalListView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_animal_list, container,
				false);

		setActionBar();
		setHasOptionsMenu(true);

	    mAnimalListView = (ListView) view.findViewById(R.id.lvAnimalList);
		mZooData = ((MyGuideApp) this.getActivity().getApplication())
				.getZooData();

		mSearchView = (SearchView) view.findViewById(R.id.searchViewAnimalList);
		if (mSearchView != null) {
			setUpSearchView();
			setUpSearchViewListeners();
		}

        setUpAdapter(getAnimalNames(mZooData.getAnimals()));
		mAnimalListView.setOnItemClickListener(new AnimalListOnClickListener());

		return view;
	}

	private void setActionBar() {
		getActivity().getActionBar().setTitle(R.string.animal_list_title);
		getActivity().getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
	}

	private void setUpSearchView() {
		mSearchView.setQueryHint(getString(R.string.search_sightseeing));

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
                searchText.setTextSize(20.f);
			}
		}
	}

	private void setUpSearchViewListeners() {
		mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String s) {
				boolean findAnimal = false;
                ArrayList<Animal> animals = new ArrayList<Animal>();

				for (Animal animal : mZooData.getAnimals()) {
					if (replacePolishChar(animal.getName().toLowerCase()).contains(
							replacePolishChar(s.toLowerCase())))
					{
                        animals.add(animal);
						findAnimal = true;
					}
				}

				if (findAnimal) {
                    setUpAdapter(getAnimalNames(animals));
				}
				else {
					mSearchView.setQuery(null, false);
					mSearchView.setQueryHint(getString(R.string.search_sightseeing_not));
				}

				return false;
			}

			@Override
			public boolean onQueryTextChange(String s) {
				return false;
			}
		});
        mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                setUpAdapter(getAnimalNames(mZooData.getAnimals()));
                return false;
            }
        });
	}

	private String replacePolishChar(String s) {
		return s.replace("ą", "a").replace("ć", "c").replace("ę", "e").replace("ł", "l")
				.replace("ń", "n").replace("ó", "o").replace("ś", "s").replace("ż", "z")
				.replace("ź", "z");
	}

	private void clearSearchView() {
		mSearchView.clearFocus();
        mSearchView.setQuery(null, false);
	}

    private void setUpAdapter(String[] data) {
        mAnimaladapter = new ArrayAdapter<String>(
                getActivity(), R.layout.animal_list_item, data);
        mAnimalListView.setAdapter(mAnimaladapter);
    }

	/**
	 * Reads names of animals from ZooLocationsData.
	 * 
	 * @return array of strings with animal names
	 */

	private String[] getAnimalNames(ArrayList<Animal> animalList) {
		ArrayList<Animal> animals = animalList;
		String[] animalNames = new String[animals.size()];
		for (int i = 0; i < animalNames.length; i++) {
			animalNames[i] = animals.get(i).getName(
					Locale.getDefault().getDisplayLanguage());
		}
		return animalNames;
	}

	private class AnimalListOnClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view,
				int position, long id) {
            clearSearchView();

			Animal animal = mZooData.getAnimals().get(position);

			Fragment[] fragments = {
					AnimalDescriptionTab
							.newInstance(R.drawable.placeholder_adult, R.string.text),
					AnimalDetailsMapFragment.newInstance(animal)
			};
			Fragment newFragment = FragmentTabManager.newInstance(R.array.animal_desc_tabs_name,
					fragments, animal);

			FragmentHelper.swapFragment(R.id.flFragmentHolder, newFragment, getFragmentManager(),
					BundleConstants.FRAGMENT_ANIMAL_DETAIL);
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		MenuItem itemSearch = menu.findItem(R.id.action_search);
		MenuItem itemFilter = menu.findItem(R.id.action_filter);
		if (itemSearch != null) itemSearch.setVisible(false);
		if (itemFilter != null) itemFilter.setVisible(false);
	}

}
