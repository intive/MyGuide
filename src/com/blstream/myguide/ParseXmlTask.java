
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

	private final String LOG_TAG = "zoolocations";
	private ProgressDialog progressDialog;
	private Context ctx;
	private ZooLocationsData data;

	public ParseXmlTask(Context ctx) {
		this.ctx = ctx;
		this.data = null;
		this.progressDialog = new ProgressDialog(ctx);
		this.progressDialog.setTitle(R.string.progress_dialog_title);
		this.progressDialog.setMessage(ctx.getResources().getString(
				R.string.progress_dialog_message));
	}

	@Override
	protected void onPreExecute() {
		this.progressDialog.show();
	}

	@Override
	protected Boolean doInBackground(File... file) {
		ParserHelper ph = new ParserHelper();
		try {
			this.data = ph.parse(ctx, file[0]);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	protected void onPostExecute(Boolean success) {
		if (success) {
			Log.i(LOG_TAG, "animals: " + this.data.getAnimals().size());
			Log.i(LOG_TAG, "ways: " + this.data.getWays().size());
			Log.i(LOG_TAG, "junctions: " + this.data.getJunctions().size());
		} else {
			Log.i(LOG_TAG, "Bad xml with data.");
		}
		progressDialog.dismiss();
	}

}
