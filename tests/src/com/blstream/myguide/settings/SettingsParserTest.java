
package com.blstream.myguide.settings;

import java.io.InputStream;

import android.test.AndroidTestCase;

import com.blstream.myguide.R;

public class SettingsParserTest extends AndroidTestCase {

	private static final String langFallback = "pl";
	private static final float internalRadius = 1;
	private static final float externalRadius = 2;

	public void testParsingSettings() {
		SettingsParser parser = new SettingsParser();
		InputStream is = null;
		Settings data = null;

		try {
			is = this.getContext().getResources().openRawResource(R.raw.config);
			data = parser.parseSettings(is);

			is.close();

		} catch (Exception e) {
			fail(e.toString());
		}

		assertNotNull(is);
		assertNotNull(data);
		assertEquals(langFallback, data.getLangFallback());
		assertEquals(internalRadius, data.getInternalRadius());
		assertEquals(externalRadius, data.getExternalRadius());

	}

}
