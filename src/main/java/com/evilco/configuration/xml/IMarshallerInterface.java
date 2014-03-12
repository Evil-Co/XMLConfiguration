package com.evilco.configuration.xml;

import com.evilco.configuration.xml.adapter.IAdapter;
import com.evilco.configuration.xml.annotation.Configuration;
import com.evilco.configuration.xml.exception.AdapterInitializationException;
import com.evilco.configuration.xml.exception.ConfigurationException;

/**
 * @auhtor Johannes Donath <johannesd@evil-co.com>
 * @copyright Copyright (C) 2014 Evil-Co <http://www.evil-co.org>
 */
public interface IMarshallerInterface {

	/**
	 * Returns an adapter instance based on the supplied adapter class.
	 * @param adapter
	 * @return
	 * @throws com.evilco.configuration.xml.exception.AdapterInitializationException
	 */
	public IAdapter getAdapterInstance (Class<? extends IAdapter> adapter) throws AdapterInitializationException;

	/**
	 * Returns an adapter instance based on the supplied object.
	 * @param object
	 * @return
	 * @throws AdapterInitializationException
	 */
	public IAdapter getAdapterInstance (Object object) throws AdapterInitializationException;

	/**
	 * Returns the type the marshaller is configured to use.
	 * @return
	 */
	public Class<?> getType ();

	/**
	 * Returns the type configuration for this marshaller.
	 * @return
	 */
	public Configuration getTypeConfiguration ();

	/**
	 * Registers a new adapter.
	 * @param adapter
	 * @throws ConfigurationException
	 */
	public void registerAdapter (Class<? extends IAdapter> adapter) throws ConfigurationException;
}
