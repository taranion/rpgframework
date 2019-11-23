/**
 * 
 */
package de.rpgframework.genericrpg;

import java.util.Date;

/**
 * @author prelle
 *
 */
public interface Datable {

	//-------------------------------------------------------------------
	/**
	 * Return the date of the change
	 */
	public Date getDate();

	//--------------------------------------------------------------------
	/**
	 * Set the date of the change
	 */
	public void setDate(Date date);
}
