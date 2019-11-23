/**
 * 
 */
package de.rpgframework.media;

import java.net.URL;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

/**
 * @author Stefan
 *
 */
public interface Media extends RoleplayingMetadata {

	//--------------------------------------------------------------------
	public MediaLibrary getLibrary();

	//--------------------------------------------------------------------
	public UUID getUUID();

	//--------------------------------------------------------------------
	/**
	 * Get location relative to library root
	 */
	public Path getRelativePath();
	public void setRelativePath(Path relativize);

	//--------------------------------------------------------------------
	/**
	 * Returns all possible URLs to obtain the media (for UPnP)
	 * @return
	 */
	public URL getAccessURL();

	//--------------------------------------------------------------------
	/**
	 * Name to identify the media within the gaming session
	 */
	public String getName();
	public void setName(String value);
	
	//--------------------------------------------------------------------
	public URL getSource();
	public void setSource(URL src);

	//--------------------------------------------------------------------
	public String getArtist();
	public void setArtist(String value);

	//--------------------------------------------------------------------
	public String getSeries();
	public void setSeries(String value);
	
	//--------------------------------------------------------------------
	public LicenseType getLicense();
	public void setLicense(LicenseType value);

	//--------------------------------------------------------------------
	public String getCopyright();
	public void setCopyright(String value);
	
	//--------------------------------------------------------------------
	public void setKeywords(List<String> keywords);
	public List<String> getKeywords();

	//--------------------------------------------------------------------
	boolean isDirty();
	void clearDirty();

	
	
}
