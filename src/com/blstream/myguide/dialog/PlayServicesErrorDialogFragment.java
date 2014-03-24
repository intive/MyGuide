
package com.blstream.myguide.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.blstream.myguide.R;

/**
 * DialogFragment that displays error dialog with and information about problem
 * with Google Play Services.
 */
public class PlayServicesErrorDialogFragment extends DialogFragment {

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage(R.string.play_services_unavailable)
				.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						getActivity().finish();
					}
				});
		return builder.create();
	}
}
