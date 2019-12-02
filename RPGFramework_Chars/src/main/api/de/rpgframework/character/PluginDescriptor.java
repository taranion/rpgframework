package de.rpgframework.character;

import java.net.URL;
import java.nio.file.Path;
import java.time.Instant;

import org.prelle.rpgframework.character.PluginRegistry.UpdateResult;

public class PluginDescriptor {
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