package de.rpgframework.devices;

import de.rpgframework.RPGFrameworkConstants;

public enum DeviceFunction { 
	
	MEDIASERVER,
	PLAYER_HANDOUT, 
	PLAYER_MESSAGE, 
	SESSION_HANDOUT, 
	SESSION_AUDIO, 
	;
	
	//--------------------------------------------------------------------
	public String getName() {
		return RPGFrameworkConstants.RES.getString("rpgtooldevice."+this.name().toLowerCase());
	}
	
}