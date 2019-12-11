package de.rpgframework.character;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Proxy;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ServiceLoader;
import java.util.StringTokenizer;
import java.util.UUID;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.rpgframework.ConfigContainer;
import de.rpgframework.ConfigOption;
import de.rpgframework.ConfigOption.Type;

/**
 * @author spr
 *
 */
public class PluginRegistry {

	public static enum UpdateResult {
		UPDATED,
		FAILED,
		VERIFICATION_FAILED
	}

	private final static Logger logger = LogManager.getLogger("rpgframework");

	private static Proxy proxy;

	private static Map<UUID, PluginDescriptor> knownPlugins = new HashMap<UUID,PluginDescriptor>();
	
	private static ConfigOption<String> cfgLoadUUIDs;
	private static List<String> loadUUIDs = new ArrayList<String>();

	//-------------------------------------------------------------------
	public static void init(ConfigContainer configRoot) {
		cfgLoadUUIDs = configRoot.createOption("uuidsToLoad", Type.TEXT, "");
		StringTokenizer tok = new StringTokenizer(cfgLoadUUIDs.getValue());
		while (tok.hasMoreTokens()) {
			loadUUIDs.add(tok.nextToken().toLowerCase());
		}
		logger.info("init(): plugins are "+cfgLoadUUIDs.getValue());
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
		} catch (Throwable e) {
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
	public static PluginDescriptor parsePluginDescriptor(InputStream in) throws IOException {
		PluginDescriptor ret = new PluginDescriptor();

		Manifest manifest = new Manifest(in);
		Attributes attrib = manifest.getMainAttributes();
		for (Entry<Object,Object> entry : attrib.entrySet()) {
			String key = ((Attributes.Name)entry.getKey()).toString();
			String val = (String)entry.getValue();
			try {
				switch (key) {
				case "Implementation-Title"  : ret.name    = val; break;
				case "Implementation-Vendor" : ret.vendor  = val; break;
				case "Implementation-Version": ret.version = val; break;
				case "UUID"      : ret.uuid       = UUID.fromString(val); break;
				case "State"     : ret.state      = PluginState.valueOf(val); break;
				case "Url"       : ret.homepage   = new URL(val); break;
				case "Bugtracker": ret.bugtracker = new URL(val); break;
				case "Build-Time": ret.timestamp  = Instant.parse(val); break;
				case "Package"   : ret.system     = val; break;
				}
			} catch (Exception e) {
				logger.error("Failed parsing manifest header:\n"+entry.getKey()+" = "+entry.getValue(),e);
			}
		}
		return ret;
	}

	//-------------------------------------------------------------------
	public static void register(UUID uuid, PluginDescriptor descr) {
		if (uuid==null) throw new NullPointerException("UUID may not be null");
//		if (knownPlugins.containsKey(uuid)) throw new IllegalStateException("Already registered a plugin with that UUID");
		knownPlugins.put(uuid, descr);
	}

	//-------------------------------------------------------------------
	public static PluginDescriptor getRegistered(UUID uuid) {
		if (uuid==null) throw new NullPointerException("UUID may not be null");
		return knownPlugins.get(uuid);
	}

	//-------------------------------------------------------------------
	public static List<PluginDescriptor> getKnownPlugins() {
		List<PluginDescriptor> ret = new ArrayList<PluginDescriptor>(knownPlugins.values());
		Collections.sort(ret, new Comparator<PluginDescriptor>() {

			@Override
			public int compare(PluginDescriptor o1, PluginDescriptor o2) {
				int cmp = 0;
				if (o1.system!=null) {					
					cmp = o1.system.compareTo(o2.system);
				}
				if (cmp!=0)
					return cmp;
				return o1.name.compareTo(o2.name);
			}
		});
		return ret;
	}

	//-------------------------------------------------------------------
	private static void updateLoadUUIDConfig() {
		cfgLoadUUIDs.set(String.join(" ", loadUUIDs));
	}

	//-------------------------------------------------------------------
	public static void setPluginLoading(UUID uuid, boolean state) {
		logger.info("Set loading state of "+uuid+" to "+state);
		if (state && !loadUUIDs.contains(uuid.toString().toLowerCase())) {
			loadUUIDs.add(uuid.toString().toLowerCase());
			updateLoadUUIDConfig();
		}
		else if (!state && loadUUIDs.contains(uuid.toString().toLowerCase())) {
			loadUUIDs.remove(uuid.toString().toLowerCase());
			updateLoadUUIDConfig();
		}
	}

	//-------------------------------------------------------------------
	public static boolean getPluginLoading(UUID uuid) {
		return loadUUIDs.contains(uuid.toString().toLowerCase());
	}

	//-------------------------------------------------------------------
	public static int getNumberOfPluginsToLoad() {
		return loadUUIDs.size();
	}
	
}
