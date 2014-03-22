package com.evilco.configuration.xml.adapter;

import com.evilco.configuration.xml.ConfigurationProcessor;
import com.evilco.configuration.xml.IMarshaller;
import com.evilco.configuration.xml.IUnmarshaller;
import com.evilco.configuration.xml.annotation.AdapterDefinition;
import com.evilco.configuration.xml.annotation.Factory;
import com.evilco.configuration.xml.annotation.MapType;
import com.evilco.configuration.xml.annotation.Configuration;
import com.evilco.configuration.xml.exception.ConfigurationException;
import com.evilco.configuration.xml.exception.ConfigurationProcessorException;
import com.evilco.configuration.xml.exception.ConfigurationSetupException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @auhtor Johannes Donath <johannesd@evil-co.com>
 * @copyright Copyright (C) 2014 Evil-Co <http://www.evil-co.org>
 */
@AdapterDefinition (input = Map.class, matchChildren = true, sharable = true)
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
			entryElement.setAttribute ("key", ((String) entry.getKey ()));

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
		// find type
		Class<?> keyType = null;
		Class<?> mapType = null;

		if (!field.getType ().isAnnotationPresent (MapType.class) && !field.isAnnotationPresent (MapType.class))
			mapType = ((Class<?>) (field.getGenericType () instanceof ParameterizedType ? ((ParameterizedType) field.getGenericType ()) : ((ParameterizedType) field.getType ().getGenericSuperclass ())).getActualTypeArguments ()[1]);
		else if (field.isAnnotationPresent (MapType.class))
			mapType = field.getAnnotation (MapType.class).value ();
		else
			mapType = field.getType ().getAnnotation (MapType.class).value ();

		if (!field.getType ().isAnnotationPresent (MapType.class) && !field.isAnnotationPresent (MapType.class))
			keyType = String.class;
		else if (field.isAnnotationPresent (MapType.class))
			keyType = field.getAnnotation (MapType.class).key ();
		else
			keyType = field.getType ().getAnnotation (MapType.class).key ();

		// create map
		Map<Object, Object> map = null;

		if (field.getGenericType () instanceof ParameterizedType)
			map = new HashMap<> ();
		else {
			try {
				// get type
				Class<? extends Map> mapClass = field.getType ().asSubclass (Map.class);

				// find constructor
				Constructor<? extends Map> constructor = mapClass.getConstructor ();

				// create instance
				map = constructor.newInstance ();
			} catch (NoSuchMethodException ex) {
				throw new ConfigurationSetupException ("The map of type " + field.getType ().getName () + " does not have a default no-argument constructor");
			} catch (Exception ex) {
				throw new ConfigurationProcessorException ("Could not create an instance of type " + field.getType ().getName (), ex);
			}
		}

		// find all nodes
		NodeList nodeList = element.getChildNodes ();

		// append elements
		for (int i = 0; i < nodeList.getLength (); i++) {
			if (!(nodeList.item (i) instanceof Element)) continue;
			if (!((Element) nodeList.item (i)).getTagName ().equalsIgnoreCase (child.getTagName ())) continue;

			// convert element
			Element currentElement = ((Element) nodeList.item (i));

			// get key
			Object key = currentElement.getAttribute ("key");

			// convert key
			if (!keyType.equals (String.class)) {
				boolean constructed = false;

				// search method
				for (Method method : keyType.getMethods ()) {
					// find factory method
					try {
						// check for annotation
						if (!method.isAnnotationPresent (Factory.class)) continue;

						// construct key object
						key = method.invoke (null, key);

						// done
						constructed = true;

						// exit loop
						break;
					} catch (Exception ex) { }
				}

				// search constructor
				if (!constructed) {
					try {
						// get constructor with String argument type
						Constructor<?> constructor = keyType.getConstructor (String.class);

						// create a new instance
						constructor.newInstance (key);
					} catch (Exception ex) {
						throw new ConfigurationProcessorException (ex);
					}
				}
			}

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
