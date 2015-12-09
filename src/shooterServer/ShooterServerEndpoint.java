package shooterServer;

import java.awt.Point;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.websocket.*; 
import javax.websocket.server.ServerEndpoint; 


@ServerEndpoint(value = "/shooter", decoders = {}, encoders = {})
public class ShooterServerEndpoint {
	final static int WIDTH  = 500; 
	final static int HEIGHT = 500;
	/**This set keeps track of currently connected sessions*/
	private static final Set<Session> sessions = Collections.synchronizedSet(new HashSet<Session>());
	
	private GameLauncher gameController; 
	
	@OnOpen
	public void onOpen(){
		gameController = new GameLauncher(sessions, new Point(10,10), 5, 7, 1);
		gameController.startGame();
	}
	
	@OnMessage
	public void onMessage(String message){
		
	}
	
	@OnClose
	public void onClose(){
		
	}
	
		
}
