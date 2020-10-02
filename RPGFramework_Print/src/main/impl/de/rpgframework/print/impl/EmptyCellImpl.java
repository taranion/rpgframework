/**
 *
 */
package de.rpgframework.print.impl;

import de.rpgframework.print.EmptyCell;
import de.rpgframework.print.LayoutGrid;

/**
 * @author Stefan
 *
 */
public class EmptyCellImpl extends PrintCellImpl implements EmptyCell {

	//--------------------------------------------------------------------
	public EmptyCellImpl(LayoutGrid grid, int x, int y) {
		super(grid,x,y);
	}

	//--------------------------------------------------------------------
	public String toString() {
		return "EMPTY";
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.print.PrintCell#getWidth()
	 */
	@Override
	public int getWidth() {
		return 1;
	}

}
