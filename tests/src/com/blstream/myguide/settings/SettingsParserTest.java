
package com.blstream.myguide.settings;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import android.test.AndroidTestCase;

import com.blstream.myguide.R;

public class SettingsParserTest extends AndroidTestCase {

	private static final String LANG_FALLBACK = "pl";
	private static final float INTER_RADIOUS = 1;
	private static final float EXTER_RADIOUS = 10;
	private static final String TAG_LANG = "lang_fallback";
	private static final String TAG_INTER_RADIOUS = "internal_object_radius";
	private static final String TAG_EXTER_RADIOUS = "external_object_radius";

	public void testParsingSettings() throws ParserConfigurationException, SAXException,
			IOException {

		// given
		SettingsParser parser = new SettingsParser();
		InputStream is = null;
		Settings s = null;

		// when
		is = this.getContext().getResources().openRawResource(R.raw.config);
		s = parser.parseSettings(is);
		is.close();

		// then
		assertNotNull(is);
		assertNotNull(s);
		assertEquals(LANG_FALLBACK, s.get(TAG_LANG));
		assertEquals(LANG_FALLBACK, s.getValueAsString(TAG_LANG));
		assertEquals(INTER_RADIOUS, s.getValueAsFloat(TAG_INTER_RADIOUS));
		assertEquals(EXTER_RADIOUS, s.getValueAsFloat(TAG_EXTER_RADIOUS));

	}

}
