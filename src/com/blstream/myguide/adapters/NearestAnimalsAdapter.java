package com.blstream.myguide.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.blstream.myguide.R;
import com.blstream.myguide.R.id;
import com.blstream.myguide.R.layout;
import com.blstream.myguide.zoolocations.Animal;
import com.blstream.myguide.zoolocations.AnimalDistance;

/**
 * Custom ListView Adapter for the list of nearest animals. Shows the picture of
 * an animal, animal's name, fun fact about the animal and user's distance from
 * the animal.
 * 
 * @author Agnieszka
 * */

public class NearestAnimalsAdapter extends BaseAdapter {

	private final int NUM_OF_CHARS_IN_LINE = 18;
	private Context mContext;
	private ArrayList<AnimalDistance> mAnimals;

	public NearestAnimalsAdapter(Context context,
			ArrayList<AnimalDistance> animals) {
		super();
		this.mContext = context;
		this.mAnimals = animals;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

		convertView = inflater.inflate(R.layout.custom_animal_row, null);

		ImageView animalImage = (ImageView) convertView
				.findViewById(R.id.imgvAnimalIcon);
		TextView animalName = (TextView) convertView
				.findViewById(R.id.txtvAnimalName);
		TextView animalDistance = (TextView) convertView
				.findViewById(R.id.txtvAnimalDistance);
		TextView animalFact = (TextView) convertView
				.findViewById(R.id.txtvAnimalFunFact);

		Animal animal = ((AnimalDistance) getItem(position)).getAnimal();
		int distance = ((AnimalDistance) getItem(position)).getDistance();

		animalName.setText(prepareName(animal.getName(), NUM_OF_CHARS_IN_LINE));
		animalFact.setText(animal.getDescriptionAdult().getText());
		animalDistance.setText(Integer.toString(distance) + "m");
		// TODO image

		return convertView;
	}

	public static String prepareName(String name, int characterNumber) {
		if (name.contains(" ") && name.length() > characterNumber)
			name = name.replace(' ', '\n');
		return name;
	}

	@Override
	public int getCount() {
		return mAnimals.size();
	}

	@Override
	public Object getItem(int position) {
		return mAnimals.get(position);
	}

	@Override
	public long getItemId(int position) {
		return mAnimals.indexOf(getItem(position));
	}

}
