
package com.blstream.myguide.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.blstream.myguide.R;
import com.google.android.gms.common.GooglePlayServicesUtil;

/**
 * DialogFragment that displays error dialog with and information about problem
 * with Google Play Services.
 */
public class PlayServicesErrorDialogFragment extends DialogFragment {
	public final static int CONNECTION_FAILURE_REQUEST = 9000;

	private Dialog mDialog = null;

	@Override
	public void onAttach(Activity activity) {
		int resultCode;
		resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);
		if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
			mDialog = GooglePlayServicesUtil
					.getErrorDialog(resultCode, activity, CONNECTION_FAILURE_REQUEST);
		} else {
			AlertDialog.Builder builder = new AlertDialog.Builder(activity);
			builder.setMessage(R.string.play_services_unavailable)
					.setPositiveButton(R.string.ok, null);
			mDialog = builder.create();
		}
		super.onAttach(activity);
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		return mDialog;
	}

	@Override
	public void onDetach() {
		mDialog = null;
		super.onDetach();
	}
}
