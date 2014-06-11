
package com.blstream.myguide;

import java.util.Locale;

import com.blstream.myguide.fragments.FragmentHelper;
import com.blstream.myguide.zoolocations.XmlObjectDistance;
import com.blstream.myguide.zoolocations.Animal;

import android.view.View.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BottomAnimalFragment extends Fragment {

	private ImageView mAnimalImage;
	private TextView mAnimalName;
	private TextView mDistance;
	private TextView mAnimalFunfact;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View mRootView = (LinearLayout) inflater.inflate(
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
		XmlObjectDistance animal = (XmlObjectDistance) data
				.getSerializable(BundleConstants.CLOSEST_ANIMAL);
		Animal a = (Animal) animal.getXmlObject();
		mAnimalName.setText(a.getName(Locale.getDefault().getLanguage()));
		mAnimalFunfact.setText(a.getDescriptionAdult()
				.getText());
        String[] name = a.getDescriptionAdult().getImageName().substring(4).split("\\.");
        int id = getResources().getIdentifier(name[0], "drawable",
                getActivity().getPackageName());
        mAnimalImage.setImageResource(id);
		mDistance.setText(Integer.toString(animal.getDistance()) + " m");
	}

	public void setDistance(int distance) {
		if (mDistance != null) mDistance.setText(Integer.toString(distance) + " m");
	}

	private class BottomAnimalOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			FragmentManager manager = getActivity().getSupportFragmentManager();
			Fragment map = manager.findFragmentById(R.id.map);
			FragmentTransaction transation = manager.beginTransaction();
			transation.remove(map);
			transation.commit();
			Fragment nearestAnimals = new NearestAnimalsListFragment();
			FragmentHelper.swapFragment(R.id.flFragmentHolder, nearestAnimals,
					manager, BundleConstants.FRAGMENT_NEAREST_ANIMALS);
		}

	}
}
