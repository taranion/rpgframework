/**
 * 
 */
package de.rpgframework.core;

/**
 * @author prelle
 *
 */
public interface EventBusListener {
	
	//----------------------------------------------------------------
	public void handleEvent(Object src, EventType type, Object... values);
	
}
