package com.blstream.myguide.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;

import com.blstream.myguide.R;

/**
 * Simple dialog fragment, which let user decide whether to exit application or
 * not.
 */
public class ConfirmExitDialogFragment extends DialogFragment {
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final CustomDialog dialog = new CustomDialog(getActivity());
		return dialog.setMessage(R.string.confirmation_exit)
				.setFirstButton(R.string.ok,
						new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								getActivity().finish();
							}
						})
				.setSecondButton(R.string.cancel, new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						dismiss();
					}
				});
	}
}