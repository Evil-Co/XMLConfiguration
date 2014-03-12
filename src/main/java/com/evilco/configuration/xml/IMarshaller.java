package com.evilco.configuration.xml;

import com.evilco.configuration.xml.exception.ConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @auhtor Johannes Donath <johannesd@evil-co.com>
 * @copyright Copyright (C) 2014 Evil-Co <http://www.evil-co.org>
 */
public interface IMarshaller<T> extends IMarshallerInterface {

	/**
	 * Marshals an object.
	 * @param object
	 * @return
	 */
	public Document marshal (T object) throws ConfigurationException;

	/**
	 * Marshals an object into a specific element.
	 * @param object
	 * @param parent
	 * @param child
	 * @param document
	 */
	public void marshal (Object object, Element parent, Element child, Document document) throws ConfigurationException;
}
