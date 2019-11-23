/**
 * 
 */
package de.rpgframework.character;

/**
 * This interface must be implemented by a character object of a plugin
 * adding support to serialize characters.
 * 
 * @author prelle
 *
 */
public interface RuleSpecificCharacterObject {
	
	//-------------------------------------------------------------------
	/**
	 * Returns a name by which a user interface shall display this character.
	 * 
	 * @return A printable name
	 */
	public String getName();
	
	//-------------------------------------------------------------------
	public byte[] getImage();

}
