/**
 *
 */
package de.rpgframework.character;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.rpgframework.core.RoleplayingSystem;

/**
 * @author prelle
 *
 */
public class CharacterProviderLoader {
	
	private static CharacterProvider charProv;
	private static Map<RoleplayingSystem, RulePlugin<?>> rulePlugins = new HashMap<RoleplayingSystem, RulePlugin<?>>();

	//--------------------------------------------------------------------
	public static CharacterProvider getCharacterProvider() {
			return charProv;
	}

	//--------------------------------------------------------------------
	public static void setCharacterProvider(CharacterProvider service) {
		charProv = service;
	}

	//--------------------------------------------------------------------
	public static void registerRulePlugin(RulePlugin<?> plugin) {
		if (rulePlugins.containsKey(plugin.getRules()))
			throw new IllegalStateException("Already registered a plugin for "+plugin.getRules());
		rulePlugins.put(plugin.getRules(), plugin);
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
	
}
