package de.rpgframework.print_old;


public interface PrintLine extends Iterable<PrintCell> {

	//--------------------------------------------------------------------
	public void add(int col, PrintCell value);

	//--------------------------------------------------------------------
	public boolean isOccupied(int x, int width);

	//--------------------------------------------------------------------
	public int getRequiredColumns();

	//--------------------------------------------------------------------
	public void remove(PrintCell cell);

	//--------------------------------------------------------------------
	public MultiRowCell convertToMiniTable(PrintCell cell);

	//--------------------------------------------------------------------
	/**
	 * Is there an empty cell to the right of this cell?
	 */
	public boolean canGrowHorizontal(ElementCell cell);

	//--------------------------------------------------------------------
	public void growHorizontal(ElementCell cell);

	//--------------------------------------------------------------------
	public void shrinkHorizontal(ElementCell cell);

}