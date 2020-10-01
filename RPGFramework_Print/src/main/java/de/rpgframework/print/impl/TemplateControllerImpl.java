/**
 * 
 */
package de.rpgframework.print.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.rpgframework.print.ElementCell;
import de.rpgframework.print.EmptyCell;
import de.rpgframework.print.LayoutGrid;
import de.rpgframework.print.PDFPrintElement;
import de.rpgframework.print.PrintCell;
import de.rpgframework.print.PrintUtil;
import de.rpgframework.print.TemplateController;

/**
 * @author stefa
 *
 */
public class TemplateControllerImpl implements TemplateController {
	
	private final static Logger logger = LogManager.getLogger("rpgframework.print");

	private Map<String,PDFPrintElement> elementMap;

	//---------------------------------------------------------
	public TemplateControllerImpl(LayoutGrid page, Map<String,PDFPrintElement> elementMap) {
		this.elementMap = elementMap;
		List<String> errors = validate(page);
		if (!errors.isEmpty()) {
			throw new RuntimeException("Errors validating template:\n"+String.join("\n", errors));
		}
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.print.TemplateController#validate(de.rpgframework.print.LayoutGrid)
	 */
	@Override
	public List<String> validate(LayoutGrid page) {
		List<String> errors = new ArrayList<String>();

		List<boolean[]> occupation = new ArrayList<boolean[]>();
		// At least one line
		occupation.add(new boolean[page.getColumnCount()]);
		// Now calculate
		for (PrintCell comp : page.getComponents()) {
			// Ensure line is present
			while (comp.getY()>=occupation.size()) {
				occupation.add(new boolean[page.getColumnCount()]);				
			}
			// Get element
			if (comp instanceof ElementCell) {
				ElementCell eCell = (ElementCell)comp;
				PDFPrintElement elem = elementMap.get(eCell.getElementId());
				if (elem==null) {
					logger.warn("Unknown element '"+eCell.getElementId()+"'");
					errors.add("Unknown element '"+eCell.getElementId()+"'");
					continue;
				}
				// Get requested line
				boolean[] line = occupation.get(comp.getY());
				for (int i=0; i<elem.getRequiredColumns(); i++) {
					int x=comp.getX()+i;
					if (x>=line.length) {
						logger.warn("Element '"+eCell.getElementId()+"' not within page bounds");
						errors.add("Element '"+eCell.getElementId()+"' not within page bounds");
						break;
					} else {
						line[x] = true;
					}
				}
			}
		}
//		page.setOccupation(occupation);
		return errors;
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.print.TemplateController#canBeAdded(de.rpgframework.print.LayoutGrid, de.rpgframework.print.LayoutElement, int, int)
	 */
	@Override
	public boolean canBeAdded(LayoutGrid page, PDFPrintElement elem, int x, int y) {
		// Ensure x is a valid column
		if (x<0) return false;
		if (x>=page.getColumnCount()) return false;
		// End column must also be valid
		if ((x+elem.getRequiredColumns())>page.getColumnCount()) return false;
		// After last line
		if (page.getAsLines().size()>=y)
			return true;

		// Ensure all cells are not occupied
		PrintCell[] line = PrintUtil.convertToArray(page)[y];
		for (int i=x; i<(x+elem.getRequiredColumns()); i++) {
			if (line[i]!=null && !(line[i] instanceof EmptyCell)) {
				// Already occupied
				return false;
			}
		}

		return true;
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.print.TemplateController#add(de.rpgframework.print.LayoutGrid, de.rpgframework.print.LayoutElement, int, int)
	 */
	@Override
	public void add(LayoutGrid page, PDFPrintElement elem, int x, int y) {
		if (!canBeAdded(page, elem, x, y)) {
			logger.warn("Trying to add element at invalid position");
			return;
		}
		
		if (elem instanceof PDFPrintElement) {
			ElementCell comp = page.addComponent(x, y, ((PDFPrintElement)elem));
			comp.setDisplay(elem.render(comp.getSavedRenderOptions().getAsRenderingParameter()));
			logger.info("Added "+comp);
		} else {
			logger.warn("ToDO: add "+elem.getClass());
		}
		
	}

}
