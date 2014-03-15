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
@AdapterDefinition (input = Integer.class, sharable = true)
public class IntegerAdapter implements IAdapter<Integer> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void marshal (Document document, Element parent, Element child, Integer object) throws ConfigurationException {
		child.setTextContent (object.toString ());
		parent.appendChild (child);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer unmarshal (Document document, Element element, Element child, Field field, Class<?> type) throws ConfigurationException {
		return Integer.parseInt (child.getTextContent ());
	}
}
