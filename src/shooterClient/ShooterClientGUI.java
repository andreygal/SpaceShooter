package shooterClient;

import java.awt.BorderLayout;
import java.io.IOException;

import javax.swing.JFrame;
import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

import messages.ObjectToDraw;
import messages.ObjectToDrawDecoder;
import messages.ObjectToDrawEncoder; 

@ClientEndpoint(decoders = { ObjectToDrawDecoder.class }, encoders = { ObjectToDrawEncoder.class })
public class ShooterClientGUI {
	
	final static int HEIGHT = 500; 
	final static int WIDTH  = 500; 
	private static GamePanel gamePanel; 

	@OnOpen
	public void onOpen(Session session) { 
		try {
			session.getBasicRemote().sendText("connected");
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@OnMessage
	public void onMessage(Session session, ObjectToDraw object) {
	
		gamePanel.receiveObjectToDraw(object);
		
	}
	
	@OnClose
	public void onClose(Session session, CloseReason closeReason) {
		
	}
	
	
	public static void main(String[] args) {
		createAndShowGUI(); 
		
	}
	
	
	private static void createAndShowGUI(){
		//basic GUI setup
        JFrame frame = new JFrame("Alien Invaders");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        
        // bare bones: just add a panel where the game objects are drawn
        
        GamePanel gamePanel = new GamePanel();
        frame.add(gamePanel, BorderLayout.CENTER);

        frame.setSize(WIDTH, HEIGHT);
        frame.setVisible(true);
        
        
    }
		
}
