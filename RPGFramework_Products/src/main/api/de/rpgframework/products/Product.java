/**
 * 
 */
package de.rpgframework.products;

import java.util.Collection;

import de.rpgframework.core.RoleplayingSystem;

/**
 * @author Stefan
 *
 */
public interface Product extends Comparable<Product> {

	//----------------------------------------------------------------
	public String getId();

	//----------------------------------------------------------------
	public Collection<RoleplayingSystem> getRoleplayingSystem();

	//----------------------------------------------------------------
	public String getLanguage();

	//----------------------------------------------------------------
	public String getTitle();

	//----------------------------------------------------------------
	public String getDescription();

	//----------------------------------------------------------------
	public Collection<Adventure> getAdventures();

	//----------------------------------------------------------------
	public byte[] getImage();

	//----------------------------------------------------------------
	public int getYear();

	//----------------------------------------------------------------
	public int getMonth();

}
