package com.evilco.configuration.xml;

import com.evilco.configuration.xml.adapter.IAdapter;
import com.evilco.configuration.xml.adapter.ITypeAdapter;
import com.evilco.configuration.xml.exception.AdapterInitializationException;
import com.evilco.configuration.xml.exception.ConfigurationException;
import com.evilco.configuration.xml.exception.ConfigurationProcessorException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.lang.reflect.Constructor;

/**
 * @auhtor Johannes Donath <johannesd@evil-co.com>
 * @copyright Copyright (C) 2014 Evil-Co <http://www.evil-co.org>
 */
class Marshaller<T> extends AbstractMarshaller<T> implements IMarshaller<T> {

	/**
	 * Constructs a new Marshaller.
	 * @param processor
	 * @param type
	 * @throws ConfigurationException
	 */
	public Marshaller (ConfigurationProcessor processor, Class<T> type) throws ConfigurationException {
		super (processor, type);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected IAdapter createAdapterInstance (Class<? extends IAdapter> adapter) throws AdapterInitializationException {
		try {
			try {
				// find constructor
				Constructor<? extends IAdapter> constructor = adapter.getConstructor (ConfigurationProcessor.class, IMarshaller.class);

				// make accessible
				constructor.setAccessible (true);

				// create a new instance
				return constructor.newInstance (this.processor, this);
			} catch (NoSuchMethodException ex) {
				try {
					// find constructor
					Constructor<? extends IAdapter> constructor = adapter.getConstructor (ConfigurationProcessor.class);

					// make accessible
					constructor.setAccessible (true);

					// create new instance
					return constructor.newInstance (this.processor);
				} catch (NoSuchMethodException e) {
					try {
						// find constructor
						Constructor<? extends IAdapter> constructor = adapter.getConstructor (IMarshaller.class);

						// make accessible
						constructor.setAccessible (true);

						// create new instance
						return constructor.newInstance (this);
					} catch (NoSuchMethodException f) {
						// find constructor
						Constructor<? extends IAdapter> constructor = adapter.getConstructor ();

						// make accessible
						constructor.setAccessible (true);

						// create new instance
						return constructor.newInstance ();
					}
				}
			}
		} catch (NoSuchMethodException ex) {
			throw new AdapterInitializationException ("The adapter " + adapter.getName () + " does not have one of the following constructors: IAdapter (), IAdapter (ConfigurationProcessor), IAdapter (Marshaller), IAdapter (ConfigurationProcessor, Marshaller)");
		} catch (Exception ex) {
			throw new AdapterInitializationException (ex);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ITypeAdapter createTypeAdapterInstance (Class<? extends ITypeAdapter> adapter) throws AdapterInitializationException {
		try {
			try {
				// find constructor
				Constructor<? extends ITypeAdapter> constructor = adapter.getConstructor (ConfigurationProcessor.class, IMarshaller.class);

				// make accessible
				constructor.setAccessible (true);

				// create a new instance
				return constructor.newInstance (this.processor, this);
			} catch (NoSuchMethodException ex) {
				try {
					// find constructor
					Constructor<? extends ITypeAdapter> constructor = adapter.getConstructor (ConfigurationProcessor.class);

					// make accessible
					constructor.setAccessible (true);

					// create new instance
					return constructor.newInstance (this.processor);
				} catch (NoSuchMethodException e) {
					try {
						// find constructor
						Constructor<? extends ITypeAdapter> constructor = adapter.getConstructor (IMarshaller.class);

						// make accessible
						constructor.setAccessible (true);

						// create new instance
						return constructor.newInstance (this);
					} catch (NoSuchMethodException f) {
						// find constructor
						Constructor<? extends ITypeAdapter> constructor = adapter.getConstructor ();

						// make accessible
						constructor.setAccessible (true);

						// create new instance
						return constructor.newInstance ();
					}
				}
			}
		} catch (NoSuchMethodException ex) {
			throw new AdapterInitializationException ("The adapter " + adapter.getName () + " does not have one of the following constructors: ITypeAdapter (), ITypeAdapter (ConfigurationProcessor), ITypeAdapter (Marshaller), ITypeAdapter (ConfigurationProcessor, Marshaller)");
		} catch (Exception ex) {
			throw new AdapterInitializationException (ex);
		}
	}

	/**
	 * Marshals an object.
	 * @param object
	 * @return
	 * @throws ConfigurationException
	 */
	@Override
	public Document marshal (T object) throws ConfigurationException {
		try {
			// create a document
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance ();
			builderFactory.setNamespaceAware (true);

			Document document = builderFactory.newDocumentBuilder ().newDocument ();

			// create root element
			Element rootElement = document.createElementNS (this.typeConfiguration.namespace (), this.typeConfiguration.name ());
			document.appendChild (rootElement);

			// serialize object
			this.marshal (object, rootElement, null, document);

			// return finished document
			return document;
		} catch (ParserConfigurationException ex) {
			throw new ConfigurationProcessorException (ex);
		}
	}

	/**
	 * Marshals an object.
	 * @param object
	 * @param parent
	 * @param child
	 * @param document
	 * @throws ConfigurationException
	 */
	@Override
	public void marshal (Object object, Element parent, Element child, Document document) throws ConfigurationException {
		ITypeAdapter typeAdapter = null;

		// convert element
		while ((typeAdapter = this.getTypeAdapterInstance (object)) != null) {
			object = typeAdapter.encode (object);
		}

		// marshal
		IAdapter adapter = this.getAdapterInstance (object);

		// check adapter
		if (adapter == null) throw new ConfigurationProcessorException ("No valid adapter could be found for type " + object.getClass ().getName ());

		// marshal into object
		adapter.marshal (document, parent, child, object);
	}
}
