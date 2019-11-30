/**
 * 
 */
package org.prelle.rpgframework.character;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.rpgframework.character.Attachment;
import de.rpgframework.character.CharacterHandle;
import de.rpgframework.character.CharacterHandle.Format;
import de.rpgframework.character.CharacterHandle.Type;
import de.rpgframework.character.CharacterProvider;
import de.rpgframework.core.BabylonEventBus;
import de.rpgframework.core.BabylonEventType;
import de.rpgframework.core.RoleplayingSystem;

/**
 * @author prelle
 *
 */
public class EventingCharacterProviderLight implements CharacterProvider {
	
	protected Logger logger = LogManager.getLogger("babylon.chars");
	
	private BaseCharacterProviderLight wrapped;

	//-------------------------------------------------------------------
	/**
	 */
	public EventingCharacterProviderLight(BaseCharacterProviderLight wrapped) {
		this.wrapped = wrapped;
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.character.CharacterProvider#firesEvents()
	 */
	@Override
	public boolean firesEvents() {
		return true;
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.character.CharacterProvider#getNonEventingStorage()
	 */
	@Override
	public CharacterProvider getNonEventingStorage() {
		return wrapped;
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.character.CharacterProvider#createCharacter(java.lang.String, de.rpgframework.core.RoleplayingSystem)
	 */
	@Override
	public CharacterHandle createCharacter(String charName, RoleplayingSystem ruleSystem) throws IOException {
		CharacterHandle handle = wrapped.createCharacter(charName, ruleSystem);
		BabylonEventBus.fireEvent(BabylonEventType.CHAR_ADDED, handle);
		return handle;
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.character.CharacterProvider#deleteCharacter(de.rpgframework.character.CharacterHandle)
	 */
	@Override
	public void deleteCharacter(CharacterHandle handle) throws IOException {
		wrapped.deleteCharacter(handle);
		BabylonEventBus.fireEvent(BabylonEventType.CHAR_REMOVED, handle);
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.character.CharacterProvider#renameCharacter(de.rpgframework.character.CharacterHandle, java.lang.String)
	 */
	@Override
	public void renameCharacter(CharacterHandle handle, String newName) throws IOException {
		wrapped.renameCharacter(handle, newName);
		BabylonEventBus.fireEvent(BabylonEventType.CHAR_RENAMED, handle);
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.character.CharacterProvider#getCharacters(de.rpgframework.core.Player, de.rpgframework.core.RoleplayingSystem)
	 */
	@Override
	public List<CharacterHandle> getMyCharacters(RoleplayingSystem ruleSystem) {
		return wrapped.getMyCharacters(ruleSystem);
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.character.CharacterProvider#getCharacters(de.rpgframework.core.Player)
	 */
	@Override
	public List<CharacterHandle> getMyCharacters() {
		return wrapped.getMyCharacters();
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.character.CharacterProvider#modifyAttachment(de.rpgframework.character.CharacterHandle, de.rpgframework.character.Attachment)
	 */
	@Override
	public void modifyAttachment(CharacterHandle handle, Attachment attach)	throws IOException {
		wrapped.modifyAttachment(handle, attach);
		BabylonEventBus.fireEvent(BabylonEventType.CHAR_MODIFIED, handle, null, attach, null);
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.character.CharacterProvider#removeAttachment(de.rpgframework.character.CharacterHandle, de.rpgframework.character.Attachment)
	 */
	@Override
	public void removeAttachment(CharacterHandle handle, Attachment attach)	throws IOException {
		wrapped.removeAttachment(handle, attach);
		BabylonEventBus.fireEvent(BabylonEventType.CHAR_MODIFIED, handle, null, null, attach);
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.character.CharacterProvider#addAttachment(de.rpgframework.character.CharacterHandle, de.rpgframework.character.CharacterHandle.Type, de.rpgframework.character.CharacterHandle.Format, java.lang.String, byte[])
	 */
	@Override
	public Attachment addAttachment(CharacterHandle handle, Type type,
			Format format, String filename, byte[] data) throws IOException {
		Attachment attach = wrapped.addAttachment(handle, type, format, filename, data);
		BabylonEventBus.fireEvent(BabylonEventType.CHAR_MODIFIED, handle, attach, null, null);
		return attach;
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.character.CharacterProvider#copyAttachment(de.rpgframework.character.CharacterHandle, de.rpgframework.character.Attachment)
	 */
	@Override
	public Attachment copyAttachment(CharacterHandle handle, Attachment attach)
			throws IOException {
		Attachment attach2 = wrapped.copyAttachment(handle, attach);
		BabylonEventBus.fireEvent(BabylonEventType.CHAR_MODIFIED, handle, attach, null, null);
		return attach2;
	}

}
