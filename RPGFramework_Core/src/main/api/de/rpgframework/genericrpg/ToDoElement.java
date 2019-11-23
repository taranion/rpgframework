/**
 * 
 */
package de.rpgframework.genericrpg;

/**
 * @author prelle
 *
 */
public class ToDoElement {
	
	public enum Severity {
		STOPPER,
		WARNING,
		INFO
	}
	
	private Severity severity;
	private String message;

	//-------------------------------------------------------------------
	public ToDoElement(Severity sev, String mess) {
		this.severity = sev;
		this.message  = mess;
	}

	//-------------------------------------------------------------------
	/**
	 * @return the severity
	 */
	public Severity getSeverity() {
		return severity;
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
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return message;
	}
}
