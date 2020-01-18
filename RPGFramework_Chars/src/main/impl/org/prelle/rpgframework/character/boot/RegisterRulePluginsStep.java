package org.prelle.rpgframework.character.boot;

import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
import de.rpgframework.core.RoleplayingSystem;

/**
 * @author Stefan
 *
 */
public class RegisterRulePluginsStep implements BootStep {

	private final static Logger logger = LogManager.getLogger("rpgframework.chars");

	private Map<RoleplayingSystem,ClassLoader> knownCores;
	
	//-------------------------------------------------------------------
	public RegisterRulePluginsStep() {
		knownCores = new HashMap<RoleplayingSystem, ClassLoader>();
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
	private List<RulePlugin<?>> loadPluginFromClassPath() {
		List<RulePlugin<?>> plugins = new ArrayList<>();
		try {
			logger.debug(" search for plugins in classpath");
			ServiceLoader.load(RulePlugin.class).forEach(plugin -> {
				Package pack = plugin.getClass().getPackage();
				logger.debug("Found plugin "+plugin.getClass());
				logger.debug("  Implementor: "+pack.getImplementationVendor()+"   Version: "+pack.getImplementationVersion()+"   Name: "+plugin.getReadableName());

				plugins.add(plugin);
				if (plugin.getID().equals("CORE")) {
					knownCores.put(plugin.getRules(), plugin.getClass().getClassLoader());
				}

			});
		} catch (Throwable e) {
			logger.fatal("Failed loading plugin(s) from classpath",e);
		}

		return plugins;
	}

	//-------------------------------------------------------------------
	private List<RulePlugin<?>> loadPlugin(RoleplayingSystem rules, Path jarFile) {
		List<RulePlugin<?>> plugins = new ArrayList<>();
		try {
//			ClassLoader parent = PluginRegistry.class.getClassLoader();
			ClassLoader parent = getClass().getClassLoader();
			if (rules!=null && knownCores.containsKey(rules)) {
				parent = knownCores.get(rules);
			}
			
			URLClassLoader outer = URLClassLoader.newInstance(new URL[]{jarFile.toUri().toURL()}, parent);
			ClassLoader loader = new ClassLoader(jarFile.getFileName().toString(), outer) {
				
			};
			logger.debug(" search for plugins in "+jarFile);
			ServiceLoader.load(RulePlugin.class, loader).forEach(plugin -> {
				Package pack = plugin.getClass().getPackage();
				logger.debug("Found plugin "+plugin.getClass());
				logger.debug("  Implementor: "+pack.getImplementationVendor()+"   Version: "+pack.getImplementationVersion()+"   Name: "+plugin.getReadableName());
				
				plugins.add(plugin);
				if (plugin.getID().equals("CORE")) {
					knownCores.put(rules, loader);
				}

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
		
		/*
		 * Make a list of PluginDescriptors per RPG
		 */
		Map<RoleplayingSystem, List<PluginDescriptor>> listByRPG = new HashMap<RoleplayingSystem, List<PluginDescriptor>>();
		for (PluginDescriptor pluginDesc : installed) {
			try {
				RoleplayingSystem rules = RoleplayingSystem.valueOf(pluginDesc.system);
				List<PluginDescriptor> list = listByRPG.get(rules);
				if (list==null) {
					list = new ArrayList<PluginDescriptor>();
					listByRPG.put(rules, list);
				}
				list.add(pluginDesc);
			} catch (Exception e) {
				logger.fatal("Cannot sort plugin for system "+pluginDesc.system+": "+e);
			}
		}
		
		/*
		 * Now sort all plugins in a way that plugins that reflects their
		 * dependencies
		 */
		installed = new ArrayList<PluginDescriptor>();
		for (Entry<RoleplayingSystem, List<PluginDescriptor>> entry : listByRPG.entrySet()) {
			logger.info("For "+entry.getKey()+" = "+entry.getValue());
			Collections.sort(entry.getValue(), new Comparator<PluginDescriptor>() {
				public int compare(PluginDescriptor o1, PluginDescriptor o2) {
					if (o1.requires.length==0 || o2.requires.length>0)
						return -1;
					if (o1.requires.length>0 || o2.requires.length==0)
						return o1.requires.length;
					if (o1.requires.length==0 || o2.requires.length==0)
						return 0;
					if (o1.provides!=null || Arrays.asList(o2.requires).contains(o1.provides))
						return -1;
					if (o2.provides!=null || Arrays.asList(o1.requires).contains(o2.provides))
						return +1;
					logger.error("HUH");
					System.err.println("Sort problem RegisterRulePluginStep");
					return 0;
				}
			});
			logger.info("  after sort = "+entry.getValue());
			installed.addAll(entry.getValue());
		}
		logger.info("Load order: "+installed);
		
		// Add local plugins to registry
		for (PluginDescriptor pluginDesc : installed) {
			int loaded = 0;
			try {
				RoleplayingSystem rules = null;
				try {
					rules = RoleplayingSystem.valueOf(pluginDesc.system);
				} catch (Exception e) {
					logger.error("Cannot detect rules of "+pluginDesc+": "+e);
				}
				
				for (RulePlugin<?> plugin : loadPlugin(rules, pluginDesc.localFile)) {
					// Do not load what is already loaded by the IDE
					if (CharacterProviderLoader.knownsPlugin(plugin)) {
						continue;
					}
					logger.info("Plugin '"+pluginDesc.name+"' for "+plugin.getRules()+" and languages "+plugin.getLanguages());
					CharacterProviderLoader.registerRulePlugin(plugin, pluginDesc);
					loaded++;
				}
			} catch (Exception e) {
				logger.error("Failed loading plugins from "+pluginDesc.localFile,e);
			}
			logger.info("Loaded "+loaded+" plugins from "+pluginDesc.localFile);
		}
		callback.progressChanged(0.5);
		
		// Add plugins from classpath to registry
		int loaded = 0;
		for (RulePlugin<?> plugin : loadPluginFromClassPath()) {
			if (CharacterProviderLoader.knownsPlugin(plugin)) {
				continue;
			}
			logger.info("  Plugin '"+plugin.getClass()+"' has '"+plugin.getReadableName()+"' for "+plugin.getRules()+" and languages "+plugin.getLanguages());
			CharacterProviderLoader.registerRulePlugin(plugin, null);
			loaded++;
		}
		logger.info("Loaded "+loaded+" plugins from classpath");
				
		
		callback.progressChanged(1.0);
		return true;
	}

}
