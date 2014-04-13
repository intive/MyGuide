
package com.blstream.myguide;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Piotrek on 2014-04-05.
 */
public class FragmentPagerAdapter extends FragmentStatePagerAdapter {

	private int mTabCount;
	private Fragment[] mFragments;

	public FragmentPagerAdapter(FragmentManager fm, Fragment[] fragments, int tabCount) {
		super(fm);
		mTabCount = tabCount;
		mFragments = fragments;
	}

	@Override
	public Fragment getItem(int position) {
		switch (position) {
			case 0:
				return mFragments[position];
			case 1:
				return mFragments[position];
			case 2:
				return mFragments[position];
		}
		return null;
	}

	@Override
	public int getCount() {
		return mTabCount;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return (position + 1) + "";
	}
}
