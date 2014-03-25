package com.blstream.myguide;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class AnimalListActivity extends Activity {
	
	private ListView mAnimalKingdomsList;
	private String[] mAnimalKingdomsStrings;
	private ActionBar mActionBar;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		setContentView(R.layout.activity_animal_list);
		
		mActionBar = getActionBar();

		if (mActionBar != null) {
			mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
			mActionBar.setCustomView(R.layout.action_bar_animal_list);
			mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

			View actionBarCustomView = mActionBar.getCustomView();
		}
		
		setUpAnimalKingdomList();
		
	}
	
	private void setUpAnimalKingdomList(){
		
		mAnimalKingdomsList = (ListView)findViewById(R.id.listViewListOfAnimalKingdoms);
		mAnimalKingdomsStrings = getResources().getStringArray(R.array.animal_kingdoms);
		
		ArrayAdapter<String> kingdomsArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.animal_kingdom_item, mAnimalKingdomsStrings);
		mAnimalKingdomsList.setAdapter(kingdomsArrayAdapter);
		
	}
	
}
