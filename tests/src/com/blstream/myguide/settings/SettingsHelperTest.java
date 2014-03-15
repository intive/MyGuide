
package com.blstream.myguide.settings;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParserException;

import android.test.AndroidTestCase;

public class SettingsHelperTest extends AndroidTestCase {

	private static final String LANG_FALLBACK = "pl";
	private static final float INTER_RADIOUS = 1;
	private static final float EXTER_RADIOUS = 2;
	private static final String TAG_LANG = "lang_fallback";
	private static final String TAG_INTER_RADIOUS = "internal_object_radius";
	private static final String TAG_EXTER_RADIOUS = "external_object_radius";

	private SettingsHelper sh;
	private Settings s;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		// given
		sh = new SettingsHelper();
		s = null;
	}

	public void testXmlFromResources() throws IOException, XmlPullParserException,
			ParserConfigurationException, SAXException {

		// when
		s = sh.parse(this.getContext(), null);

		// then
		assertNotNull(s);
		assertEquals(LANG_FALLBACK, s.get(TAG_LANG));
		assertEquals(LANG_FALLBACK, s.getValueAsString(TAG_LANG));
		assertEquals(INTER_RADIOUS, s.getValueAsFloat(TAG_INTER_RADIOUS));
		assertEquals(EXTER_RADIOUS, s.getValueAsFloat(TAG_EXTER_RADIOUS));

	}

	public void testProvidedXml() throws IOException, XmlPullParserException,
			ParserConfigurationException, SAXException {

		// given
		String xml = "<configuration>"
				+ "<lang_fallback>pl</lang_fallback>"
				+ "<test_setting_0>333</test_setting_0>"
				+ "<internal_object_radius>1</internal_object_radius>"
				+ "<external_object_radius>2</external_object_radius>"
				+ "<test_setting_1>2.5</test_setting_1>"
				+ "<test_setting_2>test</test_setting_2>"
				+ "</configuration>";

		// when
		File file = File.createTempFile("settings", "xml", this.getContext().getCacheDir());
		FileWriter writer = null;
		writer = new FileWriter(file);
		writer.write(xml);
		writer.close();

		s = sh.parse(this.getContext(), file);

		// then
		assertNotNull(file);
		assertNotNull(s);

		assertEquals(LANG_FALLBACK, s.getValueAsString(TAG_LANG));
		assertEquals(INTER_RADIOUS, s.getValueAsFloat(TAG_INTER_RADIOUS));
		assertEquals(EXTER_RADIOUS, s.getValueAsFloat(TAG_EXTER_RADIOUS));

		assertEquals(2.5f, s.getValueAsFloat("test_setting_1"));
		assertEquals("test", s.getValueAsString("test_setting_2"));
		assertEquals(333, s.getValueAsInt("test_setting_0"));

	}

}
