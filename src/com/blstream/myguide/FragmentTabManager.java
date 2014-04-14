
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

import com.blstream.myguide.zoolocations.XmlObject;

/**
 * Created by Piotrek on 2014-04-05. This class is to create fragment with Tab.
 * Simple use of this class: First we create array of Fragment which are
 * Tab(twoor three) And the we create one fragment call
 * FragmentTabManager.newInstance Fragment[] fragmentsTab =
 * {FirstTab.newInstance(), SecondTab.newInstance(),ThirdTab.newInstance()};
 * Fragment newFragment = FragmentTabManager.newInstance(R.array.tabs_name,
 * fragmentsTab, XmlObjet);
 */
public class FragmentTabManager extends Fragment {

	private static final String LOG_TAG = FragmentTabManager.class.getSimpleName();

	private ViewPager mViewPager;
	private ActionBar mActionBar;
	private String mTitle;
	private Fragment[] mFragments;
	private int mStringArray;
	private XmlObject mXmlObject;

	public static FragmentTabManager newInstance() {
		return new FragmentTabManager();
	}

	/**
	 * Method create fragment which create and manage tabs
	 * 
	 * @param stringArray - array of String which are Headers in Tab
	 * @param fragment - array of fragments which are used in each Tabs
	 * @param object - It's XmlObject which we used to take his attributes.
	 * @return fragemntTab object
	 */
	public static FragmentTabManager newInstance(int stringArray, Fragment[] fragment,
			XmlObject object) {
		FragmentTabManager fragmentTab = new FragmentTabManager();

		Bundle bundle = new Bundle();
		bundle.putInt(BundleConstants.STRING_ARRAY, stringArray);
		bundle.putSerializable(BundleConstants.SELECTED_OBJECT, object);
		bundle.putSerializable(BundleConstants.TAB_FRAGMENTS, fragment);
		bundle.putCharSequence(BundleConstants.TAB_TITLE, object.getName());
		fragmentTab.setArguments(bundle);

		return fragmentTab;
	}

	public static FragmentTabManager newInstance(int stringArray, Fragment[] fragment, String title) {
		FragmentTabManager fragmentTab = new FragmentTabManager();

		Bundle bundle = new Bundle();
		bundle.putInt(BundleConstants.STRING_ARRAY, stringArray);
		bundle.putCharSequence(BundleConstants.TAB_TITLE, title);
		bundle.putSerializable(BundleConstants.TAB_FRAGMENTS, fragment);
		fragmentTab.setArguments(bundle);

		return fragmentTab;
	}

	private void getArgs() {
		Bundle args = getArguments();
		if (args != null) {
			mTitle = (String) args.getCharSequence(BundleConstants.TAB_TITLE);
			mStringArray = args.getInt(BundleConstants.STRING_ARRAY);
			mFragments = (Fragment[]) args.getSerializable(BundleConstants.TAB_FRAGMENTS);
			if (mXmlObject != null) mXmlObject = (XmlObject) args
					.getSerializable(BundleConstants.SELECTED_OBJECT);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		getArgs();
		View mRootView = inflater.inflate(R.layout.fragment_animal_description, container, false);
		setHasOptionsMenu(true);

		mActionBar = getActivity().getActionBar();

		if (mActionBar != null) {
			mActionBar.removeAllTabs();

			mActionBar.setHomeButtonEnabled(true);
			mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

			mActionBar.setTitle(mTitle);
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
		MenuItem itemFiltr = menu.findItem(R.id.action_filter);

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

	// R.array.animal_desc_tabs_name
	private void setUpTabs() {
		for (String tab_name : getResources().getStringArray(mStringArray)) {
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
		FragmentPagerAdapter mAdapter;
		mViewPager = (ViewPager) view.findViewById(R.id.pager);
		mAdapter = new FragmentPagerAdapter(getChildFragmentManager(), mFragments, getResources()
				.getStringArray(mStringArray).length);
		mViewPager.setAdapter(mAdapter);
		// all three tabs always active when this Fragment is alive
		mViewPager.setOffscreenPageLimit(getResources()
				.getStringArray(mStringArray).length);

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
