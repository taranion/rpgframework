/**
 * 
 */
package de.rpgframework;

import de.rpgframework.RPGFrameworkLoader.FunctionType;

/**
 * @author prelle
 *
 */
public interface RPGFrameworkPlugin {
	
	public FunctionType getType();

	public void initialize(RPGFramework framework);
	
}
