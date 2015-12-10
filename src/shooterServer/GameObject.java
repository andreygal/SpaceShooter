package shooterServer;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;


/**
 * Abstracts the methods and instance variables common to game objects;  
 * @author Andrey Galper 
 */
public abstract class GameObject {
	/** the x velocity of the object*/
	protected int xSpeed;
	/**the y velocity of the object*/
	protected int ySpeed;
	/**the width and height of the object*/
	protected Dimension objectSize; 
	/**the coordinates of the top left corner of the object*/
	protected Point objectPosition;  
	/**array index where the the object's image is stored*/
	protected int imageID; 
	/**is the object alive*/
	protected boolean isAlive; 
	/**true if the object was updated*/ 
	protected boolean isUpdated; 
	//Default constructor. By default, ints are set to 0 and object references to null.
	public GameObject(){}
	
	/**
	 * @param xSpeed is the x velocity of an object
	 * @param ySpeed is the y velocity of an object
	 * @param path is the path to the image used to represent a game object
	 * constructs a game object by resizing the image, setting the velocities and 
	 * calculating all the derived attributes like Dimension, Position etc.
	 */
	public GameObject(int xSpeed, int ySpeed, int imageID, Dimension objectSize) {
		this.objectSize = objectSize; 
		this.xSpeed = xSpeed; 
		this.ySpeed = ySpeed; 
		this.imageID = imageID;  
		this.isAlive = true; 
		this.isUpdated = true; 
	}

	public GameObject(int xSpeed, int ySpeed, int imageID, Dimension objectSize, Point objectPosition) {
		this(); 
		this.objectPosition = objectPosition; 
	}

	//getters and setters 
	public void setXSpeed(int xSpeed) {
		this.xSpeed = xSpeed;
	}

	public void setYSpeed(int ySpeed) {
		this.ySpeed = ySpeed;
	}

	public int getHeight() {
		return objectSize.height; 
	}

	public int getWidth() {
		return objectSize.width; 
	}
	
	public Rectangle getRectangle(){
		return new Rectangle(objectPosition.x, objectPosition.y, objectSize.width, objectSize.height);
	}
	
	@Override
	public final String toString() {
		return this.getClass().getName(); 
	}
	
	public boolean isAlive() {
		return isAlive; 
	}
}

