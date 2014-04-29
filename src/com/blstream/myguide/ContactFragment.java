
package com.blstream.myguide;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blstream.myguide.settings.Settings;
import com.blstream.myguide.zoolocations.Address;
import com.blstream.myguide.zoolocations.ContactInformation;
import com.blstream.myguide.zoolocations.Opening;
import com.google.android.gms.maps.model.LatLng;

public class ContactFragment extends Fragment
		implements Parcelable {

	private static final String LOG_TAG = ContactFragment.class.getSimpleName();

	public static ContactFragment newInstance() {
		return new ContactFragment();
	}

	private static class ViewHolder {

		private TextView addressView;
		private TextView phoneNumberView;
		private TextView urlView;

	}

	private ViewHolder mViewHolder = new ViewHolder();
	private ContactInformation mContactInfo;
	private LatLng mZooCoords;

	private Intent intentForMaps(String tag, LatLng place) {
		// building URI with StringBuilder since String.format may cause
		// fractional part of a floating point numbers to be separated with
		// a different character than expected (which is .)
		return new Intent(Intent.ACTION_VIEW)
				.setData(Uri.parse((new StringBuilder("geo:"))
						.append(place.latitude)
						.append(",")
						.append(place.longitude)
						.append("?q=")
						.append(place.latitude)
						.append(",")
						.append(place.longitude)
						.append("(")
						.append(tag)
						.append(")")
						.toString()));
	}

	private Intent intentForDialerApp(String phoneNumber) {
		return new Intent(Intent.ACTION_DIAL)
				.setData(Uri.parse(String.format("tel:%s", phoneNumber)));
	}

	private Intent intentForWebBrowser(String url) {
		return new Intent(Intent.ACTION_VIEW, Uri.parse(url));
	}

	private void inflateOpenings(ViewGroup parent) {
		LayoutInflater layoutInflater = getActivity().getLayoutInflater();
		for (Opening opening : mContactInfo.getOpenings()) {
			ViewGroup layout = (ViewGroup) layoutInflater
					.inflate(R.layout.fragment_information_contact_opening, null);
			if (layout == null) continue;

			((TextView) layout.findViewById(R.id.txtvTitle)).setText(
					opening.getDescription(getLanguage()));
			((TextView) layout.findViewById(R.id.txtvWeekdaysHours)).setText(
					formatOpeningHours(opening.getHours(Opening.When.WEEKDAYS)));
			((TextView) layout.findViewById(R.id.txtvWeekendsHours)).setText(
					formatOpeningHours(opening.getHours(Opening.When.WEEKENDS)));

			parent.addView(layout);
		}
	}

	private void setUpOnClickListeners() {
		mViewHolder.addressView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startActivity(intentForMaps(mContactInfo.getAddress().getName(), mZooCoords));
			}
		});

		mViewHolder.phoneNumberView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startActivity(intentForDialerApp(mViewHolder.phoneNumberView
						.getText()
						.toString()));
			}
		});

		mViewHolder.urlView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startActivity(intentForWebBrowser(mContactInfo.getWebsiteUrl().toString()));
			}
		});
	}

	/**
	 * Used in getting text resources from data parsed from data.xml.
	 * Compatible with {@link com.blstream.myguide.zoolocations.Language}
	 *
	 * @return language code
	 */
	protected String getLanguage() {
		return java.util.Locale.getDefault().getLanguage();
	}

	protected String formatOpeningHours(Opening.Hours hours) {
		return hours.from() + " - " + hours.to();
	}

	protected String formatAddress(Address address) {
		return (new StringBuilder())
				.append(address.getStreet())
				.append("\n")
				.append(address.getPostalCode())
				.append(" ")
				.append(address.getCity())
				.append(" ")
				.append(address.getCountry())
				.toString();
	}

	protected void setUpViews() {
		mViewHolder.addressView.setText(formatAddress(mContactInfo.getAddress()));
		mViewHolder.phoneNumberView.setText(mContactInfo.getPhoneNumber());
		mViewHolder.urlView.setText(mContactInfo.getWebsiteUrl().toString());

		setUpOnClickListeners();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		MyGuideApp app = (MyGuideApp) getActivity().getApplication();
		Settings settings = app.getSettings();

		double lat = settings.getValueAsDouble(Settings.KEY_ZOO_ENTRANCE_LAT);
		double lng = settings.getValueAsDouble(Settings.KEY_ZOO_ENTRANCE_LNG);

		mContactInfo = app.getZooData().getContactInformation();
		mZooCoords = new LatLng(lat, lng);
	}

	@Override
	public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = layoutInflater.inflate(R.layout.fragment_information_contact, container, false);
		ViewGroup seasonsLayout = (ViewGroup) rootView.findViewById(R.id.llSeasons);

		mViewHolder.addressView = (TextView) rootView.findViewById(R.id.txtvAddress);
		mViewHolder.phoneNumberView = (TextView) rootView.findViewById(R.id.txtvPhone);
		mViewHolder.urlView = (TextView) rootView.findViewById(R.id.txtvWebsite);

		inflateOpenings(seasonsLayout);
		setUpViews();

		return rootView;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int i) {
		return;
	}

}
