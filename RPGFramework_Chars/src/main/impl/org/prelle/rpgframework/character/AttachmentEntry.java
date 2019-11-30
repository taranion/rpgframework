package org.prelle.rpgframework.character;

import org.prelle.simplepersist.Attribute;

import de.rpgframework.character.CharacterHandle.Type;

public class AttachmentEntry {
	@Attribute(required=true)
	public String file;
	@Attribute(required=false)
	public Type type;
	@Attribute
	public String desc;

	//-------------------------------------------------------------------
	public AttachmentEntry() {}

	//-------------------------------------------------------------------
	public AttachmentEntry(String fname, Type type) {
		this.file = fname;
		this.type = type;
	}
}