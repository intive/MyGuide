
package com.blstream.myguide;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Piotrek on 2014-04-01. This class is only to show Using of
 * AndroidDeatils. It will be remove.
 */
public class Sample extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return View.inflate(getActivity(), R.layout.sample, null);
	}
}
