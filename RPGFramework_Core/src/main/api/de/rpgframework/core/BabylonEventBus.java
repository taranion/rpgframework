/**
 * 
 */
package de.rpgframework.core;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author prelle
 *
 */
public class BabylonEventBus {

	private static Collection<BabylonEventListener> listener;
	
	//-----------------------------------------------------------------
	static {
		listener = new ArrayList<BabylonEventListener>();
	}
	
	//-----------------------------------------------------------------
	public static synchronized void add(BabylonEventListener toAdd) {
		if (!listener.contains(toAdd))
			listener.add(toAdd);
	}
	
	//-----------------------------------------------------------------
	public static synchronized void remove(BabylonEventListener toAdd) {
		listener.remove(toAdd);
	}
	
	//-----------------------------------------------------------------
	public static synchronized Collection<BabylonEventListener> all() {
		return listener;
	}
	
	//-----------------------------------------------------------------
	public static synchronized void fireEvent(BabylonEvent event) {
//		System.out.println("fire "+event);
		for (BabylonEventListener list : new ArrayList<BabylonEventListener>(listener)) {
			try {
				list.handleAppEvent(event);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	//-----------------------------------------------------------------
	public static synchronized void fireEvent(BabylonEventType type, Object...data) {
		fireEvent(new BabylonEvent(null, type, data));
	}

}
