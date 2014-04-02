
package com.blstream.myguide.zoolocations;

import java.io.Serializable;
import java.util.HashMap;

/**
 * This class contains animal description (in various languages) and name
 * (address) of image with this animal.
 */
public class Description implements Serializable{

	private String mImageName;
	private HashMap<String, String> mTexts;

	public Description() {
		mTexts = new HashMap<String, String>();
	}

	public void addText(String lang, String text) {
		mTexts.put(lang, text);
	}

	public void setImageName(String imageName) {
		mImageName = imageName;
	}

	public String getText() {
		return mTexts.get(Language.DEFAULT);
	}

	public String getText(String language) {
		String text = mTexts.get(language);
		if (text == null) { return this.getText(); }
		return text;
	}

	public String getImageName() {
		return mImageName;
	}
}
