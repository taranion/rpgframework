/**
 * 
 */
package de.rpgframework.support.combat.map;

import de.rpgframework.support.combat.Combatant;

/**
 * @author prelle
 *
 */
public interface RangeCalculator<R> {
	
	public R getRange(Combatant attacker, Combatant defender);

}
