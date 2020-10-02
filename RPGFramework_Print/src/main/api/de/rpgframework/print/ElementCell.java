package de.rpgframework.print;

public interface ElementCell extends PrintCell {

	//--------------------------------------------------------------------
	public String getElementId();

	//--------------------------------------------------------------------
	public SavedRenderOptions getSavedRenderOptions();

//	public void setPDFPrintElement(PDFPrintElement elem);
	
	public PDFPrintElement getElement();
}