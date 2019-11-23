package de.rpgframework.genericrpg;

import de.rpgframework.genericrpg.modification.Modifyable;

public interface Reward extends Datable, Modifyable {

	//-------------------------------------------------------------------
	/**
	 * @return the title
	 */
	public String getTitle();

	//-------------------------------------------------------------------
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title);

	//-------------------------------------------------------------------
	/**
	 * @return the exp
	 */
	public int getExperiencePoints();

	//-------------------------------------------------------------------
	/**
	 * @param exp the exp to set
	 */
	public void setExperiencePoints(int exp);

	//-------------------------------------------------------------------
	/**
	 * @return the id
	 */
	public String getId();

	//-------------------------------------------------------------------
	/**
	 * @param id the id to set
	 */
	public void setId(String id);

}