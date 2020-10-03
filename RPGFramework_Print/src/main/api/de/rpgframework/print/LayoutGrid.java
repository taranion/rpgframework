/**
 * 
 */
package de.rpgframework.print;

import java.util.List;
import java.util.Map;

/**
 * @author stefa
 *
 */
public interface LayoutGrid {
	
	public int getColumnCount();
	
	public List<PrintCell> getComponents();

	public List<List<PrintCell>> getAsLines();
	 
	public ElementCell addComponent(int x, int y, PDFPrintElement elem);

	public void deleteComponent(PrintCell cell);

	public List<String> resolve(Map<String, PDFPrintElement> elementMap);
	
}
