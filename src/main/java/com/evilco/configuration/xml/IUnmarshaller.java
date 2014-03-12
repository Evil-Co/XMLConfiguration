package com.evilco.configuration.xml;

import com.evilco.configuration.xml.exception.ConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.lang.reflect.Field;

/**
 * @auhtor Johannes Donath <johannesd@evil-co.com>
 * @copyright Copyright (C) 2014 Evil-Co <http://www.evil-co.org>
 */
public interface IUnmarshaller<T> extends IMarshallerInterface {

	/**
	 * Un-Marshals an object from a specified document.
	 * @param document
	 * @return
	 */
	public T unmarshal (Document document) throws ConfigurationException;

	/**
	 * Un-Marshals an object from a specified element.
	 * @param element
	 * @return
	 * @throws ConfigurationException
	 */
	public Object unmarshal (Document document, Element element, Element child, Field field, Class type) throws ConfigurationException;
}