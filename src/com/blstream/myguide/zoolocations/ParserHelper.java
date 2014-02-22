package com.blstream.myguide.zoolocations;

import java.io.InputStream;
import android.content.pm.ApplicationInfo;
import android.content.Context;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import com.blstream.myguide.R;

public class ParserHelper {
	
	/** Check if build type of application is set to debug.
	 * @return true if yes, false if no
	 */ 
	private boolean isDebugBuild(Context ctx) {
		return ( 0 != ( ctx.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE ));	
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

		boolean debug = isDebugBuild(ctx);
		InputStream is = null;
		ZooLocationsData data = null;
		ZooLocationsDataParser parser = new ZooLocationsDataParser();
		
		try {
			if (!debug || file == null) {
				is = ctx.getResources().openRawResource(R.raw.data);
			}
			else {
				is = new FileInputStream(file);
			}
			data = parser.parse(is);
		} finally {
			is.close();
		}
		return data;	
	}
}
