package com.blstream.myguide;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;

public class AnimalListActivity extends FragmentActivity {

	/**
	 * Temporary activity showing AnimalListFragment
	 */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		setContentView(R.layout.activity_animal_list);

	}
	
}
