package org.prelle.rpgframework.character.boot;

import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.rpgframework.ConfigOption;
import de.rpgframework.PluginDescriptor;
import de.rpgframework.PluginRegistry;
import de.rpgframework.RPGFrameworkInitCallback;
import de.rpgframework.RPGFrameworkLoader;
import de.rpgframework.boot.BootStep;
import de.rpgframework.boot.StandardBootSteps;
import de.rpgframework.character.CharacterProviderLoader;
import de.rpgframework.character.RulePlugin;

/**
 * @author Stefan
 *
 */
public class LoadCharactersStep implements BootStep {

	private final static Logger logger = LogManager.getLogger("rpgframework.chars");

	//-------------------------------------------------------------------
	public LoadCharactersStep() {
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.boot.BootStep#getID()
	 */
	@Override
	public String getID() {
		return StandardBootSteps.CHARACTERS.name();
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.boot.BootStep#getWeight()
	 */
	@Override
	public int getWeight() {
		return 20;
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.boot.BootStep#shallBeDisplayedToUser()
	 */
	@Override
	public boolean shallBeDisplayedToUser() {
		return false;
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
		if (callback!=null) {
			callback.progressChanged(0);
			callback.message("Collect characters");
		}

		CharacterProviderLoader.getCharacterProvider().getMyCharacters();
		
		callback.progressChanged(1.0);
		return true;
	}

}
