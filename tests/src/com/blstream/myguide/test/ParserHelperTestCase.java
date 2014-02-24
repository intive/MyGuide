package com.blstream.myguide.test;

import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;

import android.content.Context;
import android.test.AndroidTestCase;
import android.content.res.AssetManager.AssetInputStream;
import org.xmlpull.v1.XmlPullParserException;

import com.blstream.myguide.R;
import com.blstream.myguide.zoolocations.*;

/** Class containing tests for ParserHelper.
 * Every test depends on checking if InputStream returned by method openXml()
 * is a FileInputStream or AssetInputStream.*/
public class ParserHelperTestCase extends AndroidTestCase {
	

	public static class ParserHelperDebug extends ParserHelper {
		public boolean isDebugBuild(Context ctx) {
			return true;
		}		
		public InputStream openXml(Context ctx, File file) throws IOException  {
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

	/** Checks if ParserHelper can open xml from resources when application build is set to debug.*/
	public void testOpenXmlFromResInDebugBuild() {
		ParserHelperDebug ph = new ParserHelperDebug(); 
		assertNotNull(ph);
		InputStream is = null;
		try {
			is = ph.openXml(this.getContext(), null);
			assertNotNull(is);
			assertTrue(is instanceof AssetInputStream);
		} catch (Exception e) {
			fail(e.toString());
		} finally {
			try {
				is.close();
			} catch (Exception e2) {
				fail(e2.toString());
			}
		}
	}

	/** Checks if ParserHelper can open xml from resources when application build is not set to debug.*/
	public void testOpenXmlFromResInReleaseBuild() {
		ParserHelperNotDebug ph = new ParserHelperNotDebug(); 
		assertNotNull(ph);
		InputStream is = null;
		try {
			is = ph.openXml(this.getContext(), null);
			assertNotNull(is);
			assertTrue(is instanceof AssetInputStream);
		} catch (Exception e) {
			fail(e.toString());
		} finally {
			try {
				is.close();
			} catch (Exception e2) {
				fail(e2.toString());
			}
		}
	}

	/** Checks if ParserHelper can open chosen file when application build is set to debug.*/
	public void testOpenXmlFromFileInDebugBuild() {
		
		ParserHelperDebug ph = new ParserHelperDebug(); 
		assertNotNull(ph);
		InputStream is = null;
		try {
			File dir = getContext().getCacheDir();
			File file = File.createTempFile("test1", "xml", dir); 
			assertNotNull(file);
			is = ph.openXml(this.getContext(), file);
			assertNotNull(is);
			assertTrue(is instanceof FileInputStream);
		} catch (Exception e) {
			fail(e.toString());
		} finally {
			try {
				is.close();
			} catch (Exception e2) {
				fail(e2.toString());
			}
		}
	}

	/** Checks if ParserHelper can open chosen file when application build is not set to debug.*/
	public void testOpenXmlFromFileInReleaseBuild() {
		ParserHelperNotDebug ph = new ParserHelperNotDebug(); 
		assertNotNull(ph);
		InputStream is = null;
		try {
			File dir = getContext().getCacheDir();
			File file = File.createTempFile("test2", "xml", dir); 
			assertNotNull(file);
			is = ph.openXml(this.getContext(), file);
			assertNotNull(is);
			assertTrue(is instanceof AssetInputStream);
		} catch (Exception e) {
			fail(e.toString());
		} finally {
			try {
				is.close();
			} catch (Exception e2) {
				fail(e2.toString());
			}
		}
	}

}
