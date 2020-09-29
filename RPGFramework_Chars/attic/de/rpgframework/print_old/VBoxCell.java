package de.rpgframework.print_old;

import java.util.Iterator;

public interface VBoxCell extends Iterable<PrintLine>, PrintCell {

	//--------------------------------------------------------------------
	/**
	 * @see java.util.ArrayList#iterator()
	 */
	public Iterator<PrintLine> iterator();

	//--------------------------------------------------------------------
	public void add(PrintLine line);

}