/**
 *
 */
package org.prelle.rpgframework.print;

import de.rpgframework.print.EmptyCell;

/**
 * @author Stefan
 *
 */
public class EmptyCellImpl implements EmptyCell {

	//--------------------------------------------------------------------
	public EmptyCellImpl() {
	}

	//--------------------------------------------------------------------
	public String toString() {
		return "EMPTY";
	}

	//--------------------------------------------------------------------
	/**
	 * @see de.rpgframework.print.PrintCell#getRequiredColumns()
	 */
	@Override
	public int getRequiredColumns() {
		return 1;
	}

}
