/**
 * 
 */
package org.prelle.rpgframework.character;

import java.nio.file.Path;

/**
 * @author prelle
 *
 */
@SuppressWarnings("unused")
public class SyncOperation {
	
	public enum Direction {
		UPLOAD,
		DOWNLOAD
	}
	
	private Origin localOrigin;
	private Path local;
	private Origin remoteOrigin;
	private Path remote;
	private Direction direction;

	//-------------------------------------------------------------------
	/**
	 */
	public SyncOperation() {
		// TODO Auto-generated constructor stub
	}

}
