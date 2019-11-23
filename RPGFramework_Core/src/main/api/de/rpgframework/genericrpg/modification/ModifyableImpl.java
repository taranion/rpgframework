/**
 *
 */
package de.rpgframework.genericrpg.modification;

import java.util.ArrayList;
import java.util.List;

/**
 * @author prelle
 *
 */
public abstract class ModifyableImpl implements Modifyable {

	protected transient List<Modification> modifications;

	//-------------------------------------------------------------------
	protected ModifyableImpl() {
		modifications = new ArrayList<>();
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.genericrpg.modification.Modifyable#getModifications()
	 */
	@Override
	public List<Modification> getModifications() {
		return new ArrayList<Modification>(modifications);
	}

	//-------------------------------------------------------------------
	public void clearModifications() {
		modifications.clear();;
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.genericrpg.modification.Modifyable#setModifications(java.util.List)
	 */
	@Override
	public void setModifications(List<Modification> mods) {
		modifications = mods;
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.genericrpg.modification.Modifyable#addModification(de.rpgframework.genericrpg.modification.Modification)
	 */
	@Override
	public void addModification(Modification mod) {
//		if (!modifications.contains(mod))
			modifications.add(mod);
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.genericrpg.modification.Modifyable#setModifications(java.util.List)
	 */
	@Override
	public void removeModification(Modification mod) {
		modifications.remove(mod);
	}

	//-------------------------------------------------------------------
	public void removeModificationForSource(Object src) {
		for (Modification mod : new ArrayList<Modification>(modifications)) {
			if (mod.getSource().equals(src))
				modifications.remove(src);
		}
	}

}
