package shooterServer;

import java.awt.Point;
/**
 * Represents the computer controlled enemy ship.
 * 
 * @author Andrey Galper 
 *
 */
public final class EnemyShip extends Ship {

	public EnemyShip(int xSpeed,  Point position){
		super(xSpeed, 0, path);
		objectPosition = new Point(position);
	}
	/**
	 * @see Ship#shoot()
	 * @return a bullet shot from the enemies' bow. Enemies shoot downward.  
	 */
	public Bullet shoot(){
		Point bulletPosition = new Point(objectPosition.x, objectPosition.y+(resizedSprite.getHeight(null))*2+1);	
		Bullet bullet = new Bullet(-5, "laserRed03.png", bulletPosition, resizedSprite.getWidth(null));
		return bullet; 
	}
	
	void updatePosition(){
		objectPosition.translate(xSpeed, 0);
	}
}
