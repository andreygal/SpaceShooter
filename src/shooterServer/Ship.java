package shooterServer;

import java.awt.Dimension;
import java.awt.Point;

/**
 * This class abstracts the behaviors common to ships. 
 * @author Andrey Galper
 */
public abstract class Ship extends GameObject {
	//uses the superclass' constructor. ships do not move vertically  
	public Ship(int xSpeed, int imageID, Dimension shipSize){
		super(xSpeed, 0, imageID, shipSize);
	}
	
	public Ship(int xSpeed, int ImageID, Dimension shipSize, Point position) {
		this(xSpeed, ImageID, shipSize);
		this.objectPosition = position; 
	}
	/**
	 * Moves the ship to the right. 
	 * Allows the ship to move from the extreme right to the left corner of the screen
	 */
	public void moveRight(){
		objectPosition.move(((objectPosition.x+this.xSpeed)%(ShooterServerEndpoint.WIDTH+1)), objectPosition.y);
	}
	/**
	 * Moves the ship to the left.
	 * Allows the ship to move from the extreme left to the right corner of the screen.
	 */
	public void moveLeft(){
		if(objectPosition.x<(0-objectSize.width))
			objectPosition.move((ShooterServerEndpoint.WIDTH+objectPosition.x), objectPosition.y);
		
		objectPosition.move((objectPosition.x-this.xSpeed), objectPosition.y);
	}
	//ships have to be able to shoot bullets 
	public abstract Bullet shoot();
	
}
