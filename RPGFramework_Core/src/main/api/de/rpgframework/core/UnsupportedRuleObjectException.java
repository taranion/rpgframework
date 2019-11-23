/**
 * 
 */
package de.rpgframework.core;

/**
 * An operation was called with a rule object that is not supported
 * by the called plugin.
 * 
 * @author prelle
 *
 */
@SuppressWarnings("serial")
public class UnsupportedRuleObjectException extends RuntimeException {

	//--------------------------------------------------------------------
	/**
	 * @param arg0
	 */
	public UnsupportedRuleObjectException(String mess) {
		super(mess);
	}

}
