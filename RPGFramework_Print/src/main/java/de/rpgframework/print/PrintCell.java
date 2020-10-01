/**
 *
 */
package de.rpgframework.print;

/**
 * @author Stefan
 *
 */
public interface PrintCell {
	
	public enum Type {
		EMPTY,
		/** A PDFPrintElement */
		ELEMENT,
		/** A child grid */
		GRID
	}
	
	//---------------------------------------------------------
	/**
	 * @return Width in columns
	 */
	public int getWidth();

	public int getX();
	public int getY();
	public LayoutGrid getGrid();
	public Type getType();
	
	//---------------------------------------------------------
	/** 
	 * Representation on screen.
	 * Not serialized, needs to be recreated
	 */
	public void setDisplay(Object value);
	public Object getDisplay();

}
