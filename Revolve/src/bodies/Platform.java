/*
 * Sam Aseem Tom
 * Jan 20, 2015
 * A class that builds platform blocks objects
 */

package bodies;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Platform extends Block {

	//the player's image
	private BufferedImage img;
	
	/**
	 * Creates a platform with nothing but the default properties
	 * inherits the constructor from its parent class "Block"
	 */
	public Platform() {
		super();
	}
	
	/**
	 * Creates a platform block with an x and a y
	 * @param x the block's x coordinate
	 * @param y the block's y coordinate
	 */
	public Platform(int x, int y) {
		super(x,y);
	}
	
	/**
	 * Creates a platform with ALL the features
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param w the width
	 * @param h the height
	 * @param level the level the block will appear on (different images for differnet levels)
	 */
	public Platform(int x, int y, int w, int h, int level) {
		super(x,y,w,h);
		try {
			//loads the graphic based on what level it is
			if(level < 4) {
				this.img = ImageIO.read(getClass().getResource("/images/bodyImages/platform.png"));
			} else if(level < 8) {
				this.img = ImageIO.read(getClass().getResource("/images/bodyImages/platformWorld2.png"));
			} else if (level < 10){
				this.img = ImageIO.read(getClass().getResource("/images/bodyImages/platformWorld3.png"));
			} else {
				this.img = ImageIO.read(getClass().getResource("/images/bodyImages/platformWorld4.png"));
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	/**
	 * Gets the image for the platform block
	 * @return the platform's image
	 */
	public BufferedImage getImg() {
		return img;
	}

}
