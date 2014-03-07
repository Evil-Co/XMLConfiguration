package com.evilco.configuration.xml.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Johannes Donath <johannesd@evil-co.com>
 * @package com.evilco.configuration.xml.annotation
 * @copyright Copyright (C) 2014 Evil-Co <http://www.evil-co.org>
 */
@Retention (RetentionPolicy.RUNTIME)
@Target (ElementType.FIELD)
public @interface ConfigurationPropertyWrapper {

	/**
	 * Defines the wrapper name.
	 *
	 * @return
	 */
	public String value ();
}
