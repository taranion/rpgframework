/**
 * 
 */
package de.rpgframework.print;

import java.util.ArrayList;
import java.util.List;

import de.rpgframework.print.impl.EmptyCellImpl;

/**
 * @author stefa
 *
 */
public class PrintUtil {

	//---------------------------------------------------------
	/**
	 * Convert the the page with the list of components to a
	 * two-dimensional array.
	 */
	public static PrintCell[][] convertToArray(LayoutGrid page) {
		List<PrintCell[]> lines = new ArrayList<PrintCell[]>();
		for (PrintCell comp : page.getComponents()) {
			while (lines.size()<=comp.getY()) {
				// Create required empty line
				PrintCell[] line = new PrintCell[page.getColumnCount()];
				lines.add(line);
				for (int i=0; i<line.length; i++) {
					line[i]=new EmptyCellImpl(page, i, lines.indexOf(line));
				}
			}
			// Now find correct line and replace empty cells
			PrintCell[] line = lines.get(comp.getY());
			for (int i=comp.getX(); i<(comp.getX()+comp.getWidth()); i++) {
				line[i] = comp;
			}			
		}
		
		PrintCell[][] ret = new PrintCell[lines.size()][];
		ret = lines.toArray(ret);
		return ret;
	}

}
