
package com.blstream.myguide;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.blstream.myguide.zoolocations.Animal;

//TODO navigation drawer

/**
 * Created by Piotrek on 23.03.14.
 */
public class AnimalDescriptionActivity extends FragmentActivity {

	private ViewPager mViewPager;
	private ActionBar mActionBar;
	private Animal mAnimal;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_animal_description);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			// get Animal Object
			mAnimal = (Animal) extras.getSerializable("TAG");
		}

		mActionBar = getActionBar();
		if (mActionBar != null) {
			mActionBar.setHomeButtonEnabled(true);
			mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

			mActionBar.setTitle(mAnimal.getName());
			mActionBar.setIcon(android.R.color.transparent);
			mActionBar.setDisplayHomeAsUpEnabled(true);

			setUpViewPager();
			setUpTabs();
		}
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
	private void setUpViewPager() {
		AnimalPagerAdapter mAdapter;

		mViewPager = (ViewPager) findViewById(R.id.pager);
		mAdapter = new AnimalPagerAdapter(getSupportFragmentManager());
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
