package shooterClient;
/**
 * @author Andrey Galper
 * This is the main rendering panel of the client side of the game.
 * Its task is to accept game objects from the server side, 
 * which is responsible for all the calculations, and render them
 * using paint component method. This panel is also responsible for
 * listening to key events that are used to control a player's ship. 
 */
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.swing.JPanel;
import javax.websocket.Session;

import messages.ObjectToDraw;
import shooterServer.ImageProcessor;

public class GamePanel extends JPanel implements KeyListener {
        /**stores the end game message*/
        private String message = ""; 
        /**checks if one of the end game conditions has been met*/
        private boolean isRunning; 
        /**buffer for objects to be rendered*/
        ConcurrentLinkedQueue<ObjectToDraw> buffer;
        /**Class responsible for processing images on client and server side. */
        ImageProcessor imageProcessor; 
        /**Session object used to communicate with the server*/ 
        Session session; 
       
        
        //GamePanel constructor. SetFocusable has to be set to true for the panel to respond to player's input.
        public GamePanel (Session session) {
        		this.session = session; 
        		this.imageProcessor = new ImageProcessor(); 
                isRunning = true;   //the game is running
                setBackground(Color.BLACK);
                setFocusable(true);
                buffer = new ConcurrentLinkedQueue<ObjectToDraw>(); 
                System.out.println("Panel Created");
        }
      
        /**
    	 * CHANGED
    	 * @param g Graphics component object received from the JVM.  
    	 * Method renders one "frame" of the animation.
    	 */
        @Override
        public void paintComponent(Graphics g) {
        	super.paintComponent(g);
        	System.out.println("Drawing objects using paintComp");
        	//Stores the object passed by the receiveObjects method of this panel.
        	ObjectToDraw object;
        	//Temporary storage for the ObjectToDraws data. 
        	String type; 
        	int imageID;
        	Point position; 
        	/*
        	 * If the game is running and there are left in the buffer to draw.
        	 * paint objects, based on their type, particular sprite and position on
        	 * the game board. The else clause is reserved for ending the game 
        	 * and displaying the end game message. 
        	 */
        	if(isRunning){
        		while(!buffer.isEmpty()){
        			//remove the object from the buffer 
        			object   = buffer.remove();	
        			type     = object.getType(); 
        			imageID  = object.getImageID(); 
        			position = object.getObjectPosition(); 

        			switch(type) {

        			case "shooterServer.PlayerShip":
        				g.drawImage(ImageProcessor.PlayerShip[imageID], position.x, position.y, null);
        				break;

        			case "shooterServer.EnemyShip": 
        				g.drawImage(ImageProcessor.EnemyShip[imageID], position.x, position.y, null);
        				break;

        			case "shooterServer.Bullet":
        				g.drawImage(ImageProcessor.Bullet[imageID], position.x, position.y, null);
        				break;
        				
        			case "terminator":
        				continue;

        			default:
        				System.out.println("Invalid image type");
        			}
        		} 
        	} else {
        			g.setColor(Color.BLUE);
        			g.setFont(new Font(null, Font.BOLD, 72));
        			g.drawString(message, 55, 200);
        	}
        }
        
        /**
         * This method is responsible for receiving the decoded 
         * message object (ObjectToDraw) from the onMessage method
         * of the ShooterClientGUI 
         * @param object is the a decoded message object 
         */
    	public void receiveObjectToDraw(ObjectToDraw object) {
    		System.out.println("Panel is receiving an object");
    		
    		if(object.getType().equals("terminator")){
    			System.out.println("terminator received");
    			repaint(); 
    		} else
    			System.out.println("Adding object to panel buffer");
    			System.out.println(object.getType() + " " + object.getImageID() + " " + object.getObjectPosition());
    			buffer.add(object); 
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
	
    	public void drawAll() {
    		
    	}

}