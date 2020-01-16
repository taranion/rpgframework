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
public class CollectKnownRemotePluginsStep implements BootStep {
	
	private final static String CONFIG_FILENAME = "config.txt";
	
	private final static Logger logger = LogManager.getLogger("rpgframework");
	
	private Path installDir;
	private Path pluginDir;
	private Version frameworkVersion;
	private PluginRegistryImpl registry;

	//-------------------------------------------------------------------
	public CollectKnownRemotePluginsStep(Version frameworkVersion, PluginRegistryImpl registry) {
		this.frameworkVersion = frameworkVersion;
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
		return "COLLECT_KNOWN_PLUGINS";
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.boot.BootStep#getWeight()
	 */
	@Override
	public int getWeight() {
		return 10;
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
		String profile = System.getProperty("profile", "development").toLowerCase();
		
		// Get list of plugins that could be downloaded
		List<URL> updateURLs = getUpdateURLs(profile);
		List<PluginDescriptor> available = getAvailablePlugins(updateURLs);
		logger.info("Found "+available.size()+" downloadable plugins");

		for (PluginDescriptor plugin : registry.getKnownRemotePlugins()) {
			logger.info("  Remote Plugin "+plugin.location+"  Min="+plugin.minVersion+"  Max="+plugin.maxVersion);
		}
		
		return true;
	}
	
	//-------------------------------------------------------------------
	private List<URL> getUpdateURLs(String releaseState) {
		List<URL> ret = new ArrayList<>();
		
		try {
			URL builtInURL = new URL(BabylonConstants.builtInUpdateURL+releaseState);
			ret.add(builtInURL);
		} catch (MalformedURLException e) {
			logger.fatal("Failed initializing built-in update URL: "+BabylonConstants.builtInUpdateURL+releaseState+"\n"+e);
		}

		/*
		 * Load remaining URLs from the file 'config.txt' in the plugins
		 * directory
		 */
		Path configFile = pluginDir.resolve(CONFIG_FILENAME);
		if (Files.exists(configFile)) {
			try {
				for (String url_s : Files.readAllLines(configFile)) {
					try {
						URL builtInURL = new URL(url_s);
						ret.add(builtInURL);
					} catch (MalformedURLException e) {
						logger.error("Failed initializing built-in update URL: "+url_s+"\n"+e);
					}
				}
			} catch (IOException e) {
				logger.error("Error reading update URLs from "+configFile+": "+e);
			}
		}
		
		// Debug output
		if (logger.isDebugEnabled()) {
			for (URL url : ret) {
				logger.debug("Update URL found: "+url);
			}
		}
		
		return ret;
	}
	
	//-------------------------------------------------------------------
	private static int getFileSize(URL url) throws IOException {
		HttpURLConnection con = (HttpURLConnection)url.openConnection();
		con.setReadTimeout(5000);
		con.setConnectTimeout(1000);
		con.setRequestMethod("HEAD");
		con.getResponseCode();
		return con.getContentLength();
	}
	
	//-------------------------------------------------------------------
	private List<? extends PluginDescriptor> getPluginsAt(URL updateURL) {
		List<PluginDescriptor> ret = new ArrayList<>();
		logger.info("Contact download site: "+updateURL);
		try {
			HttpURLConnection con = (HttpURLConnection)updateURL.openConnection();
			con.setReadTimeout(5000);
			con.setConnectTimeout(1000);
			int code = con.getResponseCode();
			if (code!=200) {
				logger.fatal("Failed contacting update server "+updateURL+": "+code+" "+con.getResponseMessage());
				return ret;
			}
			List<String> doc =
				      new BufferedReader(new InputStreamReader(con.getInputStream(),
				          StandardCharsets.UTF_8)).lines().collect(Collectors.toList());
			if (logger.isTraceEnabled()) {			
				for (String line : doc)
					logger.trace(line);
			}
			
			class JarAndManifest {
				URL jar;
				URL manifest;
				String filename;
			}
			Map<String,JarAndManifest> map = new HashMap<>();
			Pattern pattern = Pattern.compile(".*href=\"([^\"]*)\">.*");
			for (String foo : doc) {
				Matcher matcher = pattern.matcher(foo);
				if (!matcher.matches())
					continue;
				String filename = matcher.group(1);
				if (!filename.toLowerCase().endsWith(".jar") && !filename.toLowerCase().endsWith(".mf"))
					continue;
				String basename = filename.substring(0, filename.lastIndexOf("."));
				JarAndManifest entry = map.get(basename);
				if (entry==null) {
					entry = new JarAndManifest();
					map.put(basename, entry);
				}
				if (filename.toLowerCase().endsWith(".jar")) {
					entry.jar = new URL(updateURL+"/"+filename);
					entry.filename = filename;
				} else if (filename.toLowerCase().endsWith(".mf"))
					entry.manifest = new URL(updateURL+"/"+filename);
			}
			// List found entries
			for (Entry<String, JarAndManifest> entry : map.entrySet()) {
				logger.trace("Found "+entry.getKey()+" : jar="+entry.getValue().jar+"   Manifest="+entry.getValue().manifest);
				
				JarAndManifest tmp = entry.getValue();
				if (tmp.jar!=null && tmp.manifest!=null) {
					PluginDescriptor descriptor = registry.downloadAndParsePluginDescriptor(tmp.manifest);
					// Check that descriptor has been successfully loaded and parsed
					if (descriptor==null) {
						logger.error("Failed reading plugin descriptor from "+tmp.manifest);
						continue;
					}
					descriptor.filename = tmp.filename;
					descriptor.location = tmp.jar;
					descriptor.fileSize = getFileSize(descriptor.location);
					logger.debug("  Found "+descriptor);
					
					ret.add(descriptor);
					
					if (descriptor.uuid!=null) {
						registry.registerRemote(descriptor.uuid, descriptor);
					} 
						
				}
			}
			
			
		} catch (IOException e) {
			logger.error("Error downloading from "+updateURL+": "+e);
		}
		return ret;
	}
	
	//-------------------------------------------------------------------
	private List<PluginDescriptor> getAvailablePlugins(List<URL> updateURLs) {
		List<PluginDescriptor> ret = new ArrayList<>();
		for (URL updateURL : updateURLs) {
			ret.addAll(getPluginsAt(updateURL));
		}
		
		return ret;
	}

}
