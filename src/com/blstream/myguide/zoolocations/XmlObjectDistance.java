
package com.blstream.myguide.zoolocations;

import java.io.Serializable;

public class XmlObjectDistance implements Comparable<XmlObjectDistance>, Serializable {

	private static final long serialVersionUID = 1191737465868834056L;

	private XmlObject mXmlObject;
	private double mDistance;

	public XmlObjectDistance(XmlObject animal, double distance) {
		this.mXmlObject = animal;
		this.mDistance = distance;
	}

	public XmlObject getAnimal() {
		return mXmlObject;
	}

	public void setAnimal(XmlObject mAnimal) {
		this.mXmlObject = mAnimal;
	}

	public int getDistance() {
		return (int) mDistance;
	}

	public void setDistance(Double mDistance) {
		this.mDistance = mDistance;
	}

	@Override
	public int compareTo(XmlObjectDistance other) {
		return Double.compare(mDistance, other.getDistance());
	}

	@Override
	public boolean equals(Object o) {
		return ((XmlObjectDistance) o).getAnimal().equals(mXmlObject)
				&& ((XmlObjectDistance) o).getDistance() == (int) mDistance;
	}

}
