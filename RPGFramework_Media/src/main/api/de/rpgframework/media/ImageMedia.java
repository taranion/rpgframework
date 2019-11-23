/**
 * 
 */
package de.rpgframework.media;

/**
 * @author prelle
 *
 */
public interface ImageMedia extends Media {
	
	public int getDPI();
	public void setDPI(int dpi);
	
	public int getWidth();
	public int getHeight();
	
	public ImageType getType();
	public void setType(ImageType type);

}
