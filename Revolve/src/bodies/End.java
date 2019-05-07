/*
 * Sam Aseem Tom
 * Jan 20, 2015
 * A class that builds ending blocks, the block used for completing a level
 */
package bodies;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class End extends Block {

	private BufferedImage img;

	/**
	 * Creates an end block object
	 */
	public End() {
		super();
	}

	/**
	 * Creates an end block object with custom position
	 * 
	 * @param x the end block's x coordinate
	 * @param y the end block's y coordinate
	 */
	public End(int x, int y) {
		super(x, y);
	}

	/**
	 * Creates an end block object with custom position and size also with an
	 * image attached to it
	 * 
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param w the width
	 * @param h  the height
	 */
	public End(int x, int y, int w, int h) {
		super(x, y, w, h);
		try {
			//loads the end block image
			this.img = ImageIO.read(getClass().getResource("/images/bodyImages/finishBlock.png"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * Returns the image attached to the End block
	 * @return the image of the end block
	 */
	public BufferedImage getImg() {
		return img;
	}

}
