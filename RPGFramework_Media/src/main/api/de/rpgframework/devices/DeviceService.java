/**
 * 
 */
package de.rpgframework.devices;

import java.util.List;

//import de.rpgframework.core.SessionContext;

/**
 * @author prelle
 *
 */
public interface DeviceService {

	//--------------------------------------------------------------------
	public List<RPGToolDevice> getAvailableDevices();

	//--------------------------------------------------------------------
	/**
	 * Get devices of a specific type
	 */
	public List<RPGToolDevice> getAvailableDevices(DeviceFunction types);

	//--------------------------------------------------------------------
	/**
	 * Get devices of a specific type currently in use
	 */
//	public List<RPGToolDevice> getAvailableDevices(SessionContext context, DeviceFunction types);
	
}
