/**
 * 
 */
package de.rpgframework.print_old;

import de.rpgframework.character.CharacterHandle;

/**
 * @author prelle
 *
 */
public class PrintException extends Exception {

	private static final long serialVersionUID = -5755431925953991754L;

	private CharacterHandle handle;
	private PrintType type;
	
	//--------------------------------------------------------------------
	/**
	 * @param arg0
	 */
	public PrintException(PrintType type, String message) {
		super(message);
		this.type = type;
	}

	//--------------------------------------------------------------------
	/**
	 * @param arg0
	 * @param arg1
	 */
	public PrintException(PrintType type, String message, Throwable error) {
		super(message, error);
		this.type = type;
	}

	//--------------------------------------------------------------------
	/**
	 * @return the type
	 */
	public PrintType getType() {
		return type;
	}

	//--------------------------------------------------------------------
	/**
	 * @return the handle
	 */
	public CharacterHandle getHandle() {
		return handle;
	}

	//--------------------------------------------------------------------
	/**
	 * @param handle the handle to set
	 */
	public void setHandle(CharacterHandle handle) {
		this.handle = handle;
	}

}
