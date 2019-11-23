/**
 * 
 */
package de.rpgframework.support.combat.map;

import de.rpgframework.support.combat.Combatant;

/**
 * @author prelle
 *
 */
public interface ConstellationBattleMap<C extends Combatant,R> extends BattleMap<C,R> {
	
	//--------------------------------------------------------------------
	public void setConstellation(C attacker, C target, R range);

}
