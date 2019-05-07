/*
 * Sam Aseem Tom
 * Jan 20, 2015
 * A class used for building spike blocks, the only type of block that kills you 
 */
package bodies;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Spike extends Block {

	//the image for the spike block
	private BufferedImage img;
	//the direction the spike faces (up, down, left, right) >>> (t, b, l, r)
	private String direction;
	
	/**
	 * Creates a spike with default properties
	 */
	public Spike() {
		super();
	}
	
	/**
	 * Creates a spike with an x and a y
	 * @param x the x coordinate
	 * @param y the y coordinate
	 */
	public Spike(int x, int y) {
		super(x,y);
	}
	
	/**
	 * Creates a spike with all the properties
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param w the width
	 * @param h the height
	 * @param d the direction the spike faces 
	 * @param level the level the spike is on (tilesets)
	 */
	public Spike(int x, int y, int w, int h, String d, int level) {
		//uses the other constructor to build the block
		super(x,y,w,h);
		//defines the direction of the spike
		this.direction = d;
		//note that the level isn't saved to a private variable because it isn't used outside of this constructor
		try {
			//different images are loaded based on what direction the spike faces
			//as well as what tileset is being played
			switch(direction) {
			case "v": //a spike on the roof facing down
				if(level < 4) {
					this.img = ImageIO.read(getClass().getResource("/images/bodyImages/roofSpike.png"));
				} else if(level < 8) {
					this.img = ImageIO.read(getClass().getResource("/images/bodyImages/roofSpikeWorld2.png"));
				} else if (level < 10){
					this.img = ImageIO.read(getClass().getResource("/images/bodyImages/roofSpikeWorld3.png"));
				}else{
					this.img = ImageIO.read(getClass().getResource("/images/bodyImages/roofSpikeWorld4.png"));
				}
				break;
			case ">": //a spike on the left wall facing right
				if(level < 4) {
					this.img = ImageIO.read(getClass().getResource("/images/bodyImages/leftSpike.png"));
				} else if(level < 8) {
					this.img = ImageIO.read(getClass().getResource("/images/bodyImages/leftSpikeWorld2.png"));
				}else if (level < 10){
					this.img = ImageIO.read(getClass().getResource("/images/bodyImages/leftSpikeWorld3.png"));
				}else{
					this.img = ImageIO.read(getClass().getResource("/images/bodyImages/leftSpikeWorld4.png"));
				}
				break;
			case "<": //a spike on a right wall facing left
				if(level < 4) {
					this.img = ImageIO.read(getClass().getResource("/images/bodyImages/rightSpike.png"));
				} else if(level < 8) {
					this.img = ImageIO.read(getClass().getResource("/images/bodyImages/rightSpikeWorld2.png"));
				}else if (level < 10){
					this.img = ImageIO.read(getClass().getResource("/images/bodyImages/rightSpikeWorld3.png"));
				}else{
					this.img = ImageIO.read(getClass().getResource("/images/bodyImages/rightSpikeWorld4.png"));
				}
				break;
			case "^": //a spike facing up (on the ground)
				if(level < 4) {
					this.img = ImageIO.read(getClass().getResource("/images/bodyImages/groundSpike.png"));
				} else if(level < 8) {
					this.img = ImageIO.read(getClass().getResource("/images/bodyImages/groundSpikeWorld2.png"));
				}else if (level < 10){
					this.img = ImageIO.read(getClass().getResource("/images/bodyImages/groundSpikeWorld3.png"));
				}else{
					this.img = ImageIO.read(getClass().getResource("/images/bodyImages/groundSpikeWorld4.png"));
				}
				break;
			}
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * Gets the image for a spike, used for drawing the spikes
	 * @return the spike's image
	 */
	public BufferedImage getImg() {
		return img;
	}
	
	/**
	 * Gets the direction a given spike is facing
	 * @return the direction
	 */
	public String getDir() {
		return direction;
	}

}
