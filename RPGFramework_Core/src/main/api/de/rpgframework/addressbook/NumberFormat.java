package de.rpgframework.addressbook;

public enum NumberFormat {
	UNKNOWN,
	INTERNATIONAL,
	NATIONAL,
	LOCAL,
	/**
	 * Digits like the user dialed them - detection for type has
	 * still to be made.
	 */
	DIALED, 
}