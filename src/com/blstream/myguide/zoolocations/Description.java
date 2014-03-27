
package com.blstream.myguide.zoolocations;

import java.util.TreeMap;

/**
 * This class contains animal description (in various languages) and name
 * (address) of image with this animal.
 */
public class Description {

	private String mImageName;
	private TreeMap<String, String> mText;

	public void setText(TreeMap<String, String> text) {
		mText = text;
	}

	public void setImageName(String imageName) {
		mImageName = imageName;
	}

	public String getText() {
		return mText.get(Language.DEFAULT);
	}

	public String getText(String language) {
		String text = mText.get(language);
		if (text == null) { return this.getText(); }
		return text;
	}

	public String getImageName() {
		return mImageName;
	}
}
