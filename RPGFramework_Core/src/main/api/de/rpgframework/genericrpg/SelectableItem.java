/**
 * 
 */
package de.rpgframework.genericrpg;

/**
 * 
 * @author prelle
 *
 */
public interface SelectableItem {
	
	//-------------------------------------------------------------------
	/**
	 * A name to display to the user
	 * @return
	 */
	public String getName();
	
	//-------------------------------------------------------------------
	/**
	 * A short identifier for the type of object (e.g. 'item' or 'skill')
	 * @return
	 */
	public String getTypeId();
	
	//-------------------------------------------------------------------
	/**
	 * An internal identifier for the item - valid within its type scope
	 * @return
	 */
	public String getId();

}
