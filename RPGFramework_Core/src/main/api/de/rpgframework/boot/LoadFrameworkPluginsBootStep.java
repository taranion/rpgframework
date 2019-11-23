/**
 *
 */
package de.rpgframework.boot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.rpgframework.ConfigOption;
import de.rpgframework.RPGFramework;
import de.rpgframework.RPGFrameworkInitCallback;
import de.rpgframework.RPGFrameworkPlugin;

/**
 * @author Stefan
 *
 */
public class LoadFrameworkPluginsBootStep implements BootStep {

	private final static Logger logger = LogManager.getLogger("rpgframework");

	private RPGFramework framework;

	public LoadFrameworkPluginsBootStep(RPGFramework instance) {
		this.framework = instance;
	}

	/* (non-Javadoc)
	 * @see de.rpgframework.boot.BootStep#getID()
	 */
	@Override
	public String getID() {
		// TODO Auto-generated method stub
		return "FRAMEWORK_PLUGINS";
	}

	/* (non-Javadoc)
	 * @see de.rpgframework.boot.BootStep#getWeight()
	 */
	@Override
	public int getWeight() {
		// TODO Auto-generated method stub
		return 20;
	}

	/* (non-Javadoc)
	 * @see de.rpgframework.boot.BootStep#shallBeDisplayedToUser()
	 */
	@Override
	public boolean shallBeDisplayedToUser() {
		return false;
	}

	/* (non-Javadoc)
	 * @see de.rpgframework.boot.BootStep#getConfiguration()
	 */
	@Override
	public List<ConfigOption<?>> getConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see de.rpgframework.boot.BootStep#execute(de.rpgframework.RPGFrameworkInitCallback)
	 */
	@Override
	public boolean execute(RPGFrameworkInitCallback callback) {
		logger.debug("START: execute()--------------------------------------------");
		if (callback!=null)
			callback.message("Load framework plugins");

		List<RPGFrameworkPlugin> frameworkPlugins = new ArrayList<RPGFrameworkPlugin>();

		Iterator<RPGFrameworkPlugin> it = ServiceLoader.load(RPGFrameworkPlugin.class, RPGFramework.class.getClassLoader()).iterator();
		while (it.hasNext()) {
			try {
				RPGFrameworkPlugin plugin = it.next();
				logger.info("Found framework plugin "+plugin.getClass());
				frameworkPlugins.add(plugin);
			} catch (Throwable e) {
				logger.fatal("Error instantiating plugin",e);
				e.printStackTrace();
			}
		}

		/*
		 * Now sort plugins. First by roleplaying system, than by features
		 */
		logger.debug("Sort plugins");
		Collections.sort(frameworkPlugins, new Comparator<RPGFrameworkPlugin>() {
			public int compare(RPGFrameworkPlugin o1, RPGFrameworkPlugin o2) {
				return o1.getClass().getName().compareTo(o2.getClass().getName());
			}
		});
		for (RPGFrameworkPlugin plugin : frameworkPlugins) {
			logger.debug("Initialize "+plugin.getClass()+" // "+plugin.getClass().getPackage().getImplementationTitle());
			try {
				if (callback!=null)
					callback.message("Initialize "+plugin.getClass().getSimpleName());
				plugin.initialize(framework);
			} catch (Throwable e) {
				System.err.println("Error loading plugin: "+e);
				logger.log(Level.FATAL, "Error loading plugin",e);
			}
		}
		return true;
	}

}
