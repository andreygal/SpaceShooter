package messages;
/**
 * @author Andrey Galper
 * This class represents a watered down version of the original game object.
 * It's use is to minimize the amount of data sent from the server to the clients.
 * e.g. Images are not sent through the link
 * The four elements the client needs in order to render the game are:
 * 1. type (Enemy, player, bullet), 
 * 2. imageID (an index of the array where the 
 * 	  ImageProcessor keeps the respective image. 
 * 3. obectID a number that uniquely identifies this object.
 * 	  players have single digit IDs, enemies double digit and bullet triple digit.
 * 4. objectPosition where the panel needs to draw this object
 * respective 
 */

import java.awt.Point;

public class ObjectToDraw {
		
		private String type; 
		private int imageID; 
		private int objectID; 
		private Point objectPosition; 
		
		ObjectToDraw(String type, int imageID, int objectID) {
			this.type = type; 
			this.imageID = imageID;
			this.objectID = objectID; 
		}

		public ObjectToDraw(String type, int imageID, Point objectPosition, int objectID){
			this(type, imageID, objectID); 
			this.objectPosition = objectPosition; 
		}
		//this. in used for clarity in this case
		public ObjectToDraw(String type, int imageID, int x, int y) {
			this(type, imageID, -1);
			this.objectPosition = new Point(x,y);
		}
		
		public ObjectToDraw(String type, int imageID, int x, int y, int objectID) {
			this(type, imageID, objectID);
			this.objectPosition = new Point(x,y);
		}

		public String getType() {
			return type; 
		}
		
		public int getImageID() {
			return imageID; 
		}
		
		public Point getObjectPosition() {
			return objectPosition; 
		}
		
		public void setType(String type) {
			this.type = type; 
		}
		
		public void setImageId(int imageID) {
			this.imageID = imageID; 
		}
		
		public void setObjectPosition(Point objectPosition) {
			this.objectPosition = objectPosition; 
		}
		
		public void setObjectID(int objectID) {
			this.objectID = objectID; 
		}
		
		public int getObjectID() {
			return objectID; 
		}
		//objects are equal only if their ID numbers match 
		public boolean equals (ObjectToDraw object) {
			if(this.objectID==object.objectID)
				return true; 
			else
				return false; 
		}
}
