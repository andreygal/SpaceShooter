package shooterServer;

import java.awt.Point;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import javax.websocket.*; 
import javax.websocket.server.ServerEndpoint;

import messages.ObjectToDrawEncoder;
import messages.ObjectToDraw;
import messages.ObjectToDrawDecoder; 


@ServerEndpoint(value = "/shooter", decoders = { ObjectToDrawDecoder.class }, encoders = { ObjectToDrawEncoder.class })
public class ShooterServerEndpoint {

	final static int WIDTH  = 500; 
	final static int HEIGHT = 500;
	/**This set keeps track of currently connected sessions*/
	//private static final Set<Session> sessions = Collections.synchronizedSet(new HashSet<Session>());

	GameLauncher gameController; 
	private Logger logger = Logger.getLogger(this.getClass().getName());

	@OnOpen
	public void onOpen(Session session) {

		logger.info("Server Connected ... " + session.getId());
		System.out.println("Server endpoint onOpen");

		try {
			session.getBasicRemote().sendObject(new ObjectToDraw("PlayerShip", 0, new Point(10, 10)));
			session.getBasicRemote().sendObject(new ObjectToDraw("terminator", 0, new Point(0,0)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (EncodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

	@OnMessage
	public void onMessage(String message, Session peer) {

		System.out.println("Server's onMessage");

		if(message.equals("start")) {


			System.out.println("Server endpoint connected and listening");
			gameController = new GameLauncher(peer, new Point(10,10), 5, 7, 1);	
			GameStarter starter = new GameStarter(); 


		}	
		try {
			peer.getBasicRemote().sendObject(new ObjectToDraw("PlayerShip", 0, new Point(10, 10)));
			peer.getBasicRemote().sendObject(new ObjectToDraw("terminator", 0, new Point(0,0)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (EncodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//		try {
	//			System.out.println("sending test objects"); 
	//			peer.getBasicRemote().sendObject(new ObjectToDraw("PlayerShip", 0, new Point(10, 10)));
	//			peer.getBasicRemote().sendObject(new ObjectToDraw("terminator", 0, new Point(0,0)));
	//		} catch (EncodeException e) {
	//			System.out.println("encode ex");
	//		} catch (IOException e) {
	//			e.printStackTrace();
	//		}


@OnClose
public void onClose(){

}

class GameStarter implements Runnable {
	// Create a second thread.
	Thread t;

	GameStarter() {
		// Create a new, second thread
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
