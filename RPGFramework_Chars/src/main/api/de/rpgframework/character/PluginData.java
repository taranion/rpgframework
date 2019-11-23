/**
 * 
 */
package de.rpgframework.character;

import java.util.ResourceBundle;

/**
 * @author Stefan
 *
 */
public interface PluginData {

	//-------------------------------------------------------------------
	public String getId();

	//-------------------------------------------------------------------
	public void setPlugin(RulePlugin<?> plugin);
	
	//-------------------------------------------------------------------
	public RulePlugin<?> getPlugin();

	//-------------------------------------------------------------------
	public ResourceBundle getResourceBundle();

	//-------------------------------------------------------------------
	/**
	 * Get the resource bundle for longer descriptions - those that
	 * are able to make the harcopy obsolete.
	 * May be null
	 */
	public ResourceBundle getHelpResourceBundle();
	
}
