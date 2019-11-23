/**
 *
 */
package org.prelle.rpgframework.character;

import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * @author prelle
 *
 */
public interface BabylonConstants {
	
	final static String builtInUpdateURL = "http://www.rpgframework.de/plugins/";

	public static PropertyResourceBundle RES = (PropertyResourceBundle) ResourceBundle.getBundle(BabylonConstants.class.getName());

}
