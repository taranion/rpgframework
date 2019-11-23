/**
 * 
 */
package de.rpgframework.products;

import de.rpgframework.core.RoleplayingSystem;

/**
 * You need to implement this class to extend the functionality of RPGFramework
 * applications for specific rulesets.
 * 
 * 
 * @author prelle
 *
 */
public interface DataPlugin {
	
	//--------------------------------------------------------------------
	/**
	 * Returns an identifier uniquely identifying the plugin within
	 * a RoleplayingSystem - e.g. a year
	 */
	public String getID();
	
	//--------------------------------------------------------------------
	/**
	 * Identify the type of rules to support.
	 */
	public RoleplayingSystem getRules();
	
	//--------------------------------------------------------------------
	/**
	 * Start the plugin. This method must not block.
	 */
	public void init();
	
}
