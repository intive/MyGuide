
package com.blstream.myguide.settings;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import android.test.AndroidTestCase;

public class SettingsHelperTest extends AndroidTestCase {

	private static final String mLangFallback = "pl";
	private static final float mInternalRadius = 1;
	private static final float mExternalRadius = 2;

	public void testXmlFromResources() throws IOException, XmlPullParserException {

		SettingsHelper sh = new SettingsHelper();
		Settings s = null;
		s = sh.parse(this.getContext(), null);

		assertNotNull(s);
		assertEquals(mLangFallback, s.getLangFallback());
		assertEquals(mInternalRadius, s.getInternalRadius());
		assertEquals(mExternalRadius, s.getExternalRadius());

	}

	public void testProvidedXml() throws IOException, XmlPullParserException {

		SettingsHelper sh = new SettingsHelper();

		String xml = "<configuration>"
				+ "<lang_fallback>pl</lang_fallback>"
				+ "<internal_object_radius>1</internal_object_radius>"
				+ "<external_object_radius>2</external_object_radius>"
				+ "</configuration>";

		File file = File.createTempFile("settings", "xml", this.getContext().getCacheDir());
		FileWriter writer = null;
		writer = new FileWriter(file);
		writer.write(xml);
		writer.close();

		Settings s = sh.parse(this.getContext(), file);

		assertNotNull(file);
		assertEquals(mLangFallback, s.getLangFallback());
		assertEquals(mInternalRadius, s.getInternalRadius());
		assertEquals(mExternalRadius, s.getExternalRadius());
		assertNotNull(s);

	}

}
