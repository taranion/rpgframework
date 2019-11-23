/**
 * 
 */
package de.rpgframework.support.combat.map;

import de.rpgframework.support.combat.Combatant;

/**
 * @author prelle
 *
 */
public interface MatrixBattleMap<C extends Combatant,R> extends BattleMap<C,R> {
	
	//-------------------------------------------------------------------
	public void setMap(MatrixMap map);
	
    //------------------------------------------------
	public MatrixMap getMap();

	//-------------------------------------------------------------------
    public void setRangeCalculator(RangeCalculator<R> calculator);

	//-------------------------------------------------------------------
	public int[] getPosition(C comb);

	//-------------------------------------------------------------------
	public void setPosition(C comb, int x, int y);

	//-------------------------------------------------------------------
	/**
	 * Shortcut got getMap().getWidth()
	 */
	public int getWidth();

	//-------------------------------------------------------------------
	/**
	 * Shortcut got getMap().getHeight()
	 */
	public int getHeight();

}
