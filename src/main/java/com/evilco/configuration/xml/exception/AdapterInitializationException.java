package com.evilco.configuration.xml.exception;

/**
 * @auhtor Johannes Donath <johannesd@evil-co.com>
 * @copyright Copyright (C) 2014 Evil-Co <http://www.evil-co.org>
 */
public class AdapterInitializationException extends AdapterException {

	public AdapterInitializationException () {
		super ();
	}

	public AdapterInitializationException (String message) {
		super (message);
	}

	public AdapterInitializationException (String message, Throwable cause) {
		super (message, cause);
	}

	public AdapterInitializationException (Throwable cause) {
		super (cause);
	}

	protected AdapterInitializationException (String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super (message, cause, enableSuppression, writableStackTrace);
	}
}
