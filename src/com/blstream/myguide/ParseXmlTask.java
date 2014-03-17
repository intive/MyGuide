
package com.blstream.myguide;

import java.io.File;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.blstream.myguide.settings.Settings;
import com.blstream.myguide.settings.SettingsHelper;
import com.blstream.myguide.zoolocations.ParserHelper;
import com.blstream.myguide.zoolocations.ZooLocationsData;

/**
 * In this class xml with informations about zoo is parsing in background
 * thread. Until it is parsing, a ProgressDialog is visible.
 */
public class ParseXmlTask extends AsyncTask<File, Void, Boolean> {

	private static final String LOG_TAG = ParseXmlTask.class.getSimpleName();

	private ProgressDialog mProgressDialog;
	private Context mCtx;
	private ZooLocationsData mData;
	private Settings mSettings;

	public ParseXmlTask(Context ctx) {
		mCtx = ctx;
		mData = null;
		mSettings = null;
		mProgressDialog = new ProgressDialog(mCtx);
		mProgressDialog.setTitle(R.string.progress_dialog_title);
		mProgressDialog.setMessage(mCtx.getResources().getString(
				R.string.progress_dialog_message));
	}

	@Override
	protected void onPreExecute() {
		mProgressDialog.show();
	}

	@Override
	protected Boolean doInBackground(File... file) {
		ParserHelper ph = new ParserHelper();
		SettingsHelper sh = new SettingsHelper();
		try {
			mData = ph.parse(mCtx, file[0]);
			mSettings = sh.parse(mCtx, file[1]);
		} catch (Exception e1) {
			Log.d(LOG_TAG, e1.toString());
			try {
				mData = ph.parse(mCtx, null);
				mSettings = sh.parse(mCtx, null);
			} catch (Exception e2) {
				Log.d(LOG_TAG, e2.toString());
				return false;
			}
		}
		return true;
	}

	@Override
	protected void onPostExecute(Boolean success) {
		if (success) {
			Log.i(LOG_TAG, "animals: " + mData.getAnimals().size());
			Log.i(LOG_TAG, "ways: " + mData.getWays().size());
			Log.i(LOG_TAG, "junctions: " + mData.getJunctions().size());

			for (String key : mSettings.keySet()) {
				Log.i(LOG_TAG, "SETTINGS::  " + key + ": " + mSettings.get(key));
			}
			MyGuideApp mga = (MyGuideApp) (mCtx.getApplicationContext());
			mga.setData(mData);
			mga.setSettings(mSettings);
		}
		mProgressDialog.dismiss();
	}

}
