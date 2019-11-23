/**
 * 
 */
package de.rpgframework.media;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;

import de.rpgframework.music.Filter;
import de.rpgframework.products.Adventure;

/**
 * @author prelle
 *
 */
public interface MediaService {

	//-------------------------------------------------------------------
	public List<MediaLibrary> getMyLibraries();

	//-------------------------------------------------------------------
	public MediaLibrary addMyLibrary(URL url, String name) throws URISyntaxException, SQLException, IOException ;

	//-------------------------------------------------------------------
	public MediaLibrary addMyLibrary(URL url, String name, String login, String pass) throws URISyntaxException, SQLException, IOException ;

	//-------------------------------------------------------------------
	public void removeMyLibrary(MediaLibrary library);

	//-------------------------------------------------------------------
	public void updateMyLibrary(MediaLibrary library);

	//-------------------------------------------------------------------
	public void setEnabled(MediaLibrary library, boolean enabled);

	//-------------------------------------------------------------------
	public boolean isEnabled(MediaLibrary library);

	
	//-------------------------------------------------------------------
	public MediaLibrary getMediaLibrary(Adventure adv, Filter... filter);

	//-------------------------------------------------------------------
//	public void showOnSessionScreen(Media image);
	
	
}
