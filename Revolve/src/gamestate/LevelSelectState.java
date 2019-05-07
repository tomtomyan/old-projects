/*
 * Sam Macpherson, Tom Yan, Aseem Malhotra
 * Jan 20, 2016
 * A game state that shows the level selection screen
 */
package gamestate;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class LevelSelectState extends GameState {

	//the background for the level select screen
	private BufferedImage bg;

	//the 4 arrays used for the sections of levels (the different worlds)
	private BufferedImage[][] levelOne = new BufferedImage[4][2];
	private BufferedImage[][] levelTwo = new BufferedImage[4][2];
	private BufferedImage[][] levelThree = new BufferedImage[2][2];
	private BufferedImage[][] levelFour = new BufferedImage[2][2];

	private int currentChoice = 0;
	private String[] options = { "Level 1", "Level 2", "Level 3", "Level 4", "Level 5", "Level 6", "Level 7", "Level 8",
			"Level 9", "Level 10", "Level 11", "Level 12", "Level 13", "Level 14", "Level 15", "Level 16" };

	//the level the user chooses
	private int level;

	/**
	 * Creates a level selection screen
	 * @param gsm the game state manager 
	 */
	public LevelSelectState(GameStateManager gsm) {
		this.gsm = gsm;
		// loads all images required for level selection menu
		try {
			bg = ImageIO.read(getClass().getResourceAsStream("/images/gameStateImages/LevelSelectBackground.png"));
			for (int i = 0; i < 4; i++) {
				levelOne[i][0] = ImageIO.read(
						getClass().getResourceAsStream("/images/gameStateImages/level" + (i + 1) + "Button1.png"));
				levelOne[i][1] = ImageIO.read(
						getClass().getResourceAsStream("/images/gameStateImages/level" + (i + 1) + "Button2.png"));

				levelTwo[i][0] = ImageIO.read(
						getClass().getResourceAsStream("/images/gameStateImages/level" + (i + 5) + "Button1.png"));
				levelTwo[i][1] = ImageIO.read(
						getClass().getResourceAsStream("/images/gameStateImages/level" + (i + 5) + "Button2.png"));

			}
			for (int i = 0; i < 2; i++) {
				levelThree[i][0] = ImageIO.read(
						getClass().getResourceAsStream("/images/gameStateImages/level" + (i + 9) + "Button1.png"));
				levelThree[i][1] = ImageIO.read(
						getClass().getResourceAsStream("/images/gameStateImages/level" + (i + 9) + "Button2.png"));

				levelFour[i][0] = ImageIO.read(
						getClass().getResourceAsStream("/images/gameStateImages/level" + (i + 11) + "Button1.png"));
				levelFour[i][1] = ImageIO.read(
						getClass().getResourceAsStream("/images/gameStateImages/level" + (i + 11) + "Button2.png"));

			}

		} catch (Exception e) {
			System.out.println("error with loading level buttons");
		}
	}

	/**
	 * Draws the level selection state window based on what level is being hovered
	 * @param g the graphic element used for drawing 
	 */
	@Override
	public void draw(Graphics2D g) {
		g.drawImage(bg, 0, 0, null);
		g.setFont(new Font("Monospaced", Font.BOLD, 20));
		// draws the button images for level selection
		// and a red square around the currently selected one
		g.setColor(Color.RED);
		for (int i = 0; i < 4; i++) {
			if (currentChoice == i) {
				g.drawImage(levelOne[i][1], 130 + 60 * i, 180, null);
				g.drawRect(130 + 60 * i, 180, 56, 56);
			} else {
				g.drawImage(levelOne[i][0], 130 + 60 * i, 180, null);
			}
		}
		for (int i = 4; i < 8; i++) {
			if (currentChoice == i) {
				g.drawImage(levelTwo[i - 4][1], 200 + 60 * i, 130, null);
				g.drawRect(200 + 60 * i, 130, 56, 56);
			} else {
				g.drawImage(levelTwo[i - 4][0], 200 + 60 * i, 130, null);
			}
		}
		for (int i = 8; i < 10; i++) {
			if (currentChoice == i) {
				g.drawImage(levelThree[i - 8][1], 270 + 60 * (i - 8), 450, null);
				g.drawRect(270 + 60 * (i - 8), 450, 56, 56);
			} else {
				g.drawImage(levelThree[i - 8][0], 270 + 60 * (i - 8), 450, null);
			}
		}
		for (int i = 10; i < 12; i++) {
			if (currentChoice == i) {
				g.drawImage(levelFour[i - 10][1], 510 + 60 * (i - 10), 380, null);
				g.drawRect(510 + 60 * (i - 10), 380, 56, 56);
			} else {
				g.drawImage(levelFour[i - 10][0], 510 + 60 * (i - 10), 380, null);

			}
		}
		g.setColor(Color.BLACK);
		g.drawString("'Esc' to go back to Main Menu", 50, 580);
	}

	/**
	 * Chooses a button on the main menu currentChoice = 0 //for play the game
	 * currentChoice = -1 //for the tips menu currentChoice = -2 //for the
	 * credits
	 */
	private void select() {
		GameStateManager.states.push(new LevelState(gsm, currentChoice));
	}

	/**
	 * When a key is pressed, the selected level changes
	 * @param k the key code for the pressed key
	 */
	@Override
	public void keyPressed(int k) {
		if (k == KeyEvent.VK_ENTER) {
			select();
		}
		if (k == KeyEvent.VK_RIGHT) {
			currentChoice++;
			if (currentChoice == options.length - 4) {
				currentChoice = 0;
			}
		}
		if (k == KeyEvent.VK_LEFT) {
			currentChoice--;
			if (currentChoice == -1) {
				currentChoice = options.length - 5;
			}
		}
		if (k == KeyEvent.VK_ESCAPE) {
			GameStateManager.states.push(new MenuState(gsm));
		}
	}

	/**
	 * When a key is released, unused because key releases don't matter on the level selection state
	 * @param k the keycode
	 */
	@Override
	public void keyReleased(int k) {
	}
	
	/**
	 * When a key is typed, unused because key releases don't matter on the level selection state
	 * @param k the keycode
	 */
	@Override
	public void keyTyped(int k) {
	}

	/**
	 * The init method, unused because the level selection screen is just drawing and constructors
	 */
	@Override
	public void init() {
	}

	/**
	 * There are no updating calculations to be made when on the level selection state
	 */
	@Override
	public void update() {
	}

}
