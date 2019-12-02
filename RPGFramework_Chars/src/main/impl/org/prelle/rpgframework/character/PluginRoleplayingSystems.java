package org.prelle.rpgframework.character;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.rpgframework.RPGFramework;
import de.rpgframework.RPGFrameworkLoader;
import de.rpgframework.RPGFrameworkLoader.FunctionType;
import de.rpgframework.RPGFrameworkPlugin;
import de.rpgframework.boot.StandardBootSteps;
import de.rpgframework.character.CharacterProviderLoader;

/**
 * @author prelle
 *
 */
public class PluginRoleplayingSystems implements RPGFrameworkPlugin {
	
	private final static Logger logger = LogManager.getLogger("babylon.chars");

	@Override
	public FunctionType getType() {
		return FunctionType.CHARACTERS_AND_RULES;
	}

	@Override
	public void initialize(RPGFramework framework) {
		logger.debug("START: initialize");
		try {
			// Update plugins
			PluginUpdater.updatePlugins();
			// Load plugins
			logger.debug("Set charprov");
			CharacterProviderLoader.setCharacterProvider(new BaseCharacterProviderLight(framework.getConfiguration()));
			
			// Add steps
			LoadRulePluginsBootStep rulePlugins = new LoadRulePluginsBootStep(framework.getConfiguration());
			RPGFrameworkLoader.getInstance().addStepDefinition(StandardBootSteps.ROLEPLAYING_SYSTEMS, rulePlugins);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.fatal("Failed initializing roleplaying systems",e);
		} finally {
			logger.debug("STOP : initialize");
		}
	}

}
