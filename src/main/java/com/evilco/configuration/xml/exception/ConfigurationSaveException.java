package com.evilco.configuration.xml.exception;

/**
 * @package com.evilco.configuration.xml.exception
 * @author Johannes Donath <johannesd@evil-co.com>
 * @copyright Copyright (C) 2014 Evil-Co <http://www.evil-co.org>
 */
public class ConfigurationSaveException extends ConfigurationException {

	public ConfigurationSaveException () {
		super ();
	}

	public ConfigurationSaveException (String message) {
		super (message);
	}

	public ConfigurationSaveException (String message, Throwable cause) {
		super (message, cause);
	}

	public ConfigurationSaveException (Throwable cause) {
		super (cause);
	}
}
