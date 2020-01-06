package org.prelle.rpgframework.character.boot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.rpgframework.ConfigContainer;
import de.rpgframework.ConfigOption;
import de.rpgframework.ConfigOption.Type;
import de.rpgframework.RPGFrameworkInitCallback;
import de.rpgframework.RPGFrameworkLoader;
import de.rpgframework.boot.BootStep;
import de.rpgframework.character.CharacterProviderLoader;
import de.rpgframework.character.RulePlugin;
import de.rpgframework.character.RulePluginFeatures;
import de.rpgframework.core.RoleplayingSystem;

/**
 * @author Stefan
 *
 */
public class LoadRulePluginsBootStep implements BootStep {

	private final static Logger logger = LogManager.getLogger("rpgframework.chars");

	private List<RulePlugin<?>> rulePlugins;
	private List<RulePlugin<?>> acceptedRulePlugins;
	private ConfigContainer configRoot;
	private ConfigOption<Boolean> cfgAskOnStartup;
	private Map<RoleplayingSystem, ConfigOption<Boolean>> cfgPerRule;

	//-------------------------------------------------------------------
	public LoadRulePluginsBootStep(ConfigContainer configRoot2) {
		this.configRoot = configRoot2;
		rulePlugins = new ArrayList<RulePlugin<?>>();
		acceptedRulePlugins = new ArrayList<RulePlugin<?>>();
		cfgPerRule  = new HashMap<RoleplayingSystem, ConfigOption<Boolean>>();

		this.configRoot = configRoot.createContainer("rules");
		cfgAskOnStartup = configRoot.createOption("askOnStartUp", Type.BOOLEAN, Boolean.TRUE);
	}

	//-------------------------------------------------------------------
	public List<RulePlugin<?>> getRulePlugins() {
		return acceptedRulePlugins;
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.boot.BootStep#getID()
	 */
	@Override
	public String getID() {
		return "ROLEPLAYINGSYSTEMS";
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.boot.BootStep#getWeight()
	 */
	@Override
	public int getWeight() {
		return 40;
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.boot.BootStep#shallBeDisplayedToUser()
	 */
	@Override
	public boolean shallBeDisplayedToUser() {
		return (Boolean)cfgAskOnStartup.getValue();
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.boot.BootStep#getConfiguration()
	 */
	@Override
	public List<ConfigOption<?>> getConfiguration() {
		String searchLang = Locale.getDefault().getLanguage();
		if (!(searchLang.equalsIgnoreCase("de") || searchLang.equalsIgnoreCase("en")))
			searchLang = "en";
		logger.info("Search plugins for language: "+searchLang);

		for (RulePlugin<?> plugin : CharacterProviderLoader.getRulePlugins()) {
			try {
				if (plugin.getLanguages().contains(searchLang))
					rulePlugins.add(plugin);
				else
					logger.warn("Ignore plugin "+plugin.getClass()+" because language "+searchLang+" not supported (only "+plugin.getLanguages()+")");
			} catch (Throwable e) {
				logger.fatal("Error instantiating plugin "+plugin,e);
				e.printStackTrace();
			}
		}
		/*
		 * Now sort plugins. First by roleplaying system, than by features
		 */
		logger.debug("Sort plugins");
		try {
			Collections.sort(rulePlugins, new Comparator<RulePlugin<?>>() {
				public int compare(RulePlugin<?> o1, RulePlugin<?> o2) {
					if (o1.getRules()!=o2.getRules()) {
						return Integer.compare(o1.getRules().ordinal(), o2.getRules().ordinal());
					}
					if (o1.getSupportedFeatures().contains(RulePluginFeatures.PERSISTENCE)) return -1;
					return ((Integer)o1.getRequiredPlugins().size()).compareTo(o2.getRequiredPlugins().size());
				}
			});
		} catch (Throwable e) {
			logger.fatal("Cannot sort plugins: "+e);
			e.printStackTrace();
		}

		/*
		 * Detect all possible RoleplayingSystems
		 */
		List<RoleplayingSystem> rules = new ArrayList<RoleplayingSystem>();
		ConfigContainer perSystem = this.configRoot.createContainer("perSystem");
		for (RulePlugin<?> tmp : rulePlugins) {
			if (!rules.contains(tmp.getRules())) {
				rules.add(tmp.getRules());
				ConfigOption<Boolean> cfgRule = perSystem.createOption(tmp.getRules().name(), Type.BOOLEAN, true);
				cfgRule.setName(tmp.getRules().getName());
				cfgPerRule.put(tmp.getRules(), cfgRule);
			}
		}
		logger.debug("Available roleplaying systems: "+rules);

		List<ConfigOption<?>> ret = new ArrayList<ConfigOption<?>>();
		ret.addAll(cfgPerRule.values());
		Collections.sort(ret, new Comparator<ConfigOption<?>>() {
			public int compare(ConfigOption<?> o1, ConfigOption<?> o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
		ret.add(cfgAskOnStartup);
		return ret;
	}

	//--------------------------------------------------------------------
	private boolean isPluginLoaded(List<RulePlugin<?>> alreadyLoaded, RoleplayingSystem rules, String search) {
		// Find all loaded plugins with the required
		for (RulePlugin<?> loaded : alreadyLoaded) {
			if (loaded.getRules()==rules && loaded.getID().equals(search))
				return true;
		}
		return false;
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.boot.BootStep#execute()
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public boolean execute(RPGFrameworkInitCallback callback) {
		logger.info("START----------Load "+rulePlugins.size()+" rule plugins------------------------");
		if (callback!=null) {
			callback.progressChanged(0);
			callback.message("Initialize rule plugins");
		}


//		Iterator<RulePlugin> it = ServiceLoader.load(RulePlugin.class, RPGFramework.class.getClassLoader()).iterator();
		Iterator<RulePlugin<?>> it = rulePlugins.iterator();
		while (it.hasNext()) {
			try {
				RulePlugin plugin = it.next();
				ConfigOption<Boolean> opt = cfgPerRule.get(plugin.getRules());
				if (opt!=null && opt.getValue()==Boolean.FALSE) {
					logger.warn("Ignore plugin "+plugin.getReadableName()+" / "+plugin.getClass().getSimpleName()+" by user configuration");
					CharacterProviderLoader.unregisterRulePlugin(plugin);
					continue;
				}
				if (callback!=null)
					callback.message("Load "+plugin.getClass());
				logger.info("Found possible rule plugin "+plugin.getClass()+"/"+plugin.hashCode()+"/"+plugin.getClass().hashCode());
				acceptedRulePlugins.add(plugin);
			} catch (Throwable e) {
				logger.fatal("Error instantiating plugin",e);
				e.printStackTrace();
			}
		}
		logger.debug("Found "+acceptedRulePlugins.size()+" possible plugins");

		// All plugins are considered NOT_LOADED until they are
		List<RulePlugin<?>> notLoaded  = new ArrayList<>(acceptedRulePlugins);
		List<RulePlugin<?>> successful = new ArrayList<>();
		// Sort in a way, that plugins without requirements come first
		Collections.sort(notLoaded, new Comparator<RulePlugin<?>>() {
			public int compare(RulePlugin<?> o1, RulePlugin<?> o2) {
				if (o1.getRequiredPlugins()==null || o1.getRequiredPlugins().isEmpty()) {
					if (o2.getRequiredPlugins()!=null || !o2.getRequiredPlugins().isEmpty())
						return -1;
					return 0;
				}
				if (o2.getRequiredPlugins()==null || o2.getRequiredPlugins().isEmpty()) {
					return -1;
				}
				return 0;
			}
		});
		
		
//		// Load Plugins with identifier CORE and feature PERSISTENCE first
//		logger.debug("Load all CORE plugins first");
//		for (RulePlugin<?> plugin : notLoaded) {
//			if (plugin.getID().equals("CORE") && plugin.getSupportedFeatures().contains(RulePluginFeatures.PERSISTENCE)) {
//				Package pack = plugin.getClass().getPackage();
//				try {
//					logger.info("Initialize "+plugin.getClass()+" // "+pack.getImplementationTitle()+" // "+pack.getImplementationVersion());
//					if (callback!=null)
//						callback.message("Initialize CORE plugin "+plugin.getClass().getSimpleName());
//					double percBefore = ((double)successful.size()) / ((double)rulePlugins.size());
//					double percent = ((double)successful.size()) / ((double)rulePlugins.size());
//					plugin.init( (perc) -> {
//						logger.info("    ..."+perc);
//						if (callback!=null) {
//							double relPerc = ((percent-percBefore)/100.0 * perc) + percBefore;
//							callback.progressChanged(relPerc);
//						}
//					});
//					plugin.attachConfigurationTree(RPGFrameworkLoader.getInstance().getPluginConfigurationNode());
//					successful.add(plugin);
//					if (callback!=null)
//						callback.progressChanged(0.5*percent);
//				} catch (Throwable e) {
//					System.err.println("Error loading plugin: "+e);
//					e.printStackTrace();
//					logger.fatal("Error loading plugin: "+e,e);
//					if (callback!=null)
//						callback.errorOccurred("RPGFrameworkLoader", "Error loading plugin "+plugin.getID(), e);
//				}
//			} else
//				logger.debug("  not a CORE plugin: "+plugin.getClass()+"  "+plugin.getID()+" / "+plugin.getSupportedFeatures());
//		}
//		notLoaded.removeAll(successful);
//		logger.debug("Successfully loaded "+successful.size()+" CORE plugins");

		// Now load rest
		boolean changed = false;
		do {
			changed = false;
			outer:
			for (RulePlugin<?> plugin : notLoaded) {
				// Are requirements met
				try {
					for (String search : plugin.getRequiredPlugins()) {
						if (!isPluginLoaded(successful, plugin.getRules(), search)) {
							logger.trace("Cannot load "+plugin.getRules()+"/"+plugin.getID()+" yet, because "+plugin.getRules()+"/"+search+" is missing");
							continue outer;
						}
					}
				} catch (Throwable e) {
					System.err.println("Error determing plugin requirements for "+plugin.getClass()+": "+e);
					e.printStackTrace();
					logger.fatal("Error determing plugin requirements for "+plugin.getClass()+": "+e,e);
				}

				Package pack = plugin.getClass().getPackage();
				logger.debug("Initialize "+plugin.getClass()+" // "+pack.getImplementationTitle()+" // "+pack.getImplementationVersion());
				try {
					if (callback!=null)
						callback.message("Initialize "+plugin.getClass().getSimpleName());
					double percBefore = ((double)successful.size()) / ((double)rulePlugins.size());
					final double percent = ((double)successful.size()+1) / ((double)rulePlugins.size());
					plugin.init( (perc) -> {
						if (callback!=null) {
							double relPerc = ((percent-percBefore) * perc) + percBefore;
							logger.debug("  "+relPerc);
							callback.progressChanged(relPerc);
						}
					});
					plugin.attachConfigurationTree(RPGFrameworkLoader.getInstance().getPluginConfigurationNode());
					changed = true;
					successful.add(plugin);
					double perc2 = ((double)successful.size()) / ((double)rulePlugins.size());
					if (callback!=null)
						callback.progressChanged(perc2);
				} catch (Throwable e) {
					System.err.println("Error loading plugin: "+e);
					e.printStackTrace();
					logger.fatal("Error loading plugin: "+e,e);
					if (callback!=null)
						callback.errorOccurred("RPGFrameworkLoader", "Error loading plugin "+plugin.getID(), e);
				}
			}
			notLoaded.removeAll(successful);
		} while (changed);

		rulePlugins.removeAll(notLoaded);
		for (RulePlugin<?> failed : notLoaded) {
			logger.fatal("Failed loading plugin "+failed.getRules()+"/"+failed.getID()+" // "+failed.getReadableName());
			for (String search : failed.getRequiredPlugins()) {
				if (!isPluginLoaded(successful, failed.getRules(), search)) {
					logger.fatal("Cannot load "+failed.getRules()+"/"+failed.getID()+" yet, because "+failed.getRules()+"/"+search+" is missing");
					if (callback!=null)
						callback.errorOccurred("RPGFrameworkLoader", "Cannot load "+failed.getRules()+"/"+failed.getID()+" yet, because "+failed.getRules()+"/"+search+" is missing", null);
				}
			}
		}
		logger.info("STOP ----------Loaded rule plugins------------------------");
		// TODO Auto-generated method stub
		callback.progressChanged(1.0);
		return true;
	}

}
