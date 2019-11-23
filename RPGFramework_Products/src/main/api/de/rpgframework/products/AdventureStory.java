/**
 * 
 */
package de.rpgframework.products;

import java.util.Locale;

/**
 * @author Stefan
 *
 */
public interface AdventureStory {

	//----------------------------------------------------------------
	public String getID();

	//----------------------------------------------------------------
	/**
	 * Get title in system language
	 */
	public String getTitle();

	//----------------------------------------------------------------
	/**
	 * Get description in system language
	 */
	public String getDescription();

	//----------------------------------------------------------------
	public String getTitle(Locale lang);

	//----------------------------------------------------------------
	public String getDescription(Locale lang);

}
