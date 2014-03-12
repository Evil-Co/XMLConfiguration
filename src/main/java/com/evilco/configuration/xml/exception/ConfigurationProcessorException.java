package com.evilco.configuration.xml.exception;

/**
 * @auhtor Johannes Donath <johannesd@evil-co.com>
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

	protected ConfigurationProcessorException (String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super (message, cause, enableSuppression, writableStackTrace);
	}
}
