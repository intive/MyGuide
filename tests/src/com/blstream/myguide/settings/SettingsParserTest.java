
package com.blstream.myguide.settings;

import java.io.InputStream;

import android.test.AndroidTestCase;

import com.blstream.myguide.R;

public class SettingsParserTest extends AndroidTestCase {

	private static final String langFallback = "pl";
	private static final float internalRadius = 1;
	private static final float externalRadius = 2;
	private static final String langTag = "lang_fallback";
	private static final String interTag = "internal_object_radius";
	private static final String exterTag = "external_object_radius";

	public void testParsingSettings() {
		SettingsParser parser = new SettingsParser();
		InputStream is = null;
		Settings s = null;

		try {
			is = this.getContext().getResources().openRawResource(R.raw.config);
			s = parser.parseSettings(is);

			is.close();

		} catch (Exception e) {
			fail(e.toString());
		}

		assertNotNull(is);
		assertNotNull(s);
		assertEquals(langFallback, s.get(langTag));
		assertEquals(langFallback, s.getValueAsString(langTag));
		assertEquals(internalRadius, s.getValueAsFloat(interTag));
		assertEquals(externalRadius, s.getValueAsFloat(exterTag));

	}

}
