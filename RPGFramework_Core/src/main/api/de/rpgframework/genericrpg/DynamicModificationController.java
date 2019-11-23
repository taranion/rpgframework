/**
 * 
 */
package de.rpgframework.genericrpg;

import java.util.List;

/**
 * This controller enhances the normal controller to be able to
 * add or remove ModifyableValues. E.g. for resources in Ubiquity
 * systems
 * 
 * @author Stefan
 *
 */
public interface DynamicModificationController<T> extends ModificationController<T> {
	
	public List<T> getAvailable();
	
	public int getAddCost(T value);
	
	public int getRemoveCost(T value);

	public boolean canAdd(T data);

	public boolean canRemove(T data);

	public ModifyableValue<T> add(T data);

	public boolean remove(T value);
	
}
