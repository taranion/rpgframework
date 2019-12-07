/**
 *
 */
package de.rpgframework.character;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;

import de.rpgframework.core.RoleplayingSystem;

/**
 * @author prelle
 *
 */
public class CharacterProviderLoader {
	
	private static CharacterProvider charProv;
	private static Map<RoleplayingSystem, RulePlugin<?>> rulePlugins = new HashMap<RoleplayingSystem, RulePlugin<?>>();
	private static Map<RulePlugin<?>, PluginDescriptor> descriptors = new HashMap<RulePlugin<?>, PluginDescriptor>();

	//--------------------------------------------------------------------
	public static CharacterProvider getCharacterProvider() {
			return charProv;
	}

	//--------------------------------------------------------------------
	public static void setCharacterProvider(CharacterProvider service) {
		charProv = service;
		LogManager.getLogger("babylon.chars").debug("*********************************************Set CharacterProvider "+service);
	}

	//--------------------------------------------------------------------
	public static void registerRulePlugin(RulePlugin<?> plugin, PluginDescriptor descriptor) {
		if (rulePlugins.containsKey(plugin.getRules()))
			throw new IllegalStateException("Already registered a plugin for "+plugin.getRules());
		LogManager.getLogger("babylon.chars").debug("Register plugin "+plugin.getClass()+" = "+plugin.getReadableName());
		rulePlugins.put(plugin.getRules(), plugin);
		descriptors.put(plugin, descriptor);
	}

	//--------------------------------------------------------------------
	public static Collection<RulePlugin<?>> getRulePlugins() {
		return rulePlugins.values();
	}

	//--------------------------------------------------------------------
	@SuppressWarnings("unchecked")
	public static Collection<RulePlugin<?>> getRulePlugins(RoleplayingSystem rules) {
		return (Collection<RulePlugin<?>>) rulePlugins.get(rules);
	}

	//--------------------------------------------------------------------
	public static PluginDescriptor getPluginDescriptor(RulePlugin<?> plugin) {
		return descriptors.get(plugin);
	}
	
}
