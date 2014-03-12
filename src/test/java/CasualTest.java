import com.evilco.configuration.xml.ConfigurationProcessor;
import com.evilco.configuration.xml.IMarshaller;
import com.evilco.configuration.xml.IUnmarshaller;
import com.evilco.configuration.xml.annotation.Comment;
import com.evilco.configuration.xml.annotation.Configuration;
import com.evilco.configuration.xml.annotation.Property;
import com.evilco.configuration.xml.annotation.PropertyWrapper;
import com.evilco.configuration.xml.exception.ConfigurationException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.w3c.dom.Document;

import java.util.*;

/**
 * @auhtor Johannes Donath <johannesd@evil-co.com>
 * @copyright Copyright (C) 2014 Evil-Co <http://www.evil-co.org>
 */
@RunWith (MockitoJUnitRunner.class)
public class CasualTest {

	/**
	 * Defines a test root element name.
	 */
	public static final String ROOT_ELEMENT_NAME = "test";

	/**
	 * Defines a test namespace.
	 */
	public static final String ROOT_ELEMENT_NAMESPACE = "http://test.evil-co.org/2014/configuration";

	/**
	 * Tests whether an object can be marshalled successfully and decoded again afterwards.
	 * @throws ConfigurationException
	 */
	@Test
	public void processingTest () throws ConfigurationException {
		// get processor instance
		ConfigurationProcessor processor = ConfigurationProcessor.getInstance ();

		// create marshaller
		IMarshaller<ConfigurationTest> testMarshaller = processor.createMarshaller (ConfigurationTest.class);
		IUnmarshaller<ConfigurationTest> testUnmarshaller = processor.createUnmarshaller (ConfigurationTest.class);

		// create test object
		ConfigurationTest testObject = new ConfigurationTest ();

		// marshal object
		Document document = testMarshaller.marshal (testObject);

		// un-marshal object
		ConfigurationTest testObject1 = testUnmarshaller.unmarshal (document);

		// verify
		Assert.assertNotNull ("Un-Marshalling the document did not return a valid object", testObject1);

		// verify object
		Assert.assertEquals ("One of the variables does not match (boolean)", testObject.booleanVariable, testObject1.booleanVariable);
		Assert.assertEquals ("One of the variables does not match (double)", testObject.doubleVariable, testObject1.doubleVariable);
		Assert.assertEquals ("One of the variables does not match (enum)", testObject.enumVariable, testObject1.enumVariable);
		Assert.assertEquals ("One of the variables does not match (float)", testObject.floatVariable, testObject1.floatVariable);
		Assert.assertEquals ("One of the variables does not match (integer)", testObject.integerVariable, testObject1.integerVariable);
		Assert.assertEquals ("One of the variables does not match (string)", testObject.stringVariable, testObject1.stringVariable);
	}

	/**
	 * Tests the Marshaller.
	 * @throws ConfigurationException
	 */
	@Test
	public void marshallerInstanceTest () throws ConfigurationException {
		// get processor instance
		ConfigurationProcessor processor = ConfigurationProcessor.getInstance ();

		// verify marshaller
		Assert.assertNotNull ("The ConfigurationProcessor did not create a valid IMarshaller instance", processor.createMarshaller (ConfigurationTest.class));
		Assert.assertEquals ("The ConfigurationProcessor did not return an IMarshaller singleton", processor.createMarshaller (ConfigurationTest.class), processor.createMarshaller (ConfigurationTest.class));
	}

	/**
	 * Tests the Marshaller.
	 * @throws ConfigurationException
	 */
	@Test
	public void marshallerTest () throws ConfigurationException {
		// get processor instance
		ConfigurationProcessor processor = ConfigurationProcessor.getInstance ();

		// create marshaller
		IMarshaller<ConfigurationTest> testMarshaller = processor.createMarshaller (ConfigurationTest.class);

		// create test object
		ConfigurationTest testObject = new ConfigurationTest ();

		// marshal object
		Document document = testMarshaller.marshal (testObject);

		// verify result
		Assert.assertNotNull ("The marshaller did not return a correctly marshalled document", document);
	}

	/**
	 * Tests the marshaller validation.
	 * @throws ConfigurationException
	 */
	@Test (expected = ConfigurationException.class)
	public void marshallerValidationTest () throws ConfigurationException {
		ConfigurationProcessor.getInstance ().createMarshaller (Object.class);
	}

	/**
	 * Tests whether the document namespace is added.
	 */
	@Test
	public void namespaceTest () throws ConfigurationException {
		// create marshaller
		IMarshaller<ConfigurationTest> marshaller = ConfigurationProcessor.getInstance ().createMarshaller (ConfigurationTest.class);

		// marshal object
		Document document = marshaller.marshal (new ConfigurationTest ());

		// check namespace
		Assert.assertEquals (ROOT_ELEMENT_NAMESPACE, document.getDocumentElement ().getNamespaceURI ());
	}

	/**
	 * Tests the processor instantiation.
	 */
	@Test
	public void processorInstanceTest () {
		Assert.assertNotNull ("The ConfigurationProcessor does not return a valid instance", ConfigurationProcessor.getInstance ());
		Assert.assertEquals ("The ConfigurationProcessor does not return a singleton", ConfigurationProcessor.getInstance (), ConfigurationProcessor.getInstance ());
	}

	/**
	 * Tests the Un-Marshaller instantiation.
	 */
	@Test
	public void unmarshallerInstanceTest () throws ConfigurationException {
		// get processor instance
		ConfigurationProcessor processor = ConfigurationProcessor.getInstance ();

		// verify unmarshaller
		Assert.assertNotNull ("The ConfigurationProcessor does not return a valid IUnmarshaller instance", processor.createUnmarshaller (ConfigurationTest.class));
		Assert.assertEquals ("The ConfigurationProcessor does not return an IUnmarshaller singleton", processor.createUnmarshaller (ConfigurationTest.class), processor.createUnmarshaller (ConfigurationTest.class));
	}

	/**
	 * A simple configuration test class.
	 */
	@Configuration (name = ROOT_ELEMENT_NAME, namespace = ROOT_ELEMENT_NAMESPACE)
	public static class ConfigurationTest {

		/**
		 * Stores a test value of type Boolean.
		 */
		@Property ("booleanVariable")
		public Boolean booleanVariable = false;

		/**
		 * Stores a test value of type Double.
		 */
		@Property ("doubleVariable")
		public Double doubleVariable = 4.2d;

		/**
		 * Stores a test value of type Enum.
		 */
		@Property ("enumVariable")
		public ConfigurationEnum enumVariable = ConfigurationEnum.A;

		/**
		 * Stores a test value of type Float.
		 */
		@Property ("floatVariable")
		public Float floatVariable = 4.2f;

		/**
		 * Stores a test value of type Integer.
		 */
		@Property ("integerVariable")
		public Integer integerVariable = 42;

		/**
		 * Stores a test value of type List.
		 */
		@PropertyWrapper ("listVariable")
		@Property ("item")
		public List<Integer> listVariable = new ArrayList<> (Arrays.asList (new Integer[] {
			42,
			21
		}));

		/**
		 * Stores a test value of type Map.
		 */
		@PropertyWrapper ("mapVariable")
		@Property ("item")
		public Map<String, Integer> mapVariable = new HashMap<> ();

		/**
		 * Stores a test value of type Object.
		 */
		@Property ("objectVariable")
		public ConfigurationTestInner objectVariable = new ConfigurationTestInner ();

		/**
		 * Stores a test value of type String.
		 */
		@Property ("stringVariable")
		public String stringVariable = "Test1";

		/**
		 * Stores a test value.
		 */
		public Boolean ignoredVariable = true;

		/**
		 * Constructs a new ConfigurationTest instance.
		 */
		public ConfigurationTest () {
			this.mapVariable.put ("TheAnswer", 42);
			this.mapVariable.put ("NotQuiteTheRightAnswer", 21);
		}
	}

	/**
	 * A simple inner configuration test class.
	 */
	public static class ConfigurationTestInner {

		/**
		 * Stores another test value.
		 */
		@Property ("innerVariable")
		@Comment ("This is a test comment.")
		public String innerVariable1 = "InnerTest1";
	}

	/**
	 * A simple test Enum.
	 */
	public static enum ConfigurationEnum {
		A,
		B,
		C
	}
}
