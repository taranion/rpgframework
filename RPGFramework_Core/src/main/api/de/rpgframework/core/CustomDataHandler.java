/**
 * 
 */
package de.rpgframework.core;

import java.nio.file.Path;
import java.util.List;
import java.util.Properties;

/**
 * @author prelle
 *
 */
public interface CustomDataHandler {

	public class CustomDataPackage {
		public Path datafile;
		public Properties properties;
		public Properties helpProperties;
	}
	
	public List<String> getAvailableCustomIDs(RoleplayingSystem rules);
	
	public CustomDataPackage getCustomData(RoleplayingSystem rules, String identifier);
	
}
