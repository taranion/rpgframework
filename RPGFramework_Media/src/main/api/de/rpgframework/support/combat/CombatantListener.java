/**
 *
 */
package de.rpgframework.support.combat;

/**
 * @author prelle
 *
 */
public interface CombatantListener {

	public void combatantChanged(Combatant model);
	
	public void actionRequested(Combatant model, CombatAction action);

}
