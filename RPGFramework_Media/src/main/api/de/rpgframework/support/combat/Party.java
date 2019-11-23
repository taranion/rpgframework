/**
 * 
 */
package de.rpgframework.support.combat;

import java.util.ArrayList;

/**
 * @author prelle
 *
 */
@SuppressWarnings("serial")
public class Party<C extends Combatant> extends ArrayList<C> {

	private transient String name;

	//-------------------------------------------------------------------
	public Party() {}

	//-------------------------------------------------------------------
	public Party(String name) {
		this.name = name;
	}

	//-------------------------------------------------------------------
	public String toString() {
		if (name!=null)
			return name;
		return super.toString();
	}

	//-------------------------------------------------------------------
	public String getName() {
		return name;
	}

	//-------------------------------------------------------------------
	public void setName(String name) {
		this.name = name;
	}
	
}
