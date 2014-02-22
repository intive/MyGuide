package com.blstream.myguide;

import android.app.Activity;
import android.os.Bundle;

import android.util.Log;

import com.blstream.myguide.zoolocations.*;

public class MainActivity extends Activity
{
	
    private final static String LOG_TAG = "zoolocations";
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        ParserHelper ph = new ParserHelper();
        try {
            ZooLocationsData data = ph.parse(this, null);
            Log.i(LOG_TAG, "animals: " + data.getAnimals().size());
            Log.i(LOG_TAG, "ways: " + data.getWays().size());
            Log.i(LOG_TAG, "junctions: " + data.getJunctions().size());
        } catch (Exception e) {
            Log.d(LOG_TAG, e.toString());
        }
    }
    
}
