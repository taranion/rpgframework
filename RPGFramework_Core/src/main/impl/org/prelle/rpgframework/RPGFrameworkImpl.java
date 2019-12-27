/**
 *
 */
package org.prelle.rpgframework;

import java.lang.module.ModuleDescriptor.Version;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.prefs.Preferences;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.prelle.rpgframework.boot.CollectKnownLocalPluginsStep;
import org.prelle.rpgframework.boot.CollectKnownRemotePluginsStep;
import org.prelle.rpgframework.boot.ConfigureUpdaterBootStep;
import org.prelle.rpgframework.boot.IgnoreNotMatchingRemotePlugins;
import org.prelle.rpgframework.boot.KeepNewestRemotePluginStep;
import org.prelle.rpgframework.boot.LoadFrameworkPluginsBootStep;

import de.rpgframework.ConfigContainer;
import de.rpgframework.ConfigOption;
import de.rpgframework.PluginRegistry;
import de.rpgframework.RPGFramework;
import de.rpgframework.RPGFrameworkInitCallback;
import de.rpgframework.RPGFrameworkLoader;
import de.rpgframework.RPGFrameworkPlugin;
import de.rpgframework.StringConverter;
import de.rpgframework.boot.BootStep;
import de.rpgframework.boot.StandardBootSteps;
import de.rpgframework.core.CustomDataHandlerLoader;
import de.rpgframework.core.LicenseManager;

/**
 * @author prelle
 *
 */
public class RPGFrameworkImpl implements RPGFramework {

	private final static Logger logger = LogManager.getLogger("rpgframework");

	private ConfigContainerImpl configRoot;
	private ConfigOption<Locale> cfgLanguage;
	private ConfigOption<String> cfgDataDir;
	private ConfigContainer cfgPlugins;

	//	private FunctionCharacterAndRules charAndRules;
	//	private FunctionSessionManagement sessionManagement;
	//	private FunctionMediaLibraries    mediaLibraries;
	private LicenseManager license;

	private Map<StandardBootSteps, BootStep> stepDefinitions;
	private List<StandardBootSteps> requestedSteps;
	private List<BootStep> customSteps;
	private double percentStart = 0;
	private PluginRegistryImpl pluginRegistry;

	//--------------------------------------------------------------------
	public static void main(String[] args) {
		new RPGFrameworkImpl();
	}

	//--------------------------------------------------------------------
	public RPGFrameworkImpl() {
		requestedSteps = new ArrayList<>();
		stepDefinitions = new HashMap<>();
		customSteps = new ArrayList<>();
		try {
			prepareConfigNode();
		} catch (Exception e) {
			logger.fatal("Cannot initialize configuration",e);
			System.exit(1);
		}
		license = new LicenseManagerImpl();
		pluginRegistry = new PluginRegistryImpl();

		// Ensure a data directory is set
		logger.info("dataDir = "+cfgDataDir.getValue());
		if (cfgDataDir.getValue()==null) {
			logger.error("Missing key '"+PROP_DATADIR+"' in user preferences "+PREFERENCE_PATH);
			logger.error("Should open user dialog here and ask, but for now use default");
			cfgDataDir.set(System.getProperty("user.home")+System.getProperty("file.separator")+"rpgframework");
		}
		if (cfgDataDir.getValue()==null) {
			logger.fatal("Missing key '"+PROP_DATADIR+"' in user preferences "+PREFERENCE_PATH);
			logger.fatal(System.getProperty("user.home")+System.getProperty("file.separator")+"rpgframework");
			System.exit(0);
		}

		// Set language
		Locale lang = (Locale)cfgLanguage.getValue();
		if (lang!=null) {
			logger.info("User chosen locale = "+lang);
			Locale.setDefault(lang);
		}

		RPGFrameworkLoader.setInstance(this);
	}

	//------------------------------------------------------------------
	private void prepareConfigNode() {
		Preferences PREF = Preferences.userRoot().node(PREFERENCE_PATH);

		configRoot = new ConfigContainerImpl(PREF, "babylon");
		cfgLanguage = configRoot.createOption(PROP_LANGUAGE, ConfigOption.Type.CHOICE, Locale.getDefault());
		cfgLanguage.setOptions(Locale.GERMAN, Locale.ENGLISH);
		cfgLanguage.setValueConverter(new StringConverter<Locale>() {
			public String toString(Locale val) { return val.getLanguage();}
			public Locale fromString(String val) { return new Locale(val); }
		});
		cfgDataDir  = configRoot.createOption(PROP_DATADIR , ConfigOption.Type.DIRECTORY,
				System.getProperty("user.home")+System.getProperty("file.separator")+"rpgframework");
		//		cfgRuleLimit= configRoot.createOption(PROP_RULE_LIMIT , ConfigOption.Type.MULTI_CHOICE, null);
		//		cfgRuleLimit.setOptions(RoleplayingSystem.SPLITTERMOND, RoleplayingSystem.SHADOWRUN, RoleplayingSystem.SPACE1889);
		//		cfgRuleLimit.setValueConverter(new StringConverter<RoleplayingSystem>() {
		//			public String toString(RoleplayingSystem val) { return val.getName();}
		//			public RoleplayingSystem fromString(String val) { return RoleplayingSystem.valueOf(val); }
		//		});
		//		configRoot.createOption(PROP_UPDATE_ASK , ConfigOption.Type.BOOLEAN, Boolean.TRUE);
		//		configRoot.createOption(PROP_UPDATE_RUN , ConfigOption.Type.BOOLEAN, Boolean.TRUE);
		cfgPlugins  = configRoot.createContainer("plugins");
	}

	//--------------------------------------------------------------------
	/**
	 * @see de.rpgframework.RPGFramework#getConfiguration()
	 */
	@Override
	public ConfigContainer getConfiguration() {
		return configRoot;
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.RPGFramework#getPluginConfigurationNode()
	 */
	@Override
	public ConfigContainer getPluginConfigurationNode() {
		return cfgPlugins;
	}

	//-------------------------------------------------------------------
	@Override
	public void addStepDefinition(StandardBootSteps def, BootStep step) {
		logger.debug("  Register "+step+" as "+def);
		stepDefinitions.put(def, step);
	}

	//-------------------------------------------------------------------
	@Override
	public void addBootStep(StandardBootSteps stepDef) {
		requestedSteps.add(stepDef);
	}

	//-------------------------------------------------------------------
	@Override
	public void addBootStep(BootStep step) {
		customSteps.add(step);
	}

	//-------------------------------------------------------------------
	/**
	 * Find out which version of RPGFramework has been provided
	 */
	private static Version detectFrameworkVersion() {
		Version frameworkVersion = null;
		Optional<Version> version = RPGFramework.class.getModule().getDescriptor().version();
		if (version.isPresent()) {
			frameworkVersion = version.get();
		} else
			frameworkVersion = Version.parse("4.0.0-SNAPSHOT");
		logger.info("Framework version is "+frameworkVersion);
		return frameworkVersion;
	}
	
	//-------------------------------------------------------------------
	private void initializeFrameworkPlugins() {
		List<RPGFrameworkPlugin> frameworkPlugins = new ArrayList<RPGFrameworkPlugin>();

		Iterator<RPGFrameworkPlugin> it = ServiceLoader.load(RPGFrameworkPlugin.class, RPGFramework.class.getClassLoader()).iterator();
		while (it.hasNext()) {
			try {
				RPGFrameworkPlugin plugin = it.next();
				logger.debug("   Found framework plugin "+plugin.getClass());
				frameworkPlugins.add(plugin);
			} catch (Throwable e) {
				logger.fatal("   Error instantiating plugin",e);
				e.printStackTrace();
			}
		}

		/*
		 * Now sort plugins. First by roleplaying system, than by features
		 */
		logger.debug("   Sort plugins");
		Collections.sort(frameworkPlugins, new Comparator<RPGFrameworkPlugin>() {
			public int compare(RPGFrameworkPlugin o1, RPGFrameworkPlugin o2) {
				return o1.getClass().getName().compareTo(o2.getClass().getName());
			}
		});
		for (RPGFrameworkPlugin plugin : frameworkPlugins) {
			logger.debug("   Initialize "+plugin.getClass()+" // "+plugin.getClass().getPackage().getImplementationTitle());
			try {
				plugin.initialize(this);
			} catch (Throwable e) {
				System.err.println("Error loading plugin: "+e);
				logger.log(Level.FATAL, "Error loading plugin",e);
			}
		}

	}

	//-------------------------------------------------------------------
	/**
	 * Initializes the framework
	 *
	 * Progress phase:
	 *   0.00 - 0.50 Rule plugins
	 *   0.50 - 0.75 Framework plugins
	 *   0.75 - 1.00 This framework initialization
	 * @see de.rpgframework.RPGFramework#initialize()
	 */
	@Override
	public void initialize(RPGFrameworkInitCallback callback) {
		logger.debug("START: initialize");
		try {

			// Configure CustomDataHandler
			CustomDataHandlerLoader.setInstance(new CustomDataHandlerImpl(configRoot));

			if (RPGFrameworkLoader.getCallback()!=null) RPGFrameworkLoader.getCallback().message("Initialize framework");
			//		if (RPGFrameworkLoader.getCallback()!=null) RPGFrameworkLoader.getCallback().progressChanged(0.75);
			//		addStepDefinition(StandardBootSteps.FRAMEWORK_PLUGINS, new LoadFrameworkPluginsBootStep(this));

			pluginRegistry.init(getConfiguration().createContainer("rules"));

			List<BootStep> bootSteps = new ArrayList<BootStep>();
			
			/*
			 * 1. Boot steps to make before contacting extensions.
			 *    These steps can't be disabled
			 */
			logger.info("1. Add minimal boot steps");
			Version frameworkVersion = detectFrameworkVersion();
			bootSteps.add(new CollectKnownLocalPluginsStep(pluginRegistry));
			bootSteps.add(new CollectKnownRemotePluginsStep(frameworkVersion, pluginRegistry));
			bootSteps.add(new IgnoreNotMatchingRemotePlugins(frameworkVersion, pluginRegistry));
			bootSteps.add(new KeepNewestRemotePluginStep(pluginRegistry));
//			bootSteps.add(new LoadFrameworkPluginsBootStep(this));
			
			/*
			 * 2. Add optional boot steps that can be selected by the application
			 */
			logger.info("2. Add optional boot steps");
			addStepDefinition(StandardBootSteps.CONFIGURE_UPDATER, new ConfigureUpdaterBootStep(getConfiguration()));
			
			/*
			 * 3. Load all framework plugins
			 */
			logger.info("3. Load framework plugins");
			initializeFrameworkPlugins();
//			logger.info("1. Load framework plugins");
////			(new LoadFrameworkPluginsBootStep(this)).execute(callback);
//			ConfigureUpdaterBootStep updater = new ConfigureUpdaterBootStep(getConfiguration());
//			RPGFrameworkLoader.getInstance().addStepDefinition(StandardBootSteps.CONFIGURE_UPDATER, updater);

			/*
			 * Add custom boot steps
			 */
			logger.info("4. Custom boot steps");
			bootSteps.addAll(customSteps);

			/*
			 * Translate Bootstep definitions to Bootstep implementations
			 */
			logger.info("5. Translate Bootstep definitions to Bootstep implementation");
			for (StandardBootSteps stepDef : requestedSteps) {
				BootStep step = stepDefinitions.get(stepDef);
				if (step==null) {
					logger.warn("   No support for boot step "+stepDef);
				} else {
					logger.debug("   Use "+step+" as "+stepDef);
					bootSteps.add(step);
				}
			}

			/*
			 * Calculate total weight
			 */
			logger.info("6. Calculate total weight");
			double totalWeight = 0;
			for (BootStep step : bootSteps) {
				logger.debug("   +"+step.getWeight()+" for step "+step);
				totalWeight += step.getWeight();
			}
			logger.debug("   Total weight of steps is "+totalWeight);

			/*
			 * Execute plugins
			 */
			logger.debug("4. Execute Bootsteps = "+bootSteps);
			int sum = 0;
			for (BootStep step : bootSteps) {
				percentStart = ((double)sum) / totalWeight;
				if (callback!=null)
					callback.progressChanged(percentStart);
				logger.info(String.format("At %2f %% call %s", percentStart, step.getClass()));
				double percentPerPlugin = ((double)step.getWeight())/totalWeight;

				try {
					/*
					 * Check if BootStep shall be presented to user
					 */
					if (step.shallBeDisplayedToUser()) {
						logger.info("  a) Display options to user from "+step.getClass());
						if (callback!=null)
							callback.showConfigOptions(step.getID(), step.getConfiguration());
						logger.debug("  b) Decisions");
						for (ConfigOption<?> opt : step.getConfiguration()) {
							logger.debug("   * "+opt.getLocalId()+" = "+opt.getValue());
						}

					}

					RPGFrameworkInitCallback listener = new RPGFrameworkInitCallback() {
						public void progressChanged(double value) {
							if (callback!=null)
								callback.progressChanged(percentStart + percentPerPlugin*value);
							else
								logger.debug("Load progress: "+(percentStart + percentPerPlugin*value)*100);
						}
						public void message(String mess) {
							if (callback!=null)
								callback.message(mess);
							else
								logger.debug("Load message: "+mess);
						}
						public void errorOccurred(String location, String detail, Throwable exception) {
							if (callback!=null)
								callback.errorOccurred(location, detail, exception);
							else
								logger.debug("Load error: "+location+" / "+detail);
						}
						@Override
						public void showConfigOptions(String id, List<ConfigOption<?>> configuration) {
							logger.debug("showConfigOptions: "+id+" / "+configuration);
						}
					};
					step.execute(listener);
				} catch (Exception e) {
					logger.error("Error in step "+step,e);
				}
				sum += step.getWeight();
				percentStart = ((double)sum) / totalWeight;
				logger.info(String.format("At %2f %% finished %s", percentStart, step.getClass()));
				callback.progressChanged(1.0);
			}
		} catch (Exception e) {
			logger.fatal("Error configuring RPGFramework",e);
		} finally {
			logger.debug("STOP : initialize");
		}
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.RPGFramework#getLicenseManager()
	 */
	@Override
	public LicenseManager getLicenseManager() {
		return license;
	}

	//-------------------------------------------------------------------
	@Override
	public PluginRegistry getPluginRegistry() {
		return pluginRegistry;
	}

	//	//-------------------------------------------------------------------
	//	/**
	//	 * @see de.rpgframework.RPGFramework#getCharacterAndRules()
	//	 */
	//	@Override
	//	public FunctionCharacterAndRules getCharacterAndRules() {
	//		return charAndRules;
	//	}
	//
	//	//-------------------------------------------------------------------
	//	public void setCharacterAndRules(FunctionCharacterAndRules value) {
	//		this.charAndRules = value;
	//	}
	//
	//	//-------------------------------------------------------------------
	//	/**
	//	 * @see de.rpgframework.RPGFramework#getMediaLibraries()
	//	 */
	//	@Override
	//	public FunctionMediaLibraries getMediaLibraries() {
	//		return mediaLibraries;
	//	}
	//
	//	//-------------------------------------------------------------------
	//	public void setMediaLibraries(FunctionMediaLibraries value) {
	//		this.mediaLibraries = value;
	//	}
	//
	//	//-------------------------------------------------------------------
	//	/**
	//	 * @see de.rpgframework.RPGFramework#getSessionManagement()
	//	 */
	//	@Override
	//	public FunctionSessionManagement getSessionManagement() {
	//		if (sessionManagement==null) {
	//			return new FunctionSessionManagement() {
	//				public Webserver getWebserver() {return null;}
	//				public SocialNetworkProvider getSocialNetworkProvider() {return null;}
	//				public SessionService getSessionService() {return null;}
	//				public SessionScreen getSessionScreen() {return null;}
	//				public PlayerService getPlayerService() {return null;}
	//				public DeviceService getDeviceService() {return null;}
	//				public CharacterProviderFull getCharacterService() {return null;}
	//			};
	//		}
	//		return sessionManagement;
	//	}
	//
	//	//-------------------------------------------------------------------
	//	public void setSessionManagement(FunctionSessionManagement value) {
	//		this.sessionManagement = value;
	//	}

}
