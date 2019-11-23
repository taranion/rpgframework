/**
 * 
 */
package de.rpgframework.support.combat.map;

import de.rpgframework.support.combat.Combatant;

/**
 * @author Stefan
 *
 */
public interface PossibleTarget<C extends Combatant,R> {

	public C getCombatant();
	
	public R getRange();
	
}
