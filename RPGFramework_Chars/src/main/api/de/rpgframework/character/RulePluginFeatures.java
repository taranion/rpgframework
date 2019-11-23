/**
 * 
 */
package de.rpgframework.character;

/**
 * @author prelle
 *
 */
public enum RulePluginFeatures {

	/**
	 * Reading and writing characters from or to a byte buffer
	 */
	PERSISTENCE,
	/**
	 * Additional data (items, professions, ...)
	 */
	DATA,
	/**
	 * Enter own data
	 */
	DATA_INPUT,
	/**
	 * Printing characters (to printer or to file)
	 */
	PRINT,
	/**
	 * Create <b>and</b> level characters
	 */
	CHARACTER_CREATION,
	/**
	 * Manage combats with characters
	 */
	COMBAT,
	/**
	 * Gamemaster GUI plugin
	 */
	GAMEMASTER
	
}
