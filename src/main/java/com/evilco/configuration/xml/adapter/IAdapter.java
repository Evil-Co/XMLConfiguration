package com.evilco.configuration.xml.adapter;

import com.evilco.configuration.xml.exception.ConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @auhtor Johannes Donath <johannesd@evil-co.com>
 * @copyright Copyright (C) 2014 Evil-Co <http://www.evil-co.org>
 */
public interface IAdapter<K> {

	/**
	 * Marshals an object into XML.
	 * @param document
	 * @param parent
	 * @param child
	 */
	public void marshal (Document document, Element parent, Element child, K object) throws ConfigurationException;

	/**
	 * Un-Marshals an object from XML.
	 * @param document
	 * @param element
	 * @param type
	 * @return
	 */
	public K unmarshal (Document document, Element element, Element child, Field field, Class<?> type) throws ConfigurationException;
}
