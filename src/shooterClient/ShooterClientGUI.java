package shooterClient;

import java.awt.BorderLayout;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.websocket.Session;

import org.glassfish.tyrus.client.ClientManager; 

@ClientEndpoint()
public class ShooterClientGUI {
	final static int HEIGHT = 500; 
	final static int WIDTH  = 500; 
	
	private static GamePanel gamePanel; 

	public static void main(String[] args) {
		
		
	}
	
	
	private static void createAndShowGUI(Session session){
		//basic GUI setup
        JFrame frame = new JFrame("Alien Invaders");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        
        // bare bones: just add a panel where the game objects are drawn
        
        GamePanel gamePanel = new GamePanel();
        frame.add(gamePanel, BorderLayout.CENTER);

        frame.setSize(WIDTH, HEIGHT);
        frame.setVisible(true);
        
        // the game starts when the gamepanel animation begins
        
        gamePanel.go();
    }
		
}
