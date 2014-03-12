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
@Target (ElementType.TYPE)
public @interface TypeAdapterDefinition {

	/**
	 * Defines the input type.
	 * @return
	 */
	public Class<?> input ();

	/**
	 * Defines the output type.
	 * @return
	 */
	public Class<?> output ();

	/**
	 * Indicates whether the adapter instance can be shared.
	 * @return
	 */
	public boolean sharable () default true;
}
