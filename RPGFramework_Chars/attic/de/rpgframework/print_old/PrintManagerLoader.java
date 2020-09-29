/**
 * 
 */
package de.rpgframework.print_old;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.prelle.rpgframework.print.PrintManagerImpl;

import de.rpgframework.RPGFrameworkLoader;

/**
 * @author prelle
 *
 */
public class PrintManagerLoader {
	
	private static PrintManager instance;
	
	public static PrintManager getInstance() {
		if (instance!=null)
			return instance;
		if (RPGFrameworkLoader.getInstance().getConfiguration()!=null) {
			try {
				instance = new PrintManagerImpl(RPGFrameworkLoader.getInstance().getConfiguration());
			} catch (IOException e) {
				LogManager.getLogger("rpgframework.chars").error("Failed initializing PrintManager",e);
			}			
		}
		if (instance==null) {
			LogManager.getLogger("rpgframework.chars").fatal("No PrintManager found");
		}
		return instance;
	}
	
	public static void setInstance(PrintManager value) {
		instance = value;
	}

}
