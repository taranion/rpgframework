/**
 *
 */
package org.prelle.rpgframework;

import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author prelle
 *
 */
public interface BabylonConstants {
	
	public final static Logger logger = LogManager.getLogger("rpgframework");

	final static String builtInUpdateURL = "http://www.rpgframework.de/plugins/";

	public static PropertyResourceBundle RES = (PropertyResourceBundle) ResourceBundle.getBundle(BabylonConstants.class.getName());

}
