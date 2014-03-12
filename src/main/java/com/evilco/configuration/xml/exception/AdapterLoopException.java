package com.evilco.configuration.xml.exception;

/**
 * @auhtor Johannes Donath <johannesd@evil-co.com>
 * @copyright Copyright (C) 2014 Evil-Co <http://www.evil-co.org>
 */
public class AdapterLoopException extends AdapterException {

	public AdapterLoopException () {
		super ();
	}

	public AdapterLoopException (String message) {
		super (message);
	}

	public AdapterLoopException (String message, Throwable cause) {
		super (message, cause);
	}

	public AdapterLoopException (Throwable cause) {
		super (cause);
	}

	protected AdapterLoopException (String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super (message, cause, enableSuppression, writableStackTrace);
	}
}
