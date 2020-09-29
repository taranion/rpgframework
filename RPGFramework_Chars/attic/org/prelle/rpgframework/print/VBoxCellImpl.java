/**
 *
 */
package org.prelle.rpgframework.print;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.rpgframework.print.PrintLine;
import de.rpgframework.print.VBoxCell;

/**
 * @author Stefan
 *
 */
public class VBoxCellImpl implements VBoxCell {

	private List<PrintLine> lines;

	//--------------------------------------------------------------------
	public VBoxCellImpl() {
		lines = new ArrayList<PrintLine>();
	}

	//--------------------------------------------------------------------
	/**
	 * @see de.rpgframework.print.VBoxCell#iterator()
	 */
	@Override
	public Iterator<PrintLine> iterator() {
		return lines.iterator();
	}

	//--------------------------------------------------------------------
	/**
	 * @see de.rpgframework.print.VBoxCell#add(org.prelle.rpgframework.print.PrintLineImpl)
	 */
	@Override
	public void add(PrintLine line) {
		this.lines.add(line);
	}

	//--------------------------------------------------------------------
	/**
	 * @see de.rpgframework.print.PrintCell#getRequiredColumns()
	 */
	@Override
	public int getRequiredColumns() {
		return lines.stream().mapToInt(e -> e.getRequiredColumns()).max().getAsInt();
	}

}
