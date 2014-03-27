
package com.blstream.myguide;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Piotrek on 23.03.14.
 * In getItem Method add return Fragment statement. variable i is number of Tab
 */
public class AnimalPagerAdapter extends FragmentStatePagerAdapter {

	public AnimalPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int i) {
		switch (i) {
			case 0:
				// return new AnimalDesriptionFragment
				return null;
			case 1:
				// return new FOrChildrenDescriptionFragment
				return null;
			case 2:
				// TODO Map activity
				return null;
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
