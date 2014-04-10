
package com.blstream.myguide.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.View.OnClickListener;

import com.blstream.myguide.R;

/**
 * DialogFragment that displays error dialog with and information about problem
 * with Google Play Services.
 */
public class PlayServicesErrorDialogFragment extends DialogFragment {
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final CustomDialog dialog = new CustomDialog(getActivity());
		return dialog.setMessage(R.string.play_services_unavailable)
								.setFirstButton(R.string.ok, new OnClickListener() {
									@Override
									public void onClick(View v) {
										getActivity().finish();
									}
								});
	}
}
