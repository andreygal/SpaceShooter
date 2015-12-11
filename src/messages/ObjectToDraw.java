package messages;

import java.awt.Point;

public class ObjectToDraw {
		
		private String type; 
		private int imageID; 
		private int objectID; 
		private Point objectPosition; 
		
		ObjectToDraw(String type, int imageID) {
			this.type = type; 
			this.imageID = imageID;
		}

		public ObjectToDraw(String type, int imageID, Point objectPosition){
			this(type, imageID); 
			this.objectPosition = objectPosition; 
		}
		//this. in used for clarity in this case
		public ObjectToDraw(String type, int imageID, int x, int y) {
			this(type, imageID);
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
		
		public boolean equals (ObjectToDraw object) {
			if(this.objectID==object.objectID)
				return true; 
			else
				return false; 
		}
}
