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
public @interface AdapterDefinition {

	/**
	 * Defines the input type.
	 * @return
	 */
	public Class<?> input ();

	/**
	 * Indicates whether children of the specified value will match.
	 * @return
	 */
	public boolean matchChildren () default false;

	/**
	 * Indicates whether the adapter can be shared.
	 * @return
	 */
	public boolean sharable () default true;
}
