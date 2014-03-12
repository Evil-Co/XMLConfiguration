package com.evilco.configuration.xml.adapter;

import com.evilco.configuration.xml.ConfigurationProcessor;
import com.evilco.configuration.xml.IMarshaller;
import com.evilco.configuration.xml.IUnmarshaller;
import com.evilco.configuration.xml.annotation.*;
import com.evilco.configuration.xml.exception.ConfigurationException;
import com.evilco.configuration.xml.exception.ConfigurationProcessorException;
import com.evilco.configuration.xml.exception.ConfigurationSetupException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

/**
 * @auhtor Johannes Donath <johannesd@evil-co.com>
 * @copyright Copyright (C) 2014 Evil-Co <http://www.evil-co.org>
 */
@AdapterDefinition (input = Object.class, sharable = true)
public class ObjectAdapter implements IAdapter<Object> {

	/**
	 * Stores the parent marshaller instance.
	 */
	protected final IMarshaller marshaller;

	/**
	 * Stores the parent processor instance.
	 */
	protected final ConfigurationProcessor processor;

	/**
	 * Stores the parent unmarshaller instance.
	 */
	protected final IUnmarshaller unmarshaller;

	/**
	 * Constructs a new ObjectAdapter.
	 * @param marshaller
	 */
	public ObjectAdapter (ConfigurationProcessor processor, IMarshaller marshaller) {
		this.marshaller = marshaller;
		this.unmarshaller = null;
		this.processor = processor;
	}

	/**
	 * Constructs a new ObjectAdapter.
	 * @param processor
	 * @param unmarshaller
	 */
	public ObjectAdapter (ConfigurationProcessor processor, IUnmarshaller unmarshaller) {
		this.unmarshaller = unmarshaller;
		this.marshaller = null;
		this.processor = processor;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void marshal (Document document, Element parent, Element child, Object object) throws ConfigurationException {
		// fix element
		if (child == null)
			child = parent;
		else
			parent.appendChild (child);

		// serialize data (if any)
		if (object != null) {
			// iterate over fields
			for (Field field : object.getClass ().getFields ()) {
				// skip normal fields
				if (!field.isAnnotationPresent (Property.class)) continue;

				// define parent
				Element fieldParent = child;

				// correct parent instance
				if (field.isAnnotationPresent (PropertyWrapper.class)) {
					// search for existing parent
					NodeList parentList = child.getElementsByTagNameNS (this.marshaller.getTypeConfiguration ().namespace (), field.getAnnotation (PropertyWrapper.class).value ());

					if (parentList.getLength () > 0)
						fieldParent = ((Element) parentList.item (0));
					else
						fieldParent = document.createElementNS (this.marshaller.getTypeConfiguration ().namespace (), field.getAnnotation (PropertyWrapper.class).value ());

					// append
					child.appendChild (fieldParent);
				}

				// create element for current field
				Element fieldElement = document.createElementNS (this.marshaller.getTypeConfiguration ().namespace (), field.getAnnotation (Property.class).value ());

				// make field accessible
				field.setAccessible (true);

				// add comments
				if (field.isAnnotationPresent (Comment.class)) {
					// create comment object
					org.w3c.dom.Comment comment = document.createComment (field.getAnnotation (Comment.class).value ());

					// append to current parent
					fieldParent.appendChild (comment);
				}

				// marshal
				try {
					if (field.getType ().isAnnotationPresent (Configuration.class)) {
						IMarshaller fieldMarshaller = this.processor.createMarshaller (field.getType ());

						// marshal field
						fieldMarshaller.marshal (field.get (object), fieldParent, fieldElement, document);
					} else
						// forcefully marshal
						this.marshaller.marshal (field.get (object), fieldParent, fieldElement, document);
				} catch (IllegalAccessException ex) {
					throw new ConfigurationProcessorException (ex);
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object unmarshal (Document document, Element element, Element child, Field field, Class<?> type) throws ConfigurationException {
		// fix child
		if (child == null)
			child = element;

		// initialize object
		Object instance = null;

		if (type == null)
			type = field.getType ();

		try {
			// find constructor
			Constructor<?> constructor = type.getConstructor ();

			// make accessible
			constructor.setAccessible (true);

			// create instance
			instance = constructor.newInstance ();
		} catch (NoSuchMethodException ex) {
			throw new ConfigurationSetupException ("Could not unmarshal object of type " + type.getName () + ": Missing default no-argument constructor.");
		} catch (Exception ex) {
			throw new ConfigurationSetupException (ex);
		}

		// iterate over fields
		for (Field objectField : type.getFields ()) {
			// make field accessible
			objectField.setAccessible (true);

			// skip ignored fields
			if (!objectField.isAnnotationPresent (Property.class)) continue;

			// find correct field
			Element parent = child;

			if (objectField.isAnnotationPresent (PropertyWrapper.class)) {
				NodeList parentList = parent.getElementsByTagNameNS (this.unmarshaller.getTypeConfiguration ().namespace (), objectField.getAnnotation (PropertyWrapper.class).value ());

				// verify list of possible parent elements
				if (parentList.getLength () == 0) throw new ConfigurationProcessorException ("Could not find a element wrapper with name " + objectField.getAnnotation (PropertyWrapper.class).value () + " for property " + objectField.getName () + " in object of type " + type.getName ());

				// correct parent
				parent = ((Element) parentList.item (0));
			}

			// find field
			NodeList fieldList = parent.getElementsByTagNameNS (this.unmarshaller.getTypeConfiguration ().namespace (), objectField.getAnnotation (Property.class).value ());
			Element fieldElement = null;

			// find field element (or create dummy)
			if (fieldList.getLength () == 0 ||fieldList.getLength () > 1)
				fieldElement = document.createElementNS (this.unmarshaller.getTypeConfiguration ().namespace (), objectField.getAnnotation (Property.class).value ());
			else
				fieldElement = ((Element) fieldList.item (0));

			// un-marshal
			try {
				if (objectField.getType ().isAnnotationPresent (Configuration.class)) {
					IUnmarshaller fieldMarshaller = this.processor.createUnmarshaller (objectField.getType ());

					// marshal field
					objectField.set (instance, fieldMarshaller.unmarshal (document, parent, fieldElement, objectField, objectField.getType ()));
				} else
					// forcefully marshal
					objectField.set (instance, this.unmarshaller.unmarshal (document, parent, fieldElement, objectField, objectField.getType ()));
			} catch (IllegalAccessException ex) {
				throw new ConfigurationProcessorException (ex);
			}
		}

		// return finished instance
		return instance;
	}
}
