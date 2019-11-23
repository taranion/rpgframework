/**
 *
 */
package de.rpgframework.support.combat;

/**
 * @author Stefan
 *
 */
public enum CombatEventType {

	/**
	 * A battlemap has been selected
	 */
	BATTLEMAP_DEFINED,

	/**
	 * Positions of combatants on the battlemap changed
	 */
	BATTLEMAP_UPDATED,

	/**
	 *
	 */
	COMBAT_STATE_CHANGED,

	/**
	 * A new combatant was added to the combat
	 * but not necessarily to the battlemap.
	 * 0 = Combatant
	 */
	COMBATANT_ADDED,

	/**
	 * A combatant was removed from the combat
	 */
	COMBATANT_REMOVED,

	/**
	 * A new combatant entered the battlemap
	 */
	COMBATANT_ENTERED_COMBAT,

	/**
	 * A combatant left the battlemap
	 */
	COMBATANT_LEFT_COMBAT,

	/**
	 * The stats of a combatant changed
	 */
	COMBATANT_UPDATED,
	
	PARTY_ADDED,
	
}
