package shooterServer;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.ListIterator;
/**
 * Represents the keyboard controlled player's ship.
 * 
 * @author Andrey Galper 
 *
 */
public final class PlayerShip extends Ship {
	private int bulletVelocity; 
	/**
	 * 	
	 * @param xSpeed is the player's horizontal velocity
	 * @param path is the directory path to the ship's image file
	 * @param botInset is the width of the bottom border of a panel
	 * @param bulletVelocity is the velocity of the bullet fired by the ship object
	 */
	public PlayersShip(int xSpeed, int imageID, Dimension playerShipSize, int bulletVelocity){
		super(xSpeed, 0, imageID, playerShipSize);
		//the starting position of the player's ship, calculated to be the middle of the screen.
		objectPosition = new Point(((Main.WIDTH-this.getWidth())/2), (Main.HEIGHT-this.getHeight()*2)); 	
		this.bulletVelocity = bulletVelocity; 
	}
	/**
	 * @return a bullet fired with a desired velocity from the ship's bow.
	 */
	public Bullet shoot(){
		Bullet bullet = new Bullet(bulletVelocity, "laserRed03.png", this.objectPosition, resizedSprite.getWidth(null));
		return bullet; 
	}
	/**
	 * @param bullets is the bullet array passed by the calling panel
	 * This is a collision detection function responsible for checking intersection(s) b/w bullets and the player's ship.
	 * @return true if a collision occurred, false otherwise.
	 */
	public boolean collisionChecker(ArrayList<Bullet> bullets){
		
		Rectangle player = this.getRectangle(); 
		//a ListIterator is required to modify a collection of bullets in "real-time"
		ListIterator <Bullet> iter = bullets.listIterator(); 
		
		while(iter.hasNext()){
			Bullet bullet = iter.next();
			if(bullet!=null && player.intersects(bullet.getRectangle())){
				iter.remove();
				return true; 
			}
		}	
		
		return false; 
	}
}
