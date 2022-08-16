package org.prelle.rpgframework;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.module.ModuleDescriptor.Version;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
import de.rpgframework.PluginDescriptor;
import de.rpgframework.PluginRegistry;
import de.rpgframework.PluginState;

/**
 * @author spr
 *
 */
public class PluginRegistryImpl implements PluginRegistry {

	private final Logger logger = LogManager.getLogger("rpgframework");

	private Proxy proxy;

	private Map<UUID, PluginDescriptor> localPlugins = new HashMap<UUID,PluginDescriptor>();
	private Map<UUID, List<PluginDescriptor>> remotePlugins = new HashMap<UUID, List<PluginDescriptor>>();
	
	private ConfigOption<String> cfgLoadUUIDs;
	private List<String> loadUUIDs = new ArrayList<String>();
	private List<String> updateErrors = new ArrayList<String>();
	private List<PluginDescriptor> toDelete = new ArrayList<PluginDescriptor>();

	//-------------------------------------------------------------------
	public void init(ConfigContainer configRoot) {
		cfgLoadUUIDs = configRoot.createOption("uuidsToLoad", Type.TEXT, "");
		StringTokenizer tok = new StringTokenizer(cfgLoadUUIDs.getValue());
		while (tok.hasMoreTokens()) {
			loadUUIDs.add(tok.nextToken().toLowerCase());
		}
		logger.info("init(): plugins are "+cfgLoadUUIDs.getValue());
		
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				logger.info("Deleting outdated plugins");
				for (PluginDescriptor desc : toDelete) {
					try {
						logger.info("Should delete "+desc);
						Files.deleteIfExists(desc.localFile);
					} catch (Exception e) {
						logger.error("Failed deleting "+desc+" : "+e);
					}
				}
			}
		});
	}

	//-------------------------------------------------------------------
	public PluginDescriptor getPluginInfo(URL url) throws IOException {
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
		try {
			stream.close();
		} catch (Exception e) {
			logger.fatal("Failed closing JAR: "+e);
		}
		System.gc();

		return ret;
	}

	//-------------------------------------------------------------------
	public PluginDescriptor downloadAndParsePluginDescriptor(URL url) throws IOException {
		logger.trace("load plugin manifest "+url+"  and proxy "+proxy);
		URLConnection con = ((proxy!=null)?url.openConnection(proxy):url.openConnection());
		logger.trace("  last modified = "+con.getLastModified());
		return parsePluginDescriptor(con.getInputStream());
	}

	//-------------------------------------------------------------------
	public PluginDescriptor parsePluginDescriptor(InputStream in) throws IOException {
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
				case "Implementation-Version": ret.version = Version.parse(val); break;
				case "UUID"      : ret.uuid       = UUID.fromString(val); break;
				case "Url"       : ret.homepage   = new URL(val); break;
				case "Bugtracker": ret.bugtracker = new URL(val); break;
				case "Build-Time": ret.timestamp  = Instant.parse(val); break;
				case "Package"   : ret.system     = val; break;
				case "State"     : ret.state      = PluginState.valueOf(val); break;
				case "MinVersion": ret.minVersion = Version.parse(val); break;
				case "MaxVersion": ret.maxVersion = Version.parse(val); break;
				case "Provides"  : ret.provides   = val; break;
				case "Requires"  : ret.requires   = val.split(","); break;
				}
			} catch (Exception e) {
				logger.warn("Failed parsing manifest header of '"+ret.name+"': "+entry.getKey()+" = "+entry.getValue());
			}
		}
		return ret;
	}

	//-------------------------------------------------------------------
	public void registerLocal(UUID uuid, PluginDescriptor descr) {
		if (uuid==null) throw new NullPointerException("UUID may not be null");
		localPlugins.put(uuid, descr);
	}

	//-------------------------------------------------------------------
	public void registerRemote(UUID uuid, PluginDescriptor descr) {
		if (uuid==null) throw new NullPointerException("UUID may not be null");
		List<PluginDescriptor> list = remotePlugins.get(uuid);
		if (list==null) {
			list = new ArrayList<PluginDescriptor>();
			remotePlugins.put(uuid, list);
		}
		list.add(descr);
	}

	//-------------------------------------------------------------------
	public List<PluginDescriptor> getRegisteredRemote(UUID uuid) {
		if (uuid==null) throw new NullPointerException("UUID may not be null");
		return remotePlugins.get(uuid);
	}

	//-------------------------------------------------------------------
	@Override
	public List<PluginDescriptor> getKnownPlugins() {
		List<PluginDescriptor> ret = new ArrayList<PluginDescriptor>(localPlugins.values());
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
	@Override
	public List<PluginDescriptor> getKnownRemotePlugins() {
		List<PluginDescriptor> ret = new ArrayList<PluginDescriptor>();
		remotePlugins.values().forEach(list -> ret.addAll(list));
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
	private void updateLoadUUIDConfig() {
		cfgLoadUUIDs.set(String.join(" ", loadUUIDs));
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.PluginRegistry#setPluginLoading(java.util.UUID, boolean)
	 */
	@Override
	public void setPluginLoading(UUID uuid, boolean state) {
		if (state && !loadUUIDs.contains(uuid.toString().toLowerCase())) {
			logger.info("Set loading state of "+uuid+" to "+state);
			loadUUIDs.add(uuid.toString().toLowerCase());
			updateLoadUUIDConfig();
		}
		else if (!state && loadUUIDs.contains(uuid.toString().toLowerCase())) {
			logger.info("Set loading state of "+uuid+" to "+state);
			loadUUIDs.remove(uuid.toString().toLowerCase());
			updateLoadUUIDConfig();
		}
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.PluginRegistry#getPluginLoading(java.util.UUID)
	 */
	@Override
	public boolean getPluginLoading(UUID uuid) {
		return loadUUIDs.contains(uuid.toString().toLowerCase());
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.PluginRegistry#getNumberOfPluginsToLoad()
	 */
	@Override
	public int getNumberOfPluginsToLoad() {
		return loadUUIDs.size();
	}

	//-------------------------------------------------------------------
	public void unregisterRemote(PluginDescriptor descriptor) {
		toDelete.add(descriptor);
		
		List<PluginDescriptor> list = remotePlugins.get(descriptor.uuid);
		if (list==null) 
			return;
		list.remove(descriptor);
		
		if (list.isEmpty())
			remotePlugins.remove(descriptor.uuid);
	}

	//-------------------------------------------------------------------
	public void updateLocalPlugin(PluginDescriptor descriptor) {
		PluginDescriptor previous = localPlugins.get(descriptor.uuid);
		if (previous==null) {
			logger.info("Added new local plugin "+descriptor);
			localPlugins.put(descriptor.uuid, descriptor);
		} else {
			logger.info("Updated local plugin "+descriptor);
			localPlugins.put(descriptor.uuid, descriptor);
		}
	}

	//-------------------------------------------------------------------
	public List<String> getUpdateErrors() {
		return updateErrors;
	}

	//-------------------------------------------------------------------
	public void addUpdateError(String msg) {
		updateErrors.add(msg);
	}

	//-------------------------------------------------------------------
	@Override
	public List<PluginDescriptor> getPluginsToDelete() {
		return toDelete;
	}

	//-------------------------------------------------------------------
	public void addPluginToDeleteOnExit(PluginDescriptor desc) {
		toDelete.add(desc);
		if (desc.localFile==null) {
			try {
				throw new RuntimeException("No local file set in plugin to delete");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}
