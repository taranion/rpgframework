/**
 * 
 */
package de.rpgframework.adventure;

/**
 * This is the superclass for all elements that may be contained
 * in an enhanced adventure. Those elements are assigned to a
 * specific namespace - either the EAML base namespace or one of
 * the plugins
 * 
 * @author Stefan
 *
 */
public interface AdventureDocumentElement {

	//--------------------------------------------------------------------
	/**
	 * Get the FPI of the namespace this element belongs to
	 * @return formal public identifier
	 */
	public String getNamespace();
	
}
