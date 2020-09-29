/**
 *
 */
package de.rpgframework.print;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.prelle.simplepersist.Attribute;
import org.prelle.simplepersist.ElementList;
import org.prelle.simplepersist.Root;

/**
 * @author Stefan
 *
 */
@Root(name="grid")
public class PageDefinition implements LayoutElement {

	@Attribute
	private int columns;

	@ElementList(entry="component",type=PositionedComponent.class,inline=true)
	private List<PositionedComponent> components;
	
	/** 
	 * Marks which columns are occupied.
	 * Recalculated on update
	 */
	private transient List<boolean[]> occupation = new ArrayList<boolean[]>();

	//--------------------------------------------------------------------
	public PageDefinition() {
		components = new ArrayList<>();
	}

	//--------------------------------------------------------------------
	public PageDefinition(int columns) {
		this.columns = columns;
		components = new ArrayList<>();
	}

	//---------------------------------------------------------
	/**
	 * @param x
	 * @param y
	 * @param ref
	 * @return
	 */
	public PositionedComponent addComponent(int x, int y, String ref) {
		PositionedComponent comp = new PositionedComponent();
		comp.setColumn(x);
		comp.setLine(y);
		comp.setElementReference(ref);
		comp.setOptions(new SavedRenderOptions());
		components.add(comp);
		return comp;
	}
	
	//---------------------------------------------------------
	public Collection<PositionedComponent> getComponents() {
		return new ArrayList<PositionedComponent>(components);
	}
	
	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.print.LayoutElement#getRequiredColumns()
	 */
	@Override
	public int getRequiredColumns() {
		return columns;
	}

	//---------------------------------------------------------
	/**
	 * @return the occupation
	 */
	public List<boolean[]> getOccupation() {
		return occupation;
	}

	//---------------------------------------------------------
	/**
	 * @param occupation the occupation to set
	 */
	public void setOccupation(List<boolean[]> occupation) {
		this.occupation = occupation;
	}

//	//--------------------------------------------------------------------
//	/**
//	 * @see de.rpgframework.print.PageDefinition#canBeAdded(int, int, de.rpgframework.print.PrintCell)
//	 */
//	@Override
//	public boolean canBeAdded(int col, int line, PrintCell toAdd) {
//		if (col+toAdd.getRequiredColumns()>columns)
//			return false;
//
//		PrintLine addTo = getLine(line);
//		if (addTo==null)
//			return true;
//		// Check if all in line is free
//		return !addTo.isOccupied(col, toAdd.getRequiredColumns());
//	}
//
//	//--------------------------------------------------------------------
//	/**
//	 * @see de.rpgframework.print.PageDefinition#add(int, de.rpgframework.print.PrintCell)
//	 */
//	@Override
//	public void add(int col, int line, PrintCell toAdd) {
//		logger.warn("TODO: add "+toAdd+" to "+col+","+line);
//		PrintLine addTo = getLine(line);
//		if (addTo==null) {
//			addTo = new PrintLineImpl(columns);
//			lines.add(addTo);
//		}
//		addTo.add(col, toAdd);
//	}
//
//	//--------------------------------------------------------------------
//	private PrintLine getLine(int line) {
//		if (line>=lines.size())
//			return null;
//		return lines.get(line);
//	}
//
//	//--------------------------------------------------------------------
//	public List<String> resolveIDs(Map<String, PDFPrintElement> map) {
//		List<String> notFound = new ArrayList<>();
//		for (PrintLine line : this) {
//			List<String> not = ((PrintLineImpl)line).resolveIDs(map);
//			for (String tmp : not) {
//				if (!notFound.contains(tmp))
//					notFound.add(tmp);
//			}
//		}
//		return notFound;
//	}
//
//	//--------------------------------------------------------------------
//	/**
//	 * @see de.rpgframework.print.PageDefinition#prependLine()
//	 */
//	@Override
//	public void prependLine() {
//		logger.debug("prepend a line");
//		lines.add(0, new PrintLineImpl(columns));
//
//	}
}
