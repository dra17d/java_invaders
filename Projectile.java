// Projectile.java
// Dominick Aiudi - dra17d
// Florida State University - Summer 2019

public class Projectile extends GameObject
{
	public Projectile() { }

	public Projectile(int xPos, int yPos, int xScale, int yScale)
	{
		super(xPos, yPos, xScale, yScale);
	}

	public void move()
	{
		setPosY(getPosY() - 3);
		updateHitbox();
	}
}