/**
 *
 */
package de.rpgframework.support.combat;

/**
 * @author Stefan
 *
 */
public interface CombatantController {

	//--------------------------------------------------------------------
	/**
	 * Ask the player, gamemaster or AI to roll the initiative
	 * for the combatant.
	 */
	public void requestInitiativeRoll(InitiativeToken token);

	//--------------------------------------------------------------------
	/**
	 * Ask the player, gamemaster or AI to decide what action
	 * to perform.
	 */
	public void requestAction(ActionToken token);

}
