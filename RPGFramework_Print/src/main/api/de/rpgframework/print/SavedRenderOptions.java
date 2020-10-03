/**
 *
 */
package de.rpgframework.print;

import org.prelle.simplepersist.Attribute;

import de.rpgframework.print.PDFPrintElement.Orientation;
import de.rpgframework.print.PDFPrintElement.RenderingParameter;

/**
 * @author Stefan
 *
 */
public class SavedRenderOptions {

	@Attribute
	private Orientation orient;
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
	public SavedRenderOptions() {
		orient = Orientation.RIGHT;
	}

	//--------------------------------------------------------------------
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		if (o instanceof SavedRenderOptions) {
			SavedRenderOptions other = (SavedRenderOptions)o;
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
		return "orient="+orient+", vert="+verticalGrow+", hori="+horizontalGrow+", index="+index+", filter="+variant;
	}

	//--------------------------------------------------------------------
	public void setOrientation(Orientation value) {
		this.orient = value;
	}

	//--------------------------------------------------------------------
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
	public int getVerticalGrow() {
		return verticalGrow;
	}

	//--------------------------------------------------------------------
	public void setVerticalGrow(int verticalGrow) {
		this.verticalGrow = verticalGrow;
	}

	//--------------------------------------------------------------------
	public int getHorizontalGrow() {
		return horizontalGrow;
	}

	//--------------------------------------------------------------------
	public void setHorizontalGrow(int horizontalGrow) {
		this.horizontalGrow = horizontalGrow;
	}

	//--------------------------------------------------------------------
	public int getSelectedIndex() {
		return index;
	}

	//--------------------------------------------------------------------
	public void setSelectedIndex(int value) {
		this.index = value;
	}

	//--------------------------------------------------------------------
	public int getVariantIndex() {
		return variant;
	}

	//--------------------------------------------------------------------
	public void setVariantIndex(int value) {
		this.variant = value;
	}

	//--------------------------------------------------------------------
	/**
	 * @return the orient
	 */
	public Orientation getOrientation() {
		return orient;
	}

}
