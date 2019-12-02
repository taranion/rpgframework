/**
 *
 */
package de.rpgframework;

import de.rpgframework.boot.BootStep;
import de.rpgframework.boot.StandardBootSteps;
import de.rpgframework.core.LicenseManager;

/**
 * @author prelle
 *
 */
public interface RPGFramework {

	public final static String PREFERENCE_PATH = "/de/rpgframework";
	public final static String PROP_LANGUAGE = "language";
	public final static String PROP_DATADIR  = "dataDir";
	public final static String PROP_PROMODIR  = "promoDir";
	public final static String PROP_UPDATE_ASK = "askUpdater";
	public final static String PROP_UPDATE_RUN = "runUpdater";
	public final static String PROP_RULE_LIMIT = "ruleLimit";

	public final static String LAST_OPEN_DIR = PREFERENCE_PATH+"/lastDir/open";
	public final static String LAST_SAVE_DIR = PREFERENCE_PATH+"/lastDir/save";
	public final static String PROP_LAST_OPEN_IMAGE_DIR = "image";
	public final static String PROP_LAST_SAVE_PRINT_DIR = "print";

	//-------------------------------------------------------------------
	public void addStepDefinition(StandardBootSteps def, BootStep step);

	//-------------------------------------------------------------------
	public void addBootStep(StandardBootSteps roleplayingSystems);

	//-------------------------------------------------------------------
	public void addBootStep(BootStep step);

	public void initialize(RPGFrameworkInitCallback listener);


	public ConfigContainer getConfiguration();

	public ConfigContainer getPluginConfigurationNode();

	public LicenseManager getLicenseManager();

}
