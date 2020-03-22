/**
 * 
 */
package de.rpgframework.character;

import java.io.InputStream;
import java.util.Collection;
import java.util.List;

//import de.rpgframework.core.CommandBusListener;
import de.rpgframework.core.RoleplayingSystem;

/**
 * You need to implement this class to extend the functionality of RPGFramework
 * applications for specific rulesets.
 * 
 * 
 * @author prelle
 *
 */
public interface RulePlugin<C extends RuleSpecificCharacterObject> { //extends CommandBusListener {
	
	public interface RulePluginProgessListener { 
		public void progressChanged(double percent);
	}
	
	public class PluginKey {
		String id;
		RoleplayingSystem rules;
	}
	
	
	//--------------------------------------------------------------------
	/**
	 * Returns an identifier uniquely identifying the plugin within
	 * a RoleplayingSystem. The identifier "CORE" is reserved for the
	 * core rules. For everything else an abbreviated product name
	 * in capital letters is recommended.
	 */
	public String getID();
	
	//--------------------------------------------------------------------
	/**
	 * Identify the type of rules to support.
	 */
	public RoleplayingSystem getRules();
	
	//--------------------------------------------------------------------
	/**
	 * Returns the IDs of plugins that are required to use this plugin.
	 * Ambigious IDs need to be interpreted within the same roleplaying system.
	 */
	public Collection<String> getRequiredPlugins();
	
	//--------------------------------------------------------------------
	/**
	 * Inform the framework of the supported features by this plugin.
	 */
	public Collection<RulePluginFeatures> getSupportedFeatures();
	
	//--------------------------------------------------------------------
	/**
	 * Start the plugin. This method must not block.
	 */
	public void init(RulePluginProgessListener callback);
	
	//--------------------------------------------------------------------
	/**
	 * Return a HTML document giving information about the plugin and given
	 * credits to third party resources.
	 */
	public InputStream getAboutHTML();

	//--------------------------------------------------------------------
	/**
	 * Return a list of supported language codes
	 */
	public List<String> getLanguages();

//	//--------------------------------------------------------------------
//	/**
//	 * Attach configuration tree underneath the given container
//	 */
//	public void attachConfigurationTree(ConfigContainer addBelow);
//
//	//--------------------------------------------------------------------
//	/**
//	 * Return a list (better: tree) of configurable for actions performed
//	 * by this plugin (e.g. print options)
//	 */
//	public List<ConfigOption<?>> getConfiguration();
	
}
