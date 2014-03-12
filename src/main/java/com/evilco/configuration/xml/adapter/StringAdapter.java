package com.evilco.configuration.xml.adapter;

import com.evilco.configuration.xml.annotation.AdapterDefinition;
import com.evilco.configuration.xml.exception.ConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.lang.reflect.Field;

/**
 * @auhtor Johannes Donath <johannesd@evil-co.com>
 * @copyright Copyright (C) 2014 Evil-Co <http://www.evil-co.org>
 */
@AdapterDefinition (input = String.class)
public class StringAdapter implements IAdapter<String> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void marshal (Document document, Element parent, Element child, String object) throws ConfigurationException {
		if (object != null) child.setTextContent (object);
		parent.appendChild (child);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String unmarshal (Document document, Element element, Element child, Field field, Class<?> type) throws ConfigurationException {
		if (child.getTextContent ().length () == 0) return null;
		return child.getTextContent ();
	}
}
