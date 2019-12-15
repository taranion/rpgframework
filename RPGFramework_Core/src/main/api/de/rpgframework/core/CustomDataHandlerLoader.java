/**
 *
 */
package de.rpgframework.core;

import java.util.Iterator;
import java.util.ServiceLoader;
import java.util.logging.Logger;

/**
 * @author prelle
 *
 */
public class CustomDataHandlerLoader {

	private final static Logger logger = Logger.getLogger("rpgframework");

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
				logger.severe("Error instantiating "+instance.getClass()+": "+e.getMessage());
			} catch (Throwable e) {
				logger.severe("Failed instantiating CustomDataHandler "+e);
				e.printStackTrace();
			}
		}

		throw new RuntimeException("No implementation of "+CustomDataHandler.class+" found");
	}

}
