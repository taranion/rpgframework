package org.prelle.rpgframework.character;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.rpgframework.ConfigContainer;
import de.rpgframework.ConfigOption;
import de.rpgframework.RPGFrameworkInitCallback;
import de.rpgframework.boot.BootStep;
import de.rpgframework.character.PluginRegistry;

/**
 * @author Stefan
 *
 */
public class ConfigureUpdaterBootStep implements BootStep {

	private final static Logger logger = LogManager.getLogger("babylon.chars");

	//-------------------------------------------------------------------
	public ConfigureUpdaterBootStep(ConfigContainer configRoot2) {
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.boot.BootStep#getID()
	 */
	@Override
	public String getID() {
		return "CONFIGURE_UPDATER";
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.boot.BootStep#getWeight()
	 */
	@Override
	public int getWeight() {
		return 50;
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.boot.BootStep#shallBeDisplayedToUser()
	 */
	@Override
	public boolean shallBeDisplayedToUser() {
		return true; //PluginRegistry.getNumberOfPluginsToLoad()<4;
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.boot.BootStep#getConfiguration()
	 */
	@Override
	public List<ConfigOption<?>> getConfiguration() {
		List<ConfigOption<?>> ret = new ArrayList<ConfigOption<?>>();
		return ret;
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.boot.BootStep#execute()
	 */
	@Override
	public boolean execute(RPGFrameworkInitCallback callback) {
		logger.debug("START----------Configure updater------------------------");
		if (callback!=null) {
			callback.progressChanged(0);
			callback.message("Configure plugin updater");
		}
		
		PluginUpdater.updatePlugins();
		logger.debug("STOP ----------Configured updater------------------------");
		// TODO Auto-generated method stub
		callback.progressChanged(1.0);
		return true;
	}

}
