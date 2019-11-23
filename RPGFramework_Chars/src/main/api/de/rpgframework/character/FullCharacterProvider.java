/**
 * 
 */
package de.rpgframework.character;

import java.io.IOException;
import java.util.List;

import de.rpgframework.core.Player;
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
public interface FullCharacterProvider extends CharacterProvider {
	
	//--------------------------------------------------------------------
	/**
	 * Create a new CharacterHandle as a container for attachments a given player. 
	 * 
	 * @param player   The player to add the character to
	 * @param charName Name of the character
	 * @param ruleSystem The roleplaying system of the character
	 * @return The created handle
	 * @throws IOException Error executing operation - e.g. on directory creation or
	 *   missing internet connection
	 */
	public CharacterHandle createCharacter(Player player, String charName, RoleplayingSystem ruleSystem) throws IOException;

	//--------------------------------------------------------------------
	/**
	 * Get all characters of a given player belonging to a specific roleplaying system.
	 * 
	 * @param player Player to get characters from
	 * @param ruleSystem Roleplaying system to get characters for.
	 * @return A list of characters in no granted order.
	 */
	public List<CharacterHandle> getCharacters(Player player, RoleplayingSystem ruleSystem);

	//--------------------------------------------------------------------
	/**
	 * Get all characters of a given player.
	 * 
	 * @param player Player to get characters from
	 * @return A list of characters in no granted order.
	 */
	public List<CharacterHandle> getCharacters(Player player);
	
}
