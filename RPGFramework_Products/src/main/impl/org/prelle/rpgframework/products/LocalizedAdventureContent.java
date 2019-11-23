package org.prelle.rpgframework.products;

import java.util.Locale;

import org.prelle.simplepersist.Attribute;
import org.prelle.simplepersist.Element;

public class LocalizedAdventureContent extends Localized {

	@Element
	private String title;
	@Element(required=false)
	private String description;
	@Element(required=false,name="wikiurl")
	private String wikiURL;
	private transient byte[] cover;
	
	//--------------------------------------------------------------------
	public LocalizedAdventureContent() {
	}
	
	//--------------------------------------------------------------------
	public LocalizedAdventureContent(Locale loc) {
		super(loc);
	}

	//--------------------------------------------------------------------
	public LocalizedAdventureContent(@Attribute(name="lang") String lang) {
		super(lang);
	}

	//--------------------------------------------------------------------
	public boolean equals(Object o) {
		if (o instanceof LocalizedAdventureContent) {
			LocalizedAdventureContent other = (LocalizedAdventureContent)o;
			if (!title.equals(other.getTitle())) return false;
			if (description==null && other.getDescription()!=null) return false;
			if (description!=null && other.getDescription()==null) return false;
			if (description!=null && other.getDescription()!=null && !description.equals(other.getDescription())) return false;
			if (wikiURL==null && other.getWikiURL()!=null) return false;
			if (wikiURL!=null && other.getWikiURL()==null) return false;
			if (wikiURL!=null && other.getWikiURL()!=null && !wikiURL.equals(other.getWikiURL())) return false;
			return super.equals(other);			
		}
		return false;
	}
	
	//--------------------------------------------------------------------
 	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	//--------------------------------------------------------------------
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	//--------------------------------------------------------------------
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	//--------------------------------------------------------------------
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	//--------------------------------------------------------------------
	/**
	 * @return the wikiURL
	 */
	public String getWikiURL() {
		return wikiURL;
	}
	//--------------------------------------------------------------------
	/**
	 * @param wikiURL the wikiURL to set
	 */
	public void setWikiURL(String wikiURL) {
		this.wikiURL = wikiURL;
	}

	//-------------------------------------------------------------------
	/**
	 * @return the cover
	 */
	public byte[] getCover() {
		return cover;
	}

	//-------------------------------------------------------------------
	/**
	 * @param cover the cover to set
	 */
	public void setCover(byte[] cover) {
		this.cover = cover;
	}

}