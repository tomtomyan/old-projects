/*
 * Sam Macpherson, Tom Yan, Aseem Malhotra
 * Jan 20, 2016
 * A game state that shows the credits screen
 */
package gamestate;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class CreditsState extends GameState {

	// the fonts used for the displayed text
	private Font textFont;
	private Font headerFont;

	// the background image
	private BufferedImage bg;

	/**
	 * Creates a credits page with no gamestate manager
	 */
	public CreditsState() {
		textFont = new Font("Monospaced", Font.PLAIN, 16);
		headerFont = new Font("Monospaced", Font.BOLD, 20);
		try {
			bg = ImageIO.read(CreditsState.class.getResourceAsStream("/images/gameStateImages/creditsBackground.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Creates a credits page with all the defined properties
	 * 
	 * @param gsm
	 */
	public CreditsState(GameStateManager gsm) {
		this();
		this.gsm = gsm;
	}

	/**
	 * Unused because there isn't any init to do on the credits screen
	 */
	@Override
	public void init() {
	}

	/**
	 * Unused because there is no updating to do on the credits screen
	 */
	@Override
	public void update() {
	}

	/**
	 * Draws the credits screen
	 */
	@Override
	public void draw(Graphics2D g) {
		g.drawImage(bg, 0, 0, null);

		//draws all the text about the developers
		g.setFont(headerFont);
		g.drawString("Tom 'Don't tell me what to do' Yan", 40, 80);
		g.setFont(textFont);
		g.drawString("The man, the myth, the legend. When Yan enters the building, heads turn.", 40, 110);
		g.drawString("Programmer by day, programmer by night, boasting of a triple digit IQ,", 40, 130);
		g.drawString("he gets the job done, no matter the time constraints. Adamant,", 40, 150);
		g.drawString("ambitious, and majestic are just some of the few adjectives mortals use", 40, 170);
		g.drawString("to describe this legend. To have gazed upon his heavenly visage, is a", 40, 190);
		g.drawString("great honour.", 40, 210);

		g.setFont(headerFont);
		g.drawString("Sam 'Stays-up-way-too-late-to-finish-programming' Macpherson", 40, 250);
		g.setFont(textFont);
		g.drawString("The driving force, the one who delegates, the master of his trade,", 40, 280);
		g.drawString("with inifite bragging rights over his Math contest participation", 40, 300);
		g.drawString("certificate, he doesn't mess around. The comic relief of the group", 40, 320);
		g.drawString("whose jokes aren't funny. He gets down to business at the last minute,", 40, 340);
		g.drawString("and pulls nothing but the A+.", 40, 360);

		g.setFont(headerFont);
		g.drawString("Aseem 'Not-even-my-top-6' Malhotra", 40, 400);
		g.setFont(textFont);
		g.drawString("He likes to work with outdated project versions because he doesn't even", 40, 430);
		g.drawString("care a litte. He motivates himself, using nothing but his muscular arms", 40, 450);
		g.drawString("and his sheer intellect when slaying game bugs. He analyzes the systems", 40, 470);
		g.drawString("like he was born to do nothing else, and when asked for a time estimate", 40, 490);
		g.drawString("he always flatters himself, saying, 'it will take no more than 10", 40, 510);
		g.drawString("minutes. Few can challenge his calm, calculating exterior and he is", 40, 530);
		g.drawString("a force to be reckoned with.", 40, 550);
		
		g.setFont(new Font("Monospaced", Font.PLAIN, 15));
		g.drawString("'Esc' to go back to Main Menu", 445, 570);
	}

	/**
	 * Checks for if the user is trying to go back to the menu
	 */
	@Override
	public void keyPressed(int k) {
		if(k == KeyEvent.VK_ESCAPE) {
			GameStateManager.states.push(new MenuState(gsm));
		}
	}

	/**
	 * Unused because key released isn't important
	 */
	@Override
	public void keyReleased(int k) {
	}

	/**
	 * Unused because key typing isn't important
	 */
	@Override
	public void keyTyped(int k) {
	}

}
