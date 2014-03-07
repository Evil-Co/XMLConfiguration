package com.evilco.configuration.xml;

import com.google.common.collect.ImmutableMap;

import java.util.List;
import java.util.Map;

/**
 * @author Johannes Donath <johannesd@evil-co.com>
 * @package com.evilco.configuration.xml
 * @copyright Copyright (C) 2014 Evil-Co <http://www.evil-co.org>
 */
public enum ConfigurationPropertyType {
	BOOLEAN (Boolean.class),
	ENUM (Enum.class),
	FLOAT (Float.class),
	DOUBLE (Double.class),
	INTEGER (Integer.class),
	MAP (Map.class),
	STRING (String.class),
	LIST (List.class),
	OBJECT (null);

	/**
	 * Stores the primitive mapping.
	 */
	protected static final Map<Class<?>, ConfigurationPropertyType> PRIMITIVE_MAP = (new ImmutableMap.Builder<Class<?>, ConfigurationPropertyType> ()).
		put (boolean.class, BOOLEAN).
		put (float.class, FLOAT).
		put (double.class, DOUBLE).
		put (int.class, INTEGER).
		build ();

	/**
	 * Defines the type associated with this property type.
	 */
	private final Class<?> type;

	/**
	 * Constructs a new ConfigurationpropertyType value.
	 *
	 * @param type
	 */
	private ConfigurationPropertyType (Class<?> type) {
		this.type = type;
	}

	/**
	 * Converts a type into a property type.
	 *
	 * @param type
	 * @return
	 */
	public static ConfigurationPropertyType fromType (Class<?> type) {
		// convert primitive
		if (type.isPrimitive () && PRIMITIVE_MAP.containsKey (type)) return PRIMITIVE_MAP.get (type);

		// get representation
		for (ConfigurationPropertyType propertyType : ConfigurationPropertyType.values ()) {
			if (propertyType.getType () != null && propertyType.getType ().isAssignableFrom (type))
				return propertyType;
		}

		// fallback
		return ConfigurationPropertyType.OBJECT;
	}

	/**
	 * Returns the associated type.
	 */
	public Class<?> getType () {
		return this.type;
	}
}
