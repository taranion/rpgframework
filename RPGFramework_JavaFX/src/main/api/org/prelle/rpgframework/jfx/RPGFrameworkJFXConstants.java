/**
 *
 */
package org.prelle.rpgframework.jfx;

import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * @author prelle
 *
 */
public interface RPGFrameworkJFXConstants {

	public final static PropertyResourceBundle RES = (PropertyResourceBundle) ResourceBundle.getBundle("org/prelle/rpgframework/jfx/i18n/babylon-jfx", RPGFrameworkJFXConstants.class.getModule());

	public final static PropertyResourceBundle UI = (PropertyResourceBundle) ResourceBundle.getBundle("org/prelle/rpgframework/jfx/i18n/rpgframework-jfx", RPGFrameworkJFXConstants.class.getModule());

	public final static String BASE_LOGGER_NAME = "rpgframework.jfx";

}
