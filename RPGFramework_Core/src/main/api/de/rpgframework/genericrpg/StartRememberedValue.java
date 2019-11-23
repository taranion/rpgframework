/**
 * 
 */
package de.rpgframework.genericrpg;

/**
 * This interface is implemented by all kinds of values where
 * it is important to remember the initial value the character
 * had.
 * 
 * @author Stefan
 */
public interface StartRememberedValue {

	//--------------------------------------------------------------------
	/**
	 * Set the invested points after generation had been finished.
	 * @param points
	 */
	public void setStart(int points);
	
	//--------------------------------------------------------------------
	/**
	 * Return the invested points after generation has been finished
	 * @return
	 */
	public int getStart();

}
