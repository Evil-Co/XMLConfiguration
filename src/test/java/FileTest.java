import com.evilco.configuration.xml.ConfigurationProcessor;
import com.evilco.configuration.xml.exception.ConfigurationException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;

/**
 * @auhtor Johannes Donath <johannesd@evil-co.com>
 * @copyright Copyright (C) 2014 Evil-Co <http://www.evil-co.org>
 */
@RunWith (MockitoJUnitRunner.class)
public class FileTest {

	/**
	 * Tests the error detection of load (File).
	 * @throws ConfigurationException
	 */
	@Test (expected = ConfigurationException.class)
	public void loadFailureTest () throws ConfigurationException {
		// try to load a non-existing file
		ConfigurationProcessor.getInstance ().load (new File ("loadFailure.xml"), CasualTest.ConfigurationTest.class);
	}

	/**
	 * Tests the load & save mechanic.
	 * @throws ConfigurationException
	 */
	@Test
	public void saveLoadTest () throws ConfigurationException {
		// create file reference
		File file = new File ("test.xml");

		// delete file
		if (file.exists ()) file.delete ();

		// create test object
		CasualTest.ConfigurationTest testObject = new CasualTest.ConfigurationTest ();

		// marshal & save
		ConfigurationProcessor.getInstance ().save (testObject, file);

		// verify file existance
		Assert.assertTrue ("The test file was not created", file.exists ());
		Assert.assertNotEquals ("The file seems to be empty (file size of 0)", 0, file.length ());

		// load & unmarshal
		ConfigurationProcessor.getInstance ().load (file, CasualTest.ConfigurationTest.class);
	}
}
