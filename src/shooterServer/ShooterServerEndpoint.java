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
	
	private static GameLauncher gameController; 
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	@OnOpen
	public void onOpen(Session session) {
		
		logger.info("Server Connected ... " + session.getId());
		System.out.println("Server endpoint onOpen");
	}

	@OnMessage
	public void onMessage(String message, Session peer) {
		
		System.out.println("Server's onMessage");
		
		if(message.equals("start")) {
			System.out.println("Server endpoint connected and listening");
			gameController = new GameLauncher(peer, new Point(10,10), 5, 7, 1);
			gameController.startGame();
			
//			try {
//				peer.getBasicRemote().sendObject(new ObjectToDraw("PlayerShip", 0, new Point(10, 10)));
//				peer.getBasicRemote().sendObject(new ObjectToDraw("terminator", 0, new Point(0,0)));
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (EncodeException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
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

	}

	@OnClose
	public void onClose(){
		
	}
	
		
}
