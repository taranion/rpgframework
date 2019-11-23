/**
 * 
 */
package de.rpgframework.genericrpg.modification;


/**
 * @author Stefan
 *
 */
public interface ValueModification<T> extends Modification {

	//--------------------------------------------------------------------
	public T getModifiedItem();

	//--------------------------------------------------------------------
	/**
	 * @return The amount by which the value must be changed
	 */
	public int getValue();

	//--------------------------------------------------------------------
	/**
	 * @param value The amount by which the value must be changed
	 */
	public void setValue(int value);

}
