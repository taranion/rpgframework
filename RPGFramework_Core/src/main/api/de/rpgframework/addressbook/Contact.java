/**
 * 
 */
package de.rpgframework.addressbook;

import java.net.URI;
import java.util.Collection;
import java.util.Map.Entry;


/**
 * This kind of identity is used to describe persons.
 * 
 * @author prelle
 *
 */
public interface Contact extends Identity {
	
	//--------------------------------------------------------------------
	public Collection<URI> getIDs();
	
	//--------------------------------------------------------------------
	public void addID(URI id);
	
	//--------------------------------------------------------------------
	public void removeID(URI id);
	
	//--------------------------------------------------------------------
	public boolean containsID(URI id);
	
	//--------------------------------------------------------------------
	public void set(InfoType type, String value);
	
	//--------------------------------------------------------------------
	@SuppressWarnings("unchecked")
	public void set(InfoType type, String value, Entry<Parameter, String>... param);
	
	//--------------------------------------------------------------------
	public void setAvatar(byte[] image);
	
	//--------------------------------------------------------------------
	/**
	 * Add or overwrite a contact field
	 */
	public void addField(IdentityInformation field);
	
}
