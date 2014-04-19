package com.blstream.myguide.zoolocations;

import java.io.Serializable;

public class AnimalDistance implements Comparable<AnimalDistance>, Serializable {

	private static final long serialVersionUID = 1191737465868834056L;

	private Animal mAnimal;
	private double mDistance;

	public AnimalDistance(Animal animal, double distance) {
		this.mAnimal = animal;
		this.mDistance = distance;
	}

	public Animal getAnimal() {
		return mAnimal;
	}

	public void setAnimal(Animal mAnimal) {
		this.mAnimal = mAnimal;
	}

	public int getDistance() {
		return (int) mDistance;
	}

	public void setDistance(Double mDistance) {
		this.mDistance = mDistance;
	}

	@Override
	public int compareTo(AnimalDistance other) {
		return Double.compare(mDistance, other.getDistance());
	}

	@Override
	public boolean equals(Object o) {
		return ((AnimalDistance) o).getAnimal().equals(mAnimal)
				&& ((AnimalDistance) o).getDistance() == (int)mDistance;
	}

}
