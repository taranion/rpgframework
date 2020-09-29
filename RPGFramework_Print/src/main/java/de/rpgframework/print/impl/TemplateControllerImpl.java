/**
 * 
 */
package de.rpgframework.print.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.rpgframework.print.LayoutElement;
import de.rpgframework.print.PDFPrintElement;
import de.rpgframework.print.PageDefinition;
import de.rpgframework.print.PositionedComponent;
import de.rpgframework.print.PrintTemplate;
import de.rpgframework.print.TemplateController;

/**
 * @author stefa
 *
 */
public class TemplateControllerImpl implements TemplateController {

	private Map<String,PDFPrintElement> elementMap;
	
	//---------------------------------------------------------
	public TemplateControllerImpl(PrintTemplate value, Map<String,PDFPrintElement> elementMap) {
		this.elementMap = elementMap;
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.print.TemplateController#validate(de.rpgframework.print.PageDefinition)
	 */
	@Override
	public void validate(PageDefinition page) {
		List<String> errors = new ArrayList<String>();
		
		List<boolean[]> occupation = new ArrayList<boolean[]>();
		// At least one line
		occupation.add(new boolean[page.getRequiredColumns()]);
		// Now calculate
		component:
		for (PositionedComponent comp : page.getComponents()) {
			// Ensure line is present
			while (comp.getLine()>=occupation.size()) {
				occupation.add(new boolean[page.getRequiredColumns()]);				
			}
			// Get element
			PDFPrintElement elem = elementMap.get(comp.getElementReference());
			if (elem==null) {
				errors.add("Unknown element '"+comp.getElementReference()+"'");
				continue;
			}
			// Get requested line
			boolean[] line = occupation.get(comp.getLine());
			for (int i=0; i<elem.getRequiredColumns(); i++) {
				int x=comp.getColumn()+i;
				if (x>=line.length) {
					errors.add("Element '"+comp.getElementReference()+"' not within page bounds");
					break;
				} else {
					line[x] = true;
				}
			}
		}
		page.setOccupation(occupation);
		
	}

	@Override
	public boolean canBeAdded(PageDefinition page, LayoutElement elem, int x, int y) {
		// TODO Auto-generated method stub
		return false;
	}

}
