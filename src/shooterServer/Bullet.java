package shooterServer;

import java.awt.Point;
/**
 * Represents a generic bullet fired by an enemy or the player; 
 * @author Andrey Galper
 *
 */
public class Bullet extends GameObject {
	
	public Bullet(int ySpeed, String path, Point shipsPosition, int shipsWidth){
		super(0, ySpeed, path);
		//the starting position of the fired bullet, calculated to be at the firing ship's bow
		objectPosition = new Point(shipsPosition.x+(shipsWidth/2-1), shipsPosition.y-resizedSprite.getHeight(null)); 
	}
	/**
	 *Helps update the bullet array of the gamePanel.
	 *@return True if the bullet is still visible on the screen, false otherwise. 
	 */
	public boolean updatePosition(){
		if(this.objectPosition.y>Main.HEIGHT || this.objectPosition.y<0)
			return false; 
		else 
			objectPosition.y-=ySpeed;
			
		return true; 
	}
	
}