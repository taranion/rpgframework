/**
 *
 */
package de.rpgframework.character;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;

import de.rpgframework.core.RoleplayingSystem;

/**
 * @author prelle
 *
 */
public class CharacterProviderLoader {
	
	private static CharacterProvider charProv;
	private static Map<RoleplayingSystem, List<RulePlugin<?>>> rulePlugins = new HashMap<RoleplayingSystem, List<RulePlugin<?>>>();
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
		List<RulePlugin<?>> list = rulePlugins.get(plugin.getRules());
		if (list==null) {
			list = new ArrayList<RulePlugin<?>>();
			rulePlugins.put(plugin.getRules(), list);
		}
		list.add(plugin);
		
//		if (rulePlugins.containsKey(plugin.getRules()))
//			throw new IllegalStateException("Already registered a plugin for "+plugin.getRules()+": "+rulePlugins.get(plugin.getRules()));
		LogManager.getLogger("babylon.chars").debug("Register plugin "+plugin.getClass()+" = "+plugin.getReadableName());
//		rulePlugins.put(plugin.getRules(), plugin);
		descriptors.put(plugin, descriptor);
	}

	//--------------------------------------------------------------------
	public static Collection<RulePlugin<?>> getRulePlugins() {
		List<RulePlugin<?>> ret = new ArrayList<RulePlugin<?>>();
		rulePlugins.values().forEach( list -> ret.addAll(list));
		return ret;
	}

	//--------------------------------------------------------------------
	public static Collection<RulePlugin<?>> getRulePlugins(RoleplayingSystem rules) {
		return rulePlugins.get(rules);
	}

	//--------------------------------------------------------------------
	public static PluginDescriptor getPluginDescriptor(RulePlugin<?> plugin) {
		return descriptors.get(plugin);
	}
	
}
