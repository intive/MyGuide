
package com.blstream.myguide;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;

import com.blstream.myguide.settings.Settings;
import com.blstream.myguide.zoolocations.AccessInformation;
import com.blstream.myguide.zoolocations.Address;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Collection;

public class AccessFragment extends Fragment
		implements Parcelable {

	private static final String LOG_TAG = AccessFragment.class.getSimpleName();

	private static final int GRIDS_COLUMN_COUNT = 6;

	public static AccessFragment newInstance() {
		return new AccessFragment();
	}

	private Address mAddress;
	private SupportMapFragment mMapFragment;
	private LatLng mDestination;

	private int pixelsFromDp(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources()
				.getDisplayMetrics());
	}

	private void configureGrid(GridLayout gridLayout, int columns, Collection<String> items) {
		int itemsCount = items.size();
		int rows = itemsCount / columns + 1;
		gridLayout.setColumnCount(columns);
		gridLayout.setRowCount(rows);

		int column = 0, row = 0;
		for (String str : items) {
			if (column == columns) {
				column = 0;
				row++;
			}
			gridLayout.addView(newGridItem(str, newLayoutParams(row, column++)));
		}
	}

	protected ViewGroup.LayoutParams newLayoutParams(int row, int column) {
		GridLayout.LayoutParams params = new GridLayout.LayoutParams(GridLayout.spec(row),
				GridLayout.spec(column));
		params.setMargins(pixelsFromDp(2), pixelsFromDp(2), pixelsFromDp(2), pixelsFromDp(2));
		params.setGravity(Gravity.CENTER);

		return params;
	}

	protected View newGridItem(String text, ViewGroup.LayoutParams params) {
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View rootView = inflater.inflate(R.layout.fragment_information_access_griditem, null);
		TextView textView = (TextView) rootView.findViewById(R.id.txtvText);

		rootView.setLayoutParams(params);
		textView.setText(text);

		return rootView;
	}

	/**
	 * Used in getting text resources from data parsed from data.xml. Compatible
	 * with {@link com.blstream.myguide.zoolocations.Language}
	 * 
	 * @return language code
	 */
	protected String getLanguage() {
		return java.util.Locale.getDefault().getLanguage();
	}

	/**
	 * Creates a valid URI for Google Maps application which instruct the
	 * application to find route from <i>source</i> to <i>destination</i>.
	 * Google Maps takes current user position if no source is specified.
	 * 
	 * @param source starting point, <i>null</i> - current user position
	 * @param destination destination point
	 * @return Uri to used with Google Maps application
	 */
	protected Uri createUriForMaps(LatLng source, LatLng destination) {
		if (destination == null) throw new IllegalArgumentException("destination must not be null");

		StringBuilder sb = new StringBuilder("http://maps.google.com/maps?");
		if (source != null) sb.append("saddr=" + source.latitude + "," + source.longitude + "&");
		sb.append("daddr=" + destination.latitude + "," + destination.longitude);

		return Uri.parse(sb.toString());
	}

	protected void configureMap(GoogleMap map) {
		// lock all gestures
		map.getUiSettings().setAllGesturesEnabled(false);
		map.getUiSettings().setZoomControlsEnabled(false);

		// place camera on ZOO entrance and mark the spot
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(mDestination, 14.5f));
		map.addMarker(new MarkerOptions().position(mDestination));

		// set map's on click event: run Google Maps app to display route
		map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
			@Override
			public void onMapClick(LatLng latLng) {
				Intent intent = new Intent(Intent.ACTION_VIEW, createUriForMaps(null, mDestination));
				intent.setClassName("com.google.android.apps.maps",
						"com.google.android.maps.MapsActivity");
				startActivity(intent);
			}
		});
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		MyGuideApp app = (MyGuideApp) getActivity().getApplication();
		mAddress = app.getZooData().getContactInformation().getAddress();
	}

	@Override
	public View onCreateView(LayoutInflater layoutInflater, ViewGroup container,
			Bundle savedInstanceState) {
		AccessInformation accessInformation = ((MyGuideApp) getActivity().getApplication())
				.getZooData()
				.getAccessInformation();

		View rootView = layoutInflater.inflate(R.layout.fragment_information_access, container,
				false);
		((TextView) rootView.findViewById(R.id.txtv_parking)).setText(accessInformation
				.getParkingInformation(getLanguage()));
		((TextView) rootView.findViewById(R.id.txtvOffice)).setText(mAddress.getName());
		((TextView) rootView.findViewById(R.id.txtvAddress)).setText(mAddress.getStreet());

		configureGrid(
				(GridLayout) rootView.findViewById(R.id.grid_trams),
				GRIDS_COLUMN_COUNT,
				accessInformation.splitTrams());
		configureGrid(
				(GridLayout) rootView.findViewById(R.id.grid_buses),
				GRIDS_COLUMN_COUNT,
				accessInformation.splitBuses());

		// check if MapFragment is included in this Fragment
		// if not replace a View with new instance of MapFragment
		FragmentManager fm = getChildFragmentManager();
		mMapFragment = (SupportMapFragment) fm.findFragmentById(R.id.map);
		if (mMapFragment == null) {
			mMapFragment = SupportMapFragment.newInstance();
			fm.beginTransaction()
					.replace(R.id.map, mMapFragment)
					.commit();
			fm.executePendingTransactions();
		}
		// please note that GoogleMap object will not be available at the spot
		// so GoogleMap object will be requested in the next Fragment's
		// lifecycle method

		return rootView;
	}

	@Override
	public void onStart() {
		super.onStart();

		// read ZOO entrance from settings
		Settings settings = ((MyGuideApp) getActivity().getApplication()).getSettings();
		mDestination = new LatLng(
				settings.getValueAsDouble(Settings.KEY_ZOO_ENTRANCE_LAT),
				settings.getValueAsDouble(Settings.KEY_ZOO_ENTRANCE_LNG));

		configureMap(mMapFragment.getMap());
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int i) {

	}

}
