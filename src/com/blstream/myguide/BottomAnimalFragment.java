package com.blstream.myguide;

import com.blstream.myguide.fragments.FragmentHelper;
import com.blstream.myguide.zoolocations.AnimalDistance;

import android.view.View.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BottomAnimalFragment extends Fragment {

	private View mRootView;

	private ImageView mAnimalImage;
	private TextView mAnimalName;
	private TextView mDistance;
	private TextView mAnimalFunfact;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mRootView = (LinearLayout) inflater.inflate(
				R.layout.fragment_nearest_animal, container, false);

		mAnimalImage = (ImageView) mRootView
				.findViewById(R.id.imgvClosestAnimal);

		mAnimalName = (TextView) mRootView
				.findViewById(R.id.txtvClosestAnimalName);

		mAnimalFunfact = (TextView) mRootView
				.findViewById(R.id.txtvClosestAnimalFunFact);

		mDistance = (TextView) mRootView
				.findViewById(R.id.txtvClosestAnimalDistance);

		setAllValues(getArguments());
		mRootView.setOnClickListener(new BottomAnimalOnClickListener());

		return mRootView;
	}

	private void setAllValues(Bundle data) {
		AnimalDistance animal = (AnimalDistance) data
				.getSerializable(BundleConstants.CLOSEST_ANIMAL);
		mAnimalName.setText(animal.getAnimal().getName());
		mAnimalFunfact.setText(animal.getAnimal().getDescriptionAdult()
				.getText());
		mDistance.setText(Integer.toString(animal.getDistance())+" m");
	}

	private class BottomAnimalOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) { 
			FragmentManager manager = getActivity().getSupportFragmentManager();
			Fragment nearestAnimals = new NearestAnimalsListFragment();
			FragmentHelper.swapFragment(R.id.flFragmentHolder, nearestAnimals,
				manager, BundleConstants.FRAGMENT_NEAREST_ANIMALS);
		}

	}
}
