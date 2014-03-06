package com.evilco.configuration.xml.exception;

/**
 * @package com.evilco.bukkit.util.plugin.configuration.exception
 * @author Johannes Donath <johannesd@evil-co.com>
 * @copyright Copyright (C) 2014 Evil-Co <http://www.evil-co.org>
 */
public abstract class ConfigurationException extends Exception {

	public ConfigurationException () {
		super ();
	}

	public ConfigurationException (String message) {
		super (message);
	}

	public ConfigurationException (String message, Throwable cause) {
		super (message, cause);
	}

	public ConfigurationException (Throwable cause) {
		super (cause);
	}
}
