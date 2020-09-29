/**
 *
 */
package org.prelle.rpgframework.print;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.prelle.simplepersist.Attribute;
import org.prelle.simplepersist.ElementList;
import org.prelle.simplepersist.Root;

import de.rpgframework.print.PDFPrintElement;
import de.rpgframework.print.PageDefinition;
import de.rpgframework.print.PrintTemplate;

/**
 * @author Stefan
 *
 */
@SuppressWarnings("serial")
@Root(name="printtemplate")
@ElementList(entry="page",type=PageDefinitionImpl.class)
public class PrintTemplateImpl extends ArrayList<PageDefinition> implements PrintTemplate {

	@Attribute(name="bgfile")
	private String bgfilename;

	private transient String name;
	private transient Path backgroundImage;

	//--------------------------------------------------------------------
	public PrintTemplateImpl() {
	}

	//--------------------------------------------------------------------
	public PrintTemplateImpl(List<PageDefinition> def) {
		super.addAll(def);
	}

	//--------------------------------------------------------------------
	/**
	 * @see java.util.AbstractList#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		if (o instanceof PrintTemplate) {
			PrintTemplateImpl other = (PrintTemplateImpl)o;
			if (bgfilename==null && other.getBackgroundImageFileName()!=null) return false;
			if (bgfilename!=null && !bgfilename.equals(other.getBackgroundImageFileName())) return false;
			return super.equals(other);
		}
		return false;
	}

	//--------------------------------------------------------------------
	public String toString() {
		return "PrintTemplate("+name+")";
	}

	//--------------------------------------------------------------------
	/**
	 * @see de.rpgframework.print.PrintTemplate#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	//--------------------------------------------------------------------
	/**
	 * @see de.rpgframework.print.PrintTemplate#setName(java.lang.String)
	 */
	@Override
	public void setName(String name) {
		this.name = name;
	}

	//--------------------------------------------------------------------
	/**
	 * @see de.rpgframework.print.PrintTemplate#setPages(java.util.List)
	 */
	@Override
	public void setPages(List<PageDefinition> items) {
		clear();
		this.addAll(items);
	}

	//--------------------------------------------------------------------
	/**
	 * @see de.rpgframework.print.PrintTemplate#resolveIDs(java.util.Map)
	 */
	@Override
	public List<String> resolveIDs(List<PDFPrintElement> elements) {
		// Build hashmap
		Map<String, PDFPrintElement> map = new HashMap<>();
		for (PDFPrintElement elem : elements)
			map.put(elem.getId(), elem);

		List<String> notFound = new ArrayList<>();
		for (PageDefinition page : this) {
			List<String> not = ((PageDefinitionImpl)page).resolveIDs(map);
			for (String tmp : not) {
				if (!notFound.contains(tmp))
					notFound.add(tmp);
			}
		}
		return notFound;
	}

	//--------------------------------------------------------------------
	public String getBackgroundImageFileName() {
		return bgfilename;
	}

	//--------------------------------------------------------------------
	/**
	 * @see de.rpgframework.print.PrintTemplate#setBackgroundImage(java.nio.file.Path)
	 */
	@Override
	public void setBackgroundImage(Path path) {
		this.backgroundImage = path;
		bgfilename = path.getFileName().toString();
	}

	//--------------------------------------------------------------------
	/**
	 * @see de.rpgframework.print.PrintTemplate#getBackgroundImage()
	 */
	@Override
	public Path getBackgroundImage() {
		return backgroundImage;
	}

}
