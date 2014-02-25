
package com.blstream.myguide;

import java.io.File;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		ParseXmlTask task = new ParseXmlTask(this);
		task.execute((File) null);
	}

}
