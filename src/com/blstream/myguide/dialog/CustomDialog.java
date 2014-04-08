package com.blstream.myguide.dialog;

import com.blstream.myguide.R;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;


/**
 * Simple dialog which should be used instead of AlertDialog to provide proper
 * design. Can be used either for dialog with one ore two button.
 */
public class CustomDialog extends Dialog{
	
	public CustomDialog(Context context) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setBackgroundDrawableResource(R.drawable.rounded_dialog);
		setContentView(R.layout.fragment_dialog);
	}
	
	/**
	 * Set first button parameters
	 * 
	 * @param textId message on the button
	 * @param listener action to be invoke after click
	 */
	public CustomDialog setFirstButton(int textId, View.OnClickListener listener) {
		findViewById(R.id.btn_dialog_first).setOnClickListener(listener);
		((Button) findViewById(R.id.btn_dialog_first)).setText(textId);
		return this;
	}

	/**
	 * Set second button parameters. When you do not call this method,
	 * one-button dialog will be created.
	 * 
	 * @param textId message on the button
	 * @param listener action to be invoke after click
	 */
	public CustomDialog setSecondButton(int textId, View.OnClickListener listener) {
		Button button = (Button) findViewById(R.id.btn_dialog_second);
		button.setOnClickListener(listener);
		button.setText(textId);
		button.setVisibility(View.VISIBLE);
		return this;
	}
	
	public CustomDialog setMessage(int textId) {
		((TextView) findViewById(R.id.tv_dialog_msg)).setText(textId);
		return this;
	}
	
	public CustomDialog setMessage(String msg) {
		((TextView) findViewById(R.id.tv_dialog_msg)).setText(msg);
		return this;
	}

}
