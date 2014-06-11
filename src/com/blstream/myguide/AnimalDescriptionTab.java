
package com.blstream.myguide;

import java.util.Locale;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.blstream.myguide.zoolocations.Animal;

public class AnimalDescriptionTab extends Fragment implements Parcelable {

	private static final String LOG_TAG = AnimalDescriptionTab.class.getSimpleName();

	private View mView;
	private WebView mBrowser;
	private Animal mAnimal;

	public static AnimalDescriptionTab newInstance() {
		return new AnimalDescriptionTab();
	}

	public static AnimalDescriptionTab newInstance(Animal animal) {
		Bundle bundle = new Bundle();
		bundle.putSerializable(BundleConstants.SELECTED_ANIMAL, animal);
		AnimalDescriptionTab f = new AnimalDescriptionTab();
		f.setArguments(bundle);
		return f;
	}

	private void parseArguments() {
		Bundle arguments = getArguments();
		if (arguments == null) {
			Log.w(LOG_TAG, "No arguments given");
			return;
		}
		mAnimal = (Animal) arguments.getSerializable(BundleConstants.SELECTED_ANIMAL);
		if (mAnimal == null) Log.w(LOG_TAG, "NULL is not an Animal");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		Bundle bundle = getArguments();
		parseArguments();

		mView = View.inflate(getActivity(), R.layout.webview_animal_description, null);

		if (bundle != null) {
			mBrowser = (WebView) mView.findViewById(R.id.webv_animal_description);

			mBrowser.setWebViewClient(new WebViewClient() {
				@Override
				public void onReceivedError(WebView view, int errorCode, String description,
						String failingUrl) {
					
					mBrowser.loadUrl("file:///android_asset/nodata/nodata.html");
				}
			});
			mBrowser.loadUrl("file:///android_asset/"
					+ mAnimal.getInfoWeb(Locale.getDefault().getLanguage()));
		}

		return mView;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int i) {

	}
}