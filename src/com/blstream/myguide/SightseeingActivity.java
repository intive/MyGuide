package com.blstream.myguide;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

public class SightseeingActivity extends Activity{

    private ImageView mImgvSlidingMenu;
    private ImageView mImgvShowRoute;
    private SearchView mSearchView;
    private ImageView mSearchViewClose;

    /*public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_main, menu);
		return true;
	}*/
    
    @Override
    public void onCreate(Bundle savedInstanceState){
    	Log.d(UI_MODE_SERVICE, "OnCreate");
    
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_sightseeing);
        
        Log.d(UI_MODE_SERVICE,""+this.getActionBar());
        if (getActionBar() != null) {
            getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
            getActionBar().setCustomView(R.layout.action_bar_sightseeing);
            getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

            View v = getActionBar().getCustomView();
            setUpActionBar(v);
            setUpActionBarListeners();
        }
    }

    private void setUpActionBar(View v) {
    	Log.d(UI_MODE_SERVICE, "Setting up action bar");
        mSearchView = (SearchView) v.findViewById(R.id.svSightseeing);
        mImgvSlidingMenu = (ImageView) v.findViewById(R.id.imgvSlidingMenu);
        mImgvShowRoute = (ImageView) v.findViewById(R.id.imgvShowRoute);

        mSearchView.setQueryHint(getString(R.string.search_sightseeing));
        mSearchView.setIconified(false);
        mSearchView.clearFocus();

        int searchPlateId = mSearchView.getContext().getResources().getIdentifier("android:id/search_plate", null, null);
        View searchPlate = mSearchView.findViewById(searchPlateId);

        if (searchPlate != null) {
            searchPlate.setBackgroundResource(R.drawable.rounded_edittext);

            int searchTextId = searchPlate.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
            TextView searchText = (TextView) searchPlate.findViewById(searchTextId);
            if (searchText != null) {
                searchText.setGravity(Gravity.CENTER);
            }

            int search = searchPlate.getContext().getResources().getIdentifier("android:id/search_close_btn", null, null);
            mSearchViewClose = (ImageView) searchPlate.findViewById(search);
            if (mSearchViewClose != null) {
                mSearchViewClose.setVisibility(View.GONE);
            }
        }
    }

    private void setUpActionBarListeners() {
        mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                mSearchView.clearFocus();
                mSearchViewClose.setVisibility(View.GONE);
                return true;
            }
        });


        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                //TODO finding text

                mSearchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                //TODO we can crete database for parsed data, then we can use AutoCompleText of animals when search is use

                return false;
            }
        });

        mImgvSlidingMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO handle show sliding men
            }
        });

        mImgvShowRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO handle show route

            }
        });
    }


    @Override
    public void onBackPressed() {
            super.onBackPressed();
    }
}
