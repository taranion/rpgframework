/**
 * 
 */
package de.rpgframework.media;

/**
 * @author prelle
 *
 */
public interface MediaLibraryListener {
	
	public void mediaAdded(Media data);
	
	public void mediaUpdated(Media data);
	
	public void mediaRevmoed(Media data);

}
