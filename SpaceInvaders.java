// SpaceInvaders.java
// Dominick Aiudi - dra17d
// Florida State University - Summer 2019

import java.awt.Font;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class SpaceInvaders extends JFrame
{
	private Screen gameScreen;
	private GameObject enemies[];
	private GameObject player;
	private Projectile bullets[];

	private int direction = 1; // 1 = Left; 2 = Down; 3 = Right
	private int incrementer = 0; // Used to count # of enemy movements
	private boolean state = false, over = false, left = true; // state: false = menu; true = game
	private JPanel menuText = new JPanel(new BorderLayout());
	private JLabel labelOver;

	public SpaceInvaders()
	{
		super("Java Invaders");

		// Make Game Screen
		gameScreen = new Screen();
		add(gameScreen);
		gameScreen.requestFocus();

		// Text for menu subpanel
		JLabel text = new JLabel("Java Invaders", SwingConstants.CENTER);
		text.setForeground(Color.WHITE);
		text.setFont(new Font(Font.DIALOG, Font.PLAIN, 25));

		// Found html formatting here:
		// https://stackoverflow.com/questions/10478420/jlabel-doesnt-show-a-long-paragraph
		JLabel controls = new JLabel("<html><div style='text-align: center;'>" + 
			"Left & Right Arrow Keys to move<br>" +
			"SPACE to shoot<br>" +
			"ENTER to start and ESC for this menu<br>" +
			"</div></html>",
			SwingConstants.CENTER);
		controls.setForeground(Color.WHITE);
		controls.setFont(new Font(Font.DIALOG, Font.PLAIN, 15));

		// Menu subpanel
		menuText.add(text, BorderLayout.CENTER);
		menuText.add(controls, BorderLayout.SOUTH);
		menuText.setBackground(Color.BLACK);

		stopGame();
	}

	// Initiallizes values for the start of the game
	public void startGame()
	{
		gameScreen.remove(menuText);

		// Instantiate enemy array
		enemies = new GameObject[30];
		for (int i = 0; i < 3; ++i)
			for (int j = 0; j < 10; ++j)
				enemies[(i * 10) + j] = new GameObject((40 * j) + 80, (40 * i) + 40, 20, 20);

		// Instantiate player
		player = new GameObject(200, 400, 30, 30);

		// Instantiate bullet array
		bullets = new Projectile[10];
	}

	// A.k.a. Menu/Title Screen
	public void stopGame()
	{
		if (over)
		{
			gameScreen.removeAll();
			over = false;
		}

		gameScreen.add(menuText, BorderLayout.CENTER);

		// Remove player and enemy & bullet arrays
		enemies = new GameObject[0];
		player = null;
		bullets = null;

		// Reset incrementer and direction
		incrementer = 0;
		direction = 1;
	}

	//////////////////
	// Screen Class //
	//////////////////
	class Screen extends JPanel implements KeyListener
	{
		Screen()
		{
			super(new BorderLayout());
			this.setFocusable(true);
			addKeyListener(this);
			setBackground(Color.BLACK);

			// Update and draw
			ActionListener gameLogic = new ActionListener()
			{
				public void actionPerformed(ActionEvent event)
				{
					if (state)
						playGame();
					else
						mainMenu();
				}
			};
			new Timer(1000/60, gameLogic).start();
		}

		///////////////
		// Main Menu //
		///////////////
		private void mainMenu()
		{
			repaint();
		}

		//////////////////////
		// Playing the game //
		//////////////////////
		private void playGame()
		{
			if (over)
			{
				repaint();
				return;
			}

			// Move enemies
			for (int i = 0; i < enemies.length; ++i)
			{
				GameObject enemy = enemies[i];

				// Skip removed enemies
				if (enemy == null)
				{
					incrementer++;
					continue;
				}

				// Check game over
				if ((enemy.getPosY() + enemy.getScaleY()) == player.getPosY())
				{
					over = true;
					labelOver = new JLabel("Game Over! Press ESC to return to Menu",
												SwingConstants.CENTER);
					labelOver.setForeground(Color.WHITE);
					labelOver.setFont(new Font(Font.DIALOG, Font.PLAIN, 15));
					add(labelOver, BorderLayout.CENTER);
					validate();
				}

				// Check collision
				if (bullets[0] != null &&
					enemy.checkCollide(bullets[0].getHitbox()))
				{
					enemies[i] = null;
					bullets[0] = null;
					incrementer++;
					continue;
				}

				// Left
				if (direction == 1)
				{
					enemy.setPosX(enemy.getPosX() - 1);
					enemy.updateHitbox();
					incrementer++;

					if (incrementer == (40 * 30))
					{
						direction = 2;
						incrementer = 0;
						continue;
					}
				}

				// Right
				if (direction == 3)
				{
					enemy.setPosX(enemy.getPosX() + 1);
					enemy.updateHitbox();
					incrementer++;

					if (incrementer == (40 * 30))
					{
						direction = 2;
						incrementer = 0;
						continue;
					}
				}

				// Down
				if (direction == 2)
				{
					enemy.setPosY(enemy.getPosY() + 1);
					enemy.updateHitbox();
					incrementer++;

					if (incrementer == (10 * 30))
					{
						if (left)
						{
							direction = 3;
							left = false;
						}
						else
						{
							direction = 1;
							left = true;
						}

						incrementer = 0;
					}
				}
			}

			// Move bullets
			for (Projectile bullet : bullets)
				if (bullet != null)
					bullet.move();

			repaint();
		}

		/////////////
		// Repaint //
		/////////////
		@Override
		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);

			if (state)
				paintGame(g);
			else
				paintMenu(g);
		}

		// Menu
		private void paintMenu(Graphics g)
		{

		}

		// Game
		private void paintGame(Graphics g)
		{
			// Draw enemies
			for (GameObject enemy : enemies)
				if (enemy != null)
					g.fillRect(enemy.getPosX(), enemy.getPosY(),
							enemy.getScaleX(), enemy.getScaleY());

			// Draw bullets
			for (Projectile bullet : bullets)
				if (bullet != null)
					g.fillRect(bullet.getPosX(), bullet.getPosY(),
							bullet.getScaleX(), bullet.getScaleY());

			// Draw player
			g.fillRect(player.getPosX(), player.getPosY(),
					player.getScaleX(), player.getScaleY());
		}

		///////////////////////////
		// KeyListener Overrides //
		///////////////////////////
		@Override
		public void keyPressed(KeyEvent e)
		{
			int keyNum = e.getKeyCode();

			if (state)
			{
				// Move player left
				if (keyNum == KeyEvent.VK_LEFT)
					player.setPosX(player.getPosX() - 2);
				
				// Move player right
				if (keyNum == KeyEvent.VK_RIGHT)
					player.setPosX(player.getPosX() + 2);

				// Shoot bullet
				if (keyNum == KeyEvent.VK_SPACE)
				{
					bullets[0] = new Projectile(player.getPosX() + (player.getScaleX() / 2),
												player.getPosY() - 21,
												10, 20);
				}

				// Stop game
				if (keyNum == KeyEvent.VK_ESCAPE && state)
				{
					state = false;
					stopGame();
				}
			}

			// Start game
			if (keyNum == KeyEvent.VK_ENTER && !state)
			{
				startGame();
				state = true;
			}
		}

		@Override
		public void keyReleased(KeyEvent e) { }

		@Override
		public void keyTyped(KeyEvent e) { }

	}
}
