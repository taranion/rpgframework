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
	private String reason;

	//-------------------------------------------------------------------
	public DecisionToMake(Modification choice, String reason) {
		this.choice = choice;
		this.reason = reason;
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

	//-------------------------------------------------------------------
	/**
	 * @return the reason
	 */
	public String getReason() {
		return reason;
	}

	//-------------------------------------------------------------------
	/**
	 * @param reason the reason to set
	 */
	public void setReason(String reason) {
		this.reason = reason;
	}

}
