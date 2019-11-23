package de.rpgframework.genericrpg.modification;

import java.util.List;

public interface Modifyable {

	//-------------------------------------------------------------------
	public List<Modification> getModifications();

	//-------------------------------------------------------------------
	public void setModifications(List<Modification> mods);

	//-------------------------------------------------------------------
	public void addModification(Modification mod);

	//-------------------------------------------------------------------
	public void removeModification(Modification mod);

}