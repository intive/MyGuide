
package com.blstream.myguide.zoolocations;

public class Address {

	private String mName;
	private String mStreet;
	private String mPostalCode;
	private String mCity;
	private String mCountry;

	protected Address setName(String value) {
		mName = value;
		return this;
	}

	protected Address setStreet(String value) {
		mStreet = value;
		return this;
	}

	protected Address setPostalCode(String value) {
		mPostalCode = value;
		return this;
	}

	protected Address setCity(String value) {
		mCity = value;
		return this;
	}

	protected Address setCountry(String value) {
		mCountry = value;
		return this;
	}

	public String getName() {
		return mName;
	}

	public String getStreet() {
		return mStreet;
	}

	public String getPostalCode() {
		return mPostalCode;
	}

	public String getCity() {
		return mCity;
	}

	public String getCountry() {
		return mCountry;
	}

}
