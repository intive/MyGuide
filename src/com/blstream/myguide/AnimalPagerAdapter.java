
package com.blstream.myguide;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Piotrek on 23.03.14. In getItem Method add return Fragment
 * statement. variable i is number of Tab.
 */
public class AnimalPagerAdapter extends FragmentStatePagerAdapter {

	private Bundle mBundle;
	private AnimalDescriptionTabs mTabs;

	public AnimalPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int i) {
		switch (i) {
			case 0:
				mBundle = new Bundle();
				mBundle.putInt(BundleConstants.TAB_ID, i);
				mTabs = new AnimalDescriptionTabs();
				mTabs.setArguments(mBundle);
				return mTabs;
			case 1:
				mBundle = new Bundle();
				mBundle.putInt(BundleConstants.TAB_ID, i);
				mTabs = new AnimalDescriptionTabs();
				mTabs.setArguments(mBundle);
				return mTabs;
			case 2:
				mBundle = new Bundle();
				mBundle.putInt(BundleConstants.TAB_ID, i);
				mTabs = new AnimalDescriptionTabs();
				mTabs.setArguments(mBundle);
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
