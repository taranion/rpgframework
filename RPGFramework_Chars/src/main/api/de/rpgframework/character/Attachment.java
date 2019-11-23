package de.rpgframework.character;

import java.util.Date;

import de.rpgframework.character.CharacterHandle.Format;
import de.rpgframework.character.CharacterHandle.Type;

/**
 * This class stores a specific kind of data associated with a character.
 * 
 * 
 * @author prelle
 *
 */
public class Attachment implements Comparable<Attachment> {
	
	protected Type type;
	protected Format format;
	protected byte[] data;
	// May be required for some types and formats
	protected Object parsed;
	protected Date modified;
	protected String filename;
	
	//-------------------------------------------------------------------
	public Attachment(Type type, Format format, String filename) {
		this.type = type;
		this.format = format;
		this.filename = filename;
	}
	
	//-------------------------------------------------------------------
	public Type getType() {	return type; }
	public void setType(Type type) {this.type = type;}
	public Format getFormat() {	return format; }
	public void setFormat(Format format) { this.format = format; }
	public byte[] getData() {	return data; }
	
	//-------------------------------------------------------------------
	/**
	 * Change the data within this attachment. Remember to call
	 * {@link de.rpgframework.character.CharacterProvider#modifyAttachment(CharacterHandle, Attachment)}
	 * to save the new data to the storage.
	 * 
	 * @param data
	 * @see de.rpgframework.character.CharacterProvider#modifyAttachment(CharacterHandle, Attachment)
	 */
	public void setData(byte[] data) { this.data = data; }
	public Date getLastModified() {	return modified; }
	public void setLastModified(Date date) { modified = date; }

	//-------------------------------------------------------------------
	public String toString() {
		return type+" "+format+"("+filename+")";
	}

	//-------------------------------------------------------------------
	/**
	 * @return the filename
	 */
	public String getFilename() {
		return filename;
	}
	//-------------------------------------------------------------------
	/**
	 * @param filename the filename to set
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	//-------------------------------------------------------------------
	/**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Attachment other) {
		int comp = type.compareTo(other.getType());
		if (comp!=0) return comp;
		comp = format.compareTo(other.getFormat());
		if (comp!=0) return comp;
				
		return filename.compareTo(other.getFilename());
	}

	//-------------------------------------------------------------------
	/**
	 * @return the parsed
	 */
	public Object getParsed() {
		return parsed;
	}

	//-------------------------------------------------------------------
	/**
	 * This may be used to store data of your liking - e.g. a {@link RuleSpecificCharacterObject}
	 * for the parsed data of a type=CHARACTER, format=RULESPECIFIC attachment.
	 * @param parsed Some user data
	 */
	public void setUserData(Object parsed) {
		this.parsed = parsed;
	}
	
}