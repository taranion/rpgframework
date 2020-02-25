/**
 *
 */
package de.rpgframework.core;

import java.util.Iterator;
import java.util.ServiceLoader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author prelle
 *
 */
public class CustomDataHandlerLoader {

	private final static Logger logger = LogManager.getLogger("rpgframework");

	private static CustomDataHandler instance;

	//--------------------------------------------------------------------
	public static void setInstance(CustomDataHandler toSet) {
		instance = toSet;
	}

	//--------------------------------------------------------------------
	public static CustomDataHandler getInstance() {
		if (instance!=null)
			return instance;

		Iterator<CustomDataHandler> it = ServiceLoader.load(CustomDataHandler.class).iterator();
		while (it.hasNext()) {
			try {
				instance = it.next();
				logger.info("Found custom data handler "+instance.getClass());
				return instance;
			} catch (UnsupportedClassVersionError e) {
				logger.fatal("Error instantiating "+instance.getClass()+": "+e.getMessage());
			} catch (Throwable e) {
				logger.fatal("Failed instantiating CustomDataHandler "+e);
				e.printStackTrace();
			}
		}

//		logger.fatal("No implementation of "+CustomDataHandler.class+" found");
		return null;
	}

}
