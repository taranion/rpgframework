package de.rpgframework.print_old;



public interface PageDefinition extends Iterable<PrintLine> {

	//--------------------------------------------------------------------
	public boolean canBeAdded(int col, int line, PrintCell toAdd);

	//--------------------------------------------------------------------
	public void add(int col, int line, PrintCell toAdd);

	//--------------------------------------------------------------------
	public void prependLine();

}