/**
 *
 */
package de.rpgframework.print;

import java.nio.file.Path;
import java.util.List;

import de.rpgframework.core.RoleplayingSystem;

/**
 * @author Stefan
 *
 */
public interface PrintManager {

	public List<PrintTemplate> getAvailableTemplates(RoleplayingSystem system);

	public void saveTemplate(RoleplayingSystem system, PrintTemplate value);

	public PageDefinition createPageDefinition(int columns);

	public ElementCell createElementCell(PDFPrintElement value);

	public PrintTemplate createTemplate(List<PageDefinition> items);

	public void addBackgroundImage(RoleplayingSystem system, Path file);

}
