// GameObject.java
// Dominick Aiudi - dra17d
// Florida State University - Summer 2019

public class GameObject
{
	private int posX;
	private int posY;
	private int scaleX;
	private int scaleY;
	private Hitbox box;

	public GameObject() { }

	public GameObject(int xPos, int yPos, int xScale, int yScale)
	{
		posX = xPos;
		posY = yPos;
		scaleX = xScale;
		scaleY = yScale;
		box = new Hitbox();
	}

	// Setters
	public void setPosX(int newPos) { posX = newPos; }
	public void setPosY(int newPos) { posY = newPos; }
	public void setScaleX(int newScale) { scaleX = newScale; }
	public void setScaleY(int newScale) { scaleY = newScale; }

	// Getters
	public int getPosX() { return posX; }
	public int getPosY() { return posY; }
	public int getScaleX() { return scaleX; }
	public int getScaleY() { return scaleY; }
	public Hitbox getHitbox() { return box; }

	public boolean checkCollide(Hitbox other) { return box.collide(other); }
	public void updateHitbox() { box.update(); }

	//////////////////
	// Hitbox Class //
	//////////////////
	// Appears redundant, but the hitbox values should
	// be able to be adjusted for non-square objects
	private class Hitbox
	{
		public int boxX, boxY, boxW, boxH;

		public Hitbox() { update(); }

		public void update()
		{
			boxX = posX;
			boxY = posY;
			boxW = scaleX;
			boxH = scaleY;
		}

		public boolean collide(Hitbox other)
		{
			// Axis-Aligned Bounding-Box collision
			if ((boxX < (other.boxX + other.boxW) &&
				boxX > other.boxX) ||
				((boxX + boxW) < (other.boxX + other.boxW) &&
				(boxX + boxW) > other.boxX))
			{
				if (boxY < (other.boxY + other.boxH) &&
					boxY > other.boxY)
				{
					return true;
				}
				else if ((boxY + boxH) < (other.boxY + other.boxH) &&
					(boxY + boxH) > other.boxY)
				{
					return true;
				}
				else
					return false;
			}
			else
				return false;
		}
	}
}
