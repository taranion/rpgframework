package org.prelle.rpgframework.character;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.AccessDeniedException;
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
import org.prelle.rpgframework.character.PluginRegistry.UpdateResult;

import de.rpgframework.ExitCodes;
import de.rpgframework.RPGFrameworkConstants;
import de.rpgframework.character.CharacterProviderLoader;
import de.rpgframework.character.PluginDescriptor;
import de.rpgframework.character.RulePlugin;

/**
 * @author Stefan Prelle
 *
 */
public class PluginUpdater {
	
	private final static String CONFIG_FILENAME = "config.txt";
	
	private final static Logger logger = LogManager.getLogger("rpgframework");

	private static Path installDir;
	private static Path pluginDir;

	//-------------------------------------------------------------------
	static {
		installDir = getInstallationDirectory();
		logger.info("Installation directory: "+installDir);
		
		pluginDir  = installDir.resolve("plugins");
	}
	
	//-------------------------------------------------------------------
	private static Path getInstallationDirectory() {
		if (System.getProperty(RPGFrameworkConstants.PROPERTY_INSTALLATION_DIRECTORY)==null) {
			logger.fatal("System Property '"+RPGFrameworkConstants.PROPERTY_INSTALLATION_DIRECTORY+"' not set by main application");
			System.exit(ExitCodes.ERROR_INIT_PHASE);
		}
		
			return Paths.get(System.getProperty(RPGFrameworkConstants.PROPERTY_INSTALLATION_DIRECTORY));
//		CodeSource codeSource = PluginUpdater.class.getProtectionDomain().getCodeSource();
//		URL codeSourceURL = codeSource.getLocation();
//		if (codeSourceURL.getProtocol().equals("file")) {
//			// Started from JAR
//			try {
//				Path ret = Paths.get(codeSourceURL.toURI()).getParent().getParent();
//				return ret;
//			} catch (URISyntaxException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		} else if (codeSourceURL.getProtocol().equals("jrt")) {
//			// Started a)s runtime image
//			Path ret = Paths.get(System.getProperty("java.home")).getParent().getParent();
//			return ret;
//		}
//		
//		System.err.println("Cannot detect installation directory");
//		System.exit(ExitCodes.UNKNOWN_INSTALL_LOCATION);
//		return null;
	}
	
	//-------------------------------------------------------------------
	private static List<URL> getUpdateURLs(String releaseState) {
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
	private static List<? extends PluginDescriptor> getPluginsAt(URL updateURL) {
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
					PluginDescriptor descriptor = PluginRegistry.downloadAndParsePluginDescriptor(tmp.manifest);
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
				}
			}
			
			
		} catch (IOException e) {
			logger.error("Error downloading from "+updateURL+": "+e);
		}
		return ret;
	}
	
	//-------------------------------------------------------------------
	private static List<PluginDescriptor> getAvailablePlugins(List<URL> updateURLs) {
		List<PluginDescriptor> ret = new ArrayList<>();
		for (URL updateURL : updateURLs) {
			ret.addAll(getPluginsAt(updateURL));
		}
		
		return ret;
	}
	
	//-------------------------------------------------------------------
	private static List<PluginDescriptor> getLocallyAvailablePlugins() {
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
					PluginDescriptor descr = PluginRegistry.getPluginInfo(url);
					descr.fileSize = (int) jarPath.toFile().length();
					descr.localFile= jarPath;
					logger.debug("  Found installed "+descr);
					ret.add(descr);
				} catch (Exception e) {
					logger.error("Error parsing "+jarPath,e);
				}
			});
		} catch (IOException e) {
			logger.fatal("Failed loading plugins",e);
		}
		
		return ret;
	}

	//-------------------------------------------------------------------
	private static void downloadPlugins(List<PluginDescriptor> toDownload) {
		logger.debug("downloadPlugins: "+toDownload.size());
		ThreadGroup tgDownloads = new ThreadGroup("DownloadUpdates");
		List<Thread> threads = new ArrayList<Thread>();
		for (PluginDescriptor desc : toDownload) {
			Thread thread = new Thread(tgDownloads, () -> {
				try {
					HttpURLConnection con = (HttpURLConnection) desc.location.openConnection();
					if (con.getResponseCode()!=200) {
						desc.result = UpdateResult.FAILED;
						logger.error("Update failed for "+desc.name+" - downloading returned code "+con.getResponseCode());
						return;
					}
					Path downloadFile = pluginDir.resolve(desc.filename+".update");
					Path destFile = pluginDir.resolve(desc.filename);
					Path backupFile = pluginDir.resolve(desc.filename+".backup");
					// Delete eventually existing file
					Files.deleteIfExists(downloadFile);
					
					logger.debug("  Download "+desc.location+" to "+downloadFile);
					Files.copy(con.getInputStream(), downloadFile);
					con.getInputStream().close();
					// Verify downloaded file
					if (downloadFile.toFile().length()!=desc.fileSize) {
						logger.warn("Download okay, but filesize does not match");
						desc.result = UpdateResult.VERIFICATION_FAILED;
						return;
					}
					// Rename previous
					Files.deleteIfExists(backupFile);
					// Backup previous file
					if (Files.exists(destFile))
						Files.move(destFile, backupFile);
					// Mark downloaded as new regular
					Files.move(downloadFile, destFile);
					logger.info("  Successfully updated  "+desc.filename+" = "+desc.name);
					desc.result = UpdateResult.UPDATED;
				} catch (IOException e) {
					desc.result = UpdateResult.FAILED;
					logger.error("Update failed for "+desc.name+" - IOException",e);
				}
			}, "Update-"+desc.name);
			threads.add(thread);
		}
		
		// Start all updates
		threads.forEach( thread -> thread.start());
		// Wait for  all downloads to finish
		threads.forEach( thread -> {
			try {
				thread.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}

	//-------------------------------------------------------------------
	public static void updatePlugins() {
		List<URL> updateURLs = getUpdateURLs("development");
		List<PluginDescriptor> available = getAvailablePlugins(updateURLs);
		logger.info("Found "+available.size()+" downloadable plugins");
		
		List<PluginDescriptor> installed = getLocallyAvailablePlugins();
		logger.info("Found "+installed.size()+" installed plugins");
		
		// TODO: Update plugins
		downloadPlugins(available);
		
		// Add local plugins to classpath
		for (PluginDescriptor pluginDesc : installed) {
			try {
				for (RulePlugin<?> plugin : PluginRegistry.loadPlugin(pluginDesc.localFile)) {
					logger.info("Plugin '"+pluginDesc.name+"' has '"+plugin.getReadableName()+"' for "+plugin.getRules()+" and languages "+plugin.getLanguages());
					CharacterProviderLoader.registerRulePlugin(plugin, pluginDesc);
				}
			} catch (Exception e) {
				logger.error("Failed loading plugins from "+pluginDesc.localFile,e);
			}
		}
	}

}
