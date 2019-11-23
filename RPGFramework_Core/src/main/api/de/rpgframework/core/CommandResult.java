/**
 * 
 */
package de.rpgframework.core;

/**
 * @author prelle
 *
 */
public class CommandResult {
	
	private CommandType type;
	private boolean successful;
	private boolean processed;
	private String message;
	private Object returnValue;

	//-------------------------------------------------------------------
	/**
	 * Return successful
	 */
	public CommandResult(CommandType type, Object values) {
		this.type       = type;
		this.successful = true;
		this.processed  = true;
		returnValue     = values;
	}

	//-------------------------------------------------------------------
	/**
	 * Return successful
	 */
	public CommandResult(CommandType type, String mess, Object values) {
		this.type       = type;
		this.successful = true;
		this.processed  = true;
		this.message    = mess;
		returnValue     = values;
	}

	//-------------------------------------------------------------------
	public CommandResult(CommandType type, boolean success) {
		this.type       = type;
		this.processed  = true;
		this.successful = success;
	}

	//-------------------------------------------------------------------
	public CommandResult(CommandType type, boolean success, String mess) {
		this.type       = type;
		this.successful = success;
		this.processed  = true;
		this.message    = mess;
	}

	//-------------------------------------------------------------------
	public CommandResult(CommandType type, boolean success, String mess, boolean processed) {
		this.type       = type;
		this.successful = success;
		this.message    = mess;
		this.processed  = processed;
	}

	//-------------------------------------------------------------------
	/**
	 * @return the successful
	 */
	public boolean wasSuccessful() {
		return successful;
	}

	//-------------------------------------------------------------------
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	//-------------------------------------------------------------------
	/**
	 * @return the type
	 */
	public CommandType getType() {
		return type;
	}

	//-------------------------------------------------------------------
	public Object getReturnValue() {
		return returnValue;
	}

	//-------------------------------------------------------------------
	public void setReturnValue(Object returnValue) {
		this.returnValue = returnValue;
	}

	//-------------------------------------------------------------------
	public boolean wasProcessed() {
		return processed;
	}

}
