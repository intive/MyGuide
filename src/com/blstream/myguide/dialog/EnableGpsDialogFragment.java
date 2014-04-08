
package com.blstream.myguide.dialog;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.View.OnClickListener;

import com.blstream.myguide.R;
import com.blstream.myguide.gps.LocationUpdater;

public class EnableGpsDialogFragment extends DialogFragment {

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final CustomDialog dialog = new CustomDialog(getActivity());
		dialog.setMessage(R.string.gps_enable_dialog_msg)
				.setFirstButton(R.string.start_without_gps, new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				})
				.setSecondButton(R.string.go_to_option, new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
						startActivity(intent);
						dialog.dismiss();
					}
				});
		LocationUpdater.getInstance().markGpsEnableDialogAsShown();
		return dialog;
	}
}
