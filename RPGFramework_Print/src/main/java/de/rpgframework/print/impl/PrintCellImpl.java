/**
 * 
 */
package de.rpgframework.print.impl;

import org.prelle.simplepersist.Attribute;

import de.rpgframework.print.LayoutGrid;
import de.rpgframework.print.PrintCell;

/**
 * @author stefa
 *
 */
public abstract class PrintCellImpl implements PrintCell {

	private transient LayoutGrid grid;
	private transient Object display;
	
	protected transient Type type;
	@Attribute(name="x")
	private int x;
	@Attribute(name="y")
	private int y;

	//-------------------------------------------------------------------
	protected PrintCellImpl() {
	}

	//-------------------------------------------------------------------
	protected PrintCellImpl(LayoutGrid grid, int x, int y) {
		this.grid = grid;
		this.x    = x;
		this.y    = y;
	}
	
	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.print.PrintCell#getType()
	 */
	@Override
	public Type getType() {
		return type;
	}
	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.print.PrintCell#getX()
	 */
	@Override
	public int getX() {
		return x;
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.print.PrintCell#getY()
	 */
	@Override
	public int getY() {
		return y;
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.print.PrintCell#getGrid()
	 */
	@Override
	public LayoutGrid getGrid() {
		return grid;
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.print.PrintCell#setDisplay(java.lang.Object)
	 */
	@Override
	public void setDisplay(Object value) {
		this.display = value;
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.print.PrintCell#getDisplay()
	 */
	@Override
	public Object getDisplay() {
		return display;
	}

}
