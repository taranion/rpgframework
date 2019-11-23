/**
 * 
 */
package de.rpgframework.support.combat.map;

import de.rpgframework.media.Media;

/**
 * @author prelle
 *
 */
public interface MatrixMap extends Media {

	//-------------------------------------------------------------------
	public int getWidth();

	//-------------------------------------------------------------------
	public int getHeight();

    //------------------------------------------------
    public byte[] renderImage(int x, int y, int w, int h);

	//-------------------------------------------------------------------
	public double getFieldSize();

	//-------------------------------------------------------------------
	/**
	 * How many pixels is the width/height of a square on the map
	 */
	public void setFieldSize(double value);

	//-------------------------------------------------------------------
	public int getOffsetX();

	//-------------------------------------------------------------------
	public void setOffsetX(int value);

	//-------------------------------------------------------------------
	public int getOffsetY();

	//-------------------------------------------------------------------
	public void setOffsetY(int value);

}
