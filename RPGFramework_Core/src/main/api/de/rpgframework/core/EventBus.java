/**
 * 
 */
package de.rpgframework.core;

import java.util.ArrayList;
import java.util.List;


/**
 * @author prelle
 *
 */
public class EventBus {
	
	private static List<EventBusListener> listener;

	//----------------------------------------------------------------
	static {
		listener = new ArrayList<EventBusListener>();
	}

	//----------------------------------------------------------------
	public static void registerBusEventListener(EventBusListener callback) {
		if (!listener.contains(callback))
			listener.add(callback);
	}

	//----------------------------------------------------------------
	public static void unregisterBusEventListener(EventBusListener callback) {
		listener.remove(callback);
	}

	//----------------------------------------------------------------
	public static void fireEvent(Object src, EventType type, Object... param) {
//		System.out.println("EventBus.Fire "+type+" with "+Arrays.toString(param));
		for (EventBusListener callback : listener) {
			try {
				callback.handleEvent(src, type, param);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}			
	}

}
