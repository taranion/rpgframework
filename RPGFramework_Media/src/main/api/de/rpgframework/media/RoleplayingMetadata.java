/**
 * 
 */
package de.rpgframework.media;

import java.util.List;

import de.rpgframework.core.Genre;

/**
 * @author prelle
 *
 */
public interface RoleplayingMetadata {
	
	public enum Category {
		HANDOUT,
		MAP,
		MAPTILE,
		TILESET,
		BATTLEMAP_STATIC,
		BATTLEMAP_DYNAMIC,
		BATTLEMAPSET,
		TOKEN,
		NPC,
		LIFEFORM,
		
		MUSIC,
	}
	
	//--------------------------------------------------------------------
	public Category getCategory();
	public void setCategory(Category value);
	
	//--------------------------------------------------------------------
	public void addTag(MediaTag tag);
	public void removeTag(MediaTag tag);
	public List<MediaTag> getTags();
	
	//--------------------------------------------------------------------
	public void addGenre(Genre value);
	public void removeGenre(Genre value);
	public List<Genre> getGenres();
	public void setGenres(List<Genre> value);

}
