
package com.blstream.myguide.dialog;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

import com.blstream.myguide.R;
import com.blstream.myguide.gps.LocationUpdater;

public class EnableGpsDialogFragment extends DialogFragment {

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final Dialog dialog = new Dialog(getActivity());
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setBackgroundDrawableResource(R.drawable.rounded_dialog);
		dialog.setContentView(R.layout.fragment_enable_gps);

		((Button) dialog.findViewById(R.id.btnDontUseGps))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});
		((Button) dialog.findViewById(R.id.btnGoToSettings))
				.setOnClickListener(new OnClickListener() {
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
