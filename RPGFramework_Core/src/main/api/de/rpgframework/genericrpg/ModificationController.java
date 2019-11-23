/**
 *
 */
package de.rpgframework.genericrpg;

import java.util.List;

/**
 * This controller provides methods to handle a static list of
 * values.
 *
 * @author Stefan
 *
 */
public interface ModificationController<T> {

	public List<ModifyableValue<T>> getSelected();

	public int getIncreaseCost(ModifyableValue<T> value);

	public int getDecreaseCost(ModifyableValue<T> value);

	public boolean canBeIncreased(ModifyableValue<T> value);

	public boolean canBeDecreased(ModifyableValue<T> value);

	public boolean increase(ModifyableValue<T> value);

	public boolean decrease(ModifyableValue<T> value);

}
