/**
 * 
 */
package de.rpgframework.print.impl;

import java.util.ArrayList;
import java.util.List;

import org.prelle.simplepersist.Attribute;
import org.prelle.simplepersist.ElementList;
import org.prelle.simplepersist.ElementListUnion;
import org.prelle.simplepersist.Root;

import de.rpgframework.print.ElementCell;
import de.rpgframework.print.LayoutGrid;
import de.rpgframework.print.PDFPrintElement;
import de.rpgframework.print.PrintCell;

/**
 * @author stefa
 *
 */
@Root(name = "page")
public class LayoutGridImpl implements LayoutGrid {

	@Attribute
	private int columns;

	@ElementListUnion(value= {
			@ElementList(entry="component",type=ElementCellImpl.class,inline=true),
			@ElementList(entry="grid",type=MultiRowCellImpl.class,inline=true)			
	})
	private List<PrintCell> components;
	
	//-------------------------------------------------------------------
	public LayoutGridImpl() {
		components = new ArrayList<PrintCell>();
	}
	
	//-------------------------------------------------------------------
	public LayoutGridImpl(int col) {
		columns = col;
		components = new ArrayList<PrintCell>();
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.print.LayoutGrid#getColumnCount()
	 */
	@Override
	public int getColumnCount() {
		return columns;
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.print.LayoutGrid#getComponents()
	 */
	@Override
	public List<PrintCell> getComponents() {
		return new ArrayList<PrintCell>(components);
	}

	//-------------------------------------------------------------------
	private void replace(List<PrintCell> line, int from, int to, PrintCell with) {
		List<PrintCell> toRemove = new ArrayList<PrintCell>();
		int insertAt = -1;
		for (PrintCell cell : line) {
			if (cell.getX()==from)
				insertAt = line.indexOf(cell);
			if (cell.getX()>=from && cell.getX()<to) {
				toRemove.add(cell);
			}
		}
		// Remove empty cells
		line.removeAll(toRemove);
		line.add(insertAt, with);
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.print.LayoutGrid#getAsLines()
	 */
	@Override
	public List<List<PrintCell>> getAsLines() {
		List<List<PrintCell>> lines = new ArrayList<List<PrintCell>>();
		for (PrintCell comp : components) {
			while (lines.size()<=comp.getY()) {
				// Create required empty line
				List<PrintCell> line = new ArrayList<PrintCell>();
				lines.add(line);
				for (int i=0; i<columns; i++) {
					line.add(new EmptyCellImpl(this, i, lines.indexOf(line)));
				}
			}
			// Now find correct line and replace empty cells
			List<PrintCell> line = lines.get(comp.getY());
			replace(line, comp.getX(), comp.getX()+comp.getWidth(), comp);
		}
		return lines;
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.print.LayoutGrid#addComponent(int, int, de.rpgframework.print.PDFPrintElement)
	 */
	@Override
	public ElementCell addComponent(int x, int y, PDFPrintElement elem) {
		ElementCellImpl cell = new ElementCellImpl(this, x, y, elem);
		components.add(cell);
		return cell;
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.print.LayoutGrid#deleteComponent(de.rpgframework.print.PrintCell)
	 */
	@Override
	public void deleteComponent(PrintCell cell) {
		components.remove(cell);
	}
}
