package com.evilco.configuration.xml;

import com.evilco.configuration.xml.exception.ConfigurationException;

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
	 * Allows On-Demand initializations.
	 */
	private static class Singleton {

		/**
		 * Stores the processor instance.
		 */
		private static final ConfigurationProcessor instance = new ConfigurationProcessor ();
	}
}
