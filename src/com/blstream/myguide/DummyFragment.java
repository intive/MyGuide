
package com.blstream.myguide;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * This is a dummy placeholder fragment.
 * Will be removed in the future.
 */
public class DummyFragment extends Fragment
		implements Parcelable {

	private final static String LOG_TAG = DummyFragment.class.getSimpleName();

	public static DummyFragment newInstance(String name) {
		DummyFragment fragment = new DummyFragment();
		Bundle bundle = new Bundle();

		bundle.putString("name", name);
		fragment.setArguments(bundle);

		return fragment;
	}

	private String mName;

	private void parseArguments() {
		mName = getArguments().getString("name");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		parseArguments();
	}

	@Override
	public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = layoutInflater.inflate(R.layout.fragment_dummy, container, false);

		Log.d(LOG_TAG, mName);
		if (mName != null) {
			Button button = (Button) rootView.findViewById(R.id.worldDummiestButton);
			button.setText(mName);
		}

		return rootView;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int i) {

	}
}
