/**
 *
 */
package de.rpgframework.character;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.prelle.rpgframework.character.BabylonConstants;

import de.rpgframework.PluginDescriptor;
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
	}

	//--------------------------------------------------------------------
	public static boolean knownsPlugin(RulePlugin<?> plugin) {
		List<RulePlugin<?>> list = rulePlugins.get(plugin.getRules());
		if (list==null)
			return false;
		
		for (RulePlugin<?> tmp : list) {
			if (tmp.getClass()==plugin.getClass())
				return true;
		}
		return false;
	}

	//--------------------------------------------------------------------
	public static void registerRulePlugin(RulePlugin<?> plugin, PluginDescriptor descriptor) {
		List<RulePlugin<?>> list = rulePlugins.get(plugin.getRules());
		if (list==null) {
			list = new ArrayList<RulePlugin<?>>();
			rulePlugins.put(plugin.getRules(), list);
		}
		list.add(plugin);
		
		BabylonConstants.logger.debug("Register plugin "+plugin.getClass()+" = "+plugin.getReadableName()+" for rules "+plugin.getRules());
		descriptors.put(plugin, descriptor);
	}

	//--------------------------------------------------------------------
	public static void clearRulePlugins() {
		rulePlugins.clear();
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

	//--------------------------------------------------------------------
	public static void unregisterRulePlugin(RulePlugin<?> plugin) {
		List<RulePlugin<?>> list = rulePlugins.get(plugin.getRules());
		if (list==null) {
			return;
		}
		list.remove(plugin);
		if (list.isEmpty())
			rulePlugins.remove(plugin.getRules());
		
		BabylonConstants.logger.debug("Unregister plugin "+plugin.getClass()+" = "+plugin.getReadableName()+" for rules "+plugin.getRules());
	}
	
}
