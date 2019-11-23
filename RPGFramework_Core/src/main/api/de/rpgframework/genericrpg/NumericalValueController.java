/**
 *
 */
package de.rpgframework.genericrpg;


/**
 * This controller provides methods to handle a static list of
 * values.
 *
 * @author Stefan
 *
 */
public interface NumericalValueController<T,V extends SelectedValue<T>> {

	public boolean canBeIncreased(V value);

	public boolean canBeDecreased(V value);

	public boolean increase(V value);

	public boolean decrease(V value);

}
