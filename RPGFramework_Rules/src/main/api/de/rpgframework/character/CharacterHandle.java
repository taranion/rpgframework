/**
 * 
 */
package de.rpgframework.character;

import java.util.Date;
import java.util.UUID;

import de.rpgframework.core.RoleplayingSystem;

/**
 * @author prelle
 *
 */
public class CharacterHandle {
	
	protected UUID uuid;
	protected RoleplayingSystem rules;
	protected String name;
	protected Date lastModified;

	//-------------------------------------------------------------------
	public String getName() {
		return name;
	}

	//-------------------------------------------------------------------
	public RoleplayingSystem getRuleIdentifier() {
		return rules;
	}
	
	//-------------------------------------------------------------------
	public Date getLastModified() {
		return lastModified;
	}
	
	//-------------------------------------------------------------------
	public UUID getUUID() {
		return uuid;
	}

	//-------------------------------------------------------------------
	public void setName(String value) {
		this.name= value;
	}
	
}
