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
public enum TagTempo implements MediaTag {

	SPHERIC,
	SLOW,
	NEUTRAL,
	DYNAMIC,
	DRAMATIC,
	;

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.media.MediaTag#getName()
	 */
	@Override
	public String getName() {
		try {
			return RPGFrameworkConstants.RES.getString("tag.tempo."+this.name().toLowerCase());
		} catch (MissingResourceException e) {
			System.err.println("Missing "+e.getKey()+" in "+RPGFrameworkConstants.RES.getBaseBundleName());
		}
		return "tag.tempo."+this.name().toLowerCase();
	}

	//-------------------------------------------------------------------
	public static String getTagTypeName() {
		try {
			return RPGFrameworkConstants.RES.getString("tag.tempo");
		} catch (MissingResourceException e) {
			System.err.println("Missing "+e.getKey()+" in "+RPGFrameworkConstants.RES.getBaseBundleName());
		}
		return "tag.tempo";
	}
	
}
