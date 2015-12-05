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

import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel implements KeyListener{
        /** Stores the insets for the frame. Used when calculating the starting position of the player's ship.*/
        private Insets gpInsets; 
        /**stores the end game message*/
        private String message = ""; 
        /**checks if one of the end game conditions has been met*/
        private boolean isRunning; 
        
        
        //GamePanel constructor. SetFocusable has to be set to true for the panel to respond to player's input.
        public GamePanel () {
                isRunning = true; 
                setBackground(Color.BLACK);
                gpInsets = getInsets(); 
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
    		//if a player's ship was not destroyed, draw it on the screen.
    		if(isRunning){
    			g.drawImage(player.resizedSprite, player.objectPosition.x, player.objectPosition.y, null);
    			
    			for(int i=0; i<5; i++){
    				for(int j=0; j<7; j++){
            			g.drawImage(enemies[i][j].getImage(), enemies[i][j].objectPosition.x, enemies[i][j].objectPosition.y, null);
    				}
    			}
    			
    			for(Bullet i: bullets){
    				g.drawImage(i.resizedSprite, i.objectPosition.x, i.objectPosition.y, null);
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
}