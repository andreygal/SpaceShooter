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
import java.awt.Graphics;
import javax.swing.JPanel;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedHashSet;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.websocket.Session;

import messages.ObjectToDraw;
import shooterServer.ImageProcessor;

public class GamePanel extends JPanel implements KeyListener {
        
		/**checks if one of the end game conditions has been met*/
        private boolean isRunning; 
        /**buffer for objects to be rendered*, object received first are guaranteed to be drawn first*/
        ConcurrentLinkedQueue<ObjectToDraw> buffer;
        /**Class responsible for processing images on client and server sides. */
        ImageProcessor imageProcessor; 
        /**Session object used to communicate with the server*/ 
        Session session; 
        /**Stores objects passed by the server. Updates them on as needed basis*/ 
        private ArrayList<ObjectToDraw> storage;
        /**A linked hash set to keep track of unique game elements. due to its nature will not allow duplicates*/
        private LinkedHashSet<Integer> IDset;
        /**The frame rate of the game*/
        private int fps; 
        
        //GamePanel constructor. SetFocusable has to be set to true for the panel to respond to player's input.
        public GamePanel (Session session) {
        		this.session = session; 
        		this.imageProcessor = new ImageProcessor(); 
                isRunning = true;   //the game is running
                fps = 60; //set frames second 
                
                setBackground(Color.BLACK);
                setFocusable(true);
                
                buffer = new ConcurrentLinkedQueue<ObjectToDraw>(); 
                storage = new ArrayList<ObjectToDraw>();
                IDset = new LinkedHashSet<Integer>(); 
                
                System.out.println("Panel Created");
        }
        /**
    	 * CHANGED
    	 * @param g Graphics component object received from the JVM.  
    	 * Method renders one "frame" of the animation.
    	 */
        @Override
        public synchronized void paintComponent(Graphics g) {
        	super.paintComponent(g);
        	System.out.println("Drawing objects using paintComponent");
        	drawObjects(g);
        }
        /**
         * This method is responsible for receiving the decoded 
         * message object (ObjectToDraw) from the onMessage method
         * of the ShooterClientGUI 
         * @param object is the a decoded message object 
         */
        public synchronized void receiveObjectToDraw(ObjectToDraw incomingObject) {

        	System.out.println("Panel is receiving an object");
        	
        	if (!IDset.contains(incomingObject.getObjectID())) {
        		//if the panel does not contain the object, add it
        		System.out.println("Adding object to panel storage");
        		System.out.println(incomingObject.getType() + " " +
                                   incomingObject.getImageID() + " " + 
                                   incomingObject.getObjectID() + " " +
                                   incomingObject.getObjectPosition());
        		storage.add(incomingObject);
        		IDset.add(incomingObject.getObjectID());
        	} else if (!storage.isEmpty()) {
        		//find the object by ID and update it
        		for(ObjectToDraw storedObject : storage) {
        			//make sure the object IDs match and update
        			if(storedObject.equals(incomingObject))
        			storedObject.setObjectPosition(incomingObject.getObjectPosition());
        		}
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
    	/**
    	 * Calculates the pause needed to achieve the desired frame rate
    	 * and calls paint component at that frequency 
    	 */
    	public void drawAll() {
    		long currTime, prevTime = System.nanoTime();
    		double dt;
    		
    		while(isRunning) {
    			//calculating delta time (time between frames)
				//1.0e9 since nanoTime() returns nanoseconds that we need to convert to seconds
				currTime = System.nanoTime();
				dt = (currTime - prevTime) / 1.0e9;
				prevTime = currTime;
    			repaint(); 
    			// if delta time is less than some target FPS (30 in this case)
				// then sleep the current thread for remaining time this frame
				if( dt < 1.0/fps)
					try	{
						Thread.currentThread();
						Thread.sleep((long)((1.0/fps - dt) * 1000));
					} catch (InterruptedException e) {}
    		}
    	}
    	/**
    	 * This method is called by paint component to draw  
    	 * all currently active objects
    	 * @param g a graphics object passed from paint comp.
    	 */
    	public synchronized void drawObjects(Graphics g) {
    		String type; 
        	Point position; 	
        	int imageID;

        	//if there are objects to be drawn, draw them according to type
        	if(!storage.isEmpty()) {
        		for(ObjectToDraw object : storage) {
        			type     = object.getType(); 
        			imageID  = object.getImageID(); 
        			position = object.getObjectPosition(); 

        			switch (type) {

        			case "shooterServer.PlayerShip":
        				System.out.println(object.getObjectPosition() + " " + object.getType());
        				g.drawImage(ImageProcessor.PlayerShip[imageID], position.x, position.y, null);
        				break;

        			case "shooterServer.EnemyShip": 
        				System.out.println(object.getObjectPosition() + " " + object.getType());
        				g.drawImage(ImageProcessor.EnemyShip[imageID], position.x, position.y, null);
        				break;

        			case "shooterServer.Bullet":
        				g.drawImage(ImageProcessor.Bullet[imageID], position.x, position.y, null);
        				break;
        			} 
        		}
        	}
    	}
}

