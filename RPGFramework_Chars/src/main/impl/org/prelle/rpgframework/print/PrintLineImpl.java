/**
 *
 */
package org.prelle.rpgframework.print;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.prelle.simplepersist.Attribute;
import org.prelle.simplepersist.ElementList;
import org.prelle.simplepersist.ElementListUnion;
import org.prelle.simplepersist.Root;

import de.rpgframework.print.ElementCell;
import de.rpgframework.print.EmptyCell;
import de.rpgframework.print.MultiRowCell;
import de.rpgframework.print.PDFPrintElement;
import de.rpgframework.print.PrintCell;
import de.rpgframework.print.PrintLine;

/**
 * @author Stefan
 *
 */
@SuppressWarnings("serial")
@Root(name="linedef")
@ElementListUnion({
    @ElementList(entry="empty", type=EmptyCellImpl.class),
    @ElementList(entry="element", type=ElementCellImpl.class),
    @ElementList(entry="multirow", type=MultiRowCellImpl.class),
})
public class PrintLineImpl extends ArrayList<PrintCell> implements PrintLine {

	private final static Logger logger = LogManager.getLogger("babylon.print");

	@Attribute
	private int columns;

	private transient PrintCell[] virtual;

	//--------------------------------------------------------------------
	/*
	 * Only use for persistence
	 */
	public PrintLineImpl() {

	}

	//--------------------------------------------------------------------
	public PrintLineImpl(int columns) {
		this.columns = columns;
		calculateVirtual(columns);
	}

	//--------------------------------------------------------------------
	/**
	 * @see java.util.AbstractList#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		if (o instanceof PrintLineImpl) {
			PrintLineImpl other = (PrintLineImpl)o;
			if (columns!=other.getDefiniedColumns()) return false;
			return this.size()==other.size();
		}
		return false;
	}

	//--------------------------------------------------------------------
	private int getDefiniedColumns() {
		return columns;
	}

	//--------------------------------------------------------------------
	public String toString() {
		return "PrintLine("+this.iterator().toString()+")";
	}

	//--------------------------------------------------------------------
	private void calculateVirtual(int columns) {
		virtual = new PrintCell[columns];
		int x=0;
		for (PrintCell cell : this) {
			for (int i=0; i<cell.getRequiredColumns(); i++) {
				virtual[x+i] = cell;
			}
			x+=cell.getRequiredColumns();
		}
		// Fill rest
		for (int i=x; i<columns; i++) {
			EmptyCell cell = new EmptyCellImpl();
			add(cell);
			virtual[i] = cell;
		}
	}

	//--------------------------------------------------------------------
	/**
	 * @see de.rpgframework.print.PrintLine#add(de.rpgframework.print.PrintCell)
	 */
	public void add(int x, PrintCell value) {
		logger.debug("add "+value+" to "+x);
		if (isOccupied(x, value.getRequiredColumns())) {
			logger.debug("throw exception: not enough empty space");
			throw new IllegalArgumentException("Not enough empty space");
		}

		// Find the position to insert before
		PrintCell insertBefore = null;
		int currentPos = 0;
		for (PrintCell cell : this) {
			logger.debug("  "+cell+" from "+currentPos+" to "+(currentPos+cell.getRequiredColumns()-1));
			if (x==currentPos) {
				insertBefore = cell;
				break;
			}
			currentPos += cell.getRequiredColumns();
		}

		if (insertBefore==null) {
			// Append instead of inserting before
			logger.debug("Append");
		} else {
			logger.debug("Insert at "+x);
			// Remove previous empty cells
			int fromIndex = this.indexOf(insertBefore);
			int toIndex   = fromIndex + value.getRequiredColumns();
			this.removeRange(fromIndex, toIndex);
			logger.debug("  removed "+fromIndex+" to "+(toIndex-1));
			logger.debug("  after removing "+toString());
			super.add(fromIndex, value);
			logger.debug("  after adding "+toString());
		}

		if (virtual==null)
			calculateVirtual(columns);
		Arrays.fill(virtual, x, x+value.getRequiredColumns(), value);
	}

	//--------------------------------------------------------------------
	/**
	 * @see de.rpgframework.print.PrintLine#isOccupied(int)
	 */
	@Override
	public boolean isOccupied(int x, int width) {
		if (virtual==null)
			calculateVirtual(columns);

		for (int i=x; i<(x+width); i++) {
			if (i>=virtual.length)
				return true;
			if (!(virtual[i] instanceof EmptyCell) )
				return true;
		}
		return false;
	}

	//--------------------------------------------------------------------
	/**
	 * @see de.rpgframework.print.PrintLine#getRequiredColumns()
	 */
	@Override
	public int getRequiredColumns() {
		return this.stream().mapToInt(e -> e.getRequiredColumns()).sum();
	}

	//--------------------------------------------------------------------
	/**
	 * @see de.rpgframework.print.PrintLine#remove(de.rpgframework.print.PrintCell)
	 */
	@Override
	public void remove(PrintCell cell) {
		int indexFrom = -1;
		for (int i=0; i<virtual.length; i++) {
			if (virtual[i]==cell) {
				if (indexFrom==-1)
					indexFrom=i;
				virtual[i]= new EmptyCellImpl();
			}
		}

		int from = super.indexOf(cell);
		if (from==-1)
			return;

		if ( (cell instanceof ElementCell) && ((ElementCell)cell).getElement()==null) {
			// Cell to remove was unresolvable
			super.remove(cell);
			// Detect how many empty cells to add by finding out
			// how many cells are occupied and fill the rest
			int occupied = 0;
			for (PrintCell tmp : this) {
				occupied += tmp.getRequiredColumns();
			}
			// Rest
			for (int i=occupied; i<columns; i++) {
				super.add(new EmptyCellImpl());
			}
			calculateVirtual(columns);
		} else {
			// Fill with empty cells
			for (int i=0; i<cell.getRequiredColumns(); i++) {
				super.add(from+i, virtual[indexFrom+i]);
			}
			super.remove(cell);
		}

	}

	//--------------------------------------------------------------------
	public List<String> resolveIDs(Map<String, PDFPrintElement> map) {
		List<String> notFound = new ArrayList<>();
		for (PrintCell cell : new ArrayList<PrintCell>(this)) {
			if (cell instanceof ElementCell) {
				ElementCell realCell = (ElementCell)cell;
				PDFPrintElement elem = map.get(realCell.getElementId());
				if (elem==null) {
					if (!notFound.contains(realCell.getElementId()))
						notFound.add(realCell.getElementId());
				} else {
					logger.debug("  resolve "+realCell.getElementId()+" with "+elem);
					((ElementCellImpl)realCell).setElement(elem);
				}
			} else if (cell instanceof MultiRowCell) {
				MultiRowCellImpl multi = (MultiRowCellImpl)cell;
				multi.resolveIDs(map);
			}
		}
		// Remove those to remove
		for (PrintCell cell : new ArrayList<PrintCell>(this)) {
			if (cell instanceof ElementCell && notFound.contains(((ElementCell)cell).getElementId())) {
				logger.debug("  remove cell "+((ElementCell)cell).getElementId());
				remove(cell);
			}
		}

		calculateVirtual(columns);

		return notFound;
	}

	//--------------------------------------------------------------------
	/**
	 * @see de.rpgframework.print.PrintLine#convertToMiniTable(de.rpgframework.print.PrintCell)
	 */
	@Override
	public MultiRowCell convertToMiniTable(PrintCell cell) {
		int oldPos = super.indexOf(cell);
		if (oldPos<0)
			throw new NoSuchElementException("Not a cell of this line");
		super.remove(cell);
		MultiRowCellImpl ret = new MultiRowCellImpl(cell);
		super.add(oldPos, ret);

		for (int i=0; i<virtual.length; i++) {
			if (virtual[i]==cell)
				virtual[i]=ret;
		}

		return ret;
	}

	//--------------------------------------------------------------------
	/**
	 * @see de.rpgframework.print.PrintLine#canGrowHorizontal(de.rpgframework.print.ElementCell)
	 */
	@Override
	public boolean canGrowHorizontal(ElementCell cell) {
		int add = cell.getElement().getNextHorizontalGrowth(cell.getSavedRenderOptions().getAsRenderingParameter());
		// Find last position in virtual
		int lastIndex = Arrays.asList(virtual).lastIndexOf(cell);
		if (lastIndex<0)
			// Not found at all
			return false;
		if ( (lastIndex+add) >= columns)
			// Cell already at right border
			return false;
		// Cell to right must be empty
		for (int i=1; i<=add; i++)
			if (!(virtual[lastIndex+i] instanceof EmptyCell)) return false;
		return true;
	}

	//--------------------------------------------------------------------
	/**
	 * @see de.rpgframework.print.PrintLine#growHorizontal(de.rpgframework.print.ElementCell)
	 */
	@Override
	public void growHorizontal(ElementCell cell) {
		if (logger.isTraceEnabled())
			logger.trace("grow horizontal (possible="+canGrowHorizontal(cell)+")");
		if (!canGrowHorizontal(cell)) {
			logger.debug("Cannot grow");
			return;
		}

		// Find last position in virtual
		int lastIndex = Arrays.asList(virtual).lastIndexOf(cell);
		if (logger.isTraceEnabled())
			logger.trace("  last index of grown cell is "+lastIndex);
		for (int i=1; i<=cell.getSavedRenderOptions().getHorizontalGrow(); i++) {
			logger.debug("  expand element on column "+(lastIndex+i));
			EmptyCell toRemove = (EmptyCell) virtual[lastIndex+i];
			virtual[lastIndex+i] = cell;
			super.remove(toRemove);
		}
	}

	//--------------------------------------------------------------------
	/**
	 * @see de.rpgframework.print.PrintLine#shrinkHorizontal(de.rpgframework.print.ElementCell)
	 */
	@Override
	public void shrinkHorizontal(ElementCell cell) {
		// Nicht nötig, da Zeile immer komplett neu errechnet wird
	}

}
