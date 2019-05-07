/*
 * Sam Aseem Tom
 * Jan 20, 2015
 * A class that builds water blocks, water blocks have a control change
 */
package bodies;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Water extends Block {

	//the image for the receiver block
	private BufferedImage img;

	/**
	 * Creates a water block object
	 */
	public Water() {
		super();
	}

	/**
	 * Creates a water block with custom position
	 * @param x the x coordinate for the water block
	 * @param y the y coordinate for the water block
	 */
	public Water(int x, int y) {
		super(x, y);
	}

	/**
	 * Creates a water block with custom position and size also with an attacked
	 * image
	 * 
	 * @param x
	 * @param y
	 * @param w
	 * @param H
	 * @return Water
	 */
	public Water(int x, int y, int w, int h) {
		super(x, y, w, h);
		try {
			this.img = ImageIO.read(getClass().getResource("/images/bodyImages/water.png"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * Returns the image attached to the Water block
	 * 
	 * @param none
	 * @return img
	 */
	public BufferedImage getImg() {
		return img;
	}

}
