/**
 * 
 */
package de.rpgframework;

import java.util.PropertyResourceBundle;
import java.util.prefs.Preferences;

import de.rpgframework.ConfigOption.Type;

/**
 * @author prelle
 *
 */
public interface ConfigContainer extends ConfigNode, Iterable<ConfigNode> {

	//-------------------------------------------------------------------
	/**
	 * @param id  Local id (not full path)
	 * @return
	 */
	public ConfigOption<?> getOption(String id);

	//-------------------------------------------------------------------
	/**
	 * @param id  Local id (not full path)
	 * @return
	 */
	public ConfigNode getChild(String id);

	//-------------------------------------------------------------------
	public <T> ConfigOption<T> createOption(String id, Type type, T defValue);

	//-------------------------------------------------------------------
	public ConfigContainer createContainer(String id);

	//-------------------------------------------------------------------
	public void removeChild(String id);

	//-------------------------------------------------------------------
	public void addListener(ConfigChangeListener callback);

	//--------------------------------------------------------------------
	/**
	 * Inform all listeners of changes done in this container. This is the
	 * preferred way instead of automatically notifying them for every single
	 * option changed.
	 */
	public void fireConfigChange();

	//--------------------------------------------------------------------
	public void setResourceBundle(PropertyResourceBundle bundle);

	//--------------------------------------------------------------------
	public void changePreferences(Preferences pref);
	
}
