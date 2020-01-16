/**
 * 
 */
package org.prelle.rpgframework.boot;

import java.io.IOException;
import java.net.URL;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.prelle.rpgframework.PluginRegistryImpl;

import de.rpgframework.ConfigOption;
import de.rpgframework.ExitCodes;
import de.rpgframework.PluginDescriptor;
import de.rpgframework.RPGFrameworkConstants;
import de.rpgframework.RPGFrameworkInitCallback;
import de.rpgframework.boot.BootStep;

/**
 * Contact all download servers and build a list of usable plugins -
 * each only in its most recent possible version.
 * 
 * @author prelle
 *
 */
public class CollectKnownLocalPluginsStep implements BootStep {
	
	private final static Logger logger = LogManager.getLogger("rpgframework");
	
	private Path installDir;
	private Path pluginDir;
	private PluginRegistryImpl registry;

	//-------------------------------------------------------------------
	public CollectKnownLocalPluginsStep(PluginRegistryImpl registry) {
		this.registry = registry;
		installDir = getInstallationDirectory();
		logger.info("Installation directory: "+installDir);
		
		pluginDir  = installDir.resolve("plugins");
	}
	
	//-------------------------------------------------------------------
	private Path getInstallationDirectory() {
		if (System.getProperty(RPGFrameworkConstants.PROPERTY_INSTALLATION_DIRECTORY)==null) {
			logger.fatal("System Property '"+RPGFrameworkConstants.PROPERTY_INSTALLATION_DIRECTORY+"' not set by main application");
			System.exit(ExitCodes.ERROR_INIT_PHASE);
		}
		
		return Paths.get(System.getProperty(RPGFrameworkConstants.PROPERTY_INSTALLATION_DIRECTORY));
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.boot.BootStep#getID()
	 */
	@Override
	public String getID() {
		return "COLLECT_KNOWN_LOCAL_PLUGINS";
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
		return new ArrayList<ConfigOption<?>>();
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.boot.BootStep#execute(de.rpgframework.RPGFrameworkInitCallback)
	 */
	@Override
	public boolean execute(RPGFrameworkInitCallback callback) {
		getLocallyAvailablePlugins();

		for (PluginDescriptor plugin : registry.getKnownPlugins()) {
			logger.info("  Local Plugin "+plugin.localFile);
		}
		
		return true;
	}
	
	//-------------------------------------------------------------------
	private List<PluginDescriptor> getLocallyAvailablePlugins() {
		List<PluginDescriptor> ret = new ArrayList<>();
		try {
			// Ensure directory exists
			if (!Files.exists(pluginDir)) {
				logger.debug("Plugin directory missing - create "+pluginDir);
				try {
					Files.createDirectories(pluginDir);
				} catch (AccessDeniedException e) {
					logger.warn("Failed creating plugin directory: Access denied");
					pluginDir = Path.of(System.getProperty("user.home"), "plugins");
					if (!Files.exists(pluginDir)) {
						Files.createDirectories(pluginDir);
					}
				}
			} else if (!Files.isWritable(pluginDir)) {
				pluginDir = Path.of(System.getProperty("user.home"), "plugins");
				if (!Files.exists(pluginDir)) {
					Files.createDirectories(pluginDir);
				}
			}
			logger.debug("Get list of already installed plugins in "+pluginDir);
			
			Files.newDirectoryStream(pluginDir, "*.jar").forEach(jarPath -> {
				logger.debug("JAR "+jarPath);
				try {
					URL url = jarPath.toUri().toURL();
					PluginDescriptor descr = registry.getPluginInfo(url);
					descr.fileSize = (int) jarPath.toFile().length();
					descr.localFile= jarPath;
					descr.filename = jarPath.getFileName().toString();
					ret.add(descr);
					if (descr.uuid!=null) {
						registry.registerLocal(descr.uuid, descr);
						logger.debug("  Found installed "+descr.filename);
					} else {
						logger.fatal("  Found installed "+descr.filename+" but cannot register it without UUID\n\n");
					}
				} catch (Exception e) {
					logger.error("Error parsing "+jarPath,e);
				}
			});
		} catch (IOException e) {
			logger.fatal("Failed loading plugins",e);
		}
		
		return ret;
	}

}
