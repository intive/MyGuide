
package com.blstream.myguide.zoolocations;

import java.io.Serializable;

/**
 * Created by Piotrek on 2014-04-08.
 */
public abstract class XmlObject implements Serializable {

	abstract void addName(String lang, String name);

	public abstract String getName();
	
	public abstract String getName(String language);

	public abstract Node getNode();

}
