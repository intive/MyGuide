
package com.blstream.myguide.settings;

public class Settings {

	private String mLangFallback;
	private float mInternalRadius;
	private float mExternalRadius;

	public Settings() {

	}

	public Settings(String langFallback, float internalRadius, float externalRadius) {
		mLangFallback = langFallback;
		mInternalRadius = internalRadius;
		mExternalRadius = externalRadius;
	}

	public void setLangFallback(String langFallback) {
		mLangFallback = langFallback;
	}

	public void setInternalRadius(float internalRadius) {
		mInternalRadius = internalRadius;
	}

	public void setExternalRadius(float externalRadius) {
		mExternalRadius = externalRadius;
	}

	public String getLangFallback() {
		return mLangFallback;
	}

	public float getInternalRadius() {
		return mInternalRadius;
	}

	public float getExternalRadius() {
		return mExternalRadius;
	}

}
