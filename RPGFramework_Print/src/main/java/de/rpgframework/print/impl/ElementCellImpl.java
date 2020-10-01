/**
 *
 */
package de.rpgframework.print.impl;

import org.prelle.simplepersist.Attribute;
import org.prelle.simplepersist.Element;

import de.rpgframework.print.ElementCell;
import de.rpgframework.print.LayoutGrid;
import de.rpgframework.print.PDFPrintElement;
import de.rpgframework.print.PrintCell;
import de.rpgframework.print.SavedRenderOptions;


/**
 * @author Stefan
 *
 */
public class ElementCellImpl extends PrintCellImpl implements PrintCell, ElementCell {

	private transient PDFPrintElement element;
	
	@Attribute(name="id")
	private String elementReference;
	@Element
	private SavedRenderOptions options;

	//--------------------------------------------------------------------
	/*
	 * For persistence only
	 */
	public ElementCellImpl() {
		options = new SavedRenderOptions();
		super.type = Type.ELEMENT;
	}

	//--------------------------------------------------------------------
	public ElementCellImpl(LayoutGrid grid, int x, int y, PDFPrintElement value) {
		super(grid,x,y);
		elementReference = value.getId();
		super.type = Type.ELEMENT;
		options = new SavedRenderOptions();
		this.element = value;
		setDisplay( value.render(options.getAsRenderingParameter()) );
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.print.PrintCell#getWidth()
	 */
	@Override
	public int getWidth() {
		try {
			return element.getRequiredColumns();
		} catch (NullPointerException e) {
			throw new NullPointerException("Cannot detect width of NULL column with ID "+elementReference);
		}
	}

	//--------------------------------------------------------------------
	/**
	 * @see de.rpgframework.print.ElementCell#getElementId()
	 */
	@Override
	public String getElementId() {
		return elementReference;
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.print.ElementCell#getSavedRenderOptions()
	 */
	@Override
	public SavedRenderOptions getSavedRenderOptions() {
		return options;
	}

}
