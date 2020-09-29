/**
 * 
 */
package de.rpgframework.print;

import org.prelle.simplepersist.Attribute;
import org.prelle.simplepersist.Element;

/**
 * @author Stefan
 *
 */
public class PositionedComponent {

	@Attribute(name="id")
	private String elementReference;
	@Element
	private SavedRenderOptions options;
	
	@Attribute(name="x")
	private int column;
	@Attribute(name="y")
	private int line;
	//---------------------------------------------------------
	/**
	 * @return the elementReference
	 */
	public String getElementReference() {
		return elementReference;
	}
	//---------------------------------------------------------
	/**
	 * @param elementReference the elementReference to set
	 */
	public void setElementReference(String elementReference) {
		this.elementReference = elementReference;
	}
	//---------------------------------------------------------
	/**
	 * @return the options
	 */
	public SavedRenderOptions getOptions() {
		return options;
	}
	//---------------------------------------------------------
	/**
	 * @param options the options to set
	 */
	public void setOptions(SavedRenderOptions options) {
		this.options = options;
	}
	//---------------------------------------------------------
	/**
	 * @return the column
	 */
	public int getColumn() {
		return column;
	}
	//---------------------------------------------------------
	/**
	 * @param column the column to set
	 */
	public void setColumn(int column) {
		this.column = column;
	}
	//---------------------------------------------------------
	/**
	 * @return the line
	 */
	public int getLine() {
		return line;
	}
	//---------------------------------------------------------
	/**
	 * @param line the line to set
	 */
	public void setLine(int line) {
		this.line = line;
	}

}
