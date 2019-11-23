/**
 * 
 */
package de.rpgframework.session;

import de.rpgframework.devices.RPGToolDevice;

/**
 * This object reflects all shared screens in the running session. It ensures that all 
 * clients - local and remote get to see the same content.
 * 
 * @author prelle
 *
 */
public interface SessionScreen {
	
	public enum DisplayHint {
		/**
		 * The image is scaled until both axis fit onto the screen and fill it
		 * as good as possible. This may result in black borders on the sides
		 * or above/below.
		 */
		SCALE,
		/**
		 * The image is scaled until no black borders are visible. This may result
		 * in parts of the image being outside the visible area.
		 */
		FILL,
	}

	public void addClient(RPGToolDevice clientScreen);

	public void removeClient(RPGToolDevice clientScreen);
	
	public void update(byte[] jpgData);
	
}
