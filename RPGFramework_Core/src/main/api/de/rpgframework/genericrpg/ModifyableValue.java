/**
 *
 */
package de.rpgframework.genericrpg;

import de.rpgframework.genericrpg.modification.Modifyable;


/**
 * @author Stefan
 *
 */
public interface ModifyableValue<T> extends NumericalValue<T>, Modifyable {

	//--------------------------------------------------------------------
	public int getModifier();

	//--------------------------------------------------------------------
	/**
	 * Return the value after all modifications have been
	 * applied
	 */
	public int getModifiedValue();

}
