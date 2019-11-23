package org.prelle.rpgframework.products;

import org.prelle.simplepersist.StringValueConverter;

import de.rpgframework.products.AdventureStory;
import de.rpgframework.products.ProductServiceLoader;

/**
 * @author Stefan
 *
 */
public class AdventureStoryConverter implements StringValueConverter<AdventureStory> {

	//--------------------------------------------------------------------
	/**
	 * @see org.prelle.simplepersist.StringValueConverter#write(java.lang.Object)
	 */
	@Override
	public String write(AdventureStory value) throws Exception {
		return value.getID();
	}

	//--------------------------------------------------------------------
	/**
	 * @see org.prelle.simplepersist.StringValueConverter#read(java.lang.String)
	 */
	@Override
	public AdventureStory read(String v) throws Exception {
		return ProductServiceLoader.getInstance().getAdventureStory(v);
	}

}
