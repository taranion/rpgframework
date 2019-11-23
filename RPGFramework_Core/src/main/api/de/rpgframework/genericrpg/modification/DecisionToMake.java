/**
 * 
 */
package de.rpgframework.genericrpg.modification;

import java.util.List;

/**
 * @author prelle
 *
 */
public class DecisionToMake {
	
	private Modification choice;
	private List<Modification> decision;

	//-------------------------------------------------------------------
	public DecisionToMake(Modification choice) {
		this.choice = choice;
	}

	//-------------------------------------------------------------------
	public List<Modification> getDecision() {
		return decision;
	}

	//-------------------------------------------------------------------
	public void setDecision(List<Modification> decision) {
		this.decision = decision;
	}

	//-------------------------------------------------------------------
	public Modification getChoice() {
		return choice;
	}

	//-------------------------------------------------------------------
	public String toString() {
		if (decision==null)
			return "UNDECIDED("+choice+")";
		else
			return "DECIDED("+decision+")";
	}

}
