/*
 * Sam Tom Aseem
 * Jan 18, 2016
 * A class that builds teleporter receivers, used as a tp location, not a tp sender
 */
package bodies;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Receiver extends Block {

	//the image for the teleporter block
	private BufferedImage img;
	//the variable that determines what sending node it connects to
	private int connect;
	
	/**
	 * Creates a teleporter receiver with default attributes
	 */
	public Receiver() {
		super();
	}
	
	/**
	 * Creates a teleporter receiver with just an x and a y
	 * @param x the x coordinate
	 * @param y the y coordinate
	 */
	public Receiver(int x, int y) {
		super(x,y);
	}
	
	/**
	 * Creates a teleporter receiver with all the features
	 * @param x the x coordinate
	 * @param y the y coodrinate
	 * @param w the width
	 * @param h the height
	 * @param connect the sender node that it connects to
	 */
	public Receiver(int x, int y, int w, int h, int connect) {
		super(x,y,w,h);
		this.connect = connect;
		try {
			//loads the image
			this.img = ImageIO.read(getClass().getResource("/images/bodyImages/teleporterReceiver.png"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	/**
	 * Gives the connection that the receiving node is connected to
	 * @return the connect value that corresponds to what teleporter it receives from
	 */
	public int getConnect(){
		return this.connect;
	}

	/**
	 * Gets the image for the teleporter receiver
	 * @return the buffered image of the red teleporter block
	 */
	public BufferedImage getImg() {
		return img;
	}

}
