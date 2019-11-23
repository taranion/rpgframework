/**
 * 
 */
package de.rpgframework.sound;

/**
 * @author prelle
 *
 */
public interface Channel {

	public void setPlayable(Playable sound);

	public void setPauseAfter(int seconds);

	public void setVolume(int percent);
	
}
