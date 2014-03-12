package com.evilco.configuration.xml;

import com.evilco.configuration.xml.adapter.IAdapter;
import com.evilco.configuration.xml.adapter.ITypeAdapter;
import com.evilco.configuration.xml.exception.AdapterInitializationException;
import com.evilco.configuration.xml.exception.ConfigurationException;
import com.google.common.base.Preconditions;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

/**
 * @auhtor Johannes Donath <johannesd@evil-co.com>
 * @copyright Copyright (C) 2014 Evil-Co <http://www.evil-co.org>
 */
class Unmarshaller<T> extends AbstractMarshaller<T> implements IUnmarshaller {

	/**
	 * Constructs a new Unmarshaller.
	 * @param processor
	 * @param type
	 * @throws ConfigurationException
	 */
	public Unmarshaller (ConfigurationProcessor processor, Class<T> type) throws ConfigurationException {
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
				Constructor<? extends IAdapter> constructor = adapter.getConstructor (ConfigurationProcessor.class, IUnmarshaller.class);

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
						Constructor<? extends IAdapter> constructor = adapter.getConstructor (IUnmarshaller.class);

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
			throw new AdapterInitializationException ("The adapter " + adapter.getName () + " does not have one of the following constructors: IAdapter (), IAdapter (ConfigurationProcessor), IAdapter (Unmarshaller), IAdapter (ConfigurationProcessor, Unmarshaller)");
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
				Constructor<? extends ITypeAdapter> constructor = adapter.getConstructor (ConfigurationProcessor.class, IUnmarshaller.class);

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
						Constructor<? extends ITypeAdapter> constructor = adapter.getConstructor (IUnmarshaller.class);

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
			throw new AdapterInitializationException ("The adapter " + adapter.getName () + " does not have one of the following constructors: ITypeAdapter (), ITypeAdapter (ConfigurationProcessor), ITypeAdapter (Unmarshaller), ITypeAdapter (ConfigurationProcessor, Unmarshaller)");
		} catch (Exception ex) {
			throw new AdapterInitializationException (ex);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T unmarshal (Document document) throws ConfigurationException {
		// empty element?
		if (document.getDocumentElement () == null || document.getChildNodes ().getLength () == 0) return null;

		// unmarshal element
		return ((T) this.unmarshal (document, document.getDocumentElement (), null, null, this.type));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object unmarshal (Document document, Element element, Element child, Field field, Class type) throws ConfigurationException {
		// marshal
		IAdapter adapter = this.getAdapterInstance (this.getAdapter (type));

		// check adapter
		Preconditions.checkNotNull (adapter, "No valid adapter could be found for type " + type.getName ());

		// un-marshal into object
		Object object = adapter.unmarshal (document, element, child, field, type);

		// convert back
		ITypeAdapter typeAdapter = null;

		// convert element
		while ((typeAdapter = this.getTypeAdapterInstance (object)) != null) {
			object = typeAdapter.encode (object);
		}

		return object;
	}
}
