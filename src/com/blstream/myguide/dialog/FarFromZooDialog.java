package com.blstream.myguide.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.View.OnClickListener;

import com.blstream.myguide.R;

/**
 * Simple dialog fragment, which let user decide whether to navigate to
 * "Sightseeing" or to "How to get" screen. Class which is about to use this
 * fragment must implements {@link NavigationConfirmation}.
 */

public class FarFromZooDialog extends DialogFragment {

	/**
	 * Callback interface to communicate with attached activity. Class which is
	 * about to use {@link FarFromZooDialog} must implements this interface.
	 */
	public static interface NavigationConfirmation {
		public void onNavigationConfirm(boolean confirm);

		public void markDialogAsShown();
	}

	private NavigationConfirmation mListener;

	@Override
	public void onAttach(Activity activity) {
		try {
			mListener = (NavigationConfirmation) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement NavigationConfirmation!");
		}
		super.onAttach(activity);
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		mListener.markDialogAsShown();
		final CustomDialog dialog = new CustomDialog(getActivity());
		dialog.setMessage(R.string.to_far_from_zoo)
				.setFirstButton(R.string.view_map, new OnClickListener() {
					@Override
					public void onClick(View v) {
						mListener.onNavigationConfirm(true);
						dialog.dismiss();
					}
				})
				.setSecondButton(R.string.how_to_get, new OnClickListener() {
					@Override
					public void onClick(View v) {
						mListener.onNavigationConfirm(false);
						dialog.dismiss();
					}
				});
		return dialog;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

}