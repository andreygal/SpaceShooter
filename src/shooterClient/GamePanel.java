package shooterClient;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.swing.JPanel;
import javax.swing.Timer;
import javax.websocket.Session;

import messages.ObjectToDraw;
import shooterServer.ImageProcessor;

public class GamePanel extends JPanel implements ActionListener {
        /** Stores the insets for the frame. Used for correcting the coordinates of the player's ship.*/
        private Insets gpInsets; 
        /**stores the end game message*/
        private String message = ""; 
        /**checks if one of the end game conditions has been met*/
        private boolean isRunning; 
        /**buffer for objects to be rendered*/
        ConcurrentLinkedQueue<ObjectToDraw> buffer;
        
        ImageProcessor imageProcessor; 
        
        Session session; 
       
        static final int FRAME_RATE = 30; // animation proceeds at 30 frames per second
    	Timer t;	// animation timer
 
        
        
        
        //GamePanel constructor. SetFocusable has to be set to true for the panel to respond to player's input.
        public GamePanel (Session session) {
        	 t = new Timer(1000/FRAME_RATE, this);
        		this.session = session; 
        		this.imageProcessor = new ImageProcessor(); 
                isRunning = true; 
                gpInsets = getInsets(); 
                setBackground(Color.BLACK);
                //setFocusable(true);
                buffer = new ConcurrentLinkedQueue<ObjectToDraw>(); 
                System.out.println("Panel Created");
        }
      
        /**
    	 * CHANGED
    	 * @param g Graphics component object received from the JVM.  
    	 * Method renders one "frame" of the animation
    	 */
        @Override
        public void paintComponent(Graphics g) {
        	super.paintComponent(g);
        	System.out.println("Drawing objects using paintComp");
        	//if there are player's ships in space, render the game.
        	ObjectToDraw object;

        	String type; 
        	int imageID;
        	Point position; 

        	if(isRunning){
        		while(!buffer.isEmpty()){

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
    	
    	public void drawAll() {
    		
    	}

		@Override
		public void actionPerformed(ActionEvent e) {
			// if this is an event from the Timer, call the method that advances the animation
			if (e.getSource() == t) {
				tick();
			}
		}
    	
		 /* Make sure all GameObjects are in the right place (do any need to be removed? do we need to create any new ones?), then redraw game
		 */

		private void tick() {
			repaint();		// ask to have the game redrawn (this will invoke paintComponent() when the system says the time is right)
		}

		void go() {
			t.start();
		}	
    	
}