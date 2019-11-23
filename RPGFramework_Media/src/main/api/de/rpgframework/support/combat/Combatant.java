/**
 *
 */
package de.rpgframework.support.combat;

/**
 * @author prelle
 *
 */
public interface Combatant {

	//--------------------------------------------------------------------
	public String getName();

	//--------------------------------------------------------------------
	public byte[] getAvatar();

	//--------------------------------------------------------------------
	/**
	 * Set a temporary image that will not be saved with the character.
	 * Useful if the character does not have an own image.
	 * @param data
	 */
	public void setTemporaryAvatar(byte[] data);

	//--------------------------------------------------------------------
	public void addListener(CombatantListener callback);

	//--------------------------------------------------------------------
	public void removeListener(CombatantListener callback);

	//--------------------------------------------------------------------
	public void fireChangedEvent();

}
