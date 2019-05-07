/*
 * Sam Macpherson, Aseem Malhotra, Tom Yan
 * Jan 20, 2016
 * The game panel that has the actual game
 */
package main;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;

import gamestate.GameStateManager;
import gamestate.LevelState;

public class GamePanel extends JPanel implements Runnable, KeyListener {
	private static final long serialVersionUID = 1L;

	private Thread thread;
	private boolean isRunning = false, pause = false;
	private int fps = 60;
	private long targetTime = 1000 / fps;

	private GameStateManager gsm;

	public GamePanel() {
		setPreferredSize(new Dimension(800, 600));

		addKeyListener(this);
		setFocusable(true);

		gsm = new GameStateManager();

		start();
	}

	private void start() {
		isRunning = true;
		thread = new Thread(this);
		thread.start();
	}

	/**
	 * The game loop
	 */
	public void run() {
		long start, elapsed, wait;

		while (isRunning) {
			//the start and elapsed and wait timers
			//keep track of time to keep the fps constant
			start = System.nanoTime();
			
			repaint();

			elapsed = System.nanoTime() - start;
			wait = targetTime - elapsed / 1000000;

			if (wait <= 0) {
				wait = 5;
			}
			
			try {
				Thread.sleep(wait); //sleeps so the fps stays constant
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Draws whatever is on the gsm
	 */
	public void paintComponent(Graphics g) {
		if (!LevelState.getPause()) {
			super.paintComponent(g);
		}
		gsm.draw((Graphics2D) g);
	}

	/**
	 * When a key is pressed
	 * @param e the keyevent is translated into an int here, and passed down the line of game states
	 */
	public void keyPressed(KeyEvent e) {
		gsm.keyPressed(e.getKeyCode());
	}

	/**
	 * When a key is released
	 * @param e the keyevent is translated into an int here, and passed down the line of game states
	 */
	public void keyReleased(KeyEvent e) {
		gsm.keyReleased(e.getKeyCode());
	}
	
	/**
	 * When a key is typed
	 * @param e the keyevent is translated into an int here, and passed down the line of game states
	 */
	public void keyTyped(KeyEvent e) {
		gsm.keyTyped(e.getKeyCode());
	}
}
