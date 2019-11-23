package de.rpgframework;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Stefan Prelle
 *
 */
public class ResourceI18N {
	
	private final static Logger logger = LogManager.getLogger(RPGFramework.class);
	
	//-------------------------------------------------------------------
	public static String get(ResourceBundle res, String key) {
		if (res==null) 
			return key;
		try {
			return res.getString(key);
		} catch (MissingResourceException e) {
//			BasePluginData.reportKey(e, res);
//			System.err.println("Missing key '"+key+"' in "+res.getBaseBundleName());
			logger.error("Missing key '"+key+"' in "+res.getBaseBundleName());
//			LogManager.getLogger("shadowrun6").error(" => "+e.getStackTrace()[3]);
		}
		return key;
	}
	
	//-------------------------------------------------------------------
	public static String format(ResourceBundle res, String key, Object...objects) {
		try {
			return String.format(res.getString(key), objects);
		} catch (MissingResourceException e) {
//			BasePluginData.reportKey(e, res);
//			System.err.println("Missing key '"+key+"' in "+res.getBaseBundleName());
			logger.error("Missing key '"+key+"' in "+res.getBaseBundleName());
//			LogManager.getLogger("shadowrun6").error(" => "+e.getStackTrace()[3]);
		}
		return key;
	}

}
