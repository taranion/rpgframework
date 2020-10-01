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

	public PrintTemplate createTemplate(List<LayoutGrid> items);

	public void addBackgroundImage(RoleplayingSystem system, Path file);

}
