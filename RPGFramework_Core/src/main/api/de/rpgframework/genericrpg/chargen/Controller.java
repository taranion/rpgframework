/**
 * 
 */
package de.rpgframework.genericrpg.chargen;

import java.util.List;

import de.rpgframework.ConfigOption;
import de.rpgframework.genericrpg.ToDoElement;

/**
 * @author prelle
 *
 */
public interface Controller {
	
	//-------------------------------------------------------------------
	/**
	 * Return configurable settings for this controller
	 */
	public List<ConfigOption<?>> getConfigOptions();
	
	//-------------------------------------------------------------------
	/**
	 * Returns a list of steps to do in this controller
	 */
	public List<ToDoElement> getToDos();

}
