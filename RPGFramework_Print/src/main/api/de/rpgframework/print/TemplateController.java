/**
 * 
 */
package de.rpgframework.print;

import java.nio.file.Path;
import java.util.List;

import de.rpgframework.core.RoleplayingSystem;

/**
 * @author Stefan Prelle
 *
 */
public interface TemplateController {
	
	//---------------------------------------------------------
	/**
	 * @param page
	 * @return List of found problems
	 */
	public List<String> validate(LayoutGrid page);
	
	//---------------------------------------------------------
	public boolean canBeAdded(LayoutGrid page, PDFPrintElement elem, int x, int y);

	//---------------------------------------------------------
	public void add(LayoutGrid page, PDFPrintElement elem, int x, int y);
	
	//---------------------------------------------------------
	public boolean canBeDeleted(LayoutGrid page, PrintCell cell);

	//---------------------------------------------------------
	public void delete(LayoutGrid page, PrintCell elem);

	public LayoutGrid createPage(PrintTemplate template);

	public boolean canDeletePage(PrintTemplate template, LayoutGrid page);
	
	public void deletePage(PrintTemplate template, LayoutGrid page);

	//---------------------------------------------------------
	public void addBackgroundImage(PrintTemplate template, RoleplayingSystem system, Path path);
	
	//---------------------------------------------------------
	public boolean canGrowVertical(LayoutGrid page, PrintCell cell);
	public void growVertical(LayoutGrid page, PrintCell elem);
	
	//---------------------------------------------------------
	public boolean canShrinkVertical(LayoutGrid page, PrintCell cell);
	public void shrinkVertical(LayoutGrid page, PrintCell elem);
	
	//---------------------------------------------------------
	public boolean canGrowHorizontal(LayoutGrid page, PrintCell cell);
	public void growHorizontal(LayoutGrid page, PrintCell elem);
	
	//---------------------------------------------------------
	public boolean canShrinkHorizontal(LayoutGrid page, PrintCell cell);
	public void shrinkHorizontal(LayoutGrid page, PrintCell elem);
	
	//---------------------------------------------------------
	public boolean hasFilter(LayoutGrid page, PrintCell cell);
	public void setFilter(LayoutGrid page, ElementCell elem, int filter);
	
	//---------------------------------------------------------
	public boolean canPick(LayoutGrid page, PrintCell cell);
	public void select(LayoutGrid page, ElementCell elem, int index);
	
}
