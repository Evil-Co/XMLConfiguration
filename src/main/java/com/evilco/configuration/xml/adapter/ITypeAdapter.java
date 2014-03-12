package com.evilco.configuration.xml.adapter;

/**
 * @auhtor Johannes Donath <johannesd@evil-co.com>
 * @copyright Copyright (C) 2014 Evil-Co <http://www.evil-co.org>
 */
public interface ITypeAdapter<K, V> {

	/**
	 * Converts an object for later serialization.
	 * @param in
	 * @return
	 */
	public K decode (V in);

	/**
	 * Converts an object for later serialization.
	 * @param in
	 * @return
	 */
	public V encode (K in);
}
