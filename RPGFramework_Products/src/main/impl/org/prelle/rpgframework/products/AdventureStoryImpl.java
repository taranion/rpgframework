/**
 *
 */
package org.prelle.rpgframework.products;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.prelle.simplepersist.Attribute;
import org.prelle.simplepersist.ElementList;
import org.prelle.simplepersist.Root;

import de.rpgframework.products.AdventureStory;

/**
 * @author Stefan
 *
 */
@Root(name="advstory")
public class AdventureStoryImpl implements AdventureStory {

	@Attribute
	private String id;
    @ElementList(entry="advcontent", type=LocalizedAdventureContent.class, inline=true)
	private List<LocalizedAdventureContent> content;
	@Attribute(required=false)
	private String campaign;

	//--------------------------------------------------------------------
	public AdventureStoryImpl() {
		content = new ArrayList<LocalizedAdventureContent>();
	}

	//--------------------------------------------------------------------
	public AdventureStoryImpl(String id) {
		this.id = id;
		content = new ArrayList<LocalizedAdventureContent>();
	}

	//--------------------------------------------------------------------
	public boolean equals(Object o) {
		if (o instanceof AdventureStoryImpl) {
			AdventureStoryImpl other = (AdventureStoryImpl)o;
			if (!id.equals(other.getID())) return false;
//			if (!campaign.equals(other.getID())) return false;
//			if (rules!=other.getRules()) return false;
//			return story.getID().equals(other.getStory().getID());
			return true;
		}
		return false;
	}

	//--------------------------------------------------------------------
	/**
	 * @see de.rpgframework.products.AdventureStory#getID()
	 */
	@Override
	public String getID() {
		return id;
	}

	//--------------------------------------------------------------------
	/**
	 * @see de.rpgframework.products.AdventureStory#getTitle()
	 */
	@Override
	public String getTitle() {
		if (content.isEmpty())
			return null;

		Locale lang = Locale.getDefault();
		String ret = getTitle(lang);
		// If matching content for default language exists, return it
		if (ret!=null)
			return ret;
		// Otherwise return first
		return content.get(0).getTitle();
	}

	//--------------------------------------------------------------------
	/**
	 * @see de.rpgframework.products.AdventureStory#getDescription()
	 */
	@Override
	public String getDescription() {
		if (content.isEmpty())
			return null;

		Locale lang = Locale.getDefault();
		String ret = getDescription(lang);
		// If matching content for default language exists, return it
		if (ret!=null)
			return ret;
		// Otherwise return first
		return content.get(0).getDescription();
	}

	//--------------------------------------------------------------------
	/**
	 * @see de.rpgframework.products.AdventureStory#getTitle(java.util.Locale)
	 */
	@Override
	public String getTitle(Locale lang) {
		for (LocalizedAdventureContent tmp : content) {
			if (tmp.getLocale().getDisplayLanguage().equals(lang.getDisplayLanguage()))
					return tmp.getTitle();
		}
		return null;
	}

	//--------------------------------------------------------------------
	/**
	 * @see de.rpgframework.products.AdventureStory#getDescription(java.util.Locale)
	 */
	@Override
	public String getDescription(Locale lang) {
		for (LocalizedAdventureContent tmp : content) {
			if (tmp.getLocale().getDisplayLanguage().equals(lang.getDisplayLanguage()))
					return tmp.getDescription();
		}
		return null;
	}

	//--------------------------------------------------------------------
	public void addContent(LocalizedAdventureContent cont) {
		this.content.add(cont);
	}

}
