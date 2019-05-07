/*
 * Sam Macpherson, Tom Yan, Aseem Malhotra
 * Jan 20, 2016
 * Manages the states of the game (menu, level selection, credits etc.)
 */
package gamestate;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Stack;

public class GameStateManager {
	
	//the states game states stack that has all the states in it
	public static Stack<GameState> states;
	
	/**
	 * Creates a game state manager that has a stack of game states
	 */
	public GameStateManager() {
		states = new Stack<GameState>();
		states.push(new MenuState(this));
	}
	
	/**
	 * All game states (menu, credits etc) have a draw method and the game state manger calls whichever state is on the 
	 * top of the stack
	 * @param g the graphical element used for drawing
	 */
	public void draw(Graphics g) {
		states.peek().draw((Graphics2D)g);
	}
	
	/**
	 * A key is pressed, the GSM calls whichever state is at the top of the stack with the key that was pressed
	 * @param k the integer value for the keycode
	 */
	public void keyPressed(int k) {
		states.peek().keyPressed(k);
	}
	
	/**
	 * When a key is released, the GSM calls the top of the stack key released function
	 * @param k the keycode
	 */
	public void keyReleased(int k) {
		states.peek().keyReleased(k);
	}
	/**
	 * Not used, but included because the parent class implements keylistener
	 * @param k the key code
	 */
	public void keyTyped(int k) {
		states.peek().keyTyped(k);
	}
}
