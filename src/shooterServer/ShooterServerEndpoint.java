package shooterServer;

import javax.websocket.*; 
import javax.websocket.server.ServerEndpoint; 


@ServerEndpoint(value = "/shooter", decoders = {}, encoders = {})
public class ShooterServerEndpoint {
	
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
