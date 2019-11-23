/**
 * 
 */
package de.rpgframework.core;

import java.util.List;

/**
 * @author Stefan
 *
 */
public interface LicenseManager {

	//--------------------------------------------------------------------
	public boolean hasLicense(RoleplayingSystem rules, String value);
	
	//--------------------------------------------------------------------
	public List<License> getLicenses(RoleplayingSystem rules);
	
}
