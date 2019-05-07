/*
 * Sam Macpherson, Tom Yan, Aseem Malhotra
 * Jan 20, 2016
 * The gamestate class that all the game states inherit from (menu, credits etc)
 */
package gamestate;

public abstract class GameState {
	
	//every game state has a game state manager
	protected GameStateManager gsm;
	
	/**
	 * The initialize method for making a game state
	 */
	public abstract void init();
	/**
	 * The update method for a game state, performs calculations
	 */
	public abstract void update();
	/**
	 * Draws a game state
	 * @param g
	 */
	public abstract void draw(java.awt.Graphics2D g);
	/**
	 * When a key is pressed in a game state, this method is called
	 * @param k the key code
	 */
	public abstract void keyPressed(int k);
	/**
	 * When a key is released in a game state, this method is called
	 * @param k the key code
	 */
	public abstract void keyReleased(int k);
	/**
	 * The keytyped method, unused, but must be included because keylisteners
	 * @param k
	 */
	public abstract void keyTyped(int k);
	
}
