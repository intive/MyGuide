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
	private int mTextID;
	private int mImageID;

	public static AnimalDescriptionTabs newInstance() {
		AnimalDescriptionTabs mTabs = new AnimalDescriptionTabs();
		return mTabs;
	}

	public static AnimalDescriptionTabs newInstance(int image, int text) {
		AnimalDescriptionTabs mTabs = new AnimalDescriptionTabs();

		Bundle bundle = new Bundle();
		bundle.putInt(BundleConstants.TAB_TEXT_ID, text);
		bundle.putInt(BundleConstants.TAB_IMAGE_ID, image);
		mTabs = new AnimalDescriptionTabs();
		mTabs.setArguments(bundle);

		return mTabs;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		Bundle bundle = getArguments();
		mView = View.inflate(getActivity(), R.layout.tabs, null);

		if (bundle != null) {
			mTextID = bundle.getInt(BundleConstants.TAB_TEXT_ID);
			mImageID = bundle.getInt(BundleConstants.TAB_IMAGE_ID);

			mDescription = (TextView) mView.findViewById(R.id.textDescription);
			mDescription.setMovementMethod(new ScrollingMovementMethod());
			mImage = (ImageView) mView.findViewById(R.id.animal_image);

			mImage.setImageResource(mImageID);
			mText = getResources().getString(mTextID);
			mDescription.setText(mText);
		}

		return mView;
	}
}
