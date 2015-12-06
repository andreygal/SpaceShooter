package shooterServer;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.websocket.Session;

public class GameLauncher {
	/**A 2D array for holding references to enemy ships. can be used to seed different enemy formations. */
	private EnemyShip[][] enemies;
	/**A set representing current open sessions*/
	private final Set<Session> sessions; 
	
	GameLauncher(Set<Session> sessions) {
		this.sessions = sessions; 
	}
	
	public void startGame {
		
	}
	
	public void updateAll { 
}
