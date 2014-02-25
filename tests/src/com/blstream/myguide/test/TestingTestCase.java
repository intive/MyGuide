
package com.blstream.myguide.test;

import android.test.InstrumentationTestCase;

import java.io.InputStream;
import java.io.IOException;

import org.mockito.Mockito;

/**
 * Test used to determine whether project has been created and linked with
 * libraries successfully. Please note that this class uses a test-specific
 * resource contained in
 * <i>/tests/res/raw/testingtestcase_should_access_this_file</i>.
 * 
 * @author Rafal Bolanowski
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
		// given
		InputStream is = null;

		// when
		is = this.getInstrumentation().getContext().getResources()
				.openRawResource(R.raw.testingtestcase_should_access_this_file);
		try {
			if (is != null) is.close();
		} catch (IOException e) {
			// error closing resource
		}

		// then
		assertNotNull(is);
	}

	/** Testing access to Mockito library. */
	public void testShouldAccessMockitoLibraryAndMockFooObject() {
		// given
		Foo foo = Mockito.mock(Foo.class);

		// when
		foo.say(MESSAGE);

		// then
		Mockito.verify(foo).say(MESSAGE);
	}

}
