package com.evilco.configuration.xml.exception;

/**
 * @auhtor Johannes Donath <johannesd@evil-co.com>
 * @copyright Copyright (C) 2014 Evil-Co <http://www.evil-co.org>
 */
public abstract class AdapterException extends ConfigurationProcessorException {

	public AdapterException () {
		super ();
	}

	public AdapterException (String message) {
		super (message);
	}

	public AdapterException (String message, Throwable cause) {
		super (message, cause);
	}

	public AdapterException (Throwable cause) {
		super (cause);
	}

	protected AdapterException (String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super (message, cause, enableSuppression, writableStackTrace);
	}
}
