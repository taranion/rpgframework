/**
 *
 */
package org.prelle.rpgframework.print;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.prelle.simplepersist.Attribute;
import org.prelle.simplepersist.ElementList;
import org.prelle.simplepersist.Root;

import de.rpgframework.print.PDFPrintElement;
import de.rpgframework.print.PageDefinition;
import de.rpgframework.print.PrintCell;
import de.rpgframework.print.PrintLine;

/**
 * @author Stefan
 *
 */
@Root(name="page")
//@ElementList(entry="linedef",type=PrintLineImpl.class)
public class PageDefinitionImpl implements PageDefinition {

	private final static Logger logger = LogManager.getLogger("rpgframework.print");

	@Attribute
	private int columns;

	@ElementList(entry="linedef",type=PrintLineImpl.class,inline=true)
	private List<PrintLine> lines;

	//--------------------------------------------------------------------
	public PageDefinitionImpl() {
		lines = new ArrayList<>();
	}

	//--------------------------------------------------------------------
	public PageDefinitionImpl(int columns) {
		this.columns = columns;
		lines = new ArrayList<>();
	}

	//--------------------------------------------------------------------
	/**
	 * @see java.util.AbstractList#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		if (o instanceof PageDefinitionImpl) {
			PageDefinitionImpl other = (PageDefinitionImpl)o;
			if (columns!=other.getColumns()) return false;
			return lines.equals(other.getLines());
		}
		return false;
	}

	//--------------------------------------------------------------------
	private int getColumns() {
		return columns;
	}

	//--------------------------------------------------------------------
	private List<PrintLine> getLines() {
		return lines;
	}

	//--------------------------------------------------------------------
	/**
	 * @see de.rpgframework.print.PageDefinition#iterator()
	 */
	@Override
	public Iterator<PrintLine> iterator() {
		return lines.iterator();
	}

	//--------------------------------------------------------------------
	/**
	 * @see de.rpgframework.print.PageDefinition#canBeAdded(int, int, de.rpgframework.print.PrintCell)
	 */
	@Override
	public boolean canBeAdded(int col, int line, PrintCell toAdd) {
		if (col+toAdd.getRequiredColumns()>columns)
			return false;

		PrintLine addTo = getLine(line);
		if (addTo==null)
			return true;
		// Check if all in line is free
		return !addTo.isOccupied(col, toAdd.getRequiredColumns());
	}

	//--------------------------------------------------------------------
	/**
	 * @see de.rpgframework.print.PageDefinition#add(int, de.rpgframework.print.PrintCell)
	 */
	@Override
	public void add(int col, int line, PrintCell toAdd) {
		logger.warn("TODO: add "+toAdd+" to "+col+","+line);
		PrintLine addTo = getLine(line);
		if (addTo==null) {
			addTo = new PrintLineImpl(columns);
			lines.add(addTo);
		}
		addTo.add(col, toAdd);
	}

	//--------------------------------------------------------------------
	private PrintLine getLine(int line) {
		if (line>=lines.size())
			return null;
		return lines.get(line);
	}

	//--------------------------------------------------------------------
	public List<String> resolveIDs(Map<String, PDFPrintElement> map) {
		List<String> notFound = new ArrayList<>();
		for (PrintLine line : this) {
			List<String> not = ((PrintLineImpl)line).resolveIDs(map);
			for (String tmp : not) {
				if (!notFound.contains(tmp))
					notFound.add(tmp);
			}
		}
		return notFound;
	}

	//--------------------------------------------------------------------
	/**
	 * @see de.rpgframework.print.PageDefinition#prependLine()
	 */
	@Override
	public void prependLine() {
		logger.debug("prepend a line");
		lines.add(0, new PrintLineImpl(columns));

	}
}
