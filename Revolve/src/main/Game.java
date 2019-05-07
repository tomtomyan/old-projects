/*
 * Sam Macpherson Aseem Malhotra Tom Yan
 * Jan 20, 2016
 * The class that loads the JFrame and makes the panel for the game to be displayed on
 */
package main;

import javax.swing.JFrame;

public class Game {

	public static void main(String[] args) { 
		//makes the jframe
		JFrame window = new JFrame("Revolve, The Game");
		//sets the features of the frame
		window.setContentPane(new GamePanel()); //adds the game panel (the actual game) to the frame
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.pack();
		window.setVisible(true);
		window.setFocusTraversalKeysEnabled(true);
		window.setLocationRelativeTo(null); //centers the frame on the screen
	}
	
	
}
