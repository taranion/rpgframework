/**
 * 
 */
package org.prelle.rpgframework.jfx;

/**
 * @author prelle
 *
 */
public interface DataInputPane<T> {
	
	//-------------------------------------------------------------------
	/**
	 * Data to display
	 */
	public void setData(T data);
	
	//-------------------------------------------------------------------
	/**
	 * Current content of the GUI shall be written into the given object
	 * 
	 * @param data Object to store data in
	 */
	public void writeIntoData(T data);
	
}
