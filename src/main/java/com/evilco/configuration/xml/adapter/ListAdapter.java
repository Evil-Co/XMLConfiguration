package com.evilco.configuration.xml.adapter;

import com.evilco.configuration.xml.ConfigurationProcessor;
import com.evilco.configuration.xml.IMarshaller;
import com.evilco.configuration.xml.IUnmarshaller;
import com.evilco.configuration.xml.annotation.AdapterDefinition;
import com.evilco.configuration.xml.annotation.Configuration;
import com.evilco.configuration.xml.annotation.InnerType;
import com.evilco.configuration.xml.exception.ConfigurationException;
import com.evilco.configuration.xml.exception.ConfigurationProcessorException;
import com.evilco.configuration.xml.exception.ConfigurationSetupException;
import com.google.common.base.Preconditions;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

/**
 * @auhtor Johannes Donath <johannesd@evil-co.com>
 * @copyright Copyright (C) 2014 Evil-Co <http://www.evil-co.org>
 */
@AdapterDefinition (input = List.class, matchChildren = true, sharable = true)
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
		Class<?> listType = null;

		if (!field.getType ().isAnnotationPresent (InnerType.class) && !field.isAnnotationPresent (InnerType.class))
			listType = ((Class<?>) (field.getGenericType () instanceof ParameterizedType ? ((ParameterizedType) field.getGenericType ()) : ((ParameterizedType) field.getType ().getGenericSuperclass ())).getActualTypeArguments ()[0]);
		else if (field.isAnnotationPresent (InnerType.class))
			listType = field.getAnnotation (InnerType.class).value ();
		else
			listType = field.getType ().getAnnotation (InnerType.class).value ();

		// create new list
		List instance = null;

		if (field.getGenericType () instanceof ParameterizedType)
			instance = new ArrayList ();
		else {
			try {
				// find correct type
				Class<? extends List> advancedListType = field.getType ().asSubclass (List.class);

				// get constructor
				Constructor<? extends List> listConstructor = advancedListType.getConstructor ();

				// make accessible
				listConstructor.setAccessible (true);

				// construct a new list
				instance = listConstructor.newInstance ();
			} catch (NoSuchMethodException ex) {
				throw new ConfigurationSetupException ("List of type " + field.getType ().getName () + " does not have a default no-argument constructor");
			} catch (Exception ex) {
				throw new ConfigurationProcessorException ("Could not create an instance of list type " + field.getType ().getName (), ex);
			}
		}

		// search elements
		NodeList elementList = element.getChildNodes ();

		// iterate over all elements
		for (int i = 0; i < elementList.getLength (); i++) {
			if (!(elementList.item (i) instanceof Element)) continue;
			if (!((Element) elementList.item (i)).getTagName ().equalsIgnoreCase (child.getTagName ())) continue;

			// convert element
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
