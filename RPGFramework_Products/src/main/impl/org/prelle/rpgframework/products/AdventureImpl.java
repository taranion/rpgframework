/**
 *
 */
package org.prelle.rpgframework.products;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import org.prelle.simplepersist.AttribConvert;
import org.prelle.simplepersist.Attribute;
import org.prelle.simplepersist.Root;

import de.rpgframework.RPGFrameworkLoader;
import de.rpgframework.core.RoleplayingSystem;
import de.rpgframework.products.Adventure;
import de.rpgframework.products.AdventureStory;
import de.rpgframework.products.Product;
import de.rpgframework.products.ProductServiceLoader;

/**
 * @author prelle
 *
 */
@Root(name="adventure")
public class AdventureImpl implements Adventure {

	private transient Path directory;
	@Attribute
	private String id;
	@Attribute
	private RoleplayingSystem rules;
	@Attribute
	@AttribConvert(AdventureStoryConverter.class)
	private AdventureStory story;

	//--------------------------------------------------------------------
	public AdventureImpl() {
	}

	//--------------------------------------------------------------------
	public AdventureImpl(String id, RoleplayingSystem rules, AdventureStory story, Path directory) {
		this.id    = id;
		this.rules = rules;
		this.story = story;
		this.directory = directory;
	}

	//--------------------------------------------------------------------
	public boolean equals(Object o) {
		if (o instanceof AdventureImpl) {
			AdventureImpl other = (AdventureImpl)o;
			if (!id.equals(other.getId())) return false;
			if (rules!=other.getRules()) return false;
			if (story!=null && other.getStory()==null) return false;
			if (story==null && other.getStory()!=null) return false;
			if (story.getID()==null && other.getStory().getID()!=null) return false;
			if (story.getID()==other.getStory().getID()) return true;
			return story.getID().equals(other.getStory().getID());
		}
		return false;
	}

	//--------------------------------------------------------------------
	/**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Adventure other) {
		return getTitle().compareTo(other.getTitle());
	}

	//--------------------------------------------------------------------
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getTitle();
	}

	//--------------------------------------------------------------------
	/**
	 * @see de.rpgframework.products.rpgtool.base.Adventure#getBaseDirectory()
	 */
	@Override
	public Path getBaseDirectory() {
		return directory;
	}

	//--------------------------------------------------------------------
	public void setBaseDirectory(Path baseDir) {
		directory = baseDir;
	}

	//--------------------------------------------------------------------
	/**
	 * @see de.rpgframework.products.rpgtool.base.Adventure#getRules()
	 */
	@Override
	public RoleplayingSystem getRules() {
		return rules;
	}

	//--------------------------------------------------------------------
	/**
	 * @see de.rpgframework.products.Adventure#getId()
	 */
	@Override
	public String getId() {
		return id;
	}

	//-------------------------------------------------------------------
	@Override
	public AdventureStory getStory() {
		return story;
	}

	//-------------------------------------------------------------------
	@Override
	public String getTitle() {
		if (story!=null)
			return getStory().getTitle();
		return id;
	}

	//-------------------------------------------------------------------
	@Override
	public String getDescription() {
		return getStory().getDescription();
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.products.Adventure#getCover()
	 */
	@Override
	public byte[] getCover() {
		List<Product> products = ProductServiceLoader.getInstance().getProductWith(this);
		if (products.isEmpty())
			return null;
		Collections.sort(products);
		for (Product prod : products) {
			if (prod.getImage()!=null)
				return prod.getImage();
		}
		return null;
	}

}
