/**
 * 
 */
package org.prelle.rpgframework.boot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.module.ModuleDescriptor.Version;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.prelle.rpgframework.BabylonConstants;
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
public class IgnoreNotMatchingRemotePlugins implements BootStep {
	
	private final static Logger logger = BabylonConstants.logger;
	
	private Version frameworkVersion;
	private PluginRegistryImpl registry;

	//-------------------------------------------------------------------
	public IgnoreNotMatchingRemotePlugins(Version frameworkVersion, PluginRegistryImpl registry) {
		this.frameworkVersion = frameworkVersion;
		this.registry = registry;
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.boot.BootStep#getID()
	 */
	@Override
	public String getID() {
		return "IGNORE_REMOTE";
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.boot.BootStep#getWeight()
	 */
	@Override
	public int getWeight() {
		return 1;
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
		if (frameworkVersion==null)
			return true;
		
		for (PluginDescriptor descriptor : registry.getKnownRemotePlugins()) {
			if (descriptor.minVersion!=null && descriptor.minVersion.compareTo(frameworkVersion)>0) {
				logger.info("  Ignore remote plugin "+descriptor.filename+" because required minimum version "+descriptor.minVersion+" not provided");
				registry.unregisterRemote(descriptor);
				continue;
			}
			if (descriptor.maxVersion!=null && descriptor.maxVersion.compareTo(frameworkVersion)<0) {
				logger.info("  Ignore available plugin "+descriptor.filename+" because required maximum version "+descriptor.maxVersion+" not provided");
				registry.unregisterRemote(descriptor);
				continue;
			}
			
		}
		
		return true;
	}

}
