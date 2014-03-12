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
import java.util.HashMap;
import java.util.Map;

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
		IMarshaller<ConfigurationTestList> marshaller = ConfigurationProcessor.getInstance ().createMarshaller (ConfigurationTestList.class);
		IUnmarshaller<ConfigurationTestList> unmarshaller = ConfigurationProcessor.getInstance ().createUnmarshaller (ConfigurationTestList.class);

		// marshal into document
		Document document = marshaller.marshal (new ConfigurationTestList ());

		// verify document
		Assert.assertNotNull ("The resulting document is invalid", document);

		// un-marshal
		ConfigurationTestList testObject = unmarshaller.unmarshal (document);

		// verify object
		Assert.assertNotNull ("The resulting object is invalid", testObject);
		Assert.assertNotNull ("The resulting list is invalid", testObject.list);
	}

	/**
	 * Tests the marshalling and un-marshalling of maps.
	 * @throws ConfigurationException
	 */
	@Test
	public void extendedMapTest () throws ConfigurationException {
		// create marshaller & un-marshaller
		IMarshaller<ConfigurationTestMap> marshaller = ConfigurationProcessor.getInstance ().createMarshaller (ConfigurationTestMap.class);
		IUnmarshaller<ConfigurationTestMap> unmarshaller = ConfigurationProcessor.getInstance ().createUnmarshaller (ConfigurationTestMap.class);

		// marshal into document
		Document document = marshaller.marshal (new ConfigurationTestMap ());

		// verify document
		Assert.assertNotNull ("The resulting document is invalid", document);

		// un-marshal
		ConfigurationTestMap testObject = unmarshaller.unmarshal (document);

		// verify object
		Assert.assertNotNull ("The resulting object is invalid", testObject);
		Assert.assertNotNull ("The resulting map is invalid", testObject.map);
	}

	/**
	 * A simple test configuration.
	 */
	@Configuration
	public static class ConfigurationTestList {

		@Property ("testList")
		public TestList list = new TestList ();

		/**
		 * Constructs a new instance of ConfigurationTestList.
		 */
		public ConfigurationTestList () {
			this.list.add (42);
		}
	}

	/**
	 * A simple test configuration.
	 */
	@Configuration
	public static class ConfigurationTestMap {

		@Property ("testMap")
		public TestMap map = new TestMap ();

		/**
		 * Constructs a new instance of ConfigurationTestMap.
		 */
		public ConfigurationTestMap () {
			this.map.put ("theAnswer", 42);
		}
	}

	/**
	 * A simple test list.
	 */
	public static class TestList extends ArrayList<Integer> { }

	/**
	 * A simple test map.
	 */
	public static class TestMap extends HashMap<String, Integer> { }
}
