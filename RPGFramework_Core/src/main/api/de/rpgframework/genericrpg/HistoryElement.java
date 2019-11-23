package de.rpgframework.genericrpg;

import java.util.Date;
import java.util.List;

import de.rpgframework.genericrpg.modification.Modification;

public interface HistoryElement {

	//-------------------------------------------------------------------
	public Date getStart();

	//-------------------------------------------------------------------
	public Date getEnd();

	//-------------------------------------------------------------------
	/**
	 * @return the adventure
	 */
	public String getAdventureID();

	//-------------------------------------------------------------------
	/**
	 * @return the name
	 */
	public String getName();

	//-------------------------------------------------------------------
	/**
	 * @return the gained
	 */
	public List<Reward> getGained();

	//-------------------------------------------------------------------
	/**
	 * @return the spent
	 */
	public List<Modification> getSpent();

	//-------------------------------------------------------------------
	/**
	 * Return the sum of all rewarded experience
	 */
	public int getTotalExperience();

}