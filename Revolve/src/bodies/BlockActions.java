/*
 * Sam Aseem Tom
 * Jan 20, 2015
 * An interfaced used for building blocks. Has all the getters that a block needs
 */
package bodies;

import java.awt.image.BufferedImage;

public interface BlockActions {

	/**
	 * Gets the X coordinate of a block
	 * @return the x coordinate
	 */
	public int getX();
	/**
	 * Gets the y coordinate of a block
	 * @return the y coordinate
	 */
	public int getY();
	/**
	 * Gets the width of a block
	 * @return the width of a block
	 */
	public int getW();
	/**
	 * Gets the height of a block
	 * @return the block's height
	 */
	public int getH();
	
	/**
	 * Gets the image that is assigned to a block 
	 * @return the buffered image that corresponds to each block type
	 */
	public BufferedImage getImg();
}
