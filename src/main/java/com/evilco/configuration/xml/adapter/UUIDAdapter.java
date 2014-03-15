package com.evilco.configuration.xml.adapter;

import com.evilco.configuration.xml.annotation.AdapterDefinition;
import com.evilco.configuration.xml.exception.ConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.lang.reflect.Field;
import java.util.UUID;

/**
 * @auhtor Johannes Donath <johannesd@evil-co.com>
 * @copyright Copyright (C) 2014 Evil-Co <http://www.evil-co.org>
 */
@AdapterDefinition (input = UUID.class, sharable = true)
public class UUIDAdapter implements IAdapter<UUID> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void marshal (Document document, Element parent, Element child, UUID object) throws ConfigurationException {
		if (object != null) child.setTextContent (object.toString ());
		parent.appendChild (child);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UUID unmarshal (Document document, Element element, Element child, Field field, Class<?> type) throws ConfigurationException {
		if (child.getTextContent ().isEmpty ()) return null;
		return UUID.fromString (child.getTextContent ());
	}
}
