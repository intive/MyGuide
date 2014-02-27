package com.blstream.myguide.dialog;

import com.blstream.myguide.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * Simple dialog fragment, which let user decide whether to proceed or abort an operation.
 * Dialog should be created using {@link #newInstance(String) newInstance} factory method.
 * Class which is about to use this fragment must implements {@link ConfirmationDialogHolder}.
 * 
 * @author Andrzej_Moskal
 */

public class ConfirmationDialogFragment extends DialogFragment {

	/**
	 * Callback interface containing a method to be invoke, when confirmation
	 * is made. Class which is about to use {@link ConfirmationDialogFragment} must
	 * implements this interface.
	 */
	public static interface ConfirmationDialogHolder {
		public void onDialogConfirm();
	}

	private ConfirmationDialogHolder mListener;

	/**
	 * Factory method which should be used instead of constructors.
	 * 
	 * @param dialogMessage a text to be show on a dialog
	 */
	public static final ConfirmationDialogFragment newInstance(String dialogMessage) {
		ConfirmationDialogFragment f = new ConfirmationDialogFragment();
		Bundle b = new Bundle(1);
		b.putString("msg", dialogMessage);
		f.setArguments(b);
		return f;
	}

	@Override
	public void onAttach(Activity activity) {
		try {
			mListener = (ConfirmationDialogHolder) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement ConfirmationDialogHolder!");
		}
		super.onAttach(activity);
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		Bundle b = getArguments();
		if (b != null && b.getString("msg") != null) {
			builder.setMessage(b.getString("msg"));
		} else {
			builder.setMessage(R.string.confirmation_default);
		}
		builder.setPositiveButton(android.R.string.yes,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int id) {
							mListener.onDialogConfirm();
						}
					})
				.setNegativeButton(android.R.string.cancel, null);
		return builder.create();
	}

	@Override
	public void onDetach() {
		mListener = null;
		super.onDetach();
	}

}

