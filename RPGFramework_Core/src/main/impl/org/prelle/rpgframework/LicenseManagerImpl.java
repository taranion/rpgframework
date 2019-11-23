/**
 * 
 */
package org.prelle.rpgframework;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.rpgframework.core.CommandBus;
import de.rpgframework.core.CommandResult;
import de.rpgframework.core.CommandType;
import de.rpgframework.core.License;
import de.rpgframework.core.LicenseManager;
import de.rpgframework.core.RoleplayingSystem;

/**
 * @author Stefan
 *
 */
public class LicenseManagerImpl implements LicenseManager {

	private final static Logger logger = LogManager.getLogger("babylon.license");

	//--------------------------------------------------------------------
	public boolean hasLicense(RoleplayingSystem rules, String value) {
		CommandResult result = CommandBus.fireCommand(null, 
				CommandType.LICENSE_VERIFICATION, 
				rules, value);
		if (!result.wasProcessed()) {
//			logger.fatal("No license manager implementation found - allow operation");
//			System.exit(1);
			return false;
		}
		if (!result.wasSuccessful()) {
//			logger.fatal("Could not verify license - reject operation");
			return false;
		}
//		logger.trace(result.getMessage());
//		if (!(Boolean)result.getReturnValue())
//			logger.warn("Checking "+rules+"/"+value+" failed: "+result.getMessage());
		return (Boolean)result.getReturnValue();
	}

	//--------------------------------------------------------------------
	@SuppressWarnings("unchecked")
	public List<License> getLicenses(RoleplayingSystem rules) {
		CommandResult result = CommandBus.fireCommand(null, 
				CommandType.LICENSE_LIST, 
				rules);
		if (!result.wasProcessed()) {
//			logger.fatal("No license manager implementation found - reject operation");
			return new ArrayList<License>();
		}
		if (!result.wasSuccessful()) {
//			logger.fatal("Could not verify license - reject operation");
			return new ArrayList<>();
		}
		return (List<License>)result.getReturnValue();
	}

}
