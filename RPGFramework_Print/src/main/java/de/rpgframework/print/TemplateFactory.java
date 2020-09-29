/**
 * 
 */
package de.rpgframework.print;

import java.util.Map;

import de.rpgframework.print.impl.TemplateControllerImpl;

/**
 * @author stefa
 *
 */
public class TemplateFactory {
	
	public static TemplateController newTemplateController(PrintTemplate template, Map<String,PDFPrintElement> elementMap) {
		return new TemplateControllerImpl(template, elementMap);
	}

}
