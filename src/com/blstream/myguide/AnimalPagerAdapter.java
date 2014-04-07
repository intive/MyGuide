package com.blstream.myguide;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Piotrek on 23.03.14. In getItem Method add return Fragment
 * statement. variable i is number of Tab.
 */
public class AnimalPagerAdapter extends FragmentStatePagerAdapter {

	private static AnimalDescriptionTabs mTabs;

	public AnimalPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int i) {
		switch (i) {
		case 0:
			mTabs = AnimalDescriptionTabs.newInstance(
					R.drawable.plaeholder_adult, R.string.text);
			return mTabs;
		case 1:
			mTabs = AnimalDescriptionTabs.newInstance(
					R.drawable.placeholder_child, R.string.text);
			return mTabs;
		case 2:
			mTabs = AnimalDescriptionTabs.newInstance();
			return mTabs;
		}
		return null;
	}

	@Override
	public int getCount() {
		return 3;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return (position + 1) + "";
	}

}
