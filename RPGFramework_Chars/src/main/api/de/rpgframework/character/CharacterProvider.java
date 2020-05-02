/**
 * 
 */
package de.rpgframework.character;

import java.io.IOException;
import java.util.List;

import de.rpgframework.character.CharacterHandle.Format;
import de.rpgframework.character.CharacterHandle.Type;
import de.rpgframework.core.RoleplayingSystem;

/**
 * <p>This interface controls the storage of character related documents. The 
 * basic concept is that each character can have attachments of a specific
 * {@link CharacterHandle.Format format} and {@link CharacterHandle.Type type}.
 * Examples for those attachments may be portrait image (type CHARACTER, format IMAGE)
 * serialized character data (type CHARACTER, format RULESPECIFIC) or a 
 * background story (type BACKGROUND, format PDF).</p>
 * <p>
 * All the attachments are collected in a {@link CharacterHandle}. 
 * CharacterHandles itself belong to a {@link de.rpgframework.core.Player} and
 * have a {@link de.rpgframework.core.RoleplayingSystem} assigned. 
 * </p>
 * <pre>
 *  // Get the player that represents yourself
 * Player myself = RPGFrameworkLoader.getInstance().getPlayerService().getMyself();
 * 
 *  // Obtain an instance of the character provider ... 
 * CharacterProvider charProv = RPGFrameworkLoader.getInstance().getCharacterProvider();
 * 
 *  // Obtain an instance of the character provider ... 
 * for (CharacterHandle handle : charProv.getCharacters(myself)) {
 *    // ... do something
 * }
 * </pre>
 * 
 * <h2>Events</h2>
 * <p>
 * A CharacterProvider may fire events on the {@link de.rpgframework.core.EventBus} when
 * actions are performed that add, modify or delete attachments or character handles. The
 * main reason for this is to help synchronization between different CharacterProviders.
 * E.g. is a CharacterProvider for a local filesystem that fires events which 
 * allow a cloud service CharacterProvider to react and reflect those changes 
 * remotely.
 * </p>
 * 
 * @author prelle
 *
 */
public interface CharacterProvider {
	
	//--------------------------------------------------------------------
	/**
	 * Create a new CharacterHandle as a container for attachments for the
	 * local user. 
	 * 
	 * @param charName Name of the character
	 * @param ruleSystem The roleplaying system of the character
	 * @return The created handle
	 * @throws IOException Error executing operation - e.g. on directory creation or
	 *   missing internet connection
	 */
	public CharacterHandle createCharacter(String charName, RoleplayingSystem ruleSystem) throws IOException;
	
	//--------------------------------------------------------------------
	/**
	 * Adding an attachment to a character. Though the primary focus of this
	 * method is adding attachments only to characters of the local player,
	 * it may also be used for characters of other players as well. If this
	 * works is implementation dependant.
	 * 
	 * How many instances (per format) of attachments are allowed, depends
	 * on the type: CHARACTER and BACKGROUND may only exist once, while
	 * REPORT may exist multiple times.
	 * 
	 * @param handle Character to modify
	 * @param type   What us described in this attachment
	 * @param format What kind of data is it
	 * @param filename A proposed file name. May be null. May be ignored 
	 *   by the implementation
	 * @param data Binary data of the attachment
	 * @return The created attachment
	 * @throws  IOException Error executing operation
	 */
	public Attachment addAttachment(CharacterHandle handle, Type type, Format format, String filename, byte[] data) throws IOException;
	
//	//--------------------------------------------------------------------
//	/**
//	 * Copy the attachment from a different character provider to this one.<br/>
//	 * This method is used for synchronization.
//	 * 
//	 * @param handle Character to modify
//	 * @param attach Attachment to copy
//	 * @return Cloned attachment
//	 * @throws IOException
//	 */
//	public Attachment copyAttachment(CharacterHandle handle, Attachment attach) throws IOException;
	
	//--------------------------------------------------------------------
	/**
	 * Write modifications made to this attachment to storage.
	 * 
	 * @param handle Character to modify
	 * @param attach Attachment that has been modified
	 * @throws IOException
	 */
	public void modifyAttachment(CharacterHandle handle, Attachment attach) throws IOException;
	
	//--------------------------------------------------------------------
	/**
	 * Remove an attachment from a character.
	 * 
	 * @param handle Character to modify
	 * @param attach Attachment to remove
	 * @throws IOException
	 */
	public void removeAttachment(CharacterHandle handle, Attachment attach) throws IOException;
	
	//--------------------------------------------------------------------
	/**
	 * Delete a character and all his attachments,
	 * 
	 * @param handle Character to remove
	 * @throws IOException
	 */
	public void deleteCharacter(CharacterHandle handle) throws IOException;
	
	//--------------------------------------------------------------------
	/**
	 * Rename a character and all his attachments,
	 * 
	 * @param handle Character to remove
	 * @throws IOException
	 */
	public void renameCharacter(CharacterHandle handle, String newName) throws IOException;

	//--------------------------------------------------------------------
	public List<CharacterHandle> getMyCharacters();

	//--------------------------------------------------------------------
	/**
	 * Get all characters of a given player belonging to a specific roleplaying system.
	 * 
	 * @param player Player to get characters from
	 * @param ruleSystem Roleplaying system to get characters for.
	 * @return A list of characters in no granted order.
	 */
	public List<CharacterHandle> getMyCharacters(RoleplayingSystem ruleSystem);
	
}
