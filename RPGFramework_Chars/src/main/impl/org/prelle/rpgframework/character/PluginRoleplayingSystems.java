package org.prelle.rpgframework.character;

import de.rpgframework.RPGFramework;
import de.rpgframework.RPGFrameworkLoader.FunctionType;
import de.rpgframework.RPGFrameworkPlugin;

/**
 * @author prelle
 *
 */
public class PluginRoleplayingSystems implements RPGFrameworkPlugin {

	@Override
	public FunctionType getType() {
		return FunctionType.CHARACTERS_AND_RULES;
	}

	@Override
	public void initialize(RPGFramework framework) {
		// TODO Auto-generated method stub
		PluginUpdater.updatePlugins();

	}

}
