/**
 * 
 */
package de.rpgframework.core;

import java.util.Arrays;

/**
 * @author prelle
 *
 */
public class BabylonEvent {
	
	private Object sender;
	private BabylonEventType type;
	private Object[] data;

	//-------------------------------------------------------------------
	public BabylonEvent(Object sender, BabylonEventType type, Object...data) {
		this.sender = sender;
		this.type   = type;
		this.data   = data;
	}

	//-----------------------------------------------------------------
	public String toString() {
		return type+" "+Arrays.toString(data);
	}

	//-----------------------------------------------------------------
	/**
	 * @return the sender
	 */
	public Object getSender() {
		return sender;
	}

	//-----------------------------------------------------------------
	/**
	 * @return the type
	 */
	public BabylonEventType getType() {
		return type;
	}

	//-----------------------------------------------------------------
	/**
	 * @return the data
	 */
	public Object[] getData() {
		return data;
	}

}
