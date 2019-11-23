/**
 *
 */
package de.rpgframework.print;

/**
 * @author Stefan
 *
 */
public interface MultiRowCell extends PrintCell, Iterable<PrintCell> {

	//--------------------------------------------------------------------
	public boolean add(PrintCell value);

	//--------------------------------------------------------------------
	public void remove(PrintCell cell);

}
