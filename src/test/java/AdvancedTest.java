import com.evilco.configuration.xml.ConfigurationProcessor;
import com.evilco.configuration.xml.IMarshaller;
import com.evilco.configuration.xml.IUnmarshaller;
import com.evilco.configuration.xml.annotation.Configuration;
import com.evilco.configuration.xml.annotation.Property;
import com.evilco.configuration.xml.exception.ConfigurationException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.w3c.dom.Document;

import java.util.ArrayList;

/**
 * @auhtor Johannes Donath <johannesd@evil-co.com>
 * @copyright Copyright (C) 2014 Evil-Co <http://www.evil-co.org>
 */
@RunWith (MockitoJUnitRunner.class)
public class AdvancedTest {

	/**
	 * Tests the marshalling and un-marshalling of lists.
	 * @throws ConfigurationException
	 */
	@Test
	public void extendedListTest () throws ConfigurationException {
		// create marshaller & un-marshaller
		IMarshaller<ConfigurationTest> marshaller = ConfigurationProcessor.getInstance ().createMarshaller (ConfigurationTest.class);
		IUnmarshaller<ConfigurationTest> unmarshaller = ConfigurationProcessor.getInstance ().createUnmarshaller (ConfigurationTest.class);

		// marshal into document
		Document document = marshaller.marshal (new ConfigurationTest ());

		// verify document
		Assert.assertNotNull ("The resulting document is invalid", document);

		// un-marshal
		ConfigurationTest testObject = unmarshaller.unmarshal (document);

		// verify object
		Assert.assertNotNull ("The resulting object is invalid", testObject);
		Assert.assertNotNull ("The resulting list is invalid", testObject.list);
	}

	/**
	 * A simple test configuration.
	 */
	@Configuration
	public static class ConfigurationTest {

		@Property ("testList")
		public TestList list = new TestList ();
	}

	/**
	 * A simple test list.
	 */
	public static class TestList extends ArrayList<Integer> { }
}
