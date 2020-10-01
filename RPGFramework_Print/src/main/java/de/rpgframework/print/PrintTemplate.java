package de.rpgframework.print;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.prelle.simplepersist.Attribute;
import org.prelle.simplepersist.ElementList;
import org.prelle.simplepersist.Root;

import de.rpgframework.print.impl.LayoutGridImpl;

/**
 * @author Stefan
 *
 */
@SuppressWarnings("serial")
@Root(name="printtemplate")
@ElementList(entry="page",type=LayoutGridImpl.class)
public class PrintTemplate extends ArrayList<LayoutGrid> {

	@Attribute(name="bgfile")
	private String bgfilename;

	private transient String name;
	private transient Path backgroundImage;

	//--------------------------------------------------------------------
	public PrintTemplate() {
	}

	//--------------------------------------------------------------------
	public PrintTemplate(List<LayoutGrid> def) {
		super.addAll(def);
	}

	//--------------------------------------------------------------------
	/**
	 * @see java.util.AbstractList#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		if (o instanceof PrintTemplate) {
			PrintTemplate other = (PrintTemplate)o;
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
	public String getName() {
		return name;
	}

	//--------------------------------------------------------------------
	public void setName(String name) {
		this.name = name;
	}

	//--------------------------------------------------------------------
	public void setPages(List<LayoutGrid> items) {
		clear();
		this.addAll(items);
	}

	//--------------------------------------------------------------------
	public String getBackgroundImageFileName() {
		return bgfilename;
	}

	//--------------------------------------------------------------------
	public void setBackgroundImage(Path path) {
		this.backgroundImage = path;
		bgfilename = path.getFileName().toString();
	}

	//--------------------------------------------------------------------
	public Path getBackgroundImage() {
		return backgroundImage;
	}

}
