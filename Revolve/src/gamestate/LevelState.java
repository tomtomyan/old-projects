/*
 * Sam Macpherson, Aseem Malhotra, Tom Yan
 * Jan 20, 2016
 * The level state used for actually playing levels, each level is a level state
 */
package gamestate;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JComboBox.KeySelectionManager;

import bodies.Block;
import bodies.BlockActions;
import bodies.End;
import bodies.Spike;
import bodies.Teleport;
import bodies.Water;
import bodies.Platform;
import bodies.Player;
import bodies.Receiver;
import bodies.Slime;
import main.GamePanel;
import sun.audio.AudioStream;

import java.text.DecimalFormat;

public class LevelState extends GameState {

	//when the game is paused, it needs to know if it should save the player's velocities
	private static boolean hasSavedVelocities = false;

	//when the player has left
	private boolean exited = false;

	//the decimal format used for formatting the timer
	public static DecimalFormat timerFormat;

	//the player object
	private Player player;
	//the list of blocks (platforms, spikes, etc)
	private static ArrayList<BlockActions> blocks;
	//if the user is in space (the tileset)
	private boolean space = false;
	//if the game is paused
	private static boolean pause = false, begin = true; //if the game needs to start clocking
	//the game's level
	private int level, end = 0;
	//the two variables used for timing
	private long start = 0, time = 0;
	//the background for the pause screen
	private BufferedImage pauseImg;

	//the level background
	private BufferedImage bg;

	//the three buttons that are found on the pause screen
	private BufferedImage mainOne;
	private BufferedImage mainTwo;

	private BufferedImage levelSOne;
	private BufferedImage levelSTwo;

	private BufferedImage continueOne;
	private BufferedImage continueTwo;

	//the current choice for the pause screen
	private int currentChoice = 0;
	//the options on the pause screen
	private String[] options = { "Main", "Level Select", "Continue" };

	private static double translateX, translateY; // the factor
													// to move the
													// camera by so the player
													// is always centered

	/**
	 * Creates the level based on which you have selected
	 * 
	 * @param gsm
	 * @param level
	 * @return LevetState
	 */
	public LevelState(GameStateManager gsm, int level) {
		begin = true;
		this.gsm = gsm;
		this.level = level;

		start = 0;
		time = 0;

		init();
	}

	/**
	 * Initializes some information about a level
	 */
	@Override
	public void init() {
		timerFormat = new DecimalFormat("#,000.00");
		try {
			// loads the correct background
			if (level < 4) {
				// tutorial and first world
				bg = null;
			} else if (level < 8) { // levels 0-3 have no bg, level 4-8 have the
									// // factory
				bg = ImageIO.read(getClass().getResourceAsStream("/images/levelBackgrounds/world2Background.png"));
			} else if (level < 10) {
				bg = ImageIO.read(getClass().getResourceAsStream("/images/levelBackgrounds/world3Background.png"));
			} else if (level < 12) {
				bg = ImageIO.read(getClass().getResourceAsStream("/images/levelBackgrounds/world4Background.png"));

			}

			pauseImg = ImageIO.read(getClass().getResourceAsStream("/images/gameStateImages/pauseScreen.png"));

			mainOne = ImageIO.read(getClass().getResourceAsStream("/images/gameStateImages/main1.png"));
			mainTwo = ImageIO.read(getClass().getResourceAsStream("/images/gameStateImages/main2.png"));

			levelSOne = ImageIO.read(getClass().getResourceAsStream("/images/gameStateImages/levelSelect1.png"));
			levelSTwo = ImageIO.read(getClass().getResourceAsStream("/images/gameStateImages/levelSelect2.png"));

			continueOne = ImageIO.read(getClass().getResourceAsStream("/images/gameStateImages/continueButton1.png"));
			continueTwo = ImageIO.read(getClass().getResourceAsStream("/images/gameStateImages/continueButton2.png"));

		} catch (Exception e) {
			e.printStackTrace();
		}

		blocks = new ArrayList<>(); //makes a new list for the map layout, to be populated later
		player = new Player(150, 1400); //makes the player object

		//opens the file reading for the level layout and the teleporting data files
		try {
			InputStreamReader[] isr = new InputStreamReader[2];
			if (level == -1) {
				isr[0] = new InputStreamReader(LevelState.class.getResourceAsStream("/levels/tutorial.lvl"));
				isr[1] = new InputStreamReader(LevelState.class.getResourceAsStream("/levels/tutorialTeleport.lvl"));
			}
			for (int i = 0; i < 12; i++) {
				if (level == i) {
					isr[0] = new InputStreamReader(
							LevelState.class.getResourceAsStream("/levels/level" + (i + 1) + ".lvl"));
					isr[1] = new InputStreamReader(
							LevelState.class.getResourceAsStream("/levels/Teleport" + (i + 1) + ".lvl"));
				}
			}

			BufferedReader[] br = new BufferedReader[2];
			br[0] = new BufferedReader(isr[0]);
			br[1] = new BufferedReader(isr[1]);
			String map[][] = new String[40][60];

			// Adds the level file into a 2D array
			for (int i = 0; i < 40; i++) { // number of lines in the data file
				String row = br[0].readLine(); // reads each line
				for (int j = 0; j < 60; j++) { // width of map (number of
												// blocks)
					map[i][j] = row.substring(j, j + 1); // adds each character
															// from the line
				}
			}

			// HORIZONTAL READING
			for (int i = 0; i < 40; i++) {// reads each row
				for (int j = 0; j < 60; j++) { // reads each index in the row
					if (map[i][j].equals("_")) { // if the block in the index is
													// _ (horizontal platform)
						int x = j * 40; // sets the x position to the location
										// in the array * 40 (block size)
						int width = 40; // sets the default width to 40
						while (j < 59 && map[i][j + 1].equals("_")) { // checks
																		// if
																		// next
																		// block
																		// is
																		// the
																		// same
																		// block
							width += 40; // adds 40 to the block width depending
											// on how many there are
							j++; // increments the array counter by 1
						}
						blocks.add(new Platform(x, i * 40, width, 40, level)); // adds
																				// new
																				// platform
																				// to
																				// the
																				// blocks
					} else if (map[i][j].equals("^")) { // if the block in the
														// index is ^ (up spike)
						int x = j * 40;
						int width = 40;
						while (j < 59 && map[i][j + 1].equals("^")) {
							width += 40;
							j++;
						}
						blocks.add(new Spike(x, i * 40 + 20, width, 20, "^", level));
					} else if (map[i][j].equals("v")) { // if the block in the
														// index is v (down
														// spike)
						int x = j * 40;
						int width = 40;
						while (j < 59 && map[i][j + 1].equals("v")) {
							width += 40;
							j++;
						}
						blocks.add(new Spike(x, i * 40, width, 20, "v", level));
					} else if (map[i][j].equals("s")) { // if the block in the
														// index is s (slime
														// block)
						int x = j * 40;
						int width = 40;
						while (j < 59 && map[i][j + 1].equals("s")) {
							width += 40;
							j++;
						}
						blocks.add(new Slime(x, i * 40, width, 40, level));
					} else if (map[i][j].equals("w")) { // if the block in the
														// index is w (water
														// block)
						int x = j * 40;
						int width = 40;
						while (j < 59 && map[i][j + 1].equals("w")) {
							width += 40;
							j++;
						}
						blocks.add(new Water(x, i * 40, width, 40));
					} else if (map[i][j].equals("t")) { // if the block in the
														// index is t (teleport
														// block)
						int x = j * 40;
						int width = 40;
						while (j < 59 && map[i][j + 1].equals("t")) {
							width += 40;
							j++;
						}
						blocks.add(new Teleport(x, i * 40, width, 40));
					} else if (map[i][j].equals("r")) { // if the block in the
														// index is r (teleport
														// receiver block)
						int x = j * 40;
						int width = 40;
						while (j < 59 && map[i][j + 1].equals("r")) {
							width += 40;
							j++;
						}
						blocks.add(new Receiver(x, i * 40, width, 40, Integer.parseInt(br[1].readLine())));
					} else if (map[i][j].equals("e")) { // if the block in the
														// index is e (end
														// block)
						int x = j * 40;
						int width = 40;
						while (j < 59 && map[i][j + 1].equals("e")) {
							width += 40;
							j++;
						}
						blocks.add(new End(x, i * 40, width, 40));
					}

				}
			}

			// Connecting the teleporters to the receivers
			int counter = 0;
			for (BlockActions b : LevelState.blocks) {
				if (b.getClass().toString().equals("class bodies.Teleport")) {
					for (BlockActions c : LevelState.blocks) {
						if (c.getClass().toString().equals("class bodies.Receiver")) {
							if (((Receiver) c).getConnect() == counter) {
								((Teleport) b).setSendX(c.getX());
								((Teleport) b).setSendY(c.getY());
							}
						}
					}
					counter++;
				}
			}

			// VERTICAL READING
			for (int j = 0; j < 60; j++) {// reads each column
				for (int i = 0; i < 40; i++) { // reads each index in the column
					if (map[i][j].equals("|")) { // if the block in the index is
													// | (vertical platform)
						int y = i * 40; // sets initial y value to the location
										// in the array * 40 (block height)
						int height = 40; // sets default height to 40
						while (i < 39 && map[i + 1][j].equals("|")) { // if the
																		// block
																		// below
																		// the
																		// current
																		// one
																		// is
																		// the
																		// same
							height += 40; // increases the height by 40
							i++; // increments the index so it can check again
						}
						blocks.add(new Platform(j * 40, y, 40, height, level)); // adds
																				// new
																				// platform
																				// to
																				// the
																				// blocks
					} else if (map[i][j].equals("<")) { // if the block in the
														// index is < (right
														// spike)
						int y = i * 40;
						int height = 40;
						while (i < 39 && map[i + 1][j].equals("<")) {
							height += 40;
							i++;
						}
						blocks.add(new Spike(j * 40 + 20, y, 20, height, "<", level));
					} else if (map[i][j].equals(">")) { // if the block in the
														// index is < (left
														// spike)
						int y = i * 40;
						int height = 40;
						while (i < 39 && map[i + 1][j].equals(">")) {
							height += 40;
							i++;
						}
						blocks.add(new Spike(j * 40, y, 20, height, ">", level));
					}

				}
			}

			// closes the BufferedReaders
			br[0].close();
			br[1].close();
		} catch (IOException e) {
			System.out.println("Error: " + e);
		}
	}

	/**
	 * Updating is split into multiple methods rather than one
	 */
	@Override
	public void update() {
		
	}

	/**
	 * Draws the level 
	 */
	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, 800, 600);
		// draws the background
		if (bg != null) {
			g.drawImage(bg, 0, 0, null);
		}
		if (begin) {
			start = System.currentTimeMillis();
			begin = false;
		}
		g.setColor(Color.BLACK);
		// g.fillRoundRect((int) (0 - translateX), (int) (0 - translateY), 40,
		// 40, 20, 20);
		// g.fillOval((int) (0 - translateX), (int) (0 - translateY), 40, 40);
		// else {
		// updates the player's position, gravities and draws the player
		// updates the player's movement on the screen
		pause = player.getPaused();
		// collision detects

		if (!pause) {
			if (this.level > 9 && this.level < 12) {
				space = true;
			} else {
				space = false;
			}
			player.updateMovement(space);
		} else {
			if (!hasSavedVelocities) {
				player.setVX(0);
				player.setVY(0);
				hasSavedVelocities = true;
			}
		}
		// player.updateMovement();
		end = player.updateCollision();
		if (end == 2 && !exited) {
			exited = true;
			exit();
		} else if (end == 1) {
			begin = true;
		}
		// updates where the camera is
		updateView(g);
		// draws the player

		player.draw(g);

		g.setColor(Color.BLACK);

		for (BlockActions b : blocks) {
			// only shows blocks that are on the screen
			// the if statements below use a block's x, y, w, h to determine
			// if
			// they should be on the screen
			if (b.getX() + b.getW() - translateX >= 0 && b.getX() + translateX <= 880
					&& b.getY() + b.getH() + translateY >= -80 && b.getY() + translateY <= 600) {
				// once a block has been determined to be shown, since
				// blocks
				// aren't static in width and height
				// ex. spikes are sometimes 20x40, images can't be drawn
				// with
				// static numbers, such as i * 40.

				// images need to be drawn wherever the block really IS
				if (b.getH() >= 40) { // if the block is taller than a roof
										// or
										// floor spike (ex. regular
										// platform)
					for (int i = 0; i < b.getH(); i += 40) {
						// NOW these if statements figure out the block's
						// width
						// so the block is drawn at the right coordinates
						if (b.getW() >= 40) {
							for (int j = 0; j < b.getW(); j += 40) {

								g.drawImage(b.getImg(), b.getX() + j, b.getY() + i, null);
							}
						} else { // if the width is not >= 40 (it's a
									// special
									// block)
							for (int j = 0; j < b.getW(); j += 20) {
								if (((Spike) b).getDir().equals(">")) {
									g.drawImage(b.getImg(), b.getX() + j, b.getY() + i, null);
								} else if (((Spike) b).getDir().equals("<")) {
									if (level > 7 && level < 10) {
										g.drawImage(b.getImg(), b.getX() + j - 20, b.getY() + i, null);
									} else {
										g.drawImage(b.getImg(), b.getX() + j, b.getY() + i, null);
									}

								} else {
									g.drawImage(b.getImg(), b.getX() + j, b.getY() + i, null);
								}

							}
						}
					}
				} else { // if the block is not 40px or taller, it's not
							// regular
					// so it needs its own for loop
					for (int i = 0; i < b.getH(); i += 20) {
						if (b.getW() >= 40) {
							for (int j = 0; j < b.getW(); j += 40) {
								if (((Spike) b).getDir().equals("v")) {
									g.drawImage(b.getImg(), b.getX() + j, b.getY() + i, null);
								} else if (((Spike) b).getDir().equals("^")) {
									if (level > 7 && level < 10) {
										g.drawImage(b.getImg(), b.getX() + j, b.getY() + i - 20, null);
									} else {
										g.drawImage(b.getImg(), b.getX() + j, b.getY() + i, null);
									}

								} else {
									g.drawImage(b.getImg(), b.getX() + j, b.getY() + i, null);
								}
							}
						} else {
							for (int j = 0; j < b.getW(); j += 20) {
								g.drawImage(b.getImg(), b.getX() + j, b.getY() + i, null);
							}
						}
					}
				}
			}
		}
		// checks if game is paused and pauses timer and draws pause menu
		if (pause) {
			start = System.currentTimeMillis() - time;
			int newTranslateX = (int) translateX;
			int newTranslateY = (int) translateY;
			g.drawImage(pauseImg, 0 - newTranslateX, 0 - newTranslateY, null);
			if (currentChoice == 0) {
				g.drawImage(mainTwo, 75 - newTranslateX, 370 - newTranslateY, null);
			} else {
				g.drawImage(mainOne, 75 - newTranslateX, 370 - newTranslateY, null);
			}
			if (currentChoice == 1) {
				g.drawImage(levelSTwo, 315 - newTranslateX, 370 - newTranslateY, null);
			} else {
				g.drawImage(levelSOne, 315 - newTranslateX, 370 - newTranslateY, null);
			}
			if (currentChoice == 2) {
				g.drawImage(continueTwo, 560 - newTranslateX, 370 - newTranslateY, null);
			} else {
				g.drawImage(continueOne, 560 - newTranslateX, 370 - newTranslateY, null);
			}

		} else {
			time = System.currentTimeMillis() - start;
		}

		// draws the timer in the pause menu if the game is paused, otherwise it
		// draws over the player's head
		if (!pause) { // game is not paused
			g.setFont(new Font("Monospaced", Font.BOLD, 20));
			if (level < 4) {
				g.setColor(Color.BLACK);
			} else if (level < 8) { // the darker tilesets require a lighter
									// clock text colour
				g.setColor(Color.WHITE);
			} else if (level < 10) {
				g.setColor(Color.BLACK);
			} else if (level < 12) {
				g.setColor(Color.WHITE);
			}
			// figures out which direction gravity is pulling to determine where
			// the clock should go
			if (player.getGravityDir().equals("b")) {
				g.drawString(timerFormat.format(time / 1000.0), (int) player.getX() - 16, (int) player.getY() - 12);
			} else if (player.getGravityDir().equals("t")) {
				g.drawString(timerFormat.format(time / 1000.0), (int) player.getX() - 16,
						(int) player.getY() + player.getH() + 25);
			} else if (player.getGravityDir().equals("l")) {
				g.drawString(timerFormat.format(time / 1000.0), (int) player.getX() + player.getW() + 10,
						(int) player.getY() + player.getH() / 2 + 3);
			} else if (player.getGravityDir().equals("r")) {
				g.drawString(timerFormat.format(time / 1000.0), (int) player.getX() - 82,
						(int) player.getY() + player.getH() / 2 + 3);
			}
		} else { // game is paused
			g.setColor(Color.BLACK);
			g.setFont(new Font("Monospaced", Font.BOLD, 50));
			g.drawString(timerFormat.format(time / 1000.0), 145 - (int) translateX, 157 - (int) translateY);
		}
		// if you're on the tutorial level, helpful text appears on the walls
		if (level == -1) {
			g.setFont(new Font("Monospaced", Font.PLAIN, 20));

			g.drawString("Use <- and -> to move left and right", 150, 1300);
			g.drawString("^ to jump", 1000, 1300);
			g.drawString("Esc to pause", 1800, 1300);
			g.drawString("These blocks", 2100, 1200);
			g.drawString("are bouncy!", 2100, 1220);
			g.drawString("Some jumps are too large", 1800, 980);
			g.drawString("Change gravity with", 1350, 980);
			g.drawString("'W' and 'S', but you can only", 1350, 1000);
			g.drawString("change gravity when grounded", 1350, 1020);
			g.drawString("In water, you can't use the arrow", 150, 960);
			g.drawString("keys to move, but you can change", 150, 980);
			g.drawString("gravity anytime!", 150, 1000);
			g.drawString("Some things can kill you, so watch out!", 400, 700);
			g.drawString("You can change horizontal gravity", 1330, 480);
			g.drawString("with 'A' and 'D'", 1330, 500);
			g.drawString("Note the control change when you're", 300, 420);
			g.drawString("not using normal gravity.", 300, 440);
			g.drawString("You can only change gravity when you're", 150, 170);
			g.drawString("touching the wall that acts as the 'floor'", 150, 190);
			g.drawString("The timer indicates which side of", 800, 210);
			g.drawString("the player is OPPOSITE to a floor", 800, 230);
			g.drawString("Also... Teleporters!", 1300, 200);
			g.drawString("Goal ->", 2150, 145);
		}

	}

	/**
	 * Translates the camera based on where the player is, or the edges of the
	 * map
	 * 
	 * @param g,
	 *            the graphic element for using the .translate() method.
	 */
	public void updateView(Graphics g) {
		if (player.getX() > 2000) { // if the player gets past the visible right
									// region of the map
			translateX = -1600; // fix the translate value so it doens't show
								// white space
		} else if (player.getX() > 400) { // if the player gets past the left
											// visible region of the map
			translateX = 400 - player.getX(); // make translate variable adjust
												// with player movement
		} else {
			translateX = 0; // otherwise translate value is fixed on the left
		}
		if (player.getY() > 1240) { // if the player is at the bottom of the map
			translateY = -1000; // fix the translate value so map doesn't bounce
								// when jumping
		} else if (player.getY() < 240) { // if the player is above the top
											// visible region
			translateY = 0; // fix he translate value so it doesn't show white
							// space at the top
		} else {
			translateY = 240 - player.getY(); // otherwise translate variable
												// adjusts with player movement
		}

		player.setX(player.getX() + player.getVx()); // adds player horizontal
														// velocity to the
														// player's x position
		player.setY(player.getY() + player.getVy()); // adds player vertical
														// velocity to the
														// player's y position

		g.translate((int) translateX, (int) translateY); // translates the
															// screen with the
															// translate
															// variables
	}

	/**
	 * Mutator method to change the translateX variable
	 * 
	 * @param tX
	 */
	public static void setTranslateX(double tX) {
		translateX = tX;
	}

	/**
	 * Mutator method to change the translateY variable
	 * 
	 * @param tY
	 */
	public static void setTranslateY(double tY) {
		translateY = tY;
	}

	/**
	 * When the user finishes a level, an "end of level" screen shows with high
	 * time scores
	 */
	public void exit() {
		if (level != -1) { // if it's not the tutorial, shows the end of level
							// screen
			GameStateManager.states.push(new LevelEndState(gsm, level, time));
			GameStateManager.states.peek().init();
		} else { // goes back to the main menu
			GameStateManager.states.push(new MenuState(gsm));
		}

	}

	/**
	 * Called if the user selects something on the pause menu
	 */
	private void select() {
		if (currentChoice == 0) { // user wants to go back to main menu
			GameStateManager.states.push(new MenuState(gsm));
		}
		if (currentChoice == 1) { // user wants to go back to level selection
			GameStateManager.states.push(new LevelSelectState(gsm));
		}
		if (currentChoice == 2) { // user wants to keep playing the current
									// level (unpause)
			player.keyPressed(KeyEvent.VK_ESCAPE);
			pause = false;
		}
	}

	/**
	 * When a key is pressed
	 * @param k the keycode
	 */
	@Override
	public void keyPressed(int k) {
		if (!pause) {
			player.keyPressed(k);
		} else {
			if (k == KeyEvent.VK_ENTER) {
				select();
			}

			if (k == KeyEvent.VK_RIGHT) {
				currentChoice++;
				if (currentChoice == options.length) {
					currentChoice = 0;
				}
			}
			if (k == KeyEvent.VK_LEFT) {
				currentChoice--;
				if (currentChoice == -1) {
					currentChoice = options.length - 1;
				}
			}
			if (k == KeyEvent.VK_ESCAPE) {
				currentChoice = 2;
				// player.keyPressed(k);
				// player.getKeys()[9] = false;
				select();
			}
			if (k == KeyEvent.VK_ALT) { // cheat code to beat a level (use
										// during the pause screen)
				exited = true;
				exit();
			}
		}
	}

	/**
	 * When a key is released
	 * @param k the keycode 
	 */
	@Override
	public void keyReleased(int k) {
		player.keyReleased(k);
	}

	/**
	 * When a key is typed, unused because nothing gets typed
	 */
	@Override
	public void keyTyped(int k) {
	}

	/**
	 * returns whether or not the velocity has been saved
	 * 
	 * @param none
	 * @return boolean
	 */
	public boolean getVelocitiesSaved() {
		if (hasSavedVelocities) {
			return true;
		}
		return false;
	}

	/**
	 * sets whether or not to save the velocity
	 * 
	 * @param b
	 * @return void
	 */
	public static void setVelocitiesSaved(boolean b) {
		hasSavedVelocities = b;
	}

	/**
	 * Returns whether or not game is paused
	 * 
	 * @param none
	 * @return pause
	 */
	public static boolean getPause() {
		return pause;
	}

	/**
	 * Returns all the blocks of the map as a BlockActions ArrayList
	 * 
	 * @param none
	 * @return blocks
	 */
	public static ArrayList<BlockActions> getBlocks() {
		return blocks;
	}
}
