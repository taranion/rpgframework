/**
 * 
 */
package org.prelle.rpgframework.products;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.rpgframework.core.RoleplayingSystemListConverter;
import org.prelle.simplepersist.AttribConvert;
import org.prelle.simplepersist.Attribute;
import org.prelle.simplepersist.Element;
import org.prelle.simplepersist.ElementList;
import org.prelle.simplepersist.Root;

import de.rpgframework.core.RoleplayingSystem;
import de.rpgframework.products.Adventure;
import de.rpgframework.products.Product;

/**
 * @author Stefan
 *
 */
@Root(name="product")
public class ProductImpl extends Localized implements Product {

	private transient Path directory;
	@Attribute
	private String id;
	@Attribute
	@AttribConvert(RoleplayingSystemListConverter.class)
	private List<RoleplayingSystem> rules;
	@Element
	private String title;
	@Element
	private String description;
	@ElementList(entry="adventure",type=AdventureImpl.class,convert=AdventureConverter.class)
	private List<Adventure> adventures;
	private transient byte[] cover;
	@Attribute
	private int year;
	@Attribute
	private int month;

	//--------------------------------------------------------------------
	public ProductImpl() {
		rules      = new ArrayList<>();
		adventures = new ArrayList<Adventure>();
	}

	//--------------------------------------------------------------------
	/**
	 * @see de.rpgframework.products.Product#getId()
	 */
	@Override
	public String getId() {
		return id;
	}

	//--------------------------------------------------------------------
	/**
	 * @see de.rpgframework.products.Product#getRoleplayingSystem()
	 */
	@Override
	public Collection<RoleplayingSystem> getRoleplayingSystem() {
		return rules;
	}

	//--------------------------------------------------------------------
	/**
	 * @see de.rpgframework.products.Product#getTitle()
	 */
	@Override
	public String getTitle() {
		return title;
	}

	//--------------------------------------------------------------------
	/**
	 * @see de.rpgframework.products.Product#getDescription()
	 */
	@Override
	public String getDescription() {
		return description;
	}

	//--------------------------------------------------------------------
	/**
	 * @see de.rpgframework.products.Product#getAdventures()
	 */
	@Override
	public Collection<Adventure> getAdventures() {
		return adventures;
	}

	//--------------------------------------------------------------------
	/**
	 * @see de.rpgframework.products.Product#getImage()
	 */
	@Override
	public byte[] getImage() {
		return cover;
	}
	
	//--------------------------------------------------------------------
	public void setImage(byte[] data) {
		this.cover = data;
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.products.Product#getYear()
	 */
	@Override
	public int getYear() {
		return year;
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.products.Product#getMonth()
	 */
	@Override
	public int getMonth() {
		return month;
	}

	//-------------------------------------------------------------------
	/**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Product other) {
		int cmp = ((Integer)year).compareTo(other.getYear());
		if (cmp!=0) return cmp; 
		cmp = ((Integer)month).compareTo(other.getMonth());
		if (cmp!=0) return cmp; 
		return title.compareTo(other.getTitle());
	}
	
	//--------------------------------------------------------------------
	public Path getBaseDirectory() {
		return directory;
	}
	
	//--------------------------------------------------------------------
	public void setBaseDirectory(Path baseDir) {
		directory = baseDir;
	}

	//--------------------------------------------------------------------
	public void addAdventure(AdventureImpl impl1) {
		adventures.add(impl1);
	}

}
