package de.rpgframework.genericrpg.modification;

import de.rpgframework.genericrpg.Datable;


/**
 * @author prelle
 *
 */
public interface Modification extends Datable, Comparable<Modification>, Cloneable {

	//-------------------------------------------------------------------
	public Modification clone();

	//-------------------------------------------------------------------
	public int getExpCost();

	//-------------------------------------------------------------------
	public void setExpCost(int expCost);

	//-------------------------------------------------------------------
	public Object getSource();

	//-------------------------------------------------------------------
	public void setSource(Object src);

	//-------------------------------------------------------------------
//	public UUID getUUID();
   
}// Modification

