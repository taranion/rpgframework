/**
 *
 */
package de.rpgframework.support.combat;

/**
 * @author Stefan
 *
 */
public class StandardInitiativeToken implements InitiativeToken {

	private Combatant combatant;
	private int result;

	//--------------------------------------------------------------------
	public StandardInitiativeToken(Combatant comb) {
		this.combatant = comb;
	}

	//--------------------------------------------------------------------
	/**
	 * @see de.rpgframework.support.combat.InitiativeToken#getCombatant()
	 */
	@Override
	public Combatant getCombatant() {
		return combatant;
	}

	//--------------------------------------------------------------------
	/**
	 * @return the result
	 */
	public int getResult() {
		return result;
	}

	//--------------------------------------------------------------------
	/**
	 * @param result the result to set
	 */
	public void setResult(int result) {
		this.result = result;
	}

}
