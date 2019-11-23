/**
 * 
 */
package de.rpgframework.media.map;

import de.rpgframework.media.ImageMedia;

/**
 * @author prelle
 *
 */
public interface TileDefinition extends ImageMedia {
	
	public TileType getTileType();
	public void setTileType(TileType type);
	
	public boolean isGeomorph();
	public void setGeomorph(boolean isGeomorph);

}
