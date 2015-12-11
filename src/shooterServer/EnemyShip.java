package shooterServer;

import java.awt.Dimension;
import java.awt.Point;
/**
 * Represents a computer controlled enemy ship.
 * 
 * @author Andrey Galper 
 *
 */
public final class EnemyShip extends Ship {

	public EnemyShip(int xSpeed, int imageID, Dimension enemySize, Point enemyPosition){
		super(xSpeed, imageID, enemySize, enemyPosition);
	}
	/**
	 * @see Ship#shoot()
	 * @return a bullet shot from the enemies' bow. Enemies shoot downward.  
	 */
	public Bullet shoot(){
		Point bulletPosition = new Point(objectPosition.x, objectPosition.y+((int)objectSize.getHeight())*2+1);	
		Bullet bullet = new Bullet(-5, imageID, objectSize, bulletPosition, (int)objectSize.getWidth());
		return bullet; 
	}
	
}
