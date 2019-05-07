/*
 * Sam Tom Aseem
 * Jan 6 2016
 * Player class that has all the collision detection and player physics
 */

package bodies;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import gamestate.LevelState;

public class Player {

	// contains the player's sprite image
	private BufferedImage player;

	// booleans used for jumping
	private boolean jumping, grounded, jumped, water = false;

	// sound files used by the player
	private AudioClip jumpSound, bounceSound, deathSound, teleportSound;

	private boolean keys[] = new boolean[10]; // the list of keys the user can
												// press
	private double gravity = 0.1, gravC = 0.1; // the initial y gravity
	private double hGravity = 0; // initially, there is no horizontal gravity
	private String gravityDir = "b"; // initially gravity is pulling down
	private double friction = 0.8; // friction in both directions is 0.8. this
									// slows down the player after releasing a
									// key
	private double vFriction = 0.8;
	private double savedVX = 0, savedVY = 0; // used for when the game pauses
	private double savedX, savedY; // saves the player's x and y at the start so
									// they can be re spawned
	private double x, y; // the player's x and y positions
	private int width, height; // the player's width and height
	private double speed = 10, accel = 2;
	private double vX, vY; // the player's y and x speeds

	//the blocks of the map
	private ArrayList<BlockActions> blocks = LevelState.getBlocks(); 

	private static final int JUMP_SPEED = 15; // the player's jump speed
	private static final int BOUNCE_SPEED = 20; // the base bounce speed from
												// bouncy blocks

	public Player(int x, int y) {
		this.x = x;
		this.y = y;
		// savedX and savedY for if the user dies
		savedX = x;
		savedY = y;
		// player's height and width
		height = 40;
		width = 40;
		// the variables handling when the user is airborne
		jumping = true;
		grounded = false;
		jumped = false;

		// loads all the sounds for the game
		URL jumpURL = Player.class.getResource("jump.wav");
		// URL dashURL = Player.class.getResource("dash.wav");
		URL bounceURL = Player.class.getResource("bounce.wav");
		URL deathURL = Player.class.getResource("death.wav");
		URL teleportURL = Player.class.getResource("teleport.wav");

		jumpSound = Applet.newAudioClip(jumpURL);
		bounceSound = Applet.newAudioClip(bounceURL);
		deathSound = Applet.newAudioClip(deathURL);
		teleportSound = Applet.newAudioClip(teleportURL);

		try {
			// loads the player image
			player = ImageIO.read(getClass().getResource("/images/bodyImages/player.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		LevelState.setTranslateX(0);
		LevelState.setTranslateY(0);
	}

	/**
	 * Jumps the player. Determines which way gravity is facing, and changes y
	 * or x velocity
	 * 
	 * @param vel
	 *            the velocity to change in whatever direction is the opposite
	 *            to gravity
	 */
	public void jump(double vel) {
		// if the player isn't performing a regular jump, the bounce sound
		// should be played
		if (vel > JUMP_SPEED) {
			bounceSound.play(); // play the jump sound
		} else {
			jumpSound.play(); // play the regular jump sound
		}

		// the player jumps
		jumping = true;
		grounded = false;
		// sets the velocity (x or y) based on which way the gravity is facing
		if (gravityDir.equals("t")) {
			vY = vel;
		} else if (gravityDir.equals("b")) {
			vY = -vel;
		} else if (gravityDir.equals("l")) {
			vX = vel;
		} else if (gravityDir.equals("r")) {
			vX = -vel;
		}

		jumped = true;
	}

	/**
	 * The main update method for the player. It calls all the things that need
	 * to be updated per frame, in order
	 * 
	 * @param g
	 *            the graphics tool that's needed for the draw method and the
	 *            camera updating
	 */
	public void update(Graphics g) {

	}

	/**
	 * Controls shifts in gravity and plain old movement
	 */
	public void updateMovement(boolean space) {
		if (space) {
			water = true;
		}
		if (water) {
			gravC = 0.1;
			accel = 0;
			speed = 3;
		} else {
			gravC = 0.5;
			accel = 2;
			speed = 10;
		}
		// GRAVITY CHANGING (WASD)
		// The gravity changes based on which key the user pressed (WASD)
		// Gravity can be changed if the user is touching the ground, or is in
		// water
		if (grounded || water) {

			if (keys[5] || (water && gravity == -.5) || (!water && gravity == -.1)) { // w
																						// key
				gravityDir = "t";
				gravity = -gravC;
				if (!water) {
					hGravity = 0;
				}
			} else if (keys[6] || (water && hGravity == -.5) || (!water && hGravity == -.1)) { // a
																								// key
				gravityDir = "l";
				if (!water) {
					gravity = 0;
				}
				hGravity = -gravC;
			} else if (keys[7] || (water && gravity == .5) || (!water && gravity == .1)) { // s
																							// key
				gravityDir = "b";
				gravity = gravC;
				if (!water) {
					hGravity = 0;
				}
			} else if (keys[8] || (water && hGravity == .5) || (!water && hGravity == .1)) { // d
																								// key
				gravityDir = "r";
				if (!water) {
					gravity = 0;
				}
				hGravity = gravC;
			}
		} else { // if they're not grounded OR they're not in water
			if (gravityDir == "t") {
				gravity = -gravC;
				hGravity = 0;
			} else if (gravityDir == "l") {
				hGravity = -gravC;
				gravity = 0;
			} else if (gravityDir == "b") {
				gravity = gravC;
				hGravity = 0;
			} else if (gravityDir == "r") {
				hGravity = gravC;
				gravity = 0;
			}
		}

		// MOVEMENT (ARROW KEYS)
		// if gravity is pulling up OR down, OR the player is in water
		// the right and left keys are used for moving
		if (gravityDir.equals("t") || gravityDir.equals("b") || water == true) {

			if (keys[0]) { // right key
				if (vX < speed) {
					vX += accel;
				}
			}
			if (keys[1]) { // left key
				if (vX > -speed) {
					vX -= accel;
				}
			}
			if (!water) {
				// player can only jump while grounded
				if (keys[2] && gravityDir.equals("b")) { // up key
					if (!jumping && grounded && !jumped) {
						jump(JUMP_SPEED);
					}
				}
				if (keys[3] && gravityDir.equals("t")) { // down key
					if (!jumping && grounded && !jumped) {
						jump(JUMP_SPEED);
					}
				}
			}
		}
		// if the gravity is pulling side to side, or water is true, the up and
		// down keys are for moving, not jumping
		if (gravityDir.equals("l") || gravityDir.equals("r") || water == true) {
			if (keys[2]) { // up key
				if (vY < speed) {
					vY -= accel;
				}
			}
			if (keys[3]) { // down key
				if (vY > -speed) {
					vY += accel;
				}
			}
			if (!water) {
				if (keys[0] && gravityDir.equals("l")) { // right key
					if (!jumping && grounded && !jumped) {
						// jumps the player
						jump(JUMP_SPEED);
					}
				}
				if (keys[1] && gravityDir.equals("r")) { // left key
					if (!jumping && grounded && !jumped) {
						jump(JUMP_SPEED);
					}
				}
			}
		}
		// if the user is in water
		if (water) {
			vX *= 0.99;
			vY += gravity;
			vY *= 0.99;
			vX += hGravity;

		} else if (gravityDir.equals("t") || gravityDir.equals("b")) {
			// the user is not in water, and gravity is either up (t) or down
			// (b)
			vX *= friction;
			vY += gravity;
		} else if (gravityDir.equals("l") || gravityDir.equals("r")) {
			// the user is not in water, and the gravity is either left (l) or
			// right (r)
			vY *= vFriction;
			vX += hGravity;
		}

		// sets groudned to false for collision checking
		grounded = false;

	}

	/**
	 * Gets if the user has pressed the esc key
	 * 
	 * @return true if they have pressed it, false if they're not
	 */
	public boolean getPaused() {
		if (keys[9]) {
			return true;
		}
		return false;
	}

	/**
	 * Performs collision detection for the player and figures out which blocks
	 * its touching Used for specialized collisions (ex. spikes), and regular
	 * blocks
	 * 
	 * @return the int that means what it's touching (for example, 2 is touching
	 *         the finish block, 0 is nothing)
	 */
	public int updateCollision() {
		// loops through every block in the arraylist of blocks
		// to check collision with each one
		water = false;

		for (BlockActions b : blocks) {
			if (b.getX() >= x - 40 && b.getX() <= x + 40 || b.getY() >= y - 40 && b.getY() <= y + 40) {

				// gets the string for whichever direction the player is
				// colliding with the block
				// the string will be "" if there is no collision
				String dir = collisionCheck(this, b);
				// these upper if statements check which direction gravity is
				// pointing
				if (gravityDir.equals("t")) { // a gravity is pulling the player
												// up
					if (dir == "l" || dir == "r") { // if there is a left or
													// right collision
						vX = 0; // stops the player in their tracks
					} else if (dir == "b") { // since gravity is pulling up, a
												// "b" collision means the
												// player hits his head
						vY = 0; // stops the player from falling
					} else if (dir == "t") { // there is a collision on a block
						// that acts as the ground
						grounded = true; // the player is grounded

						jumping = false; // no longer jumping
						jumped = false;
						// figures out if the player is touching slime
						// if they are, jumps them by the slime's bounce speed
						if (b.getClass().toString().equals("class bodies.Slime")) {
							jump(BOUNCE_SPEED);
						} else if (b.getClass().toString().equals("class bodies.Teleport")) {

							// make this into method
							teleportSound.play();
							x = ((Teleport) b).getSendX();
							y = ((Teleport) b).getSendY() + 40;
						}
					}

				} else if (gravityDir.equals("b")) { // if the gravity is
														// pulling down
					// checks horizontal collision, if they are colliding,
					// x speed is set to 0
					if (dir == "l" || dir == "r") {
						vX = 0;
					} else if (dir == "t") {
						vY = 0;
					} else if (dir == "b") {
						// basically if they're grounded, these variables need
						// to be set
						// because they're used in jumping and falling
						grounded = true;
						jumping = false;
						jumped = false;
						// if they're touching a slime block
						if (b.getClass().toString().equals("class bodies.Slime")) {
							jump(BOUNCE_SPEED); // jump with a higher bounce
												// than a regular jump
						} else if (b.getClass().toString().equals("class bodies.Teleport")) {
							// or if they're touching a teleporter (not a
							// receiver)
							teleportSound.play();
							// teleport them to the receiver
							x = ((Teleport) b).getSendX();
							y = ((Teleport) b).getSendY() - 40;
						}
					}
				} else if (gravityDir.equals("l")) { // if gravity is pulling to
														// the left
					// testing for "side" collisions (which are actually bottoms
					// and tops of blocks)
					if (dir == "t" || dir == "b") {
						vY = 0; // if there is one, y speed is 0
					} else if (dir == "r") {
						vX = 0;
					} else if (dir == "l") { // if there is a left collision,
												// the left direction acts as
												// the ground
						grounded = true;
						jumping = false;
						jumped = false;
						if (b.getClass().toString().equals("class bodies.Slime")) {
							jump(BOUNCE_SPEED);
						} else if (b.getClass().toString().equals("class bodies.Teleport")) {
							teleportSound.play();
							x = ((Teleport) b).getSendX() + 40;
							y = ((Teleport) b).getSendY();
						}
					}
				} else if (gravityDir.equals("r")) { // if gravity is pulling to
														// the right
					// t and b act as floors and roofs
					// so y speed is set to 0 if they're being touched
					if (dir == "t" || dir == "b") {
						vY = 0;
					} else if (dir == "l") { // l acts as a roof, but its still
												// horizontal, so vX is 0
						vX = 0;
					} else if (dir == "r") { // r acts as a floor, so they have
												// to be touching a right wall
												// to jump
						grounded = true;
						jumping = false;
						jumped = false;
						// if they're touching slime
						if (b.getClass().toString().equals("class bodies.Slime")) {
							jump(BOUNCE_SPEED); // big jump!
						} else if (b.getClass().toString().equals("class bodies.Teleport")) {
							// if they're touching a teleporter sending node
							teleportSound.play();
							x = ((Teleport) b).getSendX() - 40;
							y = ((Teleport) b).getSendY();
						}
					}
				}

				// if the player is hitting a block
				if (dir != "") {
					// if the user hits a spike block
					if (b.getClass().toString().equals("class bodies.Spike")) {
						// everything here has to be done if the user dies,
						// defaults are brought back, death sound played etc.
						deathSound.play();
						this.x = savedX;
						this.y = savedY;
						vX = 0;
						vY = 0;
						LevelState.setTranslateX(0);
						LevelState.setTranslateY(0);
						gravityDir = "b";
						gravity = 0.5;
						return 1;
					} else if (b.getClass().toString().equals("class bodies.End")) {
						return 2;
					}

				}
			}

		}

		// if the user is grounded, their speed that corresponds to which
		// direction gravity is pulling is set to 0
		if (grounded) {
			if (gravityDir.equals("t") || gravityDir.equals("b")) {
				vY = 0;
			}
			if (gravityDir.equals("l") || gravityDir.equals("r")) {
				vX = 0;
			}

		}
		return 0;
	}

	/**
	 * Draws the actual player sprite
	 * 
	 * @param g
	 *            the graphic tool to use for the drawing
	 */
	public void draw(Graphics g) {
		g.drawImage(player, (int) x, (int) y, null);
	}

	/**
	 * Used for determining what keys the user is pressing to determine gravity
	 * directions and movement
	 * 
	 * @param k
	 *            the key code used to determine what key the user presses
	 */
	public void keyPressed(int k) {
		// there is an array that is checked during the movement updating.
		// the key presses change the array, true for pressed, false for not
		// pressed.
		if (k == KeyEvent.VK_RIGHT) {
			keys[0] = true;
		}
		if (k == KeyEvent.VK_LEFT) {
			keys[1] = true;
		}
		if (k == KeyEvent.VK_UP) {
			keys[2] = true;
		}
		if (k == KeyEvent.VK_DOWN) {
			keys[3] = true;
		}
		if (k == KeyEvent.VK_Z) { // dash
			keys[4] = true;
		}
		if (k == KeyEvent.VK_W) {
			keys[5] = true;
		}
		if (k == KeyEvent.VK_A) {
			keys[6] = true;
		}
		if (k == KeyEvent.VK_S) {
			keys[7] = true;
		}
		if (k == KeyEvent.VK_D) {
			keys[8] = true;
		}
		if (k == KeyEvent.VK_ESCAPE) {
			if (!keys[9]) {
				keys[9] = true;
			} else {
				keys[9] = false;
				LevelState.setVelocitiesSaved(false);
				vX = savedVX;
				vY = savedVY;
			}
			// keys[9] = true;
		}
	}

	/**
	 * Used for determining if the player wants to stop issuing a given command
	 * (ex. move right)
	 * 
	 * @param k
	 *            the key code used for figuring out what the user just stopped
	 *            pressing
	 */
	public void keyReleased(int k) {
		// Same as the key pressed, but everything is changed back to false if
		// the player stops issuing a command
		if (k == KeyEvent.VK_RIGHT) {
			keys[0] = false;
		}
		if (k == KeyEvent.VK_LEFT) {
			keys[1] = false;
		}
		if (k == KeyEvent.VK_UP) {
			keys[2] = false;
			jumped = false;
		}
		if (k == KeyEvent.VK_DOWN) {
			keys[3] = false;
		}
		if (k == KeyEvent.VK_Z) { // dash
			keys[4] = false;
		}
		if (k == KeyEvent.VK_W) {
			keys[5] = false;
		}
		if (k == KeyEvent.VK_A) {
			keys[6] = false;
		}
		if (k == KeyEvent.VK_S) {
			keys[7] = false;
		}
		if (k == KeyEvent.VK_D) {
			keys[8] = false;
		}
		if (k == KeyEvent.VK_ESCAPE) {
			// keys[9] = false;
		}
	}

	/**
	 * Figures out if the player is colliding with any blocks
	 * 
	 * @param p
	 *            takes the player to use in the method
	 * @param b
	 *            also uses any supplied block for collision detection
	 * @return the direction of collision, l for left, r for right etc.
	 */
	public String collisionCheck(Player p, BlockActions b) {
		// get the vectors to check against
		// vX = [playerX + (playerWidth / 2)] - [blockX + (blockWidth / 2)]
		// vY = [playerY + (playerHeight / 2)] - [blockY + (blockHeight/ 2)]
		double vX = (p.x + (p.width / 2)) - (b.getX() + (b.getW() / 2)),
				vY = (p.y + (p.height / 2)) - (b.getY() + (b.getH() / 2)),
				// add the half widths and half heights of the objects
				// hWidth = (playerWidth / 2) + (blockWidth / 2)
				// hHeights = (playerHeight / 2) + (blockHeight / 2)
				hWidths = (p.width / 2) + (b.getW() / 2), hHeights = (p.height / 2) + (b.getH() / 2);

		// the string that will be eventually returned, telling the game what
		// direction there is a collision on
		String colDir = "";

		// if the x and y vector are less than the half width or half height,
		// they we must be inside the object, causing a collision
		if (Math.abs(vX) < hWidths && Math.abs(vY) < hHeights) {
			// figures out on which side we are colliding (top, bottom, left, or
			// right)
			double oX = hWidths - Math.abs(vX), oY = hHeights - Math.abs(vY);
			if (oX >= oY) {
				if (vY > 0) {
					if (!b.getClass().toString().equals("class bodies.Water")) {
						colDir = "t"; // top collision
						// water = false;
						p.y += oY;
					} else {
						water = true;
					}
				} else {
					// colDir = "b"; // bottom collision
					if (!b.getClass().toString().equals("class bodies.Water")) {
						colDir = "b"; // top collision
						// water = false;
						p.y -= oY;
					} else {
						water = true;
					}
				}
			} else {
				if (vX > 0) {
					// colDir = "l"; // left collision
					if (!b.getClass().toString().equals("class bodies.Water")) {
						colDir = "l"; // top collision
						// water = false;
						p.x += oX;
					} else {
						water = true;
					}
				} else {
					// colDir = "r"; // right collision
					if (!b.getClass().toString().equals("class bodies.Water")) {
						colDir = "r"; // top collision
						// water = false;
						p.x -= oX;
					} else {
						water = true;
					}

				}
			}
		}
		return colDir; // returns which direction the collision is
	}

	/**
	 * Gets a player's x coordinate
	 * 
	 * @return the player's x
	 */
	public double getX() {
		return x;
	}

	/**
	 * Gets a player's y coordinate
	 * 
	 * @return the player's y
	 */
	public double getY() {
		return y;
	}

	/**
	 * Gets a player's height
	 * 
	 * @return the player's height
	 */
	public int getH() {
		return height;
	}

	/**
	 * Gets a player's width
	 * 
	 * @return the player's width
	 */
	public int getW() {
		return width;
	}

	/**
	 * Gets a player's x speed
	 * 
	 * @return the player's x speed
	 */
	public double getVx() {
		return vX;
	}

	/**
	 * Gets a player's y speed
	 * 
	 * @return the player's y speed
	 */
	public double getVy() {
		return vY;
	}

	/**
	 * Sets the player's x coordinate, used for teleporting
	 * 
	 * @param x
	 *            the new x coordinate for the player
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * Sets a player's y coordinate, used for teleporting
	 * 
	 * @param y
	 *            the new y coordinate for the player
	 */
	public void setY(double y) {
		this.y = y;
	}

	/**
	 * Gets the array of key pressed which are true if the key is pressed, false
	 * if it isn't
	 * 
	 * @return the array of key[] that shows which keys are currently being
	 *         pressed
	 */
	public boolean[] getKeys() {
		return keys;
	}

	/**
	 * Sets the player's x speed
	 * 
	 * @param vx
	 *            the new player x speed
	 */
	public void setVX(double vx) {
		savedVX = vX;
		vX = vx;
	}

	/**
	 * Sets the player's y speed
	 * 
	 * @param vy
	 *            the new y speed for the player
	 */
	public void setVY(double vy) {
		savedVY = vY;
		vY = vy;
	}

	/**
	 * gets the player's saved x speed for picking up where they left off when
	 * pausing
	 * 
	 * @return the saved x speed
	 */
	public double getSavedVX() {
		return savedVX;
	}

	/**
	 * gets the player's saved y speed for picking up where they left off when
	 * pausing
	 * 
	 * @return the saved y speed
	 */
	public double getSavedVY() {
		return savedVY;
	}

	/**
	 * gets which direction the gravity is pulling in
	 * 
	 * @return the gravity direction
	 */
	public String getGravityDir() {
		return gravityDir;
	}
}