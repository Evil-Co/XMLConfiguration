package com.evilco.configuration.xml;

import com.evilco.configuration.xml.exception.ConfigurationException;
import com.evilco.configuration.xml.exception.ConfigurationProcessorException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @auhtor Johannes Donath <johannesd@evil-co.com>
 * @copyright Copyright (C) 2014 Evil-Co <http://www.evil-co.org>
 */
public class ConfigurationProcessor {

	/**
	 * Stores all Marshallers.
	 */
	protected Map<Class<?>, IMarshaller> marshallerMap;

	/**
	 * Stores all Un-Marshallers.
	 */
	protected Map<Class<?>, IUnmarshaller> unmarshallerMap;

	/**
	 * Constructs a new processor.
	 */
	protected ConfigurationProcessor () {
		this.marshallerMap = new HashMap<> ();
		this.unmarshallerMap = new HashMap<> ();
	}

	/**
	 * Creates a marshaller instance.
	 * @return
	 */
	public IMarshaller createMarshaller (Class<?> type) throws ConfigurationException {
		if (!this.marshallerMap.containsKey (type)) this.marshallerMap.put (type, new Marshaller (this, type));
		return this.marshallerMap.get (type);
	}

	/**
	 * Creates an un-marshaller instance.
	 * @param type
	 * @return
	 */
	public IUnmarshaller createUnmarshaller (Class<?> type) throws ConfigurationException {
		if (!this.unmarshallerMap.containsKey (type)) this.unmarshallerMap.put (type, new Unmarshaller (this, type));
		return this.unmarshallerMap.get (type);
	}

	/**
	 * Returns the processor instance.
	 * @return
	 */
	public static ConfigurationProcessor getInstance () {
		return Singleton.instance;
	}

	/**
	 * Loads an object from the specified stream.
	 * @param inputStream
	 * @param type
	 * @param <T>
	 * @return
	 * @throws ConfigurationException
	 */
	public <T> T load (InputStream inputStream, Class<T> type) throws ConfigurationException {
		try {
			// create xml document
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance ();

			// set properties
			documentBuilderFactory.setNamespaceAware (true);

			// get document
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder ();

			// construct from file
			Document document = documentBuilder.parse (inputStream);

			// un-marshal
			IUnmarshaller unmarshaller = this.createUnmarshaller (type);
			return type.cast (unmarshaller.unmarshal (document));
		} catch (ParserConfigurationException ex) {
			throw new ConfigurationProcessorException (ex);
		} catch (SAXException ex) {
			throw new ConfigurationProcessorException (ex);
		} catch (IOException ex) {
			throw new ConfigurationProcessorException (ex);
		}
	}

	/**
	 * Loads an object from the specified stream.
	 * @param file
	 * @param type
	 * @param <T>
	 * @return
	 * @throws ConfigurationException
	 */
	public <T> T load (File file, Class<T> type) throws ConfigurationException {
		try {
			return this.load (new FileInputStream (file), type);
		} catch (FileNotFoundException ex) {
			throw new ConfigurationProcessorException (ex);
		}
	}

	/**
	 * Saves an object into the specified stream.
	 * @param object
	 * @param outputStream
	 * @return
	 * @throws ConfigurationException
	 */
	public void save (Object object, OutputStream outputStream) throws ConfigurationException {
		// create marshaller
		IMarshaller marshaller = this.createMarshaller (object.getClass ());

		// marshal object
		Document document = marshaller.marshal (object);

		// transform object
		try {
			Transformer transformer = TransformerFactory.newInstance ().newTransformer ();

			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			transformer.setOutputProperty ("{http://xml.apache.org/xslt}indent-amount", "8");

			Result output = new StreamResult (outputStream);
			Source source = new DOMSource (document);

			transformer.transform (source, output);
		} catch (TransformerConfigurationException ex) {
			throw new ConfigurationProcessorException (ex);
		} catch (TransformerException ex) {
			throw new ConfigurationProcessorException (ex);
		}
	}

	/**
	 * Saves an object into the specified file.
	 * @param object
	 * @param file
	 * @throws ConfigurationException
	 */
	public void save (Object object, File file) throws ConfigurationException {
		try {
			this.save (object, new FileOutputStream (file));
		} catch (IOException ex) {
			throw new ConfigurationProcessorException (ex);
		}
	}

	/**
	 * Allows On-Demand initializations.
	 */
	private static class Singleton {

		/**
		 * Stores the processor instance.
		 */
		private static final ConfigurationProcessor instance = new ConfigurationProcessor ();
	}
}
