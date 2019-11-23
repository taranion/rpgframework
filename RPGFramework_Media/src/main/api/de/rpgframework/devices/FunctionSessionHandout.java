/**
 * 
 */
package de.rpgframework.devices;

import de.rpgframework.media.Media;

/**
 * @author Stefan
 *
 */
public interface FunctionSessionHandout {

	public Media.Category[] getSupportedCategories();

	public void showHandout(Media handout);
	
}
