package de.rpgframework.character;

import java.util.Date;

public interface Attachment {
	
	public enum Format {
		RULESPECIFIC,
		RULESPECIFIC_EXTERNAL,
		HTML,
		TEXT,
		PDF,
		JSON,
		BBCODE,
		IMAGE
	}
	
	public enum Type {
		CHARACTER,
		BACKGROUND,
		REPORT,
	}

	//-------------------------------------------------------------------
	public Type getType();
	public void setType(Type type);

	//-------------------------------------------------------------------
	public Format getFormat();
	public void setFormat(Format format);

	//-------------------------------------------------------------------
	public byte[] getData();

	//-------------------------------------------------------------------
	/**
	 * Change the data within this attachment. Remember to call
	 * {@link de.rpgframework.character.CharacterProvider#modifyAttachment(CharacterHandle, Attachment)}
	 * to save the new data to the storage.
	 * 
	 * @param data
	 * @see de.rpgframework.character.CharacterProvider#modifyAttachment(CharacterHandle, Attachment)
	 */
	public void setData(byte[] data);

	//-------------------------------------------------------------------
	public Date getLastModified();
	public void setLastModified(Date date);

	//-------------------------------------------------------------------
	public String getFilename();
	public void setFilename(String filename);

	//-------------------------------------------------------------------
	/**
	 * @return the parsed
	 */
	public Object getParsed();

	//-------------------------------------------------------------------
	/**
	 * This may be used to store data of your liking - e.g. a {@link RuleSpecificCharacterObject}
	 * for the parsed data of a type=CHARACTER, format=RULESPECIFIC attachment.
	 * @param parsed Some user data
	 */
	public void setUserData(Object parsed);

}