/**
 * 
 */
package de.rpgframework.core;

import java.util.MissingResourceException;

import de.rpgframework.RPGFrameworkConstants;

/**
 * @author prelle
 *
 */
public enum Genre {

	APOCALYPTIC,
	FANTASY,
	HORROR,
	SCIFI,
	CYBERPUNK,
	TODAY,
	VINTAGE_1920,
	VINTAGE_1930,
	VINTAGE_1980,
	WESTERN,
	STEAMPUNK,
	SUPERHEROES,
	;
	

	//-------------------------------------------------------------------
	public String getName() {
		try {
			return RPGFrameworkConstants.RES.getString("genre."+this.name().toLowerCase());
		} catch (MissingResourceException e) {
			System.err.println("Missing "+e.getKey()+" in "+RPGFrameworkConstants.RES.getBaseBundleName());
		}
		return "genre."+this.name().toLowerCase();
	}

}
