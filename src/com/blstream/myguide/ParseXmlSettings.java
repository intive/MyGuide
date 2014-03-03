
package com.blstream.myguide;

import java.io.File;

import android.content.Context;
import android.util.Log;

import com.blstream.myguide.settings.Settings;
import com.blstream.myguide.settings.SettingsHelper;

public class ParseXmlSettings {

	private static final String LOG_TAG = ParseXmlSettings.class.getSimpleName();
	private Settings mSettings;

	public void parsing(Context context, File file) {

		SettingsHelper sh = new SettingsHelper();
		if (file != null && file.length() > 0) {
			try {
				mSettings = sh.parse(context, file);
				Log.i(LOG_TAG, "lang: " + mSettings.getLangFallback());
				Log.i(LOG_TAG, "internal: " + mSettings.getInternalRadius());
				Log.i(LOG_TAG, "external: " + mSettings.getExternalRadius());
			} catch (Exception e) {
				Log.d(LOG_TAG, e.toString());
			}
		}
		else {
			try {
				mSettings = sh.parse(context, null);
			} catch (Exception e2) {
				Log.d(LOG_TAG, e2.toString());

			}
		}

	}

}
