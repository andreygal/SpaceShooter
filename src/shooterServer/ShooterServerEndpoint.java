package shooterServer;

import java.awt.Point;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import javax.websocket.*; 
import javax.websocket.server.ServerEndpoint;

import messages.ObjectToDrawEncoder; 
import messages.ObjectToDrawDecoder; 


@ServerEndpoint(value = "/shooter", decoders = { ObjectToDrawDecoder.class }, encoders = { ObjectToDrawEncoder.class })
public class ShooterServerEndpoint {
	final static int WIDTH  = 500; 
	final static int HEIGHT = 500;
	/**This set keeps track of currently connected sessions*/
	private static final Set<Session> sessions = Collections.synchronizedSet(new HashSet<Session>());
	
	private GameLauncher gameController; 
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	@OnOpen
	public void onOpen(Session session) {
		
		logger.info("Server Connected ... " + session.getId());

		gameController = new GameLauncher(sessions, new Point(10,10), 5, 7, 1);
		gameController.startGame();
		System.out.println("Server endpoint connected and listening");
	}
	
	@OnMessage
	public void onMessage(String message){
		
	}
	
	@OnClose
	public void onClose(){
		
	}
	
		
}
