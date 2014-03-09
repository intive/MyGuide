
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

import com.blstream.myguide.R;

public class SettingsHelper {

	public Settings parse(Context context, File xml) throws IOException, XmlPullParserException {

		boolean debug = 0 != (context.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE);
		InputStream is = null;
		Settings settings = null;
		SettingsParser parser = new SettingsParser();

		try {
			if (!debug || xml == null) {
				is = context.getResources().openRawResource(R.raw.config);
			}
			else {
				is = new FileInputStream(xml);
			}
			settings = parser.parseSettings(is);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} finally {
			is.close();
		}
		return settings;
	}
}
