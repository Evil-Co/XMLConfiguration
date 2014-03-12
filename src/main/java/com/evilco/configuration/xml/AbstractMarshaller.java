package com.evilco.configuration.xml;

import com.evilco.configuration.xml.adapter.*;
import com.evilco.configuration.xml.annotation.Adapter;
import com.evilco.configuration.xml.annotation.AdapterDefinition;
import com.evilco.configuration.xml.annotation.Configuration;
import com.evilco.configuration.xml.annotation.TypeAdapterDefinition;
import com.evilco.configuration.xml.exception.AdapterInitializationException;
import com.evilco.configuration.xml.exception.ConfigurationException;
import com.evilco.configuration.xml.exception.ConfigurationSetupException;
import com.google.common.base.Preconditions;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/**
 * @auhtor Johannes Donath <johannesd@evil-co.com>
 * @copyright Copyright (C) 2014 Evil-Co <http://www.evil-co.org>
 */
abstract class AbstractMarshaller<T> implements IMarshallerInterface {

	/**
	 * Stores the adapter instance map.
	 */
	protected final Map<Class<? extends IAdapter>, IAdapter> adapterInstanceMap;

	/**
	 * Stores the adapter map.
	 */
	protected final Map<Class<?>, Class<? extends IAdapter>> adapterMap;

	/**
	 * Stores the parent processor instance.
	 */
	protected final ConfigurationProcessor processor;

	/**
	 * Stores the root type.
	 */
	protected final Class<T> type;

	/**
	 * Stores the type adapter instance map.
	 */
	protected final Map<Class<? extends ITypeAdapter>, ITypeAdapter> typeAdapterInstanceMap;

	/**
	 * Stores the type adapter map.
	 */
	protected final Map<Class<?>, Class<? extends ITypeAdapter>> typeAdapterMap;

	/**
	 * Stores the type configuration.
	 */
	protected final Configuration typeConfiguration;

	/**
	 * Constructs a new ConfigurationProcessor.
	 * @param processor
	 */
	public AbstractMarshaller (ConfigurationProcessor processor, Class<T> type) throws ConfigurationException {
		this.processor = processor;

		// verify class
		if (!type.isAnnotationPresent (Configuration.class)) throw new ConfigurationSetupException ("The supplied object is not setup for configuration marshalling.");

		// store arguments
		this.type = type;
		this.typeConfiguration = type.getAnnotation (Configuration.class);

		// create instances
		this.adapterMap = new HashMap<> ();
		this.typeAdapterMap = new HashMap<> ();
		this.adapterInstanceMap = new HashMap<> ();
		this.typeAdapterInstanceMap = new HashMap<> ();

		// register defaults
		this.registerAdapters ();
	}

	/**
	 * Creates a new adapter instance.
	 * @param adapter
	 * @return
	 * @throws AdapterInitializationException
	 */
	protected abstract IAdapter createAdapterInstance (Class<? extends IAdapter> adapter) throws AdapterInitializationException;

	/**
	 * Creates a new type adapter instance.
	 * @param adapter
	 * @return
	 * @throws AdapterInitializationException
	 */
	public abstract ITypeAdapter createTypeAdapterInstance (Class<? extends ITypeAdapter> adapter) throws AdapterInitializationException;

	/**
	 * Returns the adapter corresponding to a specific type.
	 * @param type
	 * @return
	 */
	public Class<? extends IAdapter> getAdapter (Class<?> type) throws AdapterInitializationException {
		// match adapter
		if (!this.adapterMap.containsKey (type)) {
			for (Map.Entry<Class<?>, Class<? extends IAdapter>> adapterEntry : this.adapterMap.entrySet ()) {
				if (!this.isWildcardAdapter (adapterEntry.getValue ())) continue;

				// get definition
				AdapterDefinition definition = adapterEntry.getValue ().getAnnotation (AdapterDefinition.class);

				// check input
				if (definition.input ().isAssignableFrom (type)) return adapterEntry.getValue ();
			}
		}

		// get fallback adapter
		if (!type.isPrimitive () && !this.adapterMap.containsKey (type)) return ObjectAdapter.class;

		// return explicit adapter
		return this.adapterMap.get (type);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IAdapter getAdapterInstance (Class<? extends IAdapter> adapter) throws AdapterInitializationException {
		this.verifyAdapter (adapter);

		// get sharable instance
		if (this.isAdapterSharable (adapter) && this.adapterInstanceMap.containsKey (adapter)) return this.adapterInstanceMap.get (adapter);

		// create a new instance
		IAdapter instance = this.createAdapterInstance (adapter);

		// store
		if (this.isAdapterSharable (adapter)) this.adapterInstanceMap.put (adapter, instance);

		// ok!
		return instance;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IAdapter getAdapterInstance (Object object) throws AdapterInitializationException {
		if (object == null) return null;
		return this.getAdapterInstance (this.getAdapter (object.getClass ()));
	}

	/**
	 * Returns the type adapter corresponding to a specific type.
	 * @param type
	 * @return
	 */
	public Class<? extends ITypeAdapter> getTypeAdapter (Class<?> type) {
		return this.typeAdapterMap.get (type);
	}

	/**
	 * Returns the type adapter instance.
	 * @param adapter
	 * @return
	 * @throws AdapterInitializationException
	 */
	public ITypeAdapter getTypeAdapterInstance (Class<? extends ITypeAdapter> adapter) throws AdapterInitializationException {
		this.verifyTypeAdapter (adapter);

		// get sharable instance
		if (this.isTypeAdapterSharable (adapter) && this.typeAdapterInstanceMap.containsKey (adapter)) return this.typeAdapterInstanceMap.get (adapter);

		// create a new instance
		ITypeAdapter instance = this.createTypeAdapterInstance (adapter);

		// store
		if (this.isTypeAdapterSharable (adapter)) this.typeAdapterInstanceMap.put (adapter, instance);

		// ok!
		return instance;
	}

	/**
	 * Returns the type adapter corresponding to a specific type.
	 * @param object
	 * @return
	 * @throws AdapterInitializationException
	 */
	public ITypeAdapter getTypeAdapterInstance (Object object) throws AdapterInitializationException {
		if (object == null) return null;

		// get corresponding adapter
		Class<? extends ITypeAdapter> adapter = this.getTypeAdapter (object.getClass ());

		// verify adapter
		if (adapter == null) return null;

		// get instance
		return this.getTypeAdapterInstance (adapter);
	}

	/**
	 * Returns the Marshaller type.
	 * @return
	 */
	@Override
	public Class<?> getType () {
		return this.type;
	}

	/**
	 * Returns the Marshaller type configuration.
	 * @return
	 */
	@Override
	public Configuration getTypeConfiguration () {
		return this.typeConfiguration;
	}

	/**
	 * Checks whether the adapter is sharable.
	 * @param adapter
	 * @return
	 * @throws AdapterInitializationException
	 */
	public boolean isAdapterSharable (Class<? extends IAdapter> adapter) throws AdapterInitializationException {
		this.verifyAdapter (adapter);

		// check
		return adapter.getAnnotation (AdapterDefinition.class).sharable ();
	}

	/**
	 * Checks whether the adapter is sharable.
	 * @param adapter
	 * @return
	 * @throws AdapterInitializationException
	 */
	public boolean isTypeAdapterSharable (Class<? extends ITypeAdapter> adapter) throws AdapterInitializationException {
		this.verifyTypeAdapter (adapter);

		// check
		return adapter.getAnnotation (TypeAdapterDefinition.class).sharable ();
	}

	/**
	 * Checks whether an adapter will match it's children.
	 * @param adapter
	 * @return
	 * @throws AdapterInitializationException
	 */
	public boolean isWildcardAdapter (Class<? extends IAdapter> adapter) throws AdapterInitializationException {
		this.verifyAdapter (adapter);

		// get annotation
		return adapter.getAnnotation (AdapterDefinition.class).matchChildren ();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerAdapter (Class<? extends IAdapter> adapter) throws ConfigurationException {
		this.verifyAdapter (adapter);

		// get annotation
		AdapterDefinition definition = adapter.getAnnotation (AdapterDefinition.class);

		// register adapter
		this.adapterMap.put (definition.input (), adapter);
	}

	/**
	 * Registers all default adapters.
	 */
	protected void registerAdapters () throws ConfigurationException {
		// register class based adapters
		if (this.type.isAnnotationPresent (Adapter.class)) {
			for (Class<? extends IAdapter> adapter : this.type.getAnnotation (Adapter.class).value ()) this.registerAdapter (adapter);
		}

		// TODO: Register type adapters

		// register default adapters
		this.registerAdapter (StringAdapter.class);
		this.registerAdapter (BooleanAdapter.class);
		this.registerAdapter (EnumAdapter.class);
		this.registerAdapter (FloatAdapter.class);
		this.registerAdapter (DoubleAdapter.class);
		this.registerAdapter (IntegerAdapter.class);
		this.registerAdapter (MapAdapter.class);
		this.registerAdapter (ListAdapter.class);
	}

	/**
	 * Verifies an adapter class.
	 * @param adapter
	 * @throws AdapterInitializationException
	 */
	public void verifyAdapter (Class<? extends IAdapter> adapter) throws AdapterInitializationException {
		Preconditions.checkNotNull (adapter, "adapter");
		if (!adapter.isAnnotationPresent (AdapterDefinition.class)) throw new AdapterInitializationException ("Cannot initialize adapter of type " + adapter.getName () + ": Missing @AdapterDefinition annotation");
	}

	/**
	 * Verifies an adapter class.
	 * @param adapter
	 * @throws AdapterInitializationException
	 */
	public void verifyTypeAdapter (Class<? extends ITypeAdapter> adapter) throws AdapterInitializationException {
		if (!adapter.isAnnotationPresent (TypeAdapterDefinition.class)) throw new AdapterInitializationException ("Cannot initialize adapter of type " + adapter.getName () + ": Missing @TypeAdapterDefinition annotation");
	}
}
