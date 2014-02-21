package com.blstream.myguide.test;

import android.test.InstrumentationTestCase;

import java.io.InputStream;
import java.io.IOException;

import org.mockito.Mockito;

/** Test used to determine whether project has been created and linked with libraries successfully.
 * Please note that this class uses a test-specific resource contained in <i>/tests/res/raw/testingtestcase_should_access_this_file</i>.
 *
 * @author  Rafal Bolanowski
 */
public class TestingTestCase extends InstrumentationTestCase {
    
    /** Used in testing {@link org.mockito.Mockito} library. */
    public static class Foo {
        
        public String say(String msg) {
            return msg;
        }
        
    }
    
    private static final String MESSAGE = "dummyString";
   
    /** Testing access to resources. */    
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
    
    /** Testing access to Mockito library. */
    public void testShouldAccessMockitoLibraryAndMockFooObject() {
        Foo foo = Mockito.mock(Foo.class);
        assertNotNull(foo);
        
        foo.say(MESSAGE);
        Mockito.verify(foo).say(MESSAGE);
    }

}

