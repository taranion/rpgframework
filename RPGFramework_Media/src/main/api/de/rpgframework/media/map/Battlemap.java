/**
 * 
 */
package de.rpgframework.media.map;

import java.util.List;

import de.rpgframework.media.ImageMedia;

/**
 * @author prelle
 *
 */
public interface Battlemap extends ImageMedia {
	
	public int getOffsetX();
	public void setOffsetX(int value);
	
	public int getOffsetY();
	public void setOffsetY(int value);
	
	public List<String> getLayer();

}
