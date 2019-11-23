/**
 * 
 */
package org.prelle.rpgframework;

import java.util.List;

import de.rpgframework.RPGFrameworkInitCallback;
import de.rpgframework.RPGFrameworkLoader.FunctionType;
import de.rpgframework.core.RoleplayingSystem;

/**
 * @author prelle
 *
 */
public interface BabylonPlugin extends Comparable<BabylonPlugin> {

	//-------------------------------------------------------------------
	public FunctionType getType();
	
	//-------------------------------------------------------------------
	public void initialize(RPGFrameworkImpl fwImpl, ConfigContainerImpl config, RPGFrameworkInitCallback callback, List<RoleplayingSystem> limit);
	
	//-------------------------------------------------------------------
	/**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	default public int compareTo(BabylonPlugin other) {
		return ((Integer)getType().ordinal()).compareTo(other.getType().ordinal());
	}
}
