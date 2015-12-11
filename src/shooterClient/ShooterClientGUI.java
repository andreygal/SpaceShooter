package shooterClient;
/**
 * @author Andrey Galper
 * This class combines a GUI  and a client 
 * endpoint for the Alien Invaders game
 */
import java.awt.BorderLayout;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.DeploymentException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

import org.glassfish.tyrus.client.ClientManager;

import messages.ObjectToDraw;
import messages.ObjectToDrawDecoder;
import messages.ObjectToDrawEncoder;


@ClientEndpoint(decoders = { ObjectToDrawDecoder.class }, 
				encoders = { ObjectToDrawEncoder.class })
public class ShooterClientGUI {
	/**
	 * Used to determine height of the panel. 
	 * Must be the same value for both the client 
	 * and the server.
	 */
	final static int HEIGHT = 500; 
	/**
	 * Used to determine width of the panel. 
	 * Must be the same value for both the client 
	 * and the server.
	 */
	final static int WIDTH  = 500; 
	/**
	 * Holds the main game panel, which is responsible for rendering.
	 */
	private static GamePanel gamePanel;
	/**
	 *Used to track communication between the server and the client. 
	 */
	private Logger logger = Logger.getLogger(this.getClass().getName());

	/**
	 * The method detects when a connection with the server has
	 * been established and in response creates a GUI 
	 * and sends a "start" signal to begin the game.
	 * @param session holds the connection to the server.
	 */
	@OnOpen
	public void onOpen(Session session) { 
		//validate the connection and output to console 
		logger.info("Connected ... " + session.getId());
		System.out.println("in onOpen");
		//send a message to the server signaling the client is ready to start the game
		try {
			createAndShowGUI(session);
			session.getBasicRemote().sendText("start");
			System.out.println("sending start to server");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	/**
	 * This method passes an object representing a player,
	 * enemy, or a bullet to the drawing panel of the client.
	 * @param session is the current active session
	 * @param object  is the object to be rendered 
	 */
	@OnMessage
	public void onMessage(Session session, ObjectToDraw object) {
		
		logger.info("Received ... " + object.toString());
		//receive the object to be rendered and pass it to gamePanel
		System.out.println(object.getType() + " " + 
				object.getImageID() + " " + object.getObjectID() + " " + object.getObjectPosition());
		gamePanel.receiveObjectToDraw(object);
		
	}
	
	/**
	 * Gracefully close the connection. Still working on this method.
	 * @param session the current active session.
	 * @param closeReason 
	 */
	@OnClose
	public void onClose(Session session, CloseReason closeReason) {
		logger.info(String.format("Session %s close because of %s",
				session.getId(), closeReason));
	}
	
	//connect to the client and start rendering continuously 
	public static void main(String[] args) {
		//establishes a connection with the game server.
		ClientManager client = ClientManager.createClient();
		try {
					client.connectToServer(ShooterClientGUI.class, new URI(
					"ws://localhost:8025/websockets/shooter"));
		} catch (DeploymentException | URISyntaxException
				/*| InterruptedException*/ | IOException e) {
			throw new RuntimeException(e);
		}
		//starts the persistent rendering methdod
		gamePanel.drawAll();
	}
	
	/**
	 * Creates a simple GUI at the center of which is the 
	 * main rendering panel - GamePanel
	 * @param session provides a way for the panel to 
	 * communicate back to the game server. 
	 */
	private static void createAndShowGUI(Session session){
		//basic GUI setup
        JFrame frame = new JFrame("Alien Invaders Online");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        // bare bones: just add a panel where the game objects are drawn
        // and pass it a connection to the server
        gamePanel = new GamePanel(session);
        frame.add(gamePanel, BorderLayout.CENTER);
        frame.setSize(WIDTH, HEIGHT);
        frame.setVisible(true);
    }
	
}


	