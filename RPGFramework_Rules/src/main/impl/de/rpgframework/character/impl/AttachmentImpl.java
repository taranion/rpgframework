package de.rpgframework.character.impl;

import java.util.Date;

import de.rpgframework.character.Attachment;

/**
 * This class stores a specific kind of data associated with a character.
 * 
 * 
 * @author prelle
 *
 */
public class AttachmentImpl implements Comparable<Attachment>, Attachment {
	
	protected Type type;
	protected Format format;
	protected byte[] data;
	// May be required for some types and formats
	protected Object parsed;
	protected Date modified;
	protected String filename;
	
	//-------------------------------------------------------------------
	public AttachmentImpl(Type type, Format format, String filename) {
		this.type = type;
		this.format = format;
		this.filename = filename;
	}
	
	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.character.Attachment#getType()
	 */
	@Override
	public Type getType() {	return type; }
	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.character.Attachment#setType(de.rpgframework.character.CharacterHandle.Type)
	 */
	@Override
	public void setType(Type type) {this.type = type;}
	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.character.Attachment#getFormat()
	 */
	@Override
	public Format getFormat() {	return format; }
	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.character.Attachment#setFormat(de.rpgframework.character.CharacterHandle.Format)
	 */
	@Override
	public void setFormat(Format format) { this.format = format; }
	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.character.Attachment#getData()
	 */
	@Override
	public byte[] getData() {	return data; }
	
	//-------------------------------------------------------------------
	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.character.Attachment#setData(byte[])
	 */
	@Override
	public void setData(byte[] data) { this.data = data; }
	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.character.Attachment#getLastModified()
	 */
	@Override
	public Date getLastModified() {	return modified; }
	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.character.Attachment#setLastModified(java.util.Date)
	 */
	@Override
	public void setLastModified(Date date) { modified = date; }

	//-------------------------------------------------------------------
	public String toString() {
		return type+" "+format+"("+filename+")";
	}

	//-------------------------------------------------------------------
	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.character.Attachment#getFilename()
	 */
	@Override
	public String getFilename() {
		return filename;
	}
	//-------------------------------------------------------------------
	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.character.Attachment#setFilename(java.lang.String)
	 */
	@Override
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
	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.character.Attachment#getParsed()
	 */
	@Override
	public Object getParsed() {
		return parsed;
	}

	//-------------------------------------------------------------------
	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.character.Attachment#setUserData(java.lang.Object)
	 */
	@Override
	public void setUserData(Object parsed) {
		this.parsed = parsed;
	}
	
}