package shooterServer;

import java.awt.Dimension;
import java.awt.Point;
/**
 * Represents a generic bullet fired by an enemy or the player; 
 * @author Andrey Galper
 *
 */
public class Bullet extends GameObject {
	
	public Bullet(int ySpeed, int imageID, Dimension bulletSize, Point bulPosition, int shipsWidth){
		super(0, ySpeed, imageID, bulletSize);
		//the starting position of the fired bullet, calculated to be at the firing ship's bow
		objectPosition = new Point(bulPosition.x+(shipsWidth/2-1), bulPosition.y-(int)bulletSize.getHeight()); 
	}
	/**
	 *Helps update the bullet array of the gamePanel.
	 *@return True if the bullet is still visible on the screen, false otherwise. 
	 */
	public boolean updatePosition(){
		if(this.objectPosition.y>ShooterServerEndpoint.HEIGHT || this.objectPosition.y<0)
			return false; 
		else 
			objectPosition.y-=ySpeed;
			
		return true; 
	}
	
}