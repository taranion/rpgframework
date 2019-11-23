/**
 * 
 */
package de.rpgframework.adventure;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author Stefan
 *
 */
public interface EnhancedAdventureXMLPlugin {

	//--------------------------------------------------------------------
	/**
	 * Return the XML namespace that this plugin serves
	 * @return
	 */
	public String getNamespace();
	
	//--------------------------------------------------------------------
	public AdventureDocumentElement domDecode(Element elem);
	
	//--------------------------------------------------------------------
	public Element domEncode(Document doc, AdventureDocumentElement toEncode);
	
}
