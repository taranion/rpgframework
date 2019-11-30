/**
 * 
 */
package org.prelle.rpgframework.character;

import java.nio.file.Path;

/**
 * @author prelle
 *
 */
public interface FileSystemSyncListener {

	public void localDirectoryCreated(Path path);

}
