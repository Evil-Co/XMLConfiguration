package com.evilco.configuration.xml.adapter;

import com.evilco.configuration.xml.ConfigurationProcessor;
import com.evilco.configuration.xml.IMarshaller;
import com.evilco.configuration.xml.IUnmarshaller;
import com.evilco.configuration.xml.annotation.AdapterDefinition;
import com.evilco.configuration.xml.annotation.Configuration;
import com.evilco.configuration.xml.exception.ConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @auhtor Johannes Donath <johannesd@evil-co.com>
 * @copyright Copyright (C) 2014 Evil-Co <http://www.evil-co.org>
 */
@AdapterDefinition (input = Map.class, matchChildren = true)
public class MapAdapter implements IAdapter<Map> {

	/**
	 * Stores the parent marshaller.
	 */
	protected final IMarshaller marshaller;

	/**
	 * Stores the parent processor.
	 */
	protected final ConfigurationProcessor processor;

	/**
	 * Stores the parent unmarshaller.
	 */
	protected final IUnmarshaller unmarshaller;

	/**
	 * Constructs a new MapAdapter.
	 * @param marshaller
	 */
	public MapAdapter (ConfigurationProcessor processor, IMarshaller marshaller) {
		this.marshaller = marshaller;
		this.processor = processor;
		this.unmarshaller = null;
	}

	/**
	 * Constructs a new MapAdapter.
	 * @param processor
	 * @param unmarshaller
	 */
	public MapAdapter (ConfigurationProcessor processor, IUnmarshaller unmarshaller) {
		this.unmarshaller = unmarshaller;
		this.processor = processor;
		this.marshaller = null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void marshal (Document document, Element parent, Element child, Map object) throws ConfigurationException {
		for (Map.Entry entry : ((Set<Map.Entry>) object.entrySet ())) {
			// copy element
			Element entryElement = ((Element) child.cloneNode (false));

			// add key
			entryElement.setAttributeNS (this.marshaller.getTypeConfiguration ().namespace (), "key", ((String) entry.getKey ()));

			// marshal
			if (entry.getValue ().getClass ().isAnnotationPresent (Configuration.class)) {
				IMarshaller fieldMarshaller = this.processor.createMarshaller (entry.getValue ().getClass ());

				// marshal field
				fieldMarshaller.marshal (entry.getValue (), parent, entryElement, document);
			} else
				// forcefully marshal
				this.marshaller.marshal (entry.getValue (), parent, entryElement, document);
		}

		if (object.entrySet ().size () == 0) parent.appendChild (child);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map unmarshal (Document document, Element element, Element child, Field field, Class<?> type) throws ConfigurationException {
		// create map
		Map<String, Object> map = new HashMap<> ();

		// get type
		ParameterizedType parameterizedType = ((ParameterizedType) field.getGenericType ());
		Class<?> mapType = ((Class<?>) parameterizedType.getActualTypeArguments ()[1]);

		// find all nodes
		NodeList nodeList = element.getElementsByTagNameNS (this.unmarshaller.getTypeConfiguration ().namespace (), child.getTagName ());

		// append elements
		for (int i = 0; i < nodeList.getLength (); i++) {
			Element currentElement = ((Element) nodeList.item (i));

			// get key
			String key = currentElement.getAttributeNS (this.unmarshaller.getTypeConfiguration ().namespace (), "key");

			// unmarshal
			if (mapType.isAnnotationPresent (Configuration.class)) {
				IUnmarshaller fieldMarshaller = this.processor.createUnmarshaller (mapType);

				// marshal field
				map.put (key, fieldMarshaller.unmarshal (document, element, currentElement, null, mapType));
			} else
				// forcefully marshal
				map.put (key, this.unmarshaller.unmarshal (document, element, currentElement, null, mapType));
		}

		return map;
	}
}
