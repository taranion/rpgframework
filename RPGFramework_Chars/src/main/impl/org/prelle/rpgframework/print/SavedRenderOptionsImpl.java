/**
 *
 */
package org.prelle.rpgframework.print;

import org.prelle.simplepersist.Attribute;

import de.rpgframework.print.PDFPrintElement;
import de.rpgframework.print.PDFPrintElement.Orientation;
import de.rpgframework.print.PDFPrintElement.RenderingParameter;
import de.rpgframework.print.SavedRenderOptions;

/**
 * @author Stefan
 *
 */
public class SavedRenderOptionsImpl implements SavedRenderOptions {

	@Attribute
	private PDFPrintElement.Orientation orient;
	@Attribute(name="gvert")
	private int verticalGrow;
	@Attribute(name="hvert")
	private int horizontalGrow;
	@Attribute(name="index")
	private int index;
	@Attribute(name="variant")
	private int variant;

	//--------------------------------------------------------------------
	/**
	 */
	public SavedRenderOptionsImpl() {
		orient = Orientation.RIGHT;
	}

	//--------------------------------------------------------------------
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		if (o instanceof SavedRenderOptionsImpl) {
			SavedRenderOptionsImpl other = (SavedRenderOptionsImpl)o;
			if (horizontalGrow!=other.getHorizontalGrow()) return false;
			if (verticalGrow!=other.getVerticalGrow()) return false;
			if (index!=other.getSelectedIndex()) return false;
			if (variant!=other.getVariantIndex()) return false;
			if (orient==null && other.getOrientation()==null) return false;
			if (orient!=null && !orient.equals(other.getOrientation())) return false;
			return true;
		}
		return false;
	}

	//--------------------------------------------------------------------
	public String toString() {
		return "orient="+orient+", vert="+verticalGrow+", hori="+horizontalGrow;
	}

	//--------------------------------------------------------------------
	/**
	 * @see de.rpgframework.print.SavedRenderOptions#setOrientation(de.rpgframework.print.PDFPrintElement.Orientation)
	 */
	@Override
	public void setOrientation(Orientation value) {
		this.orient = value;
	}

	//--------------------------------------------------------------------
	/**
	 * @see de.rpgframework.print.SavedRenderOptions#getAsRenderingParameter()
	 */
	@Override
	public RenderingParameter getAsRenderingParameter() {
		RenderingParameter ret = new RenderingParameter();
		ret.setOrientation(orient);
		ret.setHorizontalGrowthOffset(horizontalGrow);
		ret.setVerticalGrowthOffset(verticalGrow);
		ret.setIndex(index);
		ret.setFilterOption(variant);
		return ret;
	}

	//--------------------------------------------------------------------
	/**
	 * @see de.rpgframework.print.SavedRenderOptions#getVerticalGrow()
	 */
	@Override
	public int getVerticalGrow() {
		return verticalGrow;
	}

	//--------------------------------------------------------------------
	/**
	 * @see de.rpgframework.print.SavedRenderOptions#setVerticalGrow(int)
	 */
	@Override
	public void setVerticalGrow(int verticalGrow) {
		this.verticalGrow = verticalGrow;
	}

	//--------------------------------------------------------------------
	/**
	 * @see de.rpgframework.print.SavedRenderOptions#getHorizontalGrow()
	 */
	@Override
	public int getHorizontalGrow() {
		return horizontalGrow;
	}

	//--------------------------------------------------------------------
	/**
	 * @see de.rpgframework.print.SavedRenderOptions#setHorizontalGrow(int)
	 */
	@Override
	public void setHorizontalGrow(int horizontalGrow) {
		this.horizontalGrow = horizontalGrow;
	}

	//--------------------------------------------------------------------
	/**
	 * @see de.rpgframework.print.SavedRenderOptions#getSelectedIndex()
	 */
	@Override
	public int getSelectedIndex() {
		return index;
	}

	//--------------------------------------------------------------------
	/**
	 * @see de.rpgframework.print.SavedRenderOptions#setSelectedIndex()
	 */
	@Override
	public void setSelectedIndex(int value) {
		this.index = value;
	}

	//--------------------------------------------------------------------
	/**
	 * @see de.rpgframework.print.SavedRenderOptions#getVariantIndex()
	 */
	@Override
	public int getVariantIndex() {
		return variant;
	}

	//--------------------------------------------------------------------
	/**
	 * @see de.rpgframework.print.SavedRenderOptions#setVariantIndex(int)
	 */
	@Override
	public void setVariantIndex(int value) {
		this.variant = value;
	}

	//--------------------------------------------------------------------
	/**
	 * @return the orient
	 */
	public PDFPrintElement.Orientation getOrientation() {
		return orient;
	}

}
