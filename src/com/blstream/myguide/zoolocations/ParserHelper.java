package com.blstream.myguide.zoolocations;

import android.content.pm.ApplicationInfo;
import android.content.Context;
import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParserException;

import com.blstream.myguide.R;

/** Class containing method which open appropriate xml file
 * (which depends on application is in debug mode or not)
 * and return result of parsing.**/
public class ParserHelper {
		
	private final static String LOG_TAG = "zoolocations";
	
	/** Check if build type of application is set to debug.
	 * @return true if yes, false if no
	 */ 
	protected boolean isDebugBuild(Context ctx) {
		return ( 0 != ( ctx.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE ));	
	}
	
	/**If build type of application is set to debug, function returns InputStream to file chosen by user.
	 * If chosen file is null or application is not set to debug function return InputStream to
	 * predefined file from resources.
	 * @param ctx context of application
	 * @param file file chosen by user
	 * @return InputStream to appropriate file
	*/
	protected InputStream openXml(Context ctx, File file) throws IOException {
		boolean debug = isDebugBuild(ctx);
		InputStream is = null;
		if (!debug || file == null) {
			if (file != null) {
				Log.i(LOG_TAG, "In realease build: Opening res/data.xml");
			}
			is = ctx.getResources().openRawResource(R.raw.data);
		}
		else {
			is = new FileInputStream(file);
		}
		return is;
	}
	
	/** This function creates {@link ZooLocationsDataParser} and returns result of its work as a
	 * {@link ZooLocationsData} object. 
	 * If build type of application is set to debug, parser can use file chosen by user. Otherwise it uses 
	 * predefined file from resources.
	 * @param ctx context of application
	 * @param file file chosen by user
	 * @return parsed data from xml file
	 * **/
	public ZooLocationsData parse(Context ctx, File file) throws IOException, XmlPullParserException {

		InputStream is = null;
		ZooLocationsData data = null;
		ZooLocationsDataParser parser = new ZooLocationsDataParser();
		
		try {
			is = openXml(ctx, file);
			data = parser.parse(is);
		} finally {
			is.close();
		}
		return data;	
	}
}
