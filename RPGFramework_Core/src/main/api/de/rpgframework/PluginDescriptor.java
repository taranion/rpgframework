package de.rpgframework;

import java.lang.module.ModuleDescriptor.Version;
import java.net.URL;
import java.nio.file.Path;
import java.time.Instant;
import java.util.UUID;

public class PluginDescriptor implements Comparable<PluginDescriptor> {
	// Uniquely identifies the plugin, even if the name changes
	public UUID uuid;
	// A human readable plugin name
	public String name;
	/* The author of the plugin */
	public String vendor;
	/* A version identifier in the format: Major.Minor.Patch */
	public Version version;
	/* Date when the plugin was built */
	public Instant timestamp;
	/* Roleplaying system */
	public String system;
	/* Required minimum framework version */
	public Version minVersion;
	/* Required maximum framework version */
	public Version maxVersion;
	
	public URL location;
	public String filename;
	public int fileSize;
	// Is the plugin in development, actively supported or abandoned
	public PluginState state;
	public URL homepage;
	public URL bugtracker;
	public String toString() {
		return "Plugin(filename="+filename+", name="+name+", vendor="+vendor+", version="+version+", size="+fileSize+", time="+timestamp+", state="+state+")";
	}
	public transient Path localFile; 
	public transient UpdateResult result;
	public transient PluginDescriptor localToUpdate;
	
	public String getName() { return name; }
	public String getVendor() { return vendor; }
	public Version getVersion() { return version; }
	public PluginState getState() { return state; }
	
	//-------------------------------------------------------------------
	/**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(PluginDescriptor other) {
		int cmp = system.compareTo(other.system);
		if (cmp!=0)
			return cmp;
		return getName().compareTo(other.getName());
	}
	
}