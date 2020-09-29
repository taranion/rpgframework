/**
 * 
 */
package de.rpgframework.print;

/**
 * @author Stefan Prelle
 *
 */
public interface TemplateController {
	
	public void validate(PageDefinition page);
	
	public boolean canBeAdded(PageDefinition page, LayoutElement elem, int x, int y);

}
