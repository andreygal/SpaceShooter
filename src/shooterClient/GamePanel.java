package shooterClient;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Random;
import java.util.Stack;

import javax.swing.JPanel;
import javax.swing.Timer;

import messages.ObjectToDraw;
import shooterServer.ImageProcessor;

public class GamePanel extends JPanel implements KeyListener{
        /** Stores the insets for the frame. Used for correcting the coordinates of the player's ship.*/
        private Insets gpInsets; 
        /**stores the end game message*/
        private String message = ""; 
        /**checks if one of the end game conditions has been met*/
        private boolean isRunning; 
        /**buffer for objects to be rendered*/
        Stack<ObjectToDraw> buffer = new Stack<ObjectToDraw>();
        
        //GamePanel constructor. SetFocusable has to be set to true for the panel to respond to player's input.
        public GamePanel () {
                isRunning = true; 
                gpInsets = getInsets(); 
                setBackground(Color.BLACK);
                setFocusable(true);
        }
      
        /**
    	 * CHANGED
    	 * @param g Graphics component object received from the JVM.  
    	 * Method renders one "frame" of the animation
    	 */
        @Override
        public void paintComponent(Graphics g) {
        	super.paintComponent(g);
        	//if there are player's ships in space, render the game.
        	ObjectToDraw object;

        	String type; 
        	int imageID;
        	Point position; 

        	if(isRunning){
        		while(!buffer.isEmpty()){

        			object   = buffer.pop();	
        			type     = object.getType(); 
        			imageID  = object.getImageID(); 
        			position = object.getObjectPosition(); 

        			switch(type) {

        			case "PlayerShip":
        				g.drawImage(ImageProcessor.PlayerShip[imageID], position.x, position.y, null);
        				break;

        			case "EnemyShip": 
        				g.drawImage(ImageProcessor.EnemyShip[imageID], position.x, position.y, null);
        				break;

        			case "Bullet":
        				g.drawImage(ImageProcessor.Bullet[imageID], position.x, position.y, null);
        				break; 

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
    	
    	
    	public void receiveObjectToDraw(ObjectToDraw object){
    		if(object.getType()=="terminator")
    			repaint(); 
    		else
    			buffer.push(object); 
    	}
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
}