package shooterClient;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CountDownLatch;
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


@ClientEndpoint(decoders = { ObjectToDrawDecoder.class }, encoders = { ObjectToDrawEncoder.class })
public class ShooterClientGUI implements ActionListener, KeyListener {
	
	final static int HEIGHT = 500; 
	final static int WIDTH  = 500; 
	
	private static GamePanel gamePanel;
	
	//private static CountDownLatch latch;
	private Logger logger = Logger.getLogger(this.getClass().getName());


	@OnOpen
	public void onOpen(Session session) { 
		logger.info("Connected ... " + session.getId());
		System.out.println("in onOpen");
		
		try {
			createAndShowGUI(session);
			session.getBasicRemote().sendText("start");
			System.out.println("sending start to server");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
	}
	
	@OnMessage
	public void onMessage(Session session, ObjectToDraw object) {
		
		logger.info("Received ... " + object.toString());
		
		gamePanel.receiveObjectToDraw(object);
		
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
	
	@OnClose
	public void onClose(Session session, CloseReason closeReason) {
		logger.info(String.format("Session %s close because of %s",
				session.getId(), closeReason));
	//	latch.countDown();	
	}
	
	
	public static void main(String[] args) {
//		
//		latch = new CountDownLatch(1);
//
		Session peer;
		ClientManager client = ClientManager.createClient();
		
		try {
			peer = client.connectToServer(ShooterClientGUI.class, new URI(
					"ws://localhost:8025/websockets/shooter"));
			//createAndShowGUI(peer);
			//latch.await();

		} catch (DeploymentException | URISyntaxException
				/*| InterruptedException*/ | IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	private static void createAndShowGUI(Session session){
		//basic GUI setup
        JFrame frame = new JFrame("Alien Invaders");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        
        // bare bones: just add a panel where the game objects are drawn
        
        gamePanel = new GamePanel(session);
        frame.add(gamePanel, BorderLayout.CENTER);

        frame.setSize(WIDTH, HEIGHT);
        frame.setVisible(true);
        
        
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * CHANGED
	 * @param e accepts a KeyEvent coming from a left or right cursor arrows
	 * Moves the ship left or right depending on user input
	 */
	@Override
	public void keyPressed(KeyEvent e){
		if(e.getKeyCode()==KeyEvent.VK_RIGHT)
		
		if(e.getKeyCode()==KeyEvent.VK_LEFT);
	}
	/**
	 * @param e is the key released event that will call the shoot method of the player's ship
	 * if the key released was cursor up.
	 */
	@Override
	public void keyReleased(KeyEvent e){
		if(e.getKeyCode()==KeyEvent.VK_UP);
	}
	
	//method not used. implementation forced by the interface. 
	@Override
	public void keyTyped(KeyEvent e){}
}
