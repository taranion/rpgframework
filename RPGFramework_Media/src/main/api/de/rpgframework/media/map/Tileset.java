/**
 * 
 */
package de.rpgframework.media.map;

import de.rpgframework.media.Media;
import de.rpgframework.media.MediaCollection;

/**
 * @author prelle
 *
 */
public interface Tileset extends Media, MediaCollection<TileDefinition> {
	
	public void setSeries(String value);
	
	public String getSeries();

}
