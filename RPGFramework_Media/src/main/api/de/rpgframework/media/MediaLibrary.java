/**
 * 
 */
package de.rpgframework.media;

import java.io.InputStream;
import java.net.URI;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import de.rpgframework.media.RoleplayingMetadata.Category;

/**
 * @author prelle
 *
 */
public interface MediaLibrary {

	public String getName();

	public String getDescription();

	public URI getLocation();

	public boolean isEnabled();

	public boolean isReadOnly();

	public Collection<Category> getCategories();

	public List<Media> getMedia(Category value);

	public byte[] getBytes(Media media);

	public Media getMedia(UUID key);

	public InputStream getInputStream(Media media);

	public Path getLocalPath(Media media);

	//-------------------------------------------------------------------
	public byte[] getThumbnail(ImageMedia img);

}