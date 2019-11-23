/**
 * 
 */
package de.rpgframework.media;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.List;

import de.rpgframework.media.RoleplayingMetadata.Category;
import de.rpgframework.media.map.TileDefinition;
import de.rpgframework.media.map.Tileset;

/**
 * @author prelle
 *
 */
public interface WritableMediaLibrary extends MediaLibrary {
	
	public Tileset createTileset(String title, String series, String artist, URL sourceURL) throws IOException;
	
	public TileDefinition createTile(Tileset set, String title, String series, String artist, URL sourceURL, Path imageFile) throws IOException;

	public void addTilesFromZIP(Tileset set, File zipFile, MediaLibraryWaitCallback callback);
	
	
	public Media importFromFilesystem(File file, Category category) throws IOException;
	
	public <T extends Media> T importFromFilesystem(File file, MediaCollection<T> context) throws IOException;
	
	public List<Media> importFromWebserver(URL url, Category category) throws IOException;
	
	public <T extends Media> T importFromWebserver(URL url, MediaCollection<T> context) throws IOException;

	//-------------------------------------------------------------------
	/**
	 * Load the data from the given URL, generate a media object for it and
	 * prefill it with data by analyzing the data itself. If necessary, create
	 * a background task to perform subsequent imports (e.g. files from within
	 * a ZIP file)
	 * 
	 * @param library Library to add media file to
	 * @param category Type of media to import
	 * @param url URL to load from
	 */
	public List<Media> importURL(Category category, URL url) throws IOException;

	//-------------------------------------------------------------------
	/**
	 * Save the current metadata 
	 */
	public void save(Media media) throws IOException;

	//-------------------------------------------------------------------
	public void addListener(MediaLibraryListener callback);

	//-------------------------------------------------------------------
	public void removeListener(MediaLibraryListener callback);

	//-------------------------------------------------------------------
	public boolean canBeImported(String name);

	//-------------------------------------------------------------------
	/**
	 * @return Null or error message
	 */
	public String delete(Media media);

	//-------------------------------------------------------------------
	/**
	 * Create a media collection of the given medias.
	 * All of the media objects must be of the same category.
	 * 
	 * @param type Either BATTLEMAPSET or TILESET
	 * @param medias
	 * @return
	 * @throws IOException 
	 */
	public MediaCollection<? extends Media> merge(Category cat, List<? extends Media> medias) throws IOException;
	
}
