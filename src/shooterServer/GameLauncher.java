package shooterServer;
/**
 * @author Andrey Galper
 * The workhorse class of the server side of the game.
 * It's responsible for creating and destroying game objects, 
 * adjusting object positions, and detecting collisions.
 * After all the objects have been updated and checked for collisions
 * it stores them in a buffer as a linked list. At preset FPS intervals 
 * the currently active objects are broadcasted to all connected peers.
 *  
 */
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Random;

import javax.websocket.EncodeException;
import javax.websocket.Session;

import messages.ObjectToDraw;

public class GameLauncher {
	/**A 2D array for holding references to enemy ships. can be used to seed different enemy formations. */
	private EnemyShip[][] enemyFormation;
	/**Starting point for calculating the initial positions all the enemies*/
	private Point seedOrigin; 
	/**Number of rows in the enemy formation*/
	private int formationRows; 
	/**Number of columns in the enemy formation*/
	private int formationCols; 
	/**Used to choose a random image for the enemy ship*/ 
	private Random rand = new Random();
	/**The computation rate of the program. Match with FPS of the client*/
	private int fps; 
	/**
	 * Same class is used by the client. In this case it provides the 
	 * dimensions of all the game objects to the GameLauncher
	 */
	ImageProcessor imageProcessor; 
	/**Dimensions for player images*/
	private Dimension[] players;
	/**Dimensions for enemy images*/
	private	Dimension[] enemies;
	/**Dimensions for bullet images. Wanted to make this game pretty.*/
	private	Dimension[] bullets;
	/**Holds currently active players. If the player is destroyed, object is removed.*/
	private ArrayList<PlayerShip> livePlayers;
	/**Holds the bullets in game space. Out of bounds bullets are removed*/
	private ArrayList<Bullet>	  liveBullets; 
	/**Buffer for storing the objects that will be sent to the client side*/
	private LinkedList<ObjectToDraw> objectsToDraw; 
	/**If the game is over, set to false*/
	private boolean isActive;  
	/**number of players*/
	private int numOfPlayers; 
	/**
	 * Constructor sets up and initializes the actual "game" 
	 * @param seedOrigin Where to start seeding the enemies 
	 * @param formationRows How many rows of enemies
	 * @param formationCols How many columns of enemies
	 */
	GameLauncher(Point seedOrigin, int formationRows, int formationCols, int numOfPlayers) {
		//set up parameters for the enemy formation 
		this.seedOrigin = seedOrigin; 
		this.formationRows = formationRows; 
		this.formationCols = formationCols; 
		//create an image processor and obtain the dimension data used for computations
		this.imageProcessor = new ImageProcessor(); 
		this.players  = ImageProcessor.getImageDimensions(ImageProcessor.PlayerShip);
		this.enemies  = ImageProcessor.getImageDimensions(ImageProcessor.EnemyShip);
		this.bullets  = ImageProcessor.getImageDimensions(ImageProcessor.Bullet);
		this.numOfPlayers = numOfPlayers; 
		//initialize collections holding respective game objects 
		livePlayers = new ArrayList<PlayerShip>();  
		liveBullets = new ArrayList<Bullet>(); 
		//add a test player
		livePlayers.add(new PlayerShip(5, 0, players[0], 5, 0, bullets[0], 0));
		//set the computation rate
		fps = 60; 
		System.out.println("Launcher Created");
	}
	/**
	 *	This method starts the main loop of the game. This loop controls 
	 *	the "frame rate" and how quickly the objects are updated. 
	 *	I slowed down the loop after seeing that the same object was
	 *	being send multiple times to the client side. Still working it out.
	 */
	public void startGame() {
		System.out.println("gameLauncher is starting the game"); 
		isActive = true; 
		//calculate the time for each iteration of the loop using differences in system time. 
		long currTime, prevTime = System.nanoTime();
		double dt;
		//create enemies
		seedEnemies();
		//start the main game loop
		while(isActive && !livePlayers.isEmpty()) {
				//calculating delta time (time between frames)
            	//1.0e9 since nanoTime() returns nanoseconds that we need to convert to seconds
            	currTime = System.nanoTime();
            	dt = (currTime - prevTime) / 1.0e9;
            	prevTime = currTime;
			//update the positions of all active objects
			updatePositions(); 
			//check for collisions b/w all the active objects
			checkCollisions(); 
			//obtain a filled buffer
			objectsToDraw = getObjectsToDraw();
			//take elements from the buffer one by one and send them to the clients
			//using currently active sessions accessed through the static SESSIONS set 
			while(!(objectsToDraw.isEmpty())) {
				//output the current buffer size to console
				System.out.println("Sending object from buffer " + objectsToDraw.size()); 
				//remove the object from the linked list buffer
				ObjectToDraw bufferObject = objectsToDraw.remove(); 
				//display the state of object before encoding takes place 
				System.out.println(bufferObject.getType() + " " + 
						           bufferObject.getImageID() + " " + 
						           bufferObject.getObjectID() + " " + 
						           bufferObject.getObjectPosition());
				//broadcast a single object
				sendObjectToAll(bufferObject);
			}
				// if delta time is less than some target FPS (30 in this case)
				// then sleep the current thread for remaining time this frame
				if(dt < 1.0/fps)
					try	{
						Thread.currentThread();
						Thread.sleep((long)((1.0/fps - dt) * 1000));
					} catch (InterruptedException e) {}
			//as the result each frame will take approximately the same time
			//and the loop will iterate 60 times per second instead of 5000-10000
		}
	}
	/**
	 * Updates the positions of all game objects by iterating through respective collections. 
	 */
	public synchronized void updatePositions() { 
		//the for loops iterate through all enemy ships, updating their positions 
		//and setting the internal update flag of each object to true
		for(int i=0; i<formationRows; i++) {
			for(int j=0; j<formationCols; j++) {
				if(enemyFormation[i][j]!=null) {
					if(i%2==0) {
						enemyFormation[i][j].moveLeft();
						enemyFormation[i][j].setIsUpdated(true);
					}
					else if(i%2==1) {
						enemyFormation[i][j].moveRight();
						enemyFormation[i][j].setIsUpdated(true); 
					}
				}
			}
		}
		//update the positions of all active bullets
		if(!liveBullets.isEmpty()) {
			ListIterator <Bullet> iter = liveBullets.listIterator(); 
			while(iter.hasNext()) {
				Bullet bul = iter.next();
				if((bul==null) || !bul.updatePosition())
					iter.remove();
			}
		}
		//update the positions of all players 
		//code triggered by action events will go here 
	}
	/**
	 * collision checking function of the game. obtains a Rectangle object using 
	 * object dimensions from the array provided by the Image Processor. If the 
	 * rectangles of two object intersect a hit is registered. 
	 */
	public synchronized void checkCollisions() {
		//the two outside for loops iterate through all enemies in the enemyFormation 2D array  
		//for each enemy the inner while loops determines if a collision occurred b/w the enemy and the bullet
		//if the collision occurred sets the enemy reference to null and removes the bullet from the array
		for(int i=0; i<formationRows; i++) {
			for(int j=0; j<formationCols; j++) {
				if(enemyFormation[i][j]!=null){
					ListIterator <Bullet> iter = liveBullets.listIterator(); 
					//iterate through all active bullets
					while(iter.hasNext()){
						Bullet bullet = iter.next();
						if(bullet!=null){
							Rectangle enemy = enemyFormation[i][j].getRectangle(); 
							if(enemy.intersects(bullet.getRectangle())){
								enemyFormation[i][j]=null; 
								iter.remove();
								break; 
							}
						}
					}
				}
			}
		}
		//check players for collisions. if a hit occurred, remove both the player and the bullet from the game  
		ListIterator <Bullet> iter = liveBullets.listIterator(); 
		for(int i=0; i<numOfPlayers; i++) {
			while(iter.hasNext()){
				Bullet bullet = iter.next();
				if((bullet!=null) && livePlayers.get(i).getRectangle().intersects(bullet.getRectangle())) {  
					livePlayers.remove(i);
					iter.remove();
				}
			}
		}
	}
	/**
	 * the seeder method runs at the begining of the game. 
	 * populates each cell the 2D array enemyFormation with 
	 * enemies, giving each enemy a unique ID and choosing 
	 * seeding starts from the seedOrigin point passed to the 
	 * constructor of the GameLauncher class. 
	 */
	public synchronized void seedEnemies() {
		System.out.println("seeding enemies " + formationRows + " " + formationCols);
		
		//instantiate a 2D array with requested rows and columns 
		enemyFormation= new EnemyShip[formationRows][formationCols];
		//to hold random image index
		int randImageID; 
		//the loops populate the 2D array with enemy ships using random .png files from the enemySprites array
		//each ship is given a starting coordinate distinct from the predecessor 
		for(int i=0; i<formationRows; i++) {
			for(int j=0; j<formationCols; j++) {
				randImageID = rand.nextInt(enemies.length);
				System.out.println("Seed origin is now " + seedOrigin);
				enemyFormation[i][j] = new EnemyShip(1, randImageID, enemies[randImageID], seedOrigin);
				//stamp the object with a two digit ID
				enemyFormation[i][j].setObjectID((i+10*(j+1)));
				System.out.println(seedOrigin);
				seedOrigin.translate(70, 0);
				System.out.println("After inner loop seedOrigin is " + seedOrigin);
			}
			//start the next row from the original x coordinate
			seedOrigin.translate(0, 50);	
		}
	}
	/*
	 *	gathers all game objects whose coordinates have been updated into a linked list buffer 
	 *	the buffer is later passed on to the sendToAll method for broadcasting. this avoids 
	 *	sending objects whose states have not changed, reducing computations. each objects state
	 *	is copied into a respective messenger object (objectToSend) 
	 */
	public synchronized LinkedList<ObjectToDraw> getObjectsToDraw() {
		//create a buffer 
		LinkedList<ObjectToDraw> objectsToDraw = new LinkedList<ObjectToDraw>();
		//add enemies to buffer as encodable objects. send only those enemies whose fields have been updated. 
		for(int i=0; i<formationRows; i++) {
			for(int j=0; j<formationCols; j++) {
				if(enemyFormation[i][j].isAlive && enemyFormation[i][j].isUpdated) {
					System.out.println("Encoding enemy object whose coordinate is " + enemyFormation[i][j].objectPosition);
					enemyFormation[i][j].setIsUpdated(false);
					objectsToDraw.add(new ObjectToDraw(enemyFormation[i][j].toString(), 
													   enemyFormation[i][j].imageID,
													   enemyFormation[i][j].objectPosition,
													   enemyFormation[i][j].objectID));
				}
			}
		}
		//add players to the buffer 
		for(PlayerShip ship:livePlayers) {
			if(ship.isAlive() && ship.isUpdated) {
				System.out.println("Encoding player object");
				ship.setIsUpdated(false);
				objectsToDraw.add(new ObjectToDraw(ship.toString(), ship.imageID, ship.objectPosition, ship.objectID));
			}
		}
		//add bullets to the buffer 
		for(Bullet bullet:liveBullets) {
			if(bullet.isAlive())
				objectsToDraw.add(new ObjectToDraw(bullet.toString(), bullet.imageID, bullet.objectPosition, bullet.objectID)); 
		}
		//return the buffer to the caller
		return objectsToDraw; 
	}
	/**
	 * Takes a linked list buffer and broadcasts the objects to all connected peers. 
	 * @param objectToDraw
	 */
	private synchronized void sendObjectToAll(ObjectToDraw objectToDraw) {
		//goes through the SESSIONS set and sends a single object to each
		for(Session s : ShooterServerEndpoint.SESSIONS) {
			try {
				s.getBasicRemote().sendObject(objectToDraw);
			} catch(EncodeException e) {
				System.out.println("Encode exception");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
