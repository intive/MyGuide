
package com.blstream.myguide;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TabForChildren extends Fragment {
	View mView;
	TextView mDescription;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		mView = View.inflate(getActivity(), R.layout.tab_for_children, null);
		mDescription = (TextView) mView.findViewById(R.id.textDescription);
		mDescription.setMovementMethod(new ScrollingMovementMethod());

		return mView;

	}

}
