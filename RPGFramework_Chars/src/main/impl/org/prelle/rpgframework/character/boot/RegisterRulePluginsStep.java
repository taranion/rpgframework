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
import de.rpgframework.character.CharacterProviderLoader;
import de.rpgframework.character.RulePlugin;

/**
 * @author Stefan
 *
 */
public class RegisterRulePluginsStep implements BootStep {

	private final static Logger logger = LogManager.getLogger("rpgframework.chars");

	//-------------------------------------------------------------------
	public RegisterRulePluginsStep() {
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.boot.BootStep#getID()
	 */
	@Override
	public String getID() {
		return "REGISTER_RULE_PLUGINS";
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.boot.BootStep#getWeight()
	 */
	@Override
	public int getWeight() {
		return 5;
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
	public List<RulePlugin<?>> loadPlugin(Path jarFile) {
		List<RulePlugin<?>> plugins = new ArrayList<>();
		try {
			ClassLoader loader = URLClassLoader.newInstance(new URL[]{jarFile.toUri().toURL()}, PluginRegistry.class.getClassLoader());
			logger.debug(" search for plugins in "+jarFile);
			ServiceLoader.load(RulePlugin.class, loader).forEach(plugin -> {
				Package pack = plugin.getClass().getPackage();
				logger.debug("Found plugin "+plugin.getClass());
				logger.debug("  Implementor: "+pack.getImplementationVendor()+"   Version: "+pack.getImplementationVersion()+"   Name: "+plugin.getReadableName());

				plugins.add(plugin);

			});
		} catch (Throwable e) {
			logger.fatal("Failed loading plugin(s) from "+jarFile,e);
		}

		return plugins;
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.boot.BootStep#execute()
	 */
	@Override
	public boolean execute(RPGFrameworkInitCallback callback) {
		logger.info("START----------Register rule plugins------------------------");
		if (callback!=null) {
			callback.progressChanged(0);
			callback.message("Initialize rule plugins");
		}

		PluginRegistry registry = RPGFrameworkLoader.getInstance().getPluginRegistry();
		CharacterProviderLoader.clearRulePlugins();
		List<PluginDescriptor> installed = registry.getKnownPlugins();
		
		// Add local plugins to classpath
		for (PluginDescriptor pluginDesc : installed) {
			int loaded = 0;
			try {
				for (RulePlugin<?> plugin : loadPlugin(pluginDesc.localFile)) {
					logger.info("  Plugin '"+pluginDesc.name+"' has '"+plugin.getReadableName()+"' for "+plugin.getRules()+" and languages "+plugin.getLanguages());
					CharacterProviderLoader.registerRulePlugin(plugin, pluginDesc);
					loaded++;
				}
			} catch (Exception e) {
				logger.error("Failed loading plugins from "+pluginDesc.localFile,e);
			}
			logger.info("Loaded "+loaded+" plugins from "+pluginDesc.localFile);
		}
		
		callback.progressChanged(1.0);
		return true;
	}

}
