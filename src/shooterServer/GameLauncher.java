package shooterServer;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.ListIterator;
import java.util.Random;
import java.util.Set;

import javax.websocket.Session;

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
	private final Set<Session> sessions; 
	
	private Dimension[] players;
	private	Dimension[] enemies;
	private	Dimension[] bullets;
	
	private ArrayList<PlayerShip> livePlayers;
	private ArrayList<EnemyShip>  liveBullets;
	

	
	GameLauncher(Set<Session> sessions, Point seedOrigin, int formationRows, int formationCols, int numOfPlayers) {
		this.sessions = sessions; 
		this.seedOrigin = seedOrigin; 
		this.formationRows = formationRows; 
		this.formationCols = formationCols; 
		this.numOfPlayers = numOfPlayers; 
		this.players  = ImageProcessor.getImageDimensions(ImageProcessor.PlayerShip);
		this.enemies  = ImageProcessor.getImageDimensions(ImageProcessor.EnemyShip);
		this.bullets  = ImageProcessor.getImageDimensions(ImageProcessor.Bullet);
	}
	
	public void startGame() {
		
		
	}
	
	public void updateAll() { 
		//the for loops iterate through all enemy ships, updating their positions and redrawing them
				for(int i=0; i<5; i++){
					for(int j=0; j<7; j++){
						if(enemies[i][j]!=null){
							if(i%2==0)
								enemies[i][j].moveLeft();
							else if(i%2==1)
								enemies[i][j].moveRight();
							g.drawImage(enemies[i][j].getImage(), enemies[i][j].objectPosition.x, enemies[i][j].objectPosition.y, null);
						}
					}
				}
				
				//the two outside for loops iterate through all the enemies on the screen
				//for each enemy the inner while loops determines if a collision occurred b/w the enemy and the bullet
				for(int i=0; i<5; i++){
					for(int j=0; j<7; j++){
						if(enemies[i][j]!=null){
							ListIterator <Bullet> iter = bullets.listIterator(); 
							//iterate through all active bullets
							while(iter.hasNext()){
								Bullet bullet = iter.next();
								if(bullet!=null){
									Rectangle enemy = enemies[i][j].getRectangle(); 
									if(enemy.intersects(bullet.getRectangle())){
										enemies[i][j] = null; 
										iter.remove();
										break; 
									}
								}
							}
						}
					}
				}

	}

	public void seedEnemies() {
		//instantiate a 2D array with requested rows and columns 
		enemyFormation= new EnemyShip[formationRows][formationCols];
		//the loops populate the 2D array with enemy ships using random .png files from the enemySprites array
		//each ship is given a starting coordinate 
		for(int i=0; i<formationRows; i++) {
			for(int j=0; j<formationCols; j++) {
				int randImageID = rand.nextInt(enemies.length);
				enemyFormation[i][j] = new EnemyShip(1, randImageID, enemies[randImageID], seedOrigin);
				seedOrigin.translate(70, 0);
			}
			//start the next row from the original x coordinate
					seedOrigin.move(seedOrigin.x, seedOrigin.y+50);	

		}
	}
	
}
