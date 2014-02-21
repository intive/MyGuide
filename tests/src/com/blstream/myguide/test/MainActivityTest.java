package com.blstream.myguide.test;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.TextView;

import com.blstream.myguide.MainActivity;

public class MainActivityTest extends
        ActivityInstrumentationTestCase2<MainActivity> {

    private MainActivity mActivity;
    private TextView helloText;

    public MainActivityTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        setActivityInitialTouchMode(false);

        mActivity = getActivity();

        helloText = (TextView) mActivity
                .findViewById(com.blstream.myguide.R.id.hello_text);

    }

    public void testHelloText() {
        assertTrue(helloText.getText().equals("Hello World, MainActivity"));
    }

    public void testPreConditions() {
        assertTrue(helloText != null);
    }



}
