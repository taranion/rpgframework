/**
 * 
 */
package de.rpgframework.support.combat;

import java.util.Arrays;

/**
 * @author prelle
 *
 */
public class CombatEvent<C extends Combatant,R> {

	private Combat<C,R> combat;
	private CombatEventType type;
	private Object[] data;
	
	//-------------------------------------------------------------------
	/**
	 */
	public CombatEvent(Combat<C,R> combat, CombatEventType type, Object...data) {
		this.combat = combat;
		this.type   = type;
		this.data   = data;
	}

	//-------------------------------------------------------------------
	public String toString() {
		return type+"("+Arrays.toString(data)+")";
	}

	//-------------------------------------------------------------------
	public Combat<C,R> getCombat() {
		return combat;
	}

	//-------------------------------------------------------------------
	public CombatEventType getType() {
		return type;
	}

	//-------------------------------------------------------------------
	public Object[] getData() {
		return data;
	}

}
