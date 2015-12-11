package shooterServer;
/**
 * @author Andrey Galper
 *This is the server side of a communication channel between
 *the client interfaces and the server. In this game,
 *the server is responsible for creating and keeping track
 *of game all game objects (bullets, enemies, players), 
 *checking for collisions and updating positions as needed.
 *The resultant information is sent to the client using a 
 *message object - ObjectTo Draw, which contains fields
 *for type, position and image to be used in rendering the object.  
 */
import java.awt.Point;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import javax.websocket.*; 
import javax.websocket.server.ServerEndpoint;

import messages.ObjectToDrawEncoder;
import messages.ObjectToDraw;
import messages.ObjectToDrawDecoder; 


@ServerEndpoint(value = "/shooter", decoders = { ObjectToDrawDecoder.class }, 
									encoders = { ObjectToDrawEncoder.class })
public class ShooterServerEndpoint {
	/**the width of the gamePanel. must match the client*/
	final static int WIDTH  = 500; 
	/**the height of the gamePanel. must match the client*/
	final static int HEIGHT = 500;
	/**
	 * This set keeps track of currently connected sessions
	 * By making the variable static it was made available to the gameLauncher 
	 */
	static final Set<Session> SESSIONS = Collections.synchronizedSet(new HashSet<Session>());
	/**The brains of the game*/
	GameLauncher gameController; 
	/**to keep a communication log*/
	private Logger logger = Logger.getLogger(this.getClass().getName());
	/**
	 * When the connection with a peer is established,
	 * it is stored in the SESSIONS set. 
	 * @param session the session currently connecting. 
	 */
	@OnOpen
	public void onOpen(Session session) {
		logger.info("Server Connected ... " + session.getId());
		System.out.println("Storing peer information");
		SESSIONS.add(session); 
	}
	/**
	 * This method is responsible for receiving action events from the 
	 * clients and passing them to the gameController object. Still working
	 * on the implementation. For now, it starts the game when it receives  
	 * a start request from two clients.
	 * @param message the message coming from the client.
	 * @param peer currently connected peer.
	 */
	@OnMessage
	public void onMessage(String message, Session peer) {
		System.out.println("Server's onMessage");

		if(message.equals("start") && SESSIONS.size()==2) {
			//if the signal from client is received, start a new thread
			//using the inner class below 
			System.out.println("Server endpoint connected and listening");
			new GameStarter(); 
		}	
	}
	/**Method not implemented. Project did not advance this far*/
	@OnClose
	public void onClose(){}
	/**
	 * This inner class is responsible 
	 * for starting and maintaining the 
	 * game computation thread that uses 
	 * the GameLauncher class. 
	 */
	class GameStarter implements Runnable {
		// Create a second thread.
		Thread t;
		//construct a thread 
		GameStarter() {
			// Create a new, second thread
			gameController = new GameLauncher(new Point(10,10), 1, 1, 1);	
			t = new Thread(this, "Starter Thread");
			t.start(); // Start the thread
		}
		// This is the entry point for the second thread.
		public void run() {
			System.out.println("Starting the thread");
			gameController.startGame();
		}	
	}
}
