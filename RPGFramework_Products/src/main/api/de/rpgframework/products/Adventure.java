/**
 * 
 */
package de.rpgframework.products;

import java.nio.file.Path;

import de.rpgframework.core.RoleplayingSystem;

/**
 * @author prelle
 *
 */
public interface Adventure extends Comparable<Adventure> {

	//----------------------------------------------------------------
	public String getId();

	//----------------------------------------------------------------
	public AdventureStory getStory();

	//----------------------------------------------------------------
	/**
	 * Shortcut for getStory().getTitle();
	 */
	public String getTitle();

	//----------------------------------------------------------------
	/**
	 * Shortcut for getStory().getDescription();
	 */
	public String getDescription();

	//----------------------------------------------------------------
	public RoleplayingSystem getRules();

	//--------------------------------------------------------------------
	/**
	 * Returns the local directory with the adventure resources
	 */
	public Path getBaseDirectory();

	//--------------------------------------------------------------------
	/**
	 * Shortcut for ProductService.getProductWith(this).get(0).getCover();
	 */
	public byte[] getCover();

}
