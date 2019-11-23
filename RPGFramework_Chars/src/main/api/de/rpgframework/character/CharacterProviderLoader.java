/**
 *
 */
package de.rpgframework.character;

/**
 * @author prelle
 *
 */
public class CharacterProviderLoader {
	
	private static CharacterProvider instance;

	//--------------------------------------------------------------------
	public static CharacterProvider getInstance() {
			return instance;
	}

	//--------------------------------------------------------------------
	public static void registerInstance(CharacterProvider service) {
		instance = service;
	}

}
