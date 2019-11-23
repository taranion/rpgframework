/**
 *
 */
package de.rpgframework.print;

import java.nio.file.Path;
import java.util.List;

/**
 * @author Stefan
 *
 */
public interface PrintTemplate extends Iterable<PageDefinition>{

	//--------------------------------------------------------------------
	public String getName();

	//--------------------------------------------------------------------
	public void setName(String name);

	//--------------------------------------------------------------------
	public void setPages(List<PageDefinition> items);

	//--------------------------------------------------------------------
	/**
	 * Resolve all internal references of element IDs with the corresponding
	 * elements
	 * @return IDs of elements that failed and have been removed
	 */
	public List<String> resolveIDs(List<PDFPrintElement> elements);

	//--------------------------------------------------------------------
	public void setBackgroundImage(Path path);

	//--------------------------------------------------------------------
	/**
	 * Get the file location of the background image - or null, if there
	 * is no user chosen background image
	 */
	public Path getBackgroundImage();

}
