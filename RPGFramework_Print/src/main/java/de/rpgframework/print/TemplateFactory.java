/**
 * 
 */
package de.rpgframework.print;

import de.rpgframework.print.impl.TemplateControllerImpl;

/**
 * @author stefa
 *
 */
public class TemplateFactory {
	
	public static TemplateController newTemplateController(PrintTemplate template) {
		return new TemplateControllerImpl(template);
	}

}
