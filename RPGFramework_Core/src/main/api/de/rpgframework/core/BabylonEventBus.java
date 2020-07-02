/**
 * 
 */
package de.rpgframework.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author prelle
 *
 */
public class BabylonEventBus {

	private static Collection<BabylonEventListener> listener;
	
	/** Where to cache events thrown when no listener is present */
	private static List<BabylonEvent> cachedEvents;
	
	//-----------------------------------------------------------------
	static {
		listener = new ArrayList<BabylonEventListener>();
		cachedEvents = new ArrayList<BabylonEvent>();
	}
	
	//-----------------------------------------------------------------
	public static synchronized void add(BabylonEventListener toAdd) {
		if (!listener.contains(toAdd))
			listener.add(toAdd);
		
		if (toAdd!=null) {
			Thread later = new Thread(new Runnable() {
				public void run() {
					try {
						Thread.sleep(500);
						for (BabylonEvent ev : cachedEvents) {
							toAdd.handleAppEvent(ev);
						}
					} catch (Exception e) {
					}
					cachedEvents.clear();
				}});
			later.start();
		}
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
		if (listener.isEmpty()) {
			cachedEvents.add(event);
			return;
		}
		
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
