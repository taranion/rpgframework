/**
 * 
 */
package de.rpgframework.devices;

import java.util.Collection;

/**
 * @author prelle
 *
 */
/**
 * @author prelle
 *
 */
public interface RPGToolDevice extends Comparable<RPGToolDevice> {

	//------------------------------------------------------------------
	public String getName();
	
	//------------------------------------------------------------------
	public Object getFunction(DeviceFunction func);
	
	//------------------------------------------------------------------
	public Collection<DeviceFunction> getSupportedFunctions();
	
	//------------------------------------------------------------------
	/**
	 * @return Custom icon or null, if UI should use default icon
	 * for device type
	 */
	public byte[] getIcon();	
	
	//------------------------------------------------------------------
	/**
	 * Called when the device is entering a gaming session - normally when
	 * the gamemaster decides to use it.
	 */
	public void activate();
	
	//------------------------------------------------------------------
	/**
	 * Called when the device is leaving a gaming session.
	 */
	public void deactivate();
	
}
