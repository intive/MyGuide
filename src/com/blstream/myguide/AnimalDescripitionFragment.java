
package com.blstream.myguide;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Sample fragment to show working of tabs Created by Piotrek on 23.03.14.
 */
public class AnimalDescripitionFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_animal_description, container, false);

		return rootView;
	}
}
