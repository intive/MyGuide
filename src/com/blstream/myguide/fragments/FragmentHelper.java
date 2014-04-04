package com.blstream.myguide.fragments;

import com.blstream.myguide.R;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class FragmentHelper {

	public static void initFragment(int container, Fragment fragment,
			FragmentManager fragmentManager) {
		FragmentTransaction ft = fragmentManager.beginTransaction();
		ft.add(container, fragment);
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		ft.commit();
	}

	public static void swapFragment(int container, Fragment newFragment,
			FragmentManager fragmentManager, String tag) {

		FragmentTransaction ft = fragmentManager.beginTransaction();
		ft.setCustomAnimations(R.anim.right_in, R.anim.left_out);
		ft.replace(container, newFragment, tag);
		ft.addToBackStack(null);
		ft.commit();
	}

}
