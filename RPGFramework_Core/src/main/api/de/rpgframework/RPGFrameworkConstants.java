package de.rpgframework;

import java.util.ResourceBundle;

/**
 * @author prelle
 *
 */
public interface RPGFrameworkConstants {
	
	public final static ResourceBundle RES = ResourceBundle.getBundle(RPGFrameworkConstants.class.getName());
	
	public final static String PROPERTY_INSTALLATION_DIRECTORY = "userAppInstalldir";
	public final static String PROPERTY_READ_ONLY_INSTALLATION_DIRECTORY = "appInstalldir";

}
