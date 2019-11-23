/**
 * 
 */
package de.rpgframework.media;

import java.nio.file.Path;

/**
 * @author prelle
 *
 */
public interface MediaCollection <T extends Media> extends Media, Iterable<T> {
	
	public String getName();
	
	public Path getLocalPath();

	public void add(T element);
	
}
