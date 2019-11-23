/**
 *
 */
package de.rpgframework.support.combat;

import java.util.List;

import de.rpgframework.support.combat.map.BattleMap;

/**
 * @author Stefan
 *
 */
public interface CombatRunner<C extends Combatant,R> {

	//--------------------------------------------------------------------
	/**
	 * Place active combatants
	 */
	public void prepare(BattleMap<C,R> battlefield);

	public void start();

	public void stop();

	public void save();

	//--------------------------------------------------------------------
	public List<Object> getOpenTokens();

	//--------------------------------------------------------------------
	/**
	 * The request for an initiative roll has been responded to
	 */
	public void response(InitiativeToken token);

}
