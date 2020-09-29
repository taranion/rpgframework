/**
 *
 */
package org.prelle.rpgframework.print;

import org.prelle.simplepersist.Attribute;
import org.prelle.simplepersist.Element;

import de.rpgframework.print.ElementCell;
import de.rpgframework.print.PDFPrintElement;
import de.rpgframework.print.PDFPrintElement.RenderingParameter;
import de.rpgframework.print.PrintCell;
import de.rpgframework.print.SavedRenderOptions;


/**
 * @author Stefan
 *
 */
public class ElementCellImpl implements PrintCell, ElementCell {

	@Attribute(name="ref")
	private String elementReference;
	@Element
	private SavedRenderOptionsImpl options;

	private transient PDFPrintElement element;
	private transient byte[] cachedImage;

	//--------------------------------------------------------------------
	/*
	 * For persistence only
	 */
	public ElementCellImpl() {
		options = new SavedRenderOptionsImpl();
	}

	//--------------------------------------------------------------------
	public ElementCellImpl(PDFPrintElement value) {
		elementReference = value.getId();
		this.element = value;
		options = new SavedRenderOptionsImpl();
		cachedImage = value.render(new RenderingParameter());
	}

	//--------------------------------------------------------------------
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		if (o instanceof ElementCellImpl) {
			ElementCellImpl other = (ElementCellImpl)o;
			if (elementReference==null && other.getElementId()!=null) return false;
			if (elementReference!=null && !elementReference.equals(other.getElementId())) return false;
			return options.equals(other.getSavedRenderOptions());
		}
		return false;
	}

	//--------------------------------------------------------------------
	/**
	 * @see de.rpgframework.print.ElementCell#getElement()
	 */
	@Override
	public PDFPrintElement getElement() {
		return element;
	}

	//--------------------------------------------------------------------
	public void setElement(PDFPrintElement value) {
		if (!value.getId().equals(elementReference))
			throw new IllegalArgumentException("Element must be of ID "+elementReference);
		this.element = value;
	}

	//--------------------------------------------------------------------
	/**
	 * @see de.rpgframework.print.ElementCell#getCachedImage()
	 */
	@Override
	public byte[] getCachedImage() {
		if (cachedImage==null && element!=null) {
			refresh();
		}
		return cachedImage;
	}

	//--------------------------------------------------------------------
	/**
	 * @see de.rpgframework.print.ElementCell#getRequiredColumns()
	 */
	@Override
	public int getRequiredColumns() {
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

	@Override
	public SavedRenderOptions getSavedRenderOptions() {
		return options;
	}

	//--------------------------------------------------------------------
	/**
	 * @see de.rpgframework.print.ElementCell#refresh()
	 */
	@Override
	public void refresh() {
		cachedImage = element.render(options.getAsRenderingParameter());
	}

}
