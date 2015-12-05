package shooterClient;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel{
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
                player = new PlayersShip(5, "playerShip1_blue.png", gpInsets.bottom, 5);
                setFocusable(true);
        }
       
     
}