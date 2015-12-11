package shooterServer;

import java.awt.Dimension;
import java.awt.Point;
/**
 * Represents the keyboard controlled player's ship.
 * 
 * @author Andrey Galper 
 *
 */
public final class PlayerShip extends Ship {
	private int bulletVelocity; 
	private int bulletImageID; 
	private Dimension bulletSize; 
	/**
	 * 	
	 * @param xSpeed is the player's horizontal velocity
	 * @param path is the directory path to the ship's image file
	 * @param botInset is the width of the bottom border of a panel
	 * @param bulletVelocity is the velocity of the bullet fired by the ship object
	 */
	public PlayerShip(int xSpeed, int imageID, Dimension playerShipSize, int bulletVelocity,
					  int bulletImageID, Dimension bulletSize, int playerID) {
		
		super(xSpeed, imageID, playerShipSize);
		this.objectID = playerID; 
		//the starting position of the player's ship, calculated to be the middle of the screen.
		this.objectPosition = new Point(((ShooterServerEndpoint.WIDTH-this.getWidth())/2), (ShooterServerEndpoint.HEIGHT-this.getHeight()*2)); 	
		//set the parameters of bullets fired by this ship 
		this.bulletVelocity = bulletVelocity;
		this.bulletImageID  = bulletImageID;
		this.bulletSize = bulletSize; 
	}
	/**
	 * @return a bullet fired with a desired velocity from the ship's bow.
	 */
	public Bullet shoot(){
		Bullet bullet = new Bullet(bulletVelocity, bulletImageID, bulletSize, this.objectPosition, (int)this.objectSize.getWidth());
		return bullet; 
	}
}
