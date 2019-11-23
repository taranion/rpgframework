/**
 * 
 */
package de.rpgframework.addressbook;

import java.util.Collection;

/**
 * @author prelle
 *
 */
public interface Identity extends Comparable<Identity> {
	
	//--------------------------------------------------------------------
	/**
	 * The name under which this identity shall be presented
	 * @return
	 */
	public String getDisplayName();
	
	//--------------------------------------------------------------------
	public Collection<IdentityInformation> getFields();
	
	//--------------------------------------------------------------------
	/**
	 * Get the first element matching this type
	 */
	public String get(InfoType type);
	
	//--------------------------------------------------------------------
	public String get(InfoType type, Scope scope);
	
	//--------------------------------------------------------------------
	public byte[] getAvatar();
	
}
