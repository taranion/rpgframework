/**
 * 
 */
package de.rpgframework.sound;

import java.util.List;

/**
 * @author prelle
 *
 */
public interface SoundSet {

	//-------------------------------------------------------------------
	public List<Channel> getChannels();

	//-------------------------------------------------------------------
	public void addChannel(Channel channel);

	//-------------------------------------------------------------------
	public void removeChannel(Channel channel);
	
}
