
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
	public void testOpenXmlFromResInDebugBuild() {
		// given
		ParserHelperDebug ph = new ParserHelperDebug();
		InputStream is = null;
		Exception e = null;

		// when
		try {
			is = ph.openXml(this.getContext(), null);
		} catch (Exception e1) {
			e = e1;
		} finally {
			try {
				is.close();
			} catch (Exception e2) {
				e = e2;
			}
		}

		// then
		assertNull(e);
		assertNotNull(is);
		assertTrue(is instanceof AssetInputStream);
	}

	/**
	 * Checks if ParserHelper can open xml from resources when application build
	 * is not set to debug.
	 */
	public void testOpenXmlFromResInReleaseBuild() {
		// given
		ParserHelperNotDebug ph = new ParserHelperNotDebug();
		InputStream is = null;
		Exception e = null;
		
		// when
		try {
			is = ph.openXml(this.getContext(), null);
		} catch (Exception e1) {
			e = e1;
		} finally {
			try {
				is.close();
			} catch (Exception e2) {
				e = e2;
			}
		}
		
		// then
		assertNull(e);
		assertNotNull(is);
		assertTrue(is instanceof AssetInputStream);
	}

	/**
	 * Checks if ParserHelper can open chosen file when application build is set
	 * to debug.
	 */
	public void testOpenXmlFromFileInDebugBuild() {
		// given
		ParserHelperDebug ph = new ParserHelperDebug();
		InputStream is = null;
		Exception e = null;
		
		// when
		try {
			File dir = getContext().getCacheDir();
			File file = File.createTempFile("test1", "xml", dir);
			is = ph.openXml(this.getContext(), file);
		} catch (Exception e1) {
			e = e1;
		} finally {
			try {
				is.close();
			} catch (Exception e2) {
				e = e2;
			}
		}
		
		// then
		assertNull(e);
		assertNotNull(is);
		assertTrue(is instanceof FileInputStream);
	}

	/**
	 * Checks if ParserHelper can open chosen file when application build is not
	 * set to debug.
	 */
	public void testOpenXmlFromFileInReleaseBuild() {
		
		// given
		ParserHelperNotDebug ph = new ParserHelperNotDebug();
		InputStream is = null;
		Exception e = null;
		
		//when
		try {
			File dir = getContext().getCacheDir();
			File file = File.createTempFile("test2", "xml", dir);
			is = ph.openXml(this.getContext(), file);
		} catch (Exception e1) {
			e = e1;
		} finally {
			try {
				is.close();
			} catch (Exception e2) {
				e = e2;
			}
		}
		
		// then
		assertNull(e);
		assertNotNull(is);
		assertTrue(is instanceof AssetInputStream);
	}

}
