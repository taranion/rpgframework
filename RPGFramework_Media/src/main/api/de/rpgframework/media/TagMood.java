/**
 * 
 */
package de.rpgframework.media;

import java.util.MissingResourceException;

import de.rpgframework.RPGFrameworkConstants;

/**
 * @author prelle
 *
 */
public enum TagMood implements MediaTag {

	JOLLY, // Ausgelassen
	CHEERFUL, // Fröhlich
	NEUTRAL,
	SPOOKY, // Unheimlich
	THREATENING,
	;

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.media.MediaTag#getName()
	 */
	@Override
	public String getName() {
		try {
			return RPGFrameworkConstants.RES.getString("tag.mood."+this.name().toLowerCase());
		} catch (MissingResourceException e) {
			System.err.println("Missing "+e.getKey()+" in "+RPGFrameworkConstants.RES.getBaseBundleName());
		}
		return "tag.mood."+this.name().toLowerCase();
	}

	//-------------------------------------------------------------------
	public static String getTagTypeName() {
		try {
			return RPGFrameworkConstants.RES.getString("tag.mood");
		} catch (MissingResourceException e) {
			System.err.println("Missing "+e.getKey()+" in "+RPGFrameworkConstants.RES.getBaseBundleName());
		}
		return "tag.mood";
	}
	
}
