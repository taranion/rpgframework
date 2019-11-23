/**
 * 
 */
package de.rpgframework.devices;

import de.rpgframework.core.Player;
import de.rpgframework.media.Media;

/**
 * @author Stefan
 *
 */
public interface FunctionPlayerHandout {

	public Media.Category[] getSupportedCategories();
	
	public void sendHandout(Player player, Media handout);
	
}
