/*
 * Sam Macpherson, Tom Yan, Aseem Malhotra
 * Jan 20, 2016
 * A game state that shows the end of a level screen
 */
package gamestate;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import java.io.*;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class LevelEndState extends GameState {

	//the list of high scores, read from a data file
	private ArrayList<String> highScores;

	//the images for the buttons and the background
	private BufferedImage bg;

	private BufferedImage mainOne;
	private BufferedImage mainTwo;

	private BufferedImage levelSOne;
	private BufferedImage levelSTwo;

	private BufferedImage nextLOne;
	private BufferedImage nextLTwo;

	private BufferedImage highScoreSlot;

	private boolean scoresHaveBeenRead = false;

	//these variables are for the choices 
	private int currentChoice = 2;
	private String[] options = { "Main", "Level Select", "Next Level" };
	private String[][] scores;
	
	private int level;
	private double time;

	/**
	 * Creates a level end state with the defined attributes
	 * @param gsm the game state manager
	 * @param level the level that the state is ended (used for high scores) 
	 * @param time the time that the player took
	 */
	public LevelEndState(GameStateManager gsm, int level, double time) {
		// defines the high scores array list
		highScores = new ArrayList<>();
		// asks the user for their display name
		// String name = JOptionPane.showInputDialog("What do you want your high
		// score name to be?");

		this.gsm = gsm;
		this.level = level;
		this.time = time;
		this.scoresHaveBeenRead = false;

		// loads images required for level end screen
		try {

			bg = ImageIO.read(getClass().getResourceAsStream("/images/gameStateImages/LevelEndBackground.png"));

			mainOne = ImageIO.read(getClass().getResourceAsStream("/images/gameStateImages/main1.png"));
			mainTwo = ImageIO.read(getClass().getResourceAsStream("/images/gameStateImages/main2.png"));

			levelSOne = ImageIO.read(getClass().getResourceAsStream("/images/gameStateImages/levelSelect1.png"));
			levelSTwo = ImageIO.read(getClass().getResourceAsStream("/images/gameStateImages/levelSelect2.png"));

			nextLOne = ImageIO.read(getClass().getResourceAsStream("/images/gameStateImages/nextLevelButton1.png"));
			nextLTwo = ImageIO.read(getClass().getResourceAsStream("/images/gameStateImages/nextLevelButton2.png"));

			highScoreSlot = ImageIO.read(getClass().getResourceAsStream("/images/gameStateImages/highScoreSlot.png"));

		} catch (Exception e) {
			e.printStackTrace();
		}
		// init();
	}

	/**
	 * Gets the player's name so that it can be written to the data file
	 */
	@Override
	public void init() {
		// gets the user's name
		String name = JOptionPane.showInputDialog("Enter your display name");
		if (name == null) {
			System.out.println("NAME NULL");
			System.exit(0);
		}
		// opens a writing stream to add the user to the data file of players
		try (BufferedWriter out = new BufferedWriter(
				new FileWriter("src\\highScores\\level" + (level + 1) + ".sco", true))) {
			// writes high score to the file
			out.write(name);
			out.newLine();
			out.write("" + LevelState.timerFormat.format(time / 1000.0));
			out.newLine();

		} catch (IOException e2) {
			System.out.println("Error with writing to high scores file.");
			e2.printStackTrace();
		}

		int numEntries = 0;
		// then reads through the file that was just written,
		// turning it into an array to be sorted
		try {
			BufferedReader in = new BufferedReader(new FileReader("src\\highScores\\level" + (level + 1) + ".sco"));
			boolean eof = false;
			while (!eof) {
				// reads two lines at a time
				// reads through the data file once so that it knows how big to
				// make
				// the scores array
				String input = in.readLine();
				if (input != null && input != "") {
					in.readLine();
					numEntries++;
				} else {
					eof = true;
					in.close();
				}
			}

			scores = new String[numEntries][2];
			// reads the file again to populate the array
			BufferedReader in2 = new BufferedReader(new FileReader("src\\highScores\\level" + (level + 1) + ".sco"));
			for (int i = 0; i < numEntries; i++) {
				String nameFromFile = in2.readLine();
				String personalTime = in2.readLine();
				scores[i][0] = nameFromFile;
				scores[i][1] = personalTime;
			}
			// sorts the scores array
			quikSort(scores, 0, scores.length - 1);
			scoresHaveBeenRead = true;

		} catch (IOException e) {
			System.out.println("Error reading the file for high scores.");
			e.printStackTrace();
		}
	}

	/**
	 * The fastest sorting algorithm, splits an array up and sorts in smaller
	 * sections Uses recursion, so the loop only operates on a certain section
	 * of the array at once
	 * 
	 * @param a
	 *            the array to be sorted
	 * @param lo
	 *            the beginning of the range that the algorithm is sorting
	 *            currently
	 * @param hi
	 *            the end of the range that the algorithm is sorting currently
	 */
	public void quikSort(String[][] a, int lo, int hi) {
		if (lo < hi) {
			int p = partition(a, lo, hi);

			quikSort(a, lo, p - 1);
			quikSort(a, p + 1, hi);
		}
	}

	/**
	 * Puts one item in the list (in this case, the last one) in the correct
	 * sorted position and then quik sort handles the rest of the array by
	 * splitting it up, this method sets the list up as (<n)(n)(>n) or vice
	 * versa
	 * 
	 * @param a
	 *            the array to be organized into a semi-ordered list
	 * @param start
	 *            the beginning of the range that 'partition' is looking at
	 * @param end
	 *            the end of the range that 'partition' is looking at
	 * @return the index of where the sorted item is, so quik sort can start
	 *         recursing with smaller lists
	 */
	public int partition(String[][] a, int start, int end) {
		double pivot = Double.parseDouble(a[end][1]);
		int p = start;
		for (int i = start; i < end; i++) {
			// sorts them in ascending order (lowest times are best)
			if (Double.parseDouble(a[i][1]) <= pivot) {
				swap(a, i, p);
				p++;
			}
		}
		swap(a, p, end);
		return p;
	}

	/**
	 * Swaps 2 numbers in an array; used for keeping high scores sorted
	 * 
	 * @param a
	 *            the array that the numbers are being swapped in
	 * @param in1
	 *            the index of the first number
	 * @param in2
	 *            the index of the second number
	 */
	public void swap(String[][] a, int in1, int in2) {
		String[][] tmp = new String[1][2];

		tmp[0][0] = a[in1][0];
		tmp[0][1] = a[in1][1];

		a[in1][0] = a[in2][0];
		a[in1][1] = a[in2][1];

		a[in2][0] = tmp[0][0];
		a[in2][1] = tmp[0][1];
	}

	@Override
	public void update() {

	}

	@Override
	public void draw(Graphics2D g) {
		g.drawImage(bg, 0, 0, null);

		if (currentChoice == 0) {
			g.drawImage(mainTwo, 70, 485, null);
		} else {
			g.drawImage(mainOne, 70, 485, null);
		}
		if (currentChoice == 1) {
			g.drawImage(levelSTwo, 315, 485, null);
		} else {
			g.drawImage(levelSOne, 315, 485, null);
		}
		if (currentChoice == 2) {
			g.drawImage(nextLTwo, 560, 485, null);
		} else {
			g.drawImage(nextLOne, 560, 485, null);
		}

		// prints out the sorted array of scores
		if (scoresHaveBeenRead) {
			// System.out.println("score length: " + scores.length);
			g.setColor(Color.BLACK);
			g.setFont(new Font("Monospaced", Font.BOLD, 45));
			for (int i = 0; i < scores.length && i < 5; i++) {
				g.drawImage(highScoreSlot, 50, 150 + 60 * i, null);
				g.setFont(new Font("Monospaced", Font.PLAIN, 30));
				g.drawString("" + (i + 1), 53, 175 + 60 * i);
				g.setFont(new Font("Monospaced", Font.BOLD, 45));
				g.drawString(scores[i][0], 85, 185 + 60 * i);
				// System.out.println(scores[i][0] + " " + scores[i][1]);
			}
			for (int i = 0; i < scores.length && i < 5; i++) {
				g.drawString(scores[i][1], 585, 188 + 60 * i);
			}
		}
	}

	/**
	 * This select() method layout is temporary. Eventually since we'll have
	 * credits and help menus it'll be more complicated
	 */
	private void select() {
		if (currentChoice == 0) {
			// Main Menu
			GameStateManager.states.push(new MenuState(gsm));
		}
		if (currentChoice == 1) {
			// Level Select
			GameStateManager.states.push(new LevelSelectState(gsm));
		}
		if (currentChoice == 2) {
			// Next Level
			level++;
			GameStateManager.states.push(new LevelState(gsm, level));
		}
	}

	/**
	 * When  a key is pressed this method is called
	 * @param k the key code 
	 */
	@Override
	public void keyPressed(int k) {
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
	}

	/**
	 * Releasing a key on the level end state doens't do anything
	 * @param k the keycode
	 */
	@Override
	public void keyReleased(int k) {
	}

	/**
	 * Typing a key, unused in the level end state
	 * @param k the keycode
	 */
	@Override
	public void keyTyped(int k) {
	}

}
