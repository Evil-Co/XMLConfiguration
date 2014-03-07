package com.evilco.configuration.xml.exception;

/**
 * @author Johannes Donath <johannesd@evil-co.com>
 * @package com.evilco.configuration.xml.exception
 * @copyright Copyright (C) 2014 Evil-Co <http://www.evil-co.org>
 */
public class ConfigurationProcessorException extends ConfigurationException {

	public ConfigurationProcessorException () {
		super ();
	}

	public ConfigurationProcessorException (String message) {
		super (message);
	}

	public ConfigurationProcessorException (String message, Throwable cause) {
		super (message, cause);
	}

	public ConfigurationProcessorException (Throwable cause) {
		super (cause);
	}
}
