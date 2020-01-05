/**
 * 
 */
package org.prelle.rpgframework.boot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.logging.log4j.Logger;
import org.prelle.rpgframework.BabylonConstants;
import org.prelle.rpgframework.PluginRegistryImpl;

import de.rpgframework.ConfigOption;
import de.rpgframework.PluginDescriptor;
import de.rpgframework.RPGFrameworkInitCallback;
import de.rpgframework.boot.BootStep;

/**
 * Contact all download servers and build a list of usable plugins -
 * each only in its most recent possible version.
 * 
 * @author prelle
 *
 */
public class KeepNewestRemotePluginStep implements BootStep {
	
	private final static Logger logger = BabylonConstants.logger;
	
	private PluginRegistryImpl registry;

	//-------------------------------------------------------------------
	public KeepNewestRemotePluginStep(PluginRegistryImpl registry) {
		this.registry = registry;
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.boot.BootStep#getID()
	 */
	@Override
	public String getID() {
		return "KEEP_NEWEST";
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
		
		Map<UUID, PluginDescriptor> newest = new HashMap<>();
		List<PluginDescriptor> toDelete = new ArrayList<PluginDescriptor>();
		
		for (PluginDescriptor descriptor : registry.getKnownRemotePlugins()) {
			UUID key = descriptor.uuid;
			PluginDescriptor soFar = newest.get(key);
			if (soFar==null)
				newest.put(key, descriptor);
			else {
				int cmp = soFar.version.compareTo(descriptor.getVersion()); 
				if (cmp>0) {
					// This one is newer
					toDelete.add(soFar);
					newest.put(key, descriptor);
				} else if (cmp<0) {
					toDelete.add(descriptor);
				} else {
					// Same version 
					logger.error("Remote site has plugins with identical versions\n1. "+soFar+"\n2. "+descriptor);
				}
			}
		}
		
		// Remove older versions
		for (PluginDescriptor deleteMe : toDelete) {
			logger.warn("Unregister remote, because it is outdated");
			registry.unregisterRemote(deleteMe);
		}
		
		return true;
	}

}
