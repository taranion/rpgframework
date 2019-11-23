/**
 *
 */
package de.rpgframework.core;

import java.util.List;

import de.rpgframework.worldinfo.InformationLevel;

/**
 * @author Stefan
 *
 */
public interface Filter {

	//--------------------------------------------------------------------
	public InformationLevel getInfoLevel();

	//--------------------------------------------------------------------
	public String getName();

	//--------------------------------------------------------------------
	/**
	 * @return Objects that implement toString();
	 */
	public List<?> getChoices();

}
