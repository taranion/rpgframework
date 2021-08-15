/**
 * 
 */
package de.rpgframework.core;

import java.nio.file.Path;
import java.util.List;
import java.util.PropertyResourceBundle;

/**
 * @author prelle
 *
 */
public interface CustomDataHandler {

	public class CustomDataPackage {
		public Path datafile;
		public PropertyResourceBundle properties;
		public PropertyResourceBundle helpProperties;
	}
	
	public Path getCustomDataPath(RoleplayingSystem rules);
	
	public List<String> getAvailableCustomIDs(RoleplayingSystem rules);
	
	public CustomDataPackage getCustomData(RoleplayingSystem rules, String identifier);

	public void setCustomText(RoleplayingSystem rules, String key, String value);

}
