package de.rpgframework.print;

public interface ElementCell extends PrintCell {

	//--------------------------------------------------------------------
	/**
	 * @return the element
	 */
	public PDFPrintElement getElement();

	//--------------------------------------------------------------------
	/**
	 * @return the cachedImage
	 */
	public byte[] getCachedImage();

	//--------------------------------------------------------------------
	public String getElementId();

	//--------------------------------------------------------------------
	public SavedRenderOptions getSavedRenderOptions();

	//--------------------------------------------------------------------
	/**
	 * The rendering options changed and a fresh image must be rendered
	 */
	public void refresh();

}