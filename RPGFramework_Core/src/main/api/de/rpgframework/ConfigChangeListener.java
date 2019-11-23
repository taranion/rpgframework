/**
 * 
 */
package de.rpgframework;

import java.util.Collection;

/**
 * @author prelle
 *
 */
public interface ConfigChangeListener {

	public void configChanged(ConfigContainer source, Collection<ConfigOption<?>> options);
	
}
