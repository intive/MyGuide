
package com.blstream.myguide;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Piotrek on 23.03.14. In getItem Method add return Fragment
 * statement. variable i is number of Tab.
 */
public class AnimalPagerAdapter extends FragmentStatePagerAdapter {

	public AnimalPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int i) {
		switch (i) {
			case 0:
				return AnimalDescriptionTab
						.newInstance(R.drawable.placeholder_adult, R.string.text);
			case 1:
				return AnimalDescriptionTab
						.newInstance(R.drawable.placeholder_child, R.string.text);
			case 2:
				return AnimalDescriptionTab.newInstance();
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
