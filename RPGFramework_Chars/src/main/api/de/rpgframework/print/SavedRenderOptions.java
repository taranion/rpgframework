/**
 *
 */
package de.rpgframework.print;

/**
 * @author Stefan
 *
 */
public interface SavedRenderOptions {

	public void setOrientation(PDFPrintElement.Orientation value);

	public void setHorizontalGrow(int value);

	public int getHorizontalGrow();

	public void setVerticalGrow(int value);

	public int getVerticalGrow();

	public int getSelectedIndex();

	public void setSelectedIndex(int value);

	public int getVariantIndex();

	public void setVariantIndex(int value);

	public PDFPrintElement.RenderingParameter getAsRenderingParameter();

}
