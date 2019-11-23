/**
 * 
 */
package de.rpgframework.support.combat.map;

import java.util.List;

import de.rpgframework.support.combat.Combat;
import de.rpgframework.support.combat.Combatant;

/**
 * @author prelle
 *
 */
public interface BattleMap<C extends Combatant,R> {

    //------------------------------------------------
    /**
     * Get combatants that still need positioning
     */
    public List<C> getUnpositioned();

    //------------------------------------------------
    public List<C> getPositioned();

    //------------------------------------------------
    public List<PossibleTarget<C,R>> getOpponentsInRange(C comb, R range);

    //------------------------------------------------
    public void addBattleMapListener(BattleMapListener callback);

    //------------------------------------------------
    public void removeBattleMapListener(BattleMapListener callback);

    //------------------------------------------------
	public Combat<C,R> getCombat();
    
}
