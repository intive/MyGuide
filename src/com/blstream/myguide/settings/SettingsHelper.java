
package com.blstream.myguide.settings;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.util.Log;

import com.blstream.myguide.R;

public class SettingsHelper {

	private static final String LOG_TAG = SettingsHelper.class.getSimpleName();
	private static final String INFO_RESOURCES = "Using xml file from resources.";
	private static final String INFO_SDCARD = "Using xml file from SD card.";

	public Settings parse(Context context, File xml) throws IOException, XmlPullParserException,
			ParserConfigurationException, SAXException {

		boolean debug = 0 != (context.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE);
		InputStream is = null;
		Settings settings = null;
		final SettingsParser parser = new SettingsParser();

		if (!debug || xml == null) {
			is = context.getResources().openRawResource(R.raw.config);
			Log.i(LOG_TAG, "SETTINGS::  " + INFO_RESOURCES);
		}
		else {
			is = new FileInputStream(xml);
			Log.i(LOG_TAG, "SETTINGS::  " + INFO_SDCARD);
		}
		settings = parser.parseSettings(is);

		is.close();

		return settings;
	}
}
