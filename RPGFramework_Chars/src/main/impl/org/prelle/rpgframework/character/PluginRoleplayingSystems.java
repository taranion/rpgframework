package org.prelle.rpgframework.character;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.rpgframework.RPGFramework;
import de.rpgframework.RPGFrameworkLoader.FunctionType;
import de.rpgframework.RPGFrameworkPlugin;

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
		} finally {
			logger.debug("STOP : initialize");
		}
	}

}
