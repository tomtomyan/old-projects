/*
 * The menu state, used for the home screen
 * 
 */
package gamestate;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import main.GamePanel;

public class MenuState extends GameState {
	
	//the images for the buttons and the background
	private BufferedImage bg;
	private BufferedImage play1;
	private BufferedImage play2;
	private BufferedImage tips1;
	private BufferedImage tips2;
	private BufferedImage credits1;
	private BufferedImage credits2;
	private BufferedImage quit1;
	private BufferedImage quit2;

	//the options
	private int currentChoice = 0;
	private String[] options = { "Play", "Tips", "Credits", "Quit" };

	/**
	 * Creates a menu state with a game state manager
	 * @param gsm the game state manager
	 */
	public MenuState(GameStateManager gsm) {
		this.gsm = gsm;

		try {
			//loads the images for the buttons and the background
			bg = ImageIO.read(getClass().getResourceAsStream("/images/gameStateImages/MenuBackground.png"));

			play1 = ImageIO.read(getClass().getResourceAsStream("/images/gameStateImages/playButton1.png"));
			play2 = ImageIO.read(getClass().getResourceAsStream("/images/gameStateImages/playButton2.png"));

			tips1 = ImageIO.read(getClass().getResourceAsStream("/images/gameStateImages/tipsButton1.png"));
			tips2 = ImageIO.read(getClass().getResourceAsStream("/images/gameStateImages/tipsButton2.png"));

			credits1 = ImageIO.read(getClass().getResourceAsStream("/images/gameStateImages/creditsButton1.png"));
			credits2 = ImageIO.read(getClass().getResourceAsStream("/images/gameStateImages/creditsButton2.png"));
			
			quit1 = ImageIO.read(getClass().getResourceAsStream("/images/gameStateImages/quitButton1.png"));
			quit2 = ImageIO.read(getClass().getResourceAsStream("/images/gameStateImages/quitButton2.png"));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void init() {

	}

	@Override
	public void update() {

	}

	/**
	 * Draws the menu state 
	 */
	@Override
	public void draw(Graphics2D g) {
		g.drawImage(bg, 0, 0, null);
		//based on the current choice, a different button is highlighted
		if (currentChoice == 0) {
			g.drawImage(play2, 565, 180, null);
		} else {
			g.drawImage(play1, 565, 180, null);
		}
		if (currentChoice == 1) {
			g.drawImage(tips2, 565, 280, null);
		} else {
			g.drawImage(tips1, 565, 280, null);
		}
		if (currentChoice == 2) {
			g.drawImage(credits2, 565, 380, null);
		} else {
			g.drawImage(credits1, 565, 380, null);
		}
		if (currentChoice == 3) {
			g.drawImage(quit2, 565, 480, null);
		} else {
			g.drawImage(quit1, 565, 480, null);
		}

	}
	
	/**
	 * This checks what the users selection is on the main menu
	 * 
	 * @param none
	 * @return void
	 */
	private void select() {
		if (currentChoice == 0) {
			// go to level selector
			GameStateManager.states.push(new LevelSelectState(gsm));
		}
		if (currentChoice == 1) {
			// tips
			//tutorial state is designated -1 (the levels are from 0 onward)
			GameStateManager.states.push(new LevelState(gsm, -1));
		}
		if (currentChoice == 2) {
			// credits
			GameStateManager.states.push(new CreditsState(gsm));
		}
		if(currentChoice == 3) { //closes the application
			System.exit(0);
		}
	}

	/**
	 * When a key is pressed, this method is called
	 * @param k the keycode
	 */
	@Override
	public void keyPressed(int k) {
		if (k == KeyEvent.VK_ENTER) {
			select();
		}
		if (k == KeyEvent.VK_DOWN) {
			currentChoice++;
			if (currentChoice == options.length) {
				currentChoice = 0;
			}
		}
		if (k == KeyEvent.VK_UP) {
			currentChoice--;
			if (currentChoice == -1) {
				currentChoice = options.length - 1;
			}
		}
	}

	/**
	 * When a key is released, unused because key releases on the main menu aren't important
	 * @param k the keycode
	 */
	@Override
	public void keyReleased(int k) {
	}

	/**
	 * When a key is typed, unused because key types on the main menu aren't important
	 */
	@Override
	public void keyTyped(int k) {	
	}

}
