package com.evilco.configuration.xml.adapter;

import com.evilco.configuration.xml.ConfigurationProcessor;
import com.evilco.configuration.xml.IMarshaller;
import com.evilco.configuration.xml.IUnmarshaller;
import com.evilco.configuration.xml.annotation.AdapterDefinition;
import com.evilco.configuration.xml.annotation.Configuration;
import com.evilco.configuration.xml.exception.ConfigurationException;
import com.google.common.base.Preconditions;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @auhtor Johannes Donath <johannesd@evil-co.com>
 * @copyright Copyright (C) 2014 Evil-Co <http://www.evil-co.org>
 */
@AdapterDefinition (input = List.class, matchChildren = true)
public class ListAdapter implements IAdapter<List> {

	/**
	 * Stores the parent marshaller.
	 */
	protected final IMarshaller marshaller;

	/**
	 * Stores the parent processor.
	 */
	protected final ConfigurationProcessor processor;

	/**
	 * Stores the parent un-marshaller.
	 */
	protected final IUnmarshaller unmarshaller;

	/**
	 * Constructs a new ListAdapter.
	 * @param marshaller
	 */
	public ListAdapter (ConfigurationProcessor processor, IMarshaller marshaller) {
		this.marshaller = marshaller;
		this.processor = processor;
		this.unmarshaller = null;
	}

	/**
	 * Constructs a new ListAdapter.
	 * @param processor
	 * @param unmarshaller
	 */
	public ListAdapter (ConfigurationProcessor processor, IUnmarshaller unmarshaller) {
		this.unmarshaller = unmarshaller;
		this.processor = processor;
		this.marshaller = null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void marshal (Document document, Element parent, Element child, List object) throws ConfigurationException {
		for (Object element : object) {
			// clone element
			Element entryElement = ((Element) child.cloneNode (false));

			// marshal
			if (element.getClass ().isAnnotationPresent (Configuration.class)) {
				IMarshaller fieldMarshaller = this.processor.createMarshaller (element.getClass ());

				// marshal field
				fieldMarshaller.marshal (element, parent, entryElement, document);
			} else
				// forcefully marshal
				this.marshaller.marshal (element, parent, entryElement, document);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List unmarshal (Document document, Element element, Element child, Field field, Class<?> type) throws ConfigurationException {
		Preconditions.checkNotNull (field, "Lists are required to be inside of objects and cannot be nested."); // FIXME: We might be able to work around this

		// find type
		ParameterizedType parameterizedType = ((ParameterizedType) field.getGenericType ());
		Class<?> listType = ((Class<?>) parameterizedType.getActualTypeArguments ()[0]);

		// create new list
		List instance = new ArrayList ();

		// search elements
		NodeList elementList = element.getElementsByTagNameNS (this.unmarshaller.getTypeConfiguration ().namespace (), child.getTagName ());

		// iterate over all elements
		for (int i = 0; i < elementList.getLength (); i++) {
			Element current = ((Element) elementList.item (i));

			// un-marshal
			if (element.getClass ().isAnnotationPresent (Configuration.class)) {
				IUnmarshaller fieldMarshaller = this.processor.createUnmarshaller (listType);

				// marshal field
				instance.add (fieldMarshaller.unmarshal (document, element, current, null, listType));
			} else
				// forcefully un-marshal
				instance.add (this.unmarshaller.unmarshal (document, element, current, null, listType));
		}

		return instance;
	}
}
