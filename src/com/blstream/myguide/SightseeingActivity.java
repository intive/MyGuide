package com.blstream.myguide;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

public class SightseeingActivity extends Activity{

    private ImageView mImgvSlidingMenu;
    private ImageView mImgvShowRoute;
    private SearchView mSearchView;
    private ImageView mSearchViewClose;

    @Override
    public void onCreate(Bundle savedInstanceState){
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sightseeing);

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
