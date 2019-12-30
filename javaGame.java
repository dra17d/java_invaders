// javaGame.java
// Dominick Aiudi - dra17d
// Florida State University - Summer 2019

import javax.swing.JFrame;

public class javaGame
{
	public static void main(String[] args)
	{
		SpaceInvaders game = new SpaceInvaders();
		game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.setSize(500, 500); // set frame size
		game.setVisible(true); // display frame
	}
}