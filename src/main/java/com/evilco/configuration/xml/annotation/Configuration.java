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
public @interface Configuration {

	/**
	 * Defines the root element name.
	 * @return
	 */
	public String name () default "configuration";

	/**
	 * Defines the XML namespace to use.
	 * @return
	 */
	public String namespace () default "http://www.evil-co.org/2014/configuration";
}
