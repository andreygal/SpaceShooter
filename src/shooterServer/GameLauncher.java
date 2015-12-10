package shooterServer;

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

	private Point seedOrigin; 
	private int formationRows; 
	private int formationCols; 
	private int numOfPlayers; 
	
	//for choosing random enemy sprites
	private Random rand = new Random();
	/**A set representing current open sessions*/
	//private final Set<Session> sessions; 
	Session session; 
	
	ImageProcessor imageProcessor; 
	
	private Dimension[] players;
	private	Dimension[] enemies;
	private	Dimension[] bullets;
	
	private ArrayList<PlayerShip> livePlayers;
	private ArrayList<Bullet>	  liveBullets; 
	private LinkedList<ObjectToDraw> objectsToDraw; 
	
	private boolean isActive;  
	
	GameLauncher(Session session, Point seedOrigin, int formationRows, int formationCols, int numOfPlayers) {
		//this.sessions = sessions; 
		this.session = session; 
		this.seedOrigin = seedOrigin; 
		this.formationRows = formationRows; 
		this.formationCols = formationCols; 
		this.numOfPlayers = numOfPlayers; 
		this.imageProcessor = new ImageProcessor(); 
		this.players  = ImageProcessor.getImageDimensions(ImageProcessor.PlayerShip);
		this.enemies  = ImageProcessor.getImageDimensions(ImageProcessor.EnemyShip);
		this.bullets  = ImageProcessor.getImageDimensions(ImageProcessor.Bullet);
		livePlayers = new ArrayList<PlayerShip>();  
		liveBullets = new ArrayList<Bullet>(); 
		livePlayers.add(new PlayerShip(5, 0, players[0], 5, 0, bullets[0]));
		System.out.println("Launcher Created");
	}
	
	public void startGame() {
		System.out.println("gameLauncher is starting the game"); 
		isActive = true; 
		int counter = 10; 

		long currTime, prevTime = System.nanoTime();
		double dt;

		seedEnemies();
		
		while(isActive && !livePlayers.isEmpty()) {

				//calculating delta time (time between frames)
				//1.0e9 because nanoTime() function returns nanoseconds but we need seconds
				currTime = System.nanoTime();
				dt = (currTime - prevTime) / 1.0e9;
				prevTime = currTime;

			updatePositions(); 
			checkCollisions(); 
			//broadcast to all connected clients
			objectsToDraw = getObjectsToDraw();

			while(!(objectsToDraw.isEmpty())) {
				ObjectToDraw bufferObject = objectsToDraw.remove(); 
				sendObjectToAll(bufferObject);
			}

				// if delta time is less than some target fps (60 in this case)
				// then sleep the current thread for remaining time this frame
				if( dt < 1.0/60)
					try	{
						Thread.currentThread();
						Thread.sleep((long)((1.0/60 - dt) * 1000));
					} catch (InterruptedException e) {}

			//as the result each frame will take approx. the same time
			//and loop will iterate 60 times per second only not 5000-10000 as before
		}
		
	}
	
	
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
				seedOrigin.translate(70, 0);
			}
			//start the next row from the original x coordinate
			seedOrigin.move(seedOrigin.x, seedOrigin.y+50);	

		}
	}

	public LinkedList<ObjectToDraw> getObjectsToDraw() {
		
		LinkedList<ObjectToDraw> objectsToDraw = new LinkedList<ObjectToDraw>();
		//add enemies to buffer as encodable objects 
		for(int i=0; i<formationRows; i++){
			for(int j=0; j<formationCols; j++) {
				if(enemyFormation[i][j]!=null)
					objectsToDraw.add(new ObjectToDraw(enemyFormation[i][j].toString(), 
														enemyFormation[i][j].imageID,
														enemyFormation[i][j].objectPosition));
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
		objectsToDraw.add(new ObjectToDraw("terminator", -1, null));
		
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
