/**
 * 
 */
package de.rpgframework.character;

import java.io.IOException;

/**
 * Thrown when conversion to byte array or from byte array fails.
 * @author prelle
 *
 */
@SuppressWarnings("serial")
public class DecodeEncodeException extends IOException {

	//-------------------------------------------------------------------
	public DecodeEncodeException() {
	}

	//-------------------------------------------------------------------
	/**
	 * @param arg0
	 */
	public DecodeEncodeException(String arg0) {
		super(arg0);
	}

	//-------------------------------------------------------------------
	/**
	 * @param arg0
	 */
	public DecodeEncodeException(Throwable arg0) {
		super(arg0);
	}

	//-------------------------------------------------------------------
	/**
	 * @param arg0
	 * @param arg1
	 */
	public DecodeEncodeException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
