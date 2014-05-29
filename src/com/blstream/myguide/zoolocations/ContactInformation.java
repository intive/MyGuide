
package com.blstream.myguide.zoolocations;

import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ContactInformation {

	private Address mAddress;
	private String mPhoneNumber;
	private URL mWebsiteUrl;
	private LinkedList<String> mEmails;
	private ArrayList<Opening> mOpenings;

	protected ContactInformation setAddress(Address addr) {
		mAddress = addr;
		return this;
	}

	protected ContactInformation setPhoneNumber(String phoneNumber) {
		mPhoneNumber = phoneNumber;
		return this;
	}

	protected ContactInformation setWebsiteUrl(URL websiteUrl) {
		mWebsiteUrl = websiteUrl;
		return this;
	}

	protected ContactInformation setEmails(List<String> emails) {
		mEmails = new LinkedList<String>(emails);
		return this;
	}

	protected ContactInformation setOpenings(List<Opening> openings) {
		mOpenings = new ArrayList<Opening>(openings);
		return this;
	}

	public Address getAddress() {
		return mAddress;
	}

	public String getPhoneNumber() {
		return mPhoneNumber;
	}

	public URL getWebsiteUrl() {
		return mWebsiteUrl;
	}

	public List<String> getEmails() {
		return new LinkedList<String>(mEmails);
	}

	public ArrayList<Opening> getOpenings() {
		return new ArrayList<Opening>(mOpenings);
	}

}
