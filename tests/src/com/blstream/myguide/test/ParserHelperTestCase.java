
package com.blstream.myguide.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.res.AssetManager.AssetInputStream;
import android.test.AndroidTestCase;

import com.blstream.myguide.zoolocations.ParserHelper;

/**
 * Class containing tests for ParserHelper. Every test depends on checking if
 * InputStream returned by method openXml() is a FileInputStream or
 * AssetInputStream.
 */
public class ParserHelperTestCase extends AndroidTestCase {

	public static class ParserHelperDebug extends ParserHelper {
		public boolean isDebugBuild(Context ctx) {
			return true;
		}

		public InputStream openXml(Context ctx, File file) throws IOException {
			return super.openXml(ctx, file);
		}
	}

	public static class ParserHelperNotDebug extends ParserHelper {
		public boolean isDebugBuild(Context context) {
			return false;
		}

		public InputStream openXml(Context ctx, File file) throws IOException {
			return super.openXml(ctx, file);
		}
	}

	/**
	 * Checks if ParserHelper can open xml from resources when application build
	 * is set to debug.
	 */
	public void testOpenXmlFromResInDebugBuild() throws IOException {
		// given
		ParserHelperDebug ph = new ParserHelperDebug();
		InputStream is = null;

		// when
		is = ph.openXml(this.getContext(), null);
		is.close();

		// then
		assertNotNull(is);
		assertTrue(is instanceof AssetInputStream);
	}

	/**
	 * Checks if ParserHelper can open xml from resources when application build
	 * is not set to debug.
	 */
	public void testOpenXmlFromResInReleaseBuild() throws IOException {
		// given
		ParserHelperNotDebug ph = new ParserHelperNotDebug();
		InputStream is = null;

		// when
		is = ph.openXml(this.getContext(), null);
		is.close();

		// then
		assertNotNull(is);
		assertTrue(is instanceof AssetInputStream);
	}

	/**
	 * Checks if ParserHelper can open chosen file when application build is set
	 * to debug.
	 */
	public void testOpenXmlFromFileInDebugBuild() throws IOException {
		// given
		ParserHelperDebug ph = new ParserHelperDebug();
		InputStream is = null;

		// when
		File dir = getContext().getCacheDir();
		File file = File.createTempFile("test1", "xml", dir);
		is = ph.openXml(this.getContext(), file);
		is.close();

		// then
		assertNotNull(is);
		assertTrue(is instanceof FileInputStream);
	}

	/**
	 * Checks if ParserHelper can open chosen file when application build is not
	 * set to debug.
	 */
	public void testOpenXmlFromFileInReleaseBuild() throws IOException {

		// given
		ParserHelperNotDebug ph = new ParserHelperNotDebug();
		InputStream is = null;

		// when
		File dir = getContext().getCacheDir();
		File file = File.createTempFile("test2", "xml", dir);
		is = ph.openXml(this.getContext(), file);
		is.close();

		// then
		assertNotNull(is);
		assertTrue(is instanceof AssetInputStream);
	}

}
