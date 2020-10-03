/**
 * 
 */
package de.rpgframework.print.impl;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.rpgframework.core.RoleplayingSystem;
import de.rpgframework.print.ElementCell;
import de.rpgframework.print.EmptyCell;
import de.rpgframework.print.LayoutGrid;
import de.rpgframework.print.PDFPrintElement;
import de.rpgframework.print.PDFPrintElementFeature;
import de.rpgframework.print.PrintCell;
import de.rpgframework.print.PrintTemplate;
import de.rpgframework.print.PrintUtil;
import de.rpgframework.print.SavedRenderOptions;
import de.rpgframework.print.TemplateController;

/**
 * @author Stefan
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
	
	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.print.TemplateController#delete(de.rpgframework.print.LayoutGrid, de.rpgframework.print.PrintCell)
	 */
	@Override
	public void delete(LayoutGrid page, PrintCell cell) {
		page.deleteComponent(cell);
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.print.TemplateController#createPage(de.rpgframework.print.PrintTemplate)
	 */
	@Override
	public LayoutGrid createPage(PrintTemplate template) {
		LayoutGridImpl page = new LayoutGridImpl(6);
		logger.info("Create page "+template.size()+" in template");
		template.add(page);
		return page;
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.print.TemplateController#canDeletePage(de.rpgframework.print.PrintTemplate, de.rpgframework.print.LayoutGrid)
	 */
	@Override
	public boolean canDeletePage(PrintTemplate template, LayoutGrid page) {
		return (template.size()>0);
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.print.TemplateController#canBeDeleted(de.rpgframework.print.LayoutGrid, de.rpgframework.print.PrintCell)
	 */
	@Override
	public boolean canBeDeleted(LayoutGrid page, PrintCell cell) {
		if (cell==null)
			return false;
		
		return page.getComponents().contains(cell);
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.print.TemplateController#deletePage(de.rpgframework.print.PrintTemplate, de.rpgframework.print.LayoutGrid)
	 */
	@Override
	public void deletePage(PrintTemplate template, LayoutGrid page) {
		logger.info("Remove page "+template.indexOf(page)+" from template");
		template.remove(page);
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.print.TemplateController#addBackgroundImage(de.rpgframework.print.PrintTemplate, de.rpgframework.core.RoleplayingSystem, java.nio.file.Path)
	 */
	@Override
	public void addBackgroundImage(PrintTemplate template, RoleplayingSystem system, Path path) {
		// TODO Auto-generated method stub
		
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.print.TemplateController#canGrowVertical(de.rpgframework.print.LayoutGrid, de.rpgframework.print.PrintCell)
	 */
	@Override
	public boolean canGrowVertical(LayoutGrid page, PrintCell cell) {
		if (!(cell instanceof ElementCell))
			return false;
		
		ElementCell eCell = (ElementCell)cell;
		PDFPrintElement elem = eCell.getElement();

		if (!elem.hasFeature(PDFPrintElementFeature.EXPAND_VERTICAL))
			return false;

		return true;
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.print.TemplateController#growVertical(de.rpgframework.print.LayoutGrid, de.rpgframework.print.PrintCell)
	 */
	@Override
	public void growVertical(LayoutGrid page, PrintCell elem) {
		ElementCell cell = (ElementCell)elem;
		SavedRenderOptions opt = cell.getSavedRenderOptions();
		logger.debug("  grow vertical");
		opt.setVerticalGrow(opt.getVerticalGrow()+1);
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.print.TemplateController#canShrinkVertical(de.rpgframework.print.LayoutGrid, de.rpgframework.print.PrintCell)
	 */
	@Override
	public boolean canShrinkVertical(LayoutGrid page, PrintCell cell) {
		if (!(cell instanceof ElementCell))
			return false;
		
		ElementCell eCell = (ElementCell)cell;
		SavedRenderOptions opt = eCell.getSavedRenderOptions();
		return opt.getVerticalGrow()>0;
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.print.TemplateController#shrinkVertical(de.rpgframework.print.LayoutGrid, de.rpgframework.print.PrintCell)
	 */
	@Override
	public void shrinkVertical(LayoutGrid page, PrintCell elem) {
		ElementCell cell = (ElementCell)elem;
		SavedRenderOptions opt = cell.getSavedRenderOptions();
		logger.debug("  grow vertical");
		opt.setVerticalGrow(Math.max(0,opt.getVerticalGrow()-1));
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.print.TemplateController#canGrowHorizontal(de.rpgframework.print.LayoutGrid, de.rpgframework.print.PrintCell)
	 */
	@Override
	public boolean canGrowHorizontal(LayoutGrid page, PrintCell cell) {
		// TODO Auto-generated method stub
		return false;
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.print.TemplateController#growHorizontal(de.rpgframework.print.LayoutGrid, de.rpgframework.print.PrintCell)
	 */
	@Override
	public void growHorizontal(LayoutGrid page, PrintCell elem) {
		// TODO Auto-generated method stub
		ElementCell cell = (ElementCell)elem;
		SavedRenderOptions opt = cell.getSavedRenderOptions();
			logger.debug("  grow by "+cell.getElement().getNextHorizontalGrowth(cell.getSavedRenderOptions().getAsRenderingParameter())+" columns");
		opt.setVerticalGrow(opt.getVerticalGrow()+1);
	}

	@Override
	public boolean canShrinkHorizontal(LayoutGrid page, PrintCell cell) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void shrinkHorizontal(LayoutGrid page, PrintCell elem) {
		// TODO Auto-generated method stub
		
	}

}