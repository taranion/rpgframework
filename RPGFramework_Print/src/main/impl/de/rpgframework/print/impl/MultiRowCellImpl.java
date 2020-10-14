/**
 *
 */
package de.rpgframework.print.impl;

import java.util.Iterator;

import org.prelle.simplepersist.Attribute;
import org.prelle.simplepersist.Element;
import org.prelle.simplepersist.Root;

import de.rpgframework.print.LayoutGrid;
import de.rpgframework.print.MultiRowCell;
import de.rpgframework.print.PrintCell;

/**
 * @author Stefan
 *
 */
@Root(name="multirow")
//@ElementListUnion({
//    @ElementList(entry="empty", type=EmptyCellImpl.class),
//    @ElementList(entry="element", type=ElementCellImpl.class),
//})
public class MultiRowCellImpl extends PrintCellImpl implements MultiRowCell {

	@Attribute
	private int width;
	@Element(name="grid")
	private LayoutGrid innerGrid;

	//--------------------------------------------------------------------
	public MultiRowCellImpl(LayoutGrid parent, int x, int y, int width) {
		super(parent,x,y);
		this.width = width;
		super.type = Type.GRID;
		innerGrid = new LayoutGridImpl(width);
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.print.PrintCell#getWidth()
	 */
	@Override
	public int getWidth() {
		 return width;
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.print.MultiRowCell#getInnerGrid()
	 */
	@Override
	public LayoutGrid getInnerGrid() {
		return innerGrid;
	}

}
