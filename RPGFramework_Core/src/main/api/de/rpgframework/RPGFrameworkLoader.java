/**
 *
 */
package de.rpgframework;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;
import java.util.logging.Logger;

import org.apache.logging.log4j.LogManager;

/**
 * @author prelle
 *
 */
public class RPGFrameworkLoader {

	/**
	 * Allows to define the functionality
	 */
	public enum FunctionType {
		/**
		 * License check
		 */
		LICENSE_CHECK,
		/**
		 * Loads product information and RulePlugins
		 */
		CHARACTERS_AND_RULES,
		/**
		 * Enables MediaLibraries
		 */
		MEDIA_LIBRARIES,
		/**
		 * Groups and adventures (and UPnP?)
		 */
		SESSION_MANAGEMENT,
		/**
		 * Combat
		 */
		GAMEMASTER_RULES
	}

	private final static Logger logger = Logger.getLogger("rpgframework");

	private static RPGFrameworkInitCallback callback;
	private static RPGFramework instance;
	private static List<RPGFrameworkPlugin> frameworkPlugins;

	//--------------------------------------------------------------------
	static {
		frameworkPlugins = new ArrayList<RPGFrameworkPlugin>();
	}

	//--------------------------------------------------------------------
	public static void setCallback(RPGFrameworkInitCallback callback) {
		RPGFrameworkLoader.callback = callback;
	}

	//--------------------------------------------------------------------
	public static RPGFrameworkInitCallback getCallback() {
		return callback;
	}

	//--------------------------------------------------------------------
	public static void setInstance(RPGFramework toSet) {
		instance = toSet;
	}

	//--------------------------------------------------------------------
	public static RPGFramework getInstance() {
		if (instance!=null)
			return instance;

		Iterator<RPGFramework> it = ServiceLoader.load(RPGFramework.class).iterator();
		while (it.hasNext()) {
			try {
				instance = it.next();
				LogManager.getLogger("rpgframework").debug("Found framework "+instance.getClass());
				return instance;
			} catch (UnsupportedClassVersionError e) {
				logger.severe("Error instantiating "+instance.getClass()+": "+e.getMessage());
			} catch (Throwable e) {
				logger.severe("Failed instantiating RPGFramework "+e);
				e.printStackTrace();
			}
		}

		if (callback!=null)
			callback.errorOccurred("RPGFramework", "No implementation of "+RPGFramework.class+" found", null);
		throw new RuntimeException("No implementation of "+RPGFramework.class+" found");
	}

	//--------------------------------------------------------------------
	public static Collection<RPGFrameworkPlugin> getFrameworkPlugins() {
		return frameworkPlugins;
	}

}
