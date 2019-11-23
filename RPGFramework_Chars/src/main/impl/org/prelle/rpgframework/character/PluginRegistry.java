/**
 * 
 */
package org.prelle.rpgframework.character;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ServiceLoader;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.rpgframework.ExitCodes;
import de.rpgframework.character.RulePlugin;

/**
 * @author spr
 *
 */
public class PluginRegistry {

	public static enum PluginState {
		ALPHA,
		BETA,
		ACTIVE,
		ABANDONED,
	}
	
	public static enum UpdateResult {
		UPDATED,
		FAILED,
		VERIFICATION_FAILED
	}

	static class PluginDescriptor {
		public URL location;
		public String filename;
		public String name;
		public String vendor;
		public String version;
		public Instant timestamp;
		public int fileSize;
		public PluginState state;
		public URL homepage;
		public URL bugtracker;
		public String toString() {
			return "Plugin(filename="+filename+", name="+name+", vendor="+vendor+", version="+version+", size="+fileSize+", time="+timestamp+", state="+state+")";
		}
		public transient Path localFile; 
		public transient UpdateResult result;
	}

	private final static Logger logger = LogManager.getLogger("rpgframework");

	private static Path pluginDir;
	private static List<URL> updateServer;
	private static Proxy proxy;

	private static Map<String,Boolean> pluginEnableStates;

	//-------------------------------------------------------------------
	public static void init(Path directory) {
		pluginDir = directory;
		logger.info("Expecting plugins in "+directory);
		logger.info("Download plugins from "+updateServer);

		pluginEnableStates = new HashMap();
	}

	//-------------------------------------------------------------------
	public static Path getBaseDirectory() {
		return pluginDir;
	}

	//-------------------------------------------------------------------
	private static List<Path> getJarFiles() throws IOException {
		List<Path> jarFiles = new ArrayList<>();
		Files.newDirectoryStream(pluginDir).forEach(foo -> { 
			logger.info("Found plugin JAR: "+foo);
			jarFiles.add(foo);
		});

		return jarFiles;
	}

	//-------------------------------------------------------------------
	public static PluginDescriptor getPluginInfo(URL url) throws IOException {
		PluginDescriptor ret = new PluginDescriptor();
		ret.location = url;

		logger.trace("Read PluginDescriptor from URL "+url+" using proxy "+proxy);
		URLConnection con = (proxy!=null)?url.openConnection(proxy):url.openConnection();
		con.setConnectTimeout(2000);
		con.setReadTimeout(5000);
		ZipInputStream stream = new ZipInputStream(con.getInputStream());
		ret.fileSize = con.getContentLength();

		do {
			ZipEntry entry = stream.getNextEntry();
			if (entry==null)
				break;
			if (entry.getName().equals("META-INF/MANIFEST.MF")) {
				byte[] buf = stream.readAllBytes();
				ByteArrayInputStream bins = new ByteArrayInputStream(buf);
				ret = parsePluginDescriptor(bins);
				bins.close();
				return ret;
			}
		} while (true);

		return ret;
	}

	//-------------------------------------------------------------------
	public static List<RulePlugin<?>> loadPlugin(Path jarFile) {
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
		} catch (Exception e) {
			logger.fatal("Failed loading plugin(s) from "+jarFile,e);
		}

		return plugins;
	}

	//-------------------------------------------------------------------
	public static PluginDescriptor downloadAndParsePluginDescriptor(URL url) throws IOException {
		logger.trace("load plugin manifest "+url+"  and proxy "+proxy);
		URLConnection con = ((proxy!=null)?url.openConnection(proxy):url.openConnection());
		logger.trace("  last modified = "+con.getLastModified());
		return parsePluginDescriptor(con.getInputStream());
	}

	//-------------------------------------------------------------------
		public static List<PluginDescriptor> getDownloadablePlugins() {
			List<PluginDescriptor> plugins = new ArrayList<>();

			class TempEntry {
				URL jarURL;
				PluginDescriptor desc;
			}

			// Get directory listing
			Map<String, TempEntry> listing = new HashMap<String, TempEntry>();

			/*
			 * For every configured download URL
			 */
			for (URL url : updateServer) {
				logger.debug("check "+url);
				try {
					Proxy proxy = new Proxy(Type.HTTP, new InetSocketAddress(InetAddress.getByName("192.168.12.199"), 3128));
					HttpURLConnection con = (HttpURLConnection) url.openConnection(proxy);
					InputStream in = con.getInputStream();
					byte[] read = in.readAllBytes();
					String all = new String(read);
					//			logger.info("Read = "+all);

					int pos =0;
					do {
						int index = all.indexOf("href=",pos);
						if (index<0)
							break;
						int from = index+6;
						int to   = all.indexOf("\"",from);
						pos=to;
						String line = all.substring(from,to);
						if (!line.endsWith(".mf") && !line.endsWith("jar")) 
							continue;
						// Determine basename
						String baseName = line.substring(0, line.lastIndexOf("."));
						logger.info("  "+line);
						TempEntry temp = listing.get(baseName);
						if (temp==null) {
							temp = new TempEntry();
							listing.put(baseName, temp);
						}				
						// Manifest or JAR
						if (line.toLowerCase().endsWith(".mf")) {
							URL newURL = new URL(url+"/"+line);
							temp.desc = downloadAndParsePluginDescriptor(newURL);					
						} else if (line.toLowerCase().endsWith(".jar")) {
							temp.jarURL = new URL(url+"/"+line);
						}
					} while (true);

					// For listing entries where no .MF has been found, load jar itself
					for (TempEntry entry : listing.values()) {
						logger.info(entry.jarURL+" = "+entry.desc);
						try {
							if (entry.desc==null)
								entry.desc = getPluginInfo(entry.jarURL);

							plugins.add(entry.desc);
						} catch (IOException e) {
							logger.error("Failed downloading plugin JAR "+entry.jarURL+" :: "+e);
						}
					}

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			logger.info("Found "+plugins.size()+" downloadable plugins");
			return plugins;
		}

	//-------------------------------------------------------------------
	public static List<RulePlugin<?>> getAvailablePlugins() {
		List<RulePlugin<?>> plugins = new ArrayList<>();
		List<Path> jarFiles = new ArrayList<>();
		try {
			jarFiles.addAll(getJarFiles());
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(ExitCodes.ERROR_INIT_PHASE);
		}
		for (Path jarFile : jarFiles) {
			try {
				PluginDescriptor desc = getPluginInfo(jarFile.toUri().toURL());
				logger.info("Found local: "+desc);

				// TODO: decide whether to update or not

				plugins.addAll(loadPlugin(jarFile));
			} catch (Throwable e) {
				logger.fatal("Error loading plugin "+jarFile,e);
			}
		}
		return plugins;
	}

	//-------------------------------------------------------------------
	public static PluginDescriptor parsePluginDescriptor(InputStream in) throws IOException {
		PluginDescriptor ret = new PluginDescriptor();

		Manifest manifest = new Manifest(in);
		Attributes attrib = manifest.getMainAttributes();
		for (Entry<Object,Object> entry : attrib.entrySet()) {
			String key = ((Attributes.Name)entry.getKey()).toString();
			String val = (String)entry.getValue();
			try {
				switch (key) {
				case "Package"   : ret.name      = val; break;
				case "State"     : ret.state     = PluginState.valueOf(val); break;
				case "Implementation-Vendor" : ret.vendor = val; break;
				case "Implementation-Version": ret.version = val; break;
				case "Url"       : ret.homepage   = new URL(val); break;
				case "Bugtracker": ret.bugtracker = new URL(val); break;
				case "Build-Time": ret.timestamp  = Instant.parse(val); break;
				}
			} catch (Exception e) {
				logger.error("Failed parsing manifest header:\n"+entry.getKey()+" = "+entry.getValue(),e);
			}
		}
		return ret;
	}

}
