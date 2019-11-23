/**
 * 
 */
package de.rpgframework.products;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import de.rpgframework.core.RoleplayingSystem;

/**
 * @author Stefan
 *
 */
public interface ProductService {
	
	//--------------------------------------------------------------------
	public List<Product> getProducts(RoleplayingSystem rules);
	
	//--------------------------------------------------------------------
	public byte[] loadCover(Product product);
	
	//--------------------------------------------------------------------
	public List<Adventure> getAdventures(RoleplayingSystem rules);

	//--------------------------------------------------------------------
	public Adventure getAdventure(RoleplayingSystem rules, String id);
	
	//--------------------------------------------------------------------
	public List<Product> getProductWith(Adventure adventure);

	//--------------------------------------------------------------------
	/**
	 * Creates an adventure with a title in the default locale
	 */
	public Adventure createAdventure(String id, String title, RoleplayingSystem rules) throws IOException;

	//--------------------------------------------------------------------
	public Adventure createAdventure(String id, RoleplayingSystem rules, Locale locale, String title) throws IOException;
	
	//--------------------------------------------------------------------
	public void deleteAdventure(Adventure value) throws IOException;
	
	//--------------------------------------------------------------------
	public void saveAdventure(Adventure data) throws IOException;
	
	

	
	//--------------------------------------------------------------------
	public AdventureStory getAdventureStory(String id);
	
	//--------------------------------------------------------------------
	public List<AdventureStory> getAdventureStories();

}
