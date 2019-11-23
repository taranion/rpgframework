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
public enum TagPlaces implements MediaTag {
	
	CASTLE,
	TOWER,
	MARKET,
	SHOP,
	BAR_TAVERN,
	HOTEL_INN,
	CLINIC_SPITAL,
	
	CEMETARY,
	CRYPT,
	CAVE,     // Natural
	MINE,     // Natural, expedient modified
	DUNGEON,  // Built, has artificial walls etc.
	
	SUBWAY,
	TRAIN,
	PLANE,
	HANGAR,
	
	BOAT,
	SHIP,
	HARBOR,
	
	INDUSTRIAL,
	;

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.media.MediaTag#getName()
	 */
	@Override
	public String getName() {
		try {
			return RPGFrameworkConstants.RES.getString("tag.places."+this.name().toLowerCase());
		} catch (MissingResourceException e) {
			System.err.println("Missing "+e.getKey()+" in "+RPGFrameworkConstants.RES.getBaseBundleName());
		}
		return "tag.places."+this.name().toLowerCase();
	}

	//-------------------------------------------------------------------
	public static String getTagTypeName() {
		try {
			return RPGFrameworkConstants.RES.getString("tag.places");
		} catch (MissingResourceException e) {
			System.err.println("Missing "+e.getKey()+" in "+RPGFrameworkConstants.RES.getBaseBundleName());
		}
		return "tag.places";
	}
	
}
