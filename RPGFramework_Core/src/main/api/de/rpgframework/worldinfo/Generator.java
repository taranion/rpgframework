/**
 *
 */
package de.rpgframework.worldinfo;

import java.util.List;

import de.rpgframework.core.AppliedFilter;
import de.rpgframework.core.Filter;

/**
 * @author Stefan
 *
 */
public interface Generator<T> {

	public String getName();

	public WorldInformationType getType();

	public List<Filter> getSupportedFilter();

	public boolean willWork(AppliedFilter[] choices);

	public T generate(AppliedFilter[] choices);

}
