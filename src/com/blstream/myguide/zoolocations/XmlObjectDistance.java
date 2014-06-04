
package com.blstream.myguide.zoolocations;

import java.io.Serializable;

public class XmlObjectDistance implements Comparable<XmlObjectDistance>, Serializable {

	private static final long serialVersionUID = 1191737465868834056L;

	private XmlObject mXmlObject;
	private double mDistance;

	public XmlObjectDistance(XmlObject object, double distance) {
		this.mXmlObject = object;
		this.mDistance = distance;
	}

	public XmlObject getXmlObject() {
		return mXmlObject;
	}

	public void setXmlObject(XmlObject object) {
		this.mXmlObject = object;
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
		return ((XmlObjectDistance) o).getXmlObject().equals(mXmlObject)
				&& ((XmlObjectDistance) o).getDistance() == (int) mDistance;
	}

}
