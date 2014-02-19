package com.blstream.myguide.tests;

import android.test.InstrumentationTestCase;

import java.io.InputStream;
import java.io.IOException;

public class TestingTestCase extends InstrumentationTestCase {

    public void testShouldAlwaysPass() {
        assertTrue(true);
    }
    
    public void testShouldAccessTestsSpecificResources() {
        InputStream is = this.getInstrumentation().getContext().getResources().openRawResource(R.raw.testingtestcase_should_access_this_file);
        assertNotNull(is);
        
        try {
            is.close();
            is = null;
        } catch (IOException e) {
            fail(e.toString());
        } finally {
            assertNull(is);
        }
    }

}

