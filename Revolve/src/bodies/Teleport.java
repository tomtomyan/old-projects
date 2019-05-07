/*
 * Sam Aseem Tom
 * Jan 20, 2015
 * A class that builds purple teleporting blocks, used for transporting a player to a receiver node
 */
package bodies;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Teleport extends Block {

	//the block's image
	private BufferedImage img;
	//the default sendX and sendY variables for sending coordinates
	private int sendX = 500, sendY = 500;

	/**
	 * Creates a teleporter object
	 */
	public Teleport() {
		super();
	}

	/**
	 * Creates a teleporter object with a custom location
	 * @param x the x coordinate for a teleporter
	 * @param y the y coordinate for a teleporter
	 */
	public Teleport(int x, int y) {
		super(x, y);
	}

	/**
	 * Creates a teleporter with a custom location and size also has an image
	 * attached to it
	 * @param x the x location
	 * @param y the y location
	 * @param w the width
	 * @param h the height
	 */
	public Teleport(int x, int y, int w, int h) {
		super(x, y, w, h);
		try {
			this.img = ImageIO.read(getClass().getResource("/images/bodyImages/teleporterSender.png"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * Sets the send position on the x axis
	 * @param sX the new x
	 */
	public void setSendX(int sX) {
		sendX = sX;
	}

	/**
	 * Returns the send position on the x axis
	 * @return sendX theh send position x value
	 */
	public int getSendX() {
		return sendX;
	}

	/**
	 * Sets the send position on the y axis
	 * 
	 * @param sY the send position y value
	 * @return void
	 */
	public void setSendY(int sY) {
		sendY = sY;
	}

	/**
	 * Returns the send position on the y axis
	 * @return sendY the y send position
	 */
	public int getSendY() {
		return sendY;
	}

	/**
	 * Returns the image attached to the object
	 * @return img the teleporter block's image
	 */
	public BufferedImage getImg() {
		return img;
	}

}
