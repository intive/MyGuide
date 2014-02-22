package com.blstream.myguide.zoolocations;

import java.util.ArrayList;


/**This class contains informations about animals, ways and ways'
 * junctions and theirs locations in the zoo parsed from xml file.
 * */
public class ZooLocationsData {

	private ArrayList<Animal> animals;
	private ArrayList<Way> ways;
	private ArrayList<Junction> junctions;	

	public ZooLocationsData(ArrayList<Animal> animals, ArrayList<Way> ways, ArrayList<Junction> junctions) {
		this.animals = animals;
		this.ways = ways;
		this.junctions = junctions;
	}
	
	public ArrayList<Animal> getAnimals() {
		return animals;
	}
	
	public ArrayList<Way> getWays() {
		return ways;
	}
	
	public ArrayList<Junction> getJunctions() {
		return junctions;
	}
	
	public void setAnimals(ArrayList<Animal> animals) {
		this.animals = animals;
	}

	public void setWays(ArrayList<Way> ways) {
		this.ways = ways;
	}

	public void setJunctions(ArrayList<Junction> junctions) {
		this.junctions = junctions;
	}

}
