
package com.blstream.myguide;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class AnimalDescriptionTabs extends Fragment {

	private View mView;
	private TextView mDescription;
	private ImageView mImage;
	private String mText;
	private int mTabID;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		Bundle bundle = getArguments();
		mTabID = bundle.getInt(BundleConstants.TAB_ID);
		mView = View.inflate(getActivity(), R.layout.tabs, null);

		mDescription = (TextView) mView.findViewById(R.id.textDescription);
		mDescription.setMovementMethod(new ScrollingMovementMethod());
		mImage = (ImageView) mView.findViewById(R.id.animal_image);

		switch (mTabID) {
			case 0:
				mText = getResources().getString(R.string.text);
				mDescription.setText(mText);
				mImage.setImageResource(R.drawable.plaeholder_adult);
				return mView;
			case 1:
				mText = getResources().getString(R.string.text);
				mDescription.setText(mText);
				mImage.setImageResource(R.drawable.placeholder_child);
				return mView;
			case 2:
				// TODO Map activity
				return mView;
		}

		return null;
	}
}
