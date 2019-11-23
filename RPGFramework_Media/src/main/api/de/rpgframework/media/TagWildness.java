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
public enum TagWildness implements MediaTag {

	URBAN,
	RURAL,
	NATURAL,
	DANGEROUS
	;

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.media.MediaTag#getName()
	 */
	@Override
	public String getName() {
		try {
			return RPGFrameworkConstants.RES.getString("tag.wildness."+this.name().toLowerCase());
		} catch (MissingResourceException e) {
			System.err.println("Missing "+e.getKey()+" in "+RPGFrameworkConstants.RES.getBaseBundleName());
		}
		return "tag.wildness."+this.name().toLowerCase();
	}

	//-------------------------------------------------------------------
	public static String getTagTypeName() {
		try {
			return RPGFrameworkConstants.RES.getString("tag.wildness");
		} catch (MissingResourceException e) {
			System.err.println("Missing "+e.getKey()+" in "+RPGFrameworkConstants.RES.getBaseBundleName());
		}
		return "tag.wildness";
	}

}
