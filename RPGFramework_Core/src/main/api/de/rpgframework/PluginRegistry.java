/**
 * 
 */
package de.rpgframework;

import java.util.List;
import java.util.UUID;

/**
 * @author prelle
 *
 */
public interface PluginRegistry {
	
	public List<PluginDescriptor> getKnownPlugins();

	public void setPluginLoading(UUID uuid, boolean state);
	
	public boolean getPluginLoading(UUID uuid);

	public List<PluginDescriptor> getKnownRemotePlugins();
	
}
