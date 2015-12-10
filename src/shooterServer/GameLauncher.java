package shooterServer;
/**
 * @author Andrey Galper
 * The workhorse class of the server side of the game.
 * It's responsible for creating and destroying game objects, 
 * adjusting object positions, and detecting collisions.
 * After all the objects have been update and check for collisions
 * it stores them in a buffer as a linked list. The terminator object
 * tells the program that no more objects need to be rendered per frame.
 * I was trying to work out a defect whereby a single object appears to be 
 * send multiple times, even though in line 98 of the game loop it is sent 
 * only once. 
 */
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.websocket.EncodeException;
import javax.websocket.Session;

import messages.ObjectToDraw;

public class GameLauncher {
	/**A 2D array for holding references to enemy ships. can be used to seed different enemy formations. */
	private EnemyShip[][] enemyFormation;
	/**Starting point for calculating the initial positions all the enemies*/
	Point seedOrigin; 
	/**Number of rows in the enemy formation*/
	private int formationRows; 
	/**Number of columns in the enemy formation*/
	private int formationCols; 
	/**Number of players connected to the game*/
	private int numOfPlayers; 
	/**Used to choose a random image for the enemy ship*/ 
	private Random rand = new Random();
	/**A set representing current open sessions*/
	//private final Set<Session> sessions; 
	Session session; 
	/**Same class is used by the client. In this case it provides the 
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
	/**
	 * Contructor sets up that initializes the actual "game" 
	 * @param session Holds a single session. Will be replaced by a collection of sessions for multiplayer.
	 * @param seedOrigin Where to start seeding the enemies 
	 * @param formationRows How many rows of enemies
	 * @param formationCols How many columns of enemies
	 * @param numOfPlayers  The number of connected players. 
	 */
	GameLauncher(Session session, Point seedOrigin, int formationRows, int formationCols, int numOfPlayers) {
		//this.sessions = sessions; for multiplayer. still debugging usingn single player mode. 
		this.session = session; 
		this.seedOrigin = seedOrigin; 
		this.formationRows = formationRows; 
		this.formationCols = formationCols; 
		this.numOfPlayers = numOfPlayers; 
		//create an image processor and obtain the dimension data used for computations
		this.imageProcessor = new ImageProcessor(); 
		this.players  = ImageProcessor.getImageDimensions(ImageProcessor.PlayerShip);
		this.enemies  = ImageProcessor.getImageDimensions(ImageProcessor.EnemyShip);
		this.bullets  = ImageProcessor.getImageDimensions(ImageProcessor.Bullet);
		//initialize collections holding respective game objects 
		livePlayers = new ArrayList<PlayerShip>();  
		liveBullets = new ArrayList<Bullet>(); 
		//add a test player
		livePlayers.add(new PlayerShip(5, 0, players[0], 5, 0, bullets[0]));
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
			//update the positions of all the objects
			//updatePositions(); 
			//check for collisions b/w the objects
			checkCollisions(); 
			//broadcast to all connected clients
			objectsToDraw = getObjectsToDraw();
			//take elements from the buffer one by one and send them to the clients
			//using currently active sessions passed by the server endpoint
			System.out.println(objectsToDraw.size());
			while(!(objectsToDraw.isEmpty())) {
				ObjectToDraw bufferObject = objectsToDraw.remove(); 
				sendObjectToAll(bufferObject);
			}

				// if delta time is less than some target FPS (30 in this case)
				// then sleep the current thread for remaining time this frame
				if( dt < 1.0/30)
					try	{
						Thread.currentThread();
						Thread.sleep((long)((1.0/60 - dt) * 1000));
					} catch (InterruptedException e) {}

			//as the result each frame will take approximately the same time
			//and the loop will iterate 60 times per second instead of 5000-10000
		}
		
	}
	/**
	 * Updates the positions of all game objects by iterating through respective collections. 
	 */
	public void updatePositions() { 
		//the for loops iterate through all enemy ships, updating their positions 
		for(int i=0; i<formationRows; i++) {
			for(int j=0; j<formationCols; j++) {
				if(enemyFormation[i][j]!=null) {
					if(i%2==0)
						enemyFormation[i][j].moveLeft();
					else if(i%2==1)
						enemyFormation[i][j].moveRight();
				}
			}
		}

		//update the positions of all active bullets
		if(!liveBullets.isEmpty()){
			ListIterator <Bullet> iter = liveBullets.listIterator(); 
			while(iter.hasNext()){
				Bullet bul = iter.next();
				if((bul==null) || !bul.updatePosition())
					iter.remove();
			}
		}

		//update the positions of all players 
	}

	public void checkCollisions() {

		//the two outside for loops iterate through all the enemyFormation on the screen
		//for each enemy the inner while loops determines if a collision occurred b/w the enemy and the bullet
		for(int i=0; i<formationRows; i++){
			for(int j=0; j<formationCols; j++){
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

		//check players for collisions 
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

	public void seedEnemies() {
		//instantiate a 2D array with requested rows and columns 
		enemyFormation= new EnemyShip[formationRows][formationCols];
		//to hold random image index
		int randImageID; 
		//the loops populate the 2D array with enemy ships using random .png files from the enemySprites array
		//each ship is given a starting coordinate 
		for(int i=0; i<formationRows; i++) {
			for(int j=0; j<formationCols; j++) {
				randImageID = rand.nextInt(enemies.length);
				enemyFormation[i][j] = new EnemyShip(1, randImageID, enemies[randImageID], seedOrigin);
				System.out.println(seedOrigin);
				seedOrigin.move(seedOrigin.x+70, 0);
			}
			//start the next row from the original x coordinate
			seedOrigin.move(seedOrigin.x, seedOrigin.y+50);	
		}
	}

	public LinkedList<ObjectToDraw> getObjectsToDraw() {

		LinkedList<ObjectToDraw> objectsToDraw = new LinkedList<ObjectToDraw>();
		//add enemies to buffer as encodable objects. send only those enemies whose fields have been updated. 
		for(int i=0; i<formationRows; i++) {
			for(int j=0; j<formationCols; j++) {
				if(enemyFormation[i][j].isAlive && enemyFormation[i][j].isUpdated) {
					System.out.println("Encoding enemy object");
					objectsToDraw.add(new ObjectToDraw(enemyFormation[i][j].toString(), 
													   enemyFormation[i][j].imageID,
													   enemyFormation[i][j].objectPosition));
				}
			}
		}
		//add players to the buffer 
		for(PlayerShip ship:livePlayers) {
			if(ship.isAlive()){
				System.out.println("Encoding player object");

				objectsToDraw.add(new ObjectToDraw(ship.toString(), ship.imageID, ship.objectPosition));
			}
		}
		//add bullets to the buffer 
		for(Bullet bullet:liveBullets) {
			if(bullet.isAlive())
				objectsToDraw.add(new ObjectToDraw(bullet.toString(), bullet.imageID, bullet.objectPosition)); 
		}

		//terminator object used to signal the end of the buffer
		System.out.println("Adding terminator");
		objectsToDraw.add(new ObjectToDraw("terminator", -1, 0, 0));

		return objectsToDraw; 
	}

	private void sendObjectToAll(ObjectToDraw objectToDraw) {
		try {
		session.getBasicRemote().sendObject(objectToDraw); 
		} catch (EncodeException e) {
			System.out.println("Encode Exception!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
//		for(Session s : sessions) {
//			try {
//				s.getBasicRemote().sendObject(objectToDraw);
//			} catch(EncodeException e) {
//				System.out.println("Encode exception");
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//	}
}
