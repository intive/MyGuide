package com.blstream.myguide;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.blstream.myguide.zoolocations.*;

import java.util.List;

/**
 * Created by Piotrek on 2014-04-06.
 */
public class GastronomyListFragment extends Fragment {

    private ListView mListView;
    private GastronomyAdapter mGastronomyAdapter;
    private List<Restaurant> mRestaurants;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_view, container,
                false);
        setHasOptionsMenu(true);

        getActivity().getActionBar().setTitle("Gastronomy");
        getActivity().getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);

        ZooLocationsData mZooData = ((MyGuideApp) this.getActivity().getApplication())
                .getZooData();
        mRestaurants = mZooData.getRestaurant();


        mListView = (ListView) view.findViewById(R.id.lvListItem);
        mGastronomyAdapter = new GastronomyAdapter(getActivity(),R.layout.gastronomy_list_item,mRestaurants);
        mListView.setAdapter(mGastronomyAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                //send resturant object to fragment
                //create fragment
            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem itemSearch = menu.findItem(R.id.action_search);
        MenuItem itemFiltr = menu.findItem(R.id.action_filter);

        if (itemSearch != null) itemSearch.setVisible(false);
        if (itemFiltr != null) itemFiltr.setVisible(false);

        super.onCreateOptionsMenu(menu, inflater);
    }

}
