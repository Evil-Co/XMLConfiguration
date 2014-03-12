package com.evilco.configuration.xml.adapter;

import com.evilco.configuration.xml.annotation.AdapterDefinition;
import com.evilco.configuration.xml.exception.ConfigurationException;
import com.evilco.configuration.xml.exception.ConfigurationProcessorException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.lang.reflect.Field;

/**
 * @auhtor Johannes Donath <johannesd@evil-co.com>
 * @copyright Copyright (C) 2014 Evil-Co <http://www.evil-co.org>
 */
@AdapterDefinition (input = Enum.class, matchChildren = true)
public class EnumAdapter implements IAdapter<Enum> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void marshal (Document document, Element parent, Element child, Enum object) throws ConfigurationException {
		if (object != null) child.setTextContent (object.toString ());
		parent.appendChild (child);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Enum unmarshal (Document document, Element element, Element child, Field field, Class<?> type) throws ConfigurationException {
		if (child.getTextContent ().length () == 0) return null;

		try {
			return Enum.valueOf (type.asSubclass (Enum.class), child.getTextContent ());
		} catch (Exception ex) {
			throw new ConfigurationProcessorException (ex);
		}
	}
}
