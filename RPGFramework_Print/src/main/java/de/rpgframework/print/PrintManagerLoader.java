/**
 * 
 */
package de.rpgframework.print;

/**
 * @author prelle
 *
 */
public class PrintManagerLoader {
	
	private static PrintManager instance = null;
	
	public static PrintManager getInstance() {
		return instance;
	}
	
	public static void setInstance(PrintManager value) {
		instance = value;
	}

}
