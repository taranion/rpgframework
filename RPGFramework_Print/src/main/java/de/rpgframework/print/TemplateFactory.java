/**
 * 
 */
package de.rpgframework.print;

import java.util.Map;

import de.rpgframework.print.impl.LayoutGridImpl;
import de.rpgframework.print.impl.TemplateControllerImpl;

/**
 * @author stefa
 *
 */
public class TemplateFactory {
	
	public static TemplateController newTemplateController(LayoutGrid page, Map<String,PDFPrintElement> elementMap) {
		return new TemplateControllerImpl(page, elementMap);
	}
	
	public static LayoutGrid createPageDefinition(int columns) {
		return new LayoutGridImpl(columns);
	}

}
