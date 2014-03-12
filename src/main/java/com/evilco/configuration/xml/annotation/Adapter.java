package com.evilco.configuration.xml.annotation;

import com.evilco.configuration.xml.adapter.IAdapter;

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
public @interface Adapter {

	/**
	 * Defines the adapters to register.
	 * @return
	 */
	public Class<? extends IAdapter>[] value ();

	/**
	 * Marks an adapter as sharable.
	 */
	@Retention (RetentionPolicy.RUNTIME)
	@Target (ElementType.TYPE)
	public static @interface Sharable { }
}
