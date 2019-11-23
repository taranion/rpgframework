package de.rpgframework.genericrpg;

import java.util.List;

/**
 * @author Stefan Prelle
 *
 */
public interface SelfExplainingNumericalValue<T> extends NumericalValue<T> {

	//-------------------------------------------------------------------
	/**
	 * For each modification contributing to the modifier add a line
	 * @return
	 */
	public List<String> getModifierExplanation();
	
}
