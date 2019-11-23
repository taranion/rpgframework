/**
 * 
 */
package de.rpgframework.support.combat;

/**
 * @author prelle
 *
 */
public interface CombatListener<C extends Combatant,R> {

	public void combatChanged(CombatEvent<C,R> event);
	
}
