package de.rpgframework.support.combat;

import java.util.List;

import de.rpgframework.support.combat.map.BattleMap;

public interface Combat<C extends Combatant,R> {

	public enum State {
		CHOOSE_COMBATANTS,
		CHOOSE_BATTLEFIELD,
		PLACE_COMBATANTS,
		RUNNING
	}

	//-------------------------------------------------------------------
	public void addGroup(Party<C> grp);

	//-------------------------------------------------------------------
	@SuppressWarnings("unchecked")
	public Party<C> addAsGroup(C... combatants);

	//-------------------------------------------------------------------
	/**
	 * Add combatants to group and inform listener
	 */
	@SuppressWarnings("unchecked")
	public void addToGroup(Party<C> party, C... combatants);

	//-------------------------------------------------------------------
	/**
	 * Remove combatants from group and inform listener
	 */
	@SuppressWarnings("unchecked")
	public void removeFromGroup(Party<C> party, C... combatants);

	//-------------------------------------------------------------------
	public List<Party<C>> getGroups();

	//-------------------------------------------------------------------
	public List<C> getCombatants();

	//-------------------------------------------------------------------
	public State getState();

	//-------------------------------------------------------------------
	public void setState(State newState);

	//--------------------------------------------------------------------
	public void addListener(CombatListener<C,R> callback);

	//--------------------------------------------------------------------
	public void removeListener(CombatListener<C,R> callback);

	//--------------------------------------------------------------------
	public void fireEvent(CombatEventType type, Object...data);

	//-------------------------------------------------------------------
	public BattleMap<C, R> getBattleMap();
	

}