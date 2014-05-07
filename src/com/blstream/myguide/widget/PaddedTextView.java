
package com.blstream.myguide.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.TextView;

import com.blstream.myguide.R;

/**
 * This {@link android.widget.TextView} pads included text with a selected character
 * (by default {@link com.blstream.myguide.widget.PaddedTextView#DEFAULT_PADDING_CHAR}).
 * Number of padded characters is computed dynamically such that a one more character
 * will increase current number of lines.
 *
 * Padding character can be specified in XML via {@link com.blstream.myguide.R.styleable#PaddedTextView_paddingChar} attribute.
 *
 * @author robin92
 */
public class PaddedTextView extends TextView {

	public static final char DEFAULT_PADDING_CHAR = '.';

	private char mPadChar = DEFAULT_PADDING_CHAR;

	public PaddedTextView(Context context) {
		super(context);
	}

	public PaddedTextView(Context context, AttributeSet attrs) {
		super(context, attrs);

		this.readStyleables(context, attrs, 0);
	}

	public PaddedTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		this.readStyleables(context, attrs, defStyle);
	}

	private void readCharAttribute(TypedArray typedArray) {
		String padStr = null;
		try {
			padStr = typedArray.getString(R.styleable.PaddedTextView_paddingChar);
		} catch (NullPointerException e) {}

		if (padStr == null || padStr.length() < 1) return;
		mPadChar = padStr.charAt(0);
	}

	private void readStyleables(Context context, AttributeSet attrs, int defStyle) {
		TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.PaddedTextView, 0, 0);

		try {
			readCharAttribute(ta);
		} finally {
			ta.recycle();
		}
	}

	protected void padWithChar(char c) {
		CharSequence cs = Character.toString(c);

		// add character till line is full
		int lines = getLineCount();
		while (getLineCount() == lines) append(cs);

		// remove last character since this caused line wrap
		String text = getText().toString();
		setText(text.substring(0, text.length() - 1));
	}

	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);

		padWithChar(mPadChar);
	}

}
