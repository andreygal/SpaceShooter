package shooterServer;

import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import javax.websocket.Session;

public class GameLauncher {
	/**A 2D array for holding references to enemy ships. can be used to seed different enemy formations. */
	private EnemyShip[][] enemyFormation;

	private Point seedOrigin; 
	private int formationRows; 
	private int formationCols; 
	
	//for choosing random enemy sprites
	private Random rand = new Random();
	/**A set representing current open sessions*/
	private final Set<Session> sessions; 
	
	private Dimension[] players;
	private	Dimension[] enemies;
	private	Dimension[] bullets;
	
	private ArrayList<PlayerShip> livePlayers;
	private ArrayList<EnemyShip>  liveBullets;
	

	
	GameLauncher(Set<Session> sessions, Point seedOrigin, int formationRows, int formationCols) {
		this.sessions = sessions; 
		this.seedOrigin = seedOrigin; 
		this.formationRows = formationRows; 
		this.formationCols = formationCols; 
		this.players  = ImageProcessor.getImageDimensions(ImageProcessor.PlayerShip);
		this.enemies  = ImageProcessor.getImageDimensions(ImageProcessor.EnemyShip);
		this.bullets  = ImageProcessor.getImageDimensions(ImageProcessor.Bullet);
	}
	
	public void startGame {
		
		
	}
		
	}
	
	public void updateAll { 
		
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
