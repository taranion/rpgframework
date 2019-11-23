/**
 *
 */
package de.rpgframework;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

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

		Formatter format = new Formatter() {
			public String format(LogRecord record) {
				return String.format("%5s [%s] (%s) %s\r\n", record.getLevel().getName(), record.getLoggerName(),
						record.getSourceClassName().substring(record.getSourceClassName().lastIndexOf(".")+1), record.getMessage());
			}
		};

		logger.setUseParentHandlers(false);
		logger.setLevel(Level.FINER);
		ConsoleHandler console = new java.util.logging.ConsoleHandler();
		console.setLevel(Level.WARNING);
		console.setFormatter(format);
		logger.addHandler(console);
		try {
			String logDir = System.getProperty("logdir");
			if (logDir==null || logDir.isEmpty())
				logDir = System.getProperty("user.home");
			FileHandler    logfile = new FileHandler(logDir+System.getProperty("file.separator")+"rpgframework.log");
			logfile.setLevel (Level.FINER);
			logfile.setFormatter(format);
			logger.addHandler(logfile);
		} catch (Exception e) {
			logger.warning("Failed creating bootstrap logfile. "+e);
		}

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
				logger.info("Found framework "+instance.getClass());
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
