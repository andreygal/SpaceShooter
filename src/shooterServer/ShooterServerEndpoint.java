package shooterServer;

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
	
	@OnOpen
	public void onOpen(){
		
	}
	
	@OnMessage
	public void onMessage(){
		
	}
	
	@OnClose
	public void onClose(){
		
	}
	
	private void startGame(){
		
	}
	
	private void tick(){
		
	}
}
