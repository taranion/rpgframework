/**
 *
 */
package org.prelle.rpgframework.print;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.prelle.simplepersist.Attribute;
import org.prelle.simplepersist.ElementList;
import org.prelle.simplepersist.ElementListUnion;
import org.prelle.simplepersist.Root;

import de.rpgframework.print.ElementCell;
import de.rpgframework.print.MultiRowCell;
import de.rpgframework.print.PDFPrintElement;
import de.rpgframework.print.PrintCell;

/**
 * @author Stefan
 *
 */
@SuppressWarnings("serial")
@Root(name="multirow")
@ElementListUnion({
    @ElementList(entry="empty", type=EmptyCellImpl.class),
    @ElementList(entry="element", type=ElementCellImpl.class),
})
public class MultiRowCellImpl extends ArrayList<PrintCell> implements MultiRowCell {

	private final static Logger logger = LogManager.getLogger("babylon.print");

	@Attribute
	private int columns;

	//--------------------------------------------------------------------
	/**
	 * Only for persistence
	 */
	public MultiRowCellImpl() {
	}

	//--------------------------------------------------------------------
	public MultiRowCellImpl(PrintCell convertedFrom) {
		super.add(convertedFrom);
		columns = convertedFrom.getRequiredColumns();
	}

	//--------------------------------------------------------------------
	/**
	 * @see de.rpgframework.print.PrintCell#getRequiredColumns()
	 */
	@Override
	public int getRequiredColumns() {
		 return columns;
	}

	//--------------------------------------------------------------------
	/**
	 * @see de.rpgframework.print.MultiRowCell#remove(de.rpgframework.print.PrintCell)
	 */
	@Override
	public void remove(PrintCell cell) {
		super.remove(cell);
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
		return notFound;
	}

}
