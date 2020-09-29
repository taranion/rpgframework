/**
 * 
 */
package de.rpgframework.print.impl;

import de.rpgframework.print.LayoutElement;
import de.rpgframework.print.PageDefinition;
import de.rpgframework.print.PrintTemplate;
import de.rpgframework.print.TemplateController;

/**
 * @author stefa
 *
 */
public class TemplateControllerImpl implements TemplateController {

	//---------------------------------------------------------
	public TemplateControllerImpl(PrintTemplate value) {
		
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.print.TemplateController#validate(de.rpgframework.print.PageDefinition)
	 */
	@Override
	public void validate(PageDefinition page) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean canBeAdded(PageDefinition page, LayoutElement elem, int x, int y) {
		// TODO Auto-generated method stub
		return false;
	}

}
