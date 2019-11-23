package org.prelle.rpgframework.jfx;

import javafx.scene.Node;

public interface IHasDescriptionPane {

	//-------------------------------------------------------------------
	public void setDescriptionHeading(String value);
 
	//-------------------------------------------------------------------
	public void setDescriptionPageRef(String value);
	 
	//-------------------------------------------------------------------
	/**
	 * For finer control about the descriptive element, set your own node
	 * describing the element. Use alternatively to setDescriptionText
	 */
	public void setDescriptionNode(Node value);
	 
	//-------------------------------------------------------------------
	/**
	 * Set the wrapped description text. Use alternatively to setDescriptionNode
	 */
	public void setDescriptionText(String value);
	
}
