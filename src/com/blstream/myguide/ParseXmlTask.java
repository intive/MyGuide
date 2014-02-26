
package com.blstream.myguide;

import java.io.File;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

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

	public ParseXmlTask(Context ctx) {
		mCtx = ctx;
		mData = null;
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
		try {
			mData = ph.parse(mCtx, file[0]);
			return true;
		} catch (Exception e) {
			Log.d(LOG_TAG, e.toString());
			return false;
		}
	}

	@Override
	protected void onPostExecute(Boolean success) {
		if (success) {
			Log.i(LOG_TAG, "animals: " + mData.getAnimals().size());
			Log.i(LOG_TAG, "ways: " + mData.getWays().size());
			Log.i(LOG_TAG, "junctions: " + mData.getJunctions().size());
		}
		mProgressDialog.dismiss();
	}

}
