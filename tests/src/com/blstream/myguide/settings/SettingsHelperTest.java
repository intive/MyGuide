
package com.blstream.myguide.settings;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import android.test.AndroidTestCase;

public class SettingsHelperTest extends AndroidTestCase {

	private static final String langFallback = "pl";
	private static final float internalRadius = 1;
	private static final float externalRadius = 2;
	private static final String langTag = "lang_fallback";
	private static final String interTag = "internal_object_radius";
	private static final String exterTag = "external_object_radius";

	public void testXmlFromResources() throws IOException, XmlPullParserException {

		SettingsHelper sh = new SettingsHelper();
		Settings s = null;
		s = sh.parse(this.getContext(), null);

		assertNotNull(s);
		assertEquals(langFallback, s.get(langTag));
		assertEquals(langFallback, s.getValueAsString(langTag));
		assertEquals(internalRadius, s.getValueAsFloat(interTag));
		assertEquals(externalRadius, s.getValueAsFloat(exterTag));

	}

	public void testProvidedXml() throws IOException, XmlPullParserException {

		SettingsHelper sh = new SettingsHelper();

		String xml = "<configuration>"
				+ "<lang_fallback>pl</lang_fallback>"
				+ "<test_setting_0>333</test_setting_0>"
				+ "<internal_object_radius>1</internal_object_radius>"
				+ "<external_object_radius>2</external_object_radius>"
				+ "<test_setting_1>2.5</test_setting_1>"
				+ "<test_setting_2>test</test_setting_2>"
				+ "</configuration>";

		File file = File.createTempFile("settings", "xml", this.getContext().getCacheDir());
		FileWriter writer = null;
		writer = new FileWriter(file);
		writer.write(xml);
		writer.close();

		Settings s = sh.parse(this.getContext(), file);

		assertNotNull(file);
		assertNotNull(s);

		assertEquals(langFallback, s.getValueAsString(langTag));
		assertEquals(internalRadius, s.getValueAsFloat(interTag));
		assertEquals(externalRadius, s.getValueAsFloat(exterTag));

		assertEquals(2.5f, s.getValueAsFloat("test_setting_1"));
		assertEquals("test", s.getValueAsString("test_setting_2"));
		assertEquals(333, s.getValueAsInt("test_setting_0"));

	}

}
