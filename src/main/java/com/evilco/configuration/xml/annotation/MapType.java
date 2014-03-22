package com.evilco.configuration.xml.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @auhtor Johannes Donath <johannesd@evil-co.com>
 * @copyright Copyright (C) 2014 Evil-Co <http://www.evil-co.org>
 */
@Retention (RetentionPolicy.RUNTIME)
@Target ({ ElementType.TYPE, ElementType.FIELD })
public @interface MapType {

	/**
	 * Defines the key type.
	 * @return
	 */
	public Class<?> key () default String.class;

	/**
	 * Defines the value type.
	 * @return
	 */
	public Class<?> value ();
}