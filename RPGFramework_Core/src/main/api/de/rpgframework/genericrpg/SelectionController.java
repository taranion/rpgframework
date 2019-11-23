/**
 * 
 */
package de.rpgframework.genericrpg;

import java.util.List;

import de.rpgframework.genericrpg.modification.ValueModification;

/**
 * @author Stefan
 *
 */
public interface SelectionController<T extends SelectableItem, V extends SelectedValue<T> > {

	public List<T> getAvailable();

	public List<V> getSelected();
	
	public double getSelectionCost(T data);
	
	public double getDeselectionCost(V value);

	public boolean canBeSelected(T data);

	public boolean canBeDeselected(V value);

	public ValueModification<T> createModification(V value);

	public V select(T data);

	public boolean deselect(V value);
	
	
	public boolean needsOptionSelection(T toSelect);
	
	public List<?> getOptions(T toSelect);

	public V select(T data, Object option);

}
