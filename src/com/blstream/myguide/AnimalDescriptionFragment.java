
package com.blstream.myguide;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.blstream.myguide.zoolocations.Animal;

/**
 * Created by Piotrek on 23.03.14.
 */
public class AnimalDescriptionFragment extends Fragment {

	private ViewPager mViewPager;
	private ActionBar mActionBar;
	private Animal mAnimal;

	public AnimalDescriptionFragment() {
	}

	public AnimalDescriptionFragment(Animal animal) {
		mAnimal = animal;
		Bundle args = new Bundle();
		args.putSerializable("mAnimal", mAnimal);
		setArguments(args);
	}

	private void getArgs() {
		Bundle args = getArguments();
		if (args != null) {
			mAnimal = (Animal) args.getSerializable("mAnimal");

		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// View mRootView = View.inflate(getActivity(),
		// R.layout.activity_animal_description, null);
		getArgs();
		View mRootView = inflater.inflate(R.layout.activity_animal_description, container, false);
		setHasOptionsMenu(true);

		mActionBar = getActivity().getActionBar();

		if (mActionBar != null) {
			mActionBar.removeAllTabs();

			mActionBar.setHomeButtonEnabled(true);
			mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

			mActionBar.setTitle(mAnimal.getName());
			mActionBar.setIcon(android.R.color.transparent);
			mActionBar.setDisplayHomeAsUpEnabled(true);

			setUpViewPager(mRootView);
			setUpTabs();
		}
		return mRootView;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		MenuItem itemSearch = menu.findItem(R.id.action_search);
		MenuItem itemFiltr = menu.findItem(R.id.action_filtr);
		if (itemSearch != null) itemSearch.setVisible(false);
		if (itemFiltr != null) itemFiltr.setVisible(false);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	/**
	 * Method add tabs to actionBar and set Tabs Listener.
	 */
	private void setUpTabs() {
		for (String tab_name : getResources().getStringArray(R.array.animal_desc_tabs_name)) {
			mActionBar.addTab(mActionBar.newTab().setText(tab_name)
					.setTabListener(new ActionBar.TabListener() {
						@Override
						public void onTabSelected(ActionBar.Tab tab,
								android.app.FragmentTransaction fragmentTransaction) {
							mViewPager.setCurrentItem(tab.getPosition());
						}

						@Override
						public void onTabUnselected(ActionBar.Tab tab,
								android.app.FragmentTransaction fragmentTransaction) {
						}

						@Override
						public void onTabReselected(ActionBar.Tab tab,
								android.app.FragmentTransaction fragmentTransaction) {
						}
					}));
		}
	}

	/**
	 * Method setUp ViewPager and Listener to swipe fragment
	 */
	private void setUpViewPager(View view) {
		AnimalPagerAdapter mAdapter;

		mViewPager = (ViewPager) view.findViewById(R.id.pager);
		mAdapter = new AnimalPagerAdapter(getActivity().getSupportFragmentManager());
		mViewPager.setAdapter(mAdapter);

		mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				mActionBar.setSelectedNavigationItem(position);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}
}
