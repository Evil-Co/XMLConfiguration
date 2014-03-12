package com.evilco.configuration.xml.exception;

/**
 * @auhtor Johannes Donath <johannesd@evil-co.com>
 * @copyright Copyright (C) 2014 Evil-Co <http://www.evil-co.org>
 */
public class ConfigurationSetupException extends ConfigurationProcessorException {

	public ConfigurationSetupException () {
		super ();
	}

	public ConfigurationSetupException (String message) {
		super (message);
	}

	public ConfigurationSetupException (String message, Throwable cause) {
		super (message, cause);
	}

	public ConfigurationSetupException (Throwable cause) {
		super (cause);
	}

	protected ConfigurationSetupException (String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super (message, cause, enableSuppression, writableStackTrace);
	}
}
