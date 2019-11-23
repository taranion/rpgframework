/**
 *
 */
package de.rpgframework.media;

import java.net.URL;
import java.nio.file.Path;

/**
 * Webserver that makes local media available for remote access
 *
 * @author prelle
 *
 */
public interface Webserver {

	public void addDirectory(Path localDir, Path mountPoint);

	public void addFile(Path localDir, Path mountPoint);

	public Path resolve(Path accessPath);

	public URL toURL(Path localPath);

	public Path getTempBaseDirectory();

}
