
package com.blstream.myguide.settings;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;
import android.util.Xml;

public class SettingsParser {

	private static final String TAG = Settings.class.getSimpleName();

	private String mLangFallback;
	private float mInternalRadius;
	private float mExternalRadius;

	public Settings parseSettings(InputStream is) throws XmlPullParserException, IOException,
			OutOfMemoryError {

		XmlPullParser xpp = Xml.newPullParser();
		xpp.setInput(new InputStreamReader(is));
		xpp.next();
		int eventType = xpp.getEventType();

		while (eventType != XmlPullParser.END_DOCUMENT) {
			if (eventType == XmlPullParser.START_TAG) {
				if ("configuration".equals(xpp.getName())) {
					xpp.nextTag();

					if ("lang_fallback".equals(xpp.getName())) {
						if (xpp.next() == XmlPullParser.TEXT) {
							mLangFallback = xpp.getText();
							Log.d(TAG, "langFallback: " + mLangFallback);
						}
					}
					xpp.nextTag();
					xpp.nextTag();

					if ("internal_object_radius".equals(xpp.getName())) {
						if (xpp.next() == XmlPullParser.TEXT) {
							mInternalRadius = Float.parseFloat(xpp.getText());
							Log.d(TAG, "internalRadius: " + mInternalRadius);
						}
					}
					xpp.nextTag();
					xpp.nextTag();

					if ("external_object_radius".equals(xpp.getName())) {
						if (xpp.next() == XmlPullParser.TEXT) {
							mExternalRadius = Float.parseFloat(xpp.getText());
							Log.d(TAG, "externalRadius: " + mExternalRadius);
						}
					}

				}

			}
			eventType = xpp.next();
		}

		return new Settings(mLangFallback, mInternalRadius, mExternalRadius);

	}

}
