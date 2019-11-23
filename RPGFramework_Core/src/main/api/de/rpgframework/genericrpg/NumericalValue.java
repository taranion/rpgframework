/**
 *
 */
package de.rpgframework.genericrpg;

/**
 * @author Stefan
 *
 */
public interface NumericalValue<T> extends SelectedValue<T> {

	//--------------------------------------------------------------------
	/**
	 * Returns the points invested into this value
	 * @return Invested points
	 */
	public int getPoints();

	//--------------------------------------------------------------------
	/**
	 * Set the points invested into this value;
	 */
	public void setPoints(int points);

}
