/**
 * 
 */
package de.rpgframework.addressbook;

import java.util.Map.Entry;
import java.util.Set;

/**
 * @author prelle
 *
 */
public interface IdentityInformation {
	
	//--------------------------------------------------------------------
	public String getValue();
	
	//-------------------------------------------------------------------
	public void setValue(String value);
	
	//--------------------------------------------------------------------
	public InfoType getType();

	//--------------------------------------------------------------------
	/**
	 * Get the value of a specific parameter
	 */
	public String getParameter(Parameter param);

	//--------------------------------------------------------------------
	public Set<Entry<Parameter, String>> getParameters();

	//--------------------------------------------------------------------
	public void setParameter(Parameter param, String value);
	
}
