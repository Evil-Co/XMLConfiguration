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
@AdapterDefinition (input = Float.class, sharable = true)
public class FloatAdapter implements IAdapter<Float> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void marshal (Document document, Element parent, Element child, Float object) throws ConfigurationException {
		child.setTextContent (object.toString ());
		parent.appendChild (child);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Float unmarshal (Document document, Element element, Element child, Field field, Class<?> type) throws ConfigurationException {
		return Float.parseFloat (child.getTextContent ());
	}
}
