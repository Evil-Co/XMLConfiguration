package com.evilco.configuration.xml.exception;

/**
 * @auhtor Johannes Donath <johannesd@evil-co.com>
 * @copyright Copyright (C) 2014 Evil-Co <http://www.evil-co.org>
 */
public class AdapterRegistrationException extends AdapterException {

	public AdapterRegistrationException () {
		super ();
	}

	public AdapterRegistrationException (String message) {
		super (message);
	}

	public AdapterRegistrationException (String message, Throwable cause) {
		super (message, cause);
	}

	public AdapterRegistrationException (Throwable cause) {
		super (cause);
	}

	protected AdapterRegistrationException (String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super (message, cause, enableSuppression, writableStackTrace);
	}
}
