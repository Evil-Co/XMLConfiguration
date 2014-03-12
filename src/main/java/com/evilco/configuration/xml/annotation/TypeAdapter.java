package com.evilco.configuration.xml.annotation;

import com.evilco.configuration.xml.adapter.ITypeAdapter;

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
public @interface TypeAdapter {

	/**
	 * Defines a list of adapters to register with the processor.
	 * @return
	 */
	public Class<? extends ITypeAdapter>[] value ();
}
