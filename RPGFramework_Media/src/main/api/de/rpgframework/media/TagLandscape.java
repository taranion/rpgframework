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
public enum TagLandscape implements MediaTag {
	
	TOWN,
	VILLAGE,
	FOREST,
	CLEARING,
	MEADOW,
	SWAMP,
	LAKE,
	SEA,
	ISLE,
	HILLS,
	MOUNTAINS,
	CAVE,
	TUNNEL,
	;

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.media.MediaTag#getName()
	 */
	@Override
	public String getName() {
		try {
			return RPGFrameworkConstants.RES.getString("tag.landscape."+this.name().toLowerCase());
		} catch (MissingResourceException e) {
			System.err.println("Missing "+e.getKey()+" in "+RPGFrameworkConstants.RES.getBaseBundleName());
		}
		return "tag.landscape."+this.name().toLowerCase();
	}

	//-------------------------------------------------------------------
	public static String getTagTypeName() {
		try {
			return RPGFrameworkConstants.RES.getString("tag.landscape");
		} catch (MissingResourceException e) {
			System.err.println("Missing "+e.getKey()+" in "+RPGFrameworkConstants.RES.getBaseBundleName());
		}
		return "tag.landscape";
	}
	
}
