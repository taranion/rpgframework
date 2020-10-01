/**
 * 
 */
package de.rpgframework.print;

import java.util.List;

/**
 * @author Stefan Prelle
 *
 */
public interface TemplateController {
	
	//---------------------------------------------------------
	/**
	 * @param page
	 * @return List of found problems
	 */
	public List<String> validate(LayoutGrid page);
	
	//---------------------------------------------------------
	public boolean canBeAdded(LayoutGrid page, PDFPrintElement elem, int x, int y);

	//---------------------------------------------------------
	public void add(LayoutGrid page, PDFPrintElement elem, int x, int y);
	
}
