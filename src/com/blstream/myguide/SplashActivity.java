/**
 * 
 */

package com.blstream.myguide;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/** Display splash screen while loading assets in the backgroud.
 * Splash will be display no shorter than {@link SplashActivity#mMinDisplayMillis} milliseconds.
 * 
 * @author Rafal Bolanowski
 */
public class SplashActivity extends Activity {

	private static final String LOG_TAG = SplashActivity.class.toString();
	
	private static final int DEFAULT_MIN_DISPLAY_MILLIS = 2500;	// 2500 ms
	
	protected int mMinDisplayMillis = DEFAULT_MIN_DISPLAY_MILLIS;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_splash);
		
		final long startTime = System.currentTimeMillis();
		new Thread(new Runnable() {
			@Override
			public void run() {
				
				doInBackground();
				
				// make sure min display time has elapsed
				final long duration = System.currentTimeMillis() - startTime;
				if (duration < mMinDisplayMillis) {
					try {
						Thread.sleep(mMinDisplayMillis - duration);
					} catch (InterruptedException e) {
						Thread.interrupted();	// clear interruption flag
					}
				}
				
				startNextActivity();
				finishThisActivity();
			}
		}).start();
	}
	
	protected void doInBackground() {
		// TODO: parse data & options XML
	}
	
	protected void startNextActivity() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}
	
	protected void finishThisActivity() {
		finish();
	}
	
}
