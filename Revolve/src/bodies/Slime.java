/*
 * Sam Aseem Tom
 * Jan 20, 2015
 * A class that builds slime blocks, they act as bouncy pads
 */
package bodies;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Slime extends Block {

	//the slime block image
	private BufferedImage img;
	
	/**
	 * Creates a slime block with default properties
	 */
	public Slime() {
		super();
	}
	
	/**
	 * Creates a slime block with an x and y coordinate
	 * @param x the x coordinate
	 * @param y the y coordinate
	 */
	public Slime(int x, int y) {
		super(x,y);
	}
	
	/**
	 * Creates a bouncy block with ALL the features
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param w the width
	 * @param h the height
	 * @param level the level the block will appear on (different images for differnet levels)
	 */
	public Slime(int x, int y, int w, int h, int level) {
		super(x,y,w,h);
		try {
			//loads the image based on what level it is
			//(tilesets)
			if(level < 4) { //level 1 - 4 
				this.img = ImageIO.read(getClass().getResource("/images/bodyImages/slime.png"));
			} else if(level < 8) {
				this.img = ImageIO.read(getClass().getResource("/images/bodyImages/slimeWorld2.png"));
			} else if (level < 10){
				this.img = ImageIO.read(getClass().getResource("/images/bodyImages/slimeWorld3.png"));
			} else{
				this.img = ImageIO.read(getClass().getResource("/images/bodyImages/slimeWorld4.png"));
			}
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * Gets the image for a slime block
	 * @return the block's image
	 */
	public BufferedImage getImg() {
		return img;
	}

}
