package messages;

import java.awt.Point;

public class ObjectToDraw {
		
		private String type; 
		private int imageID; 
		private Point objectPosition; 
		
		//constructor 
		ObjectToDraw(String type, int imageID, Point objectPosition){
			this.type = type; 
			this.imageID = imageID; 
			this.objectPosition = objectPosition; 
		}
		
		public String getType(){
			return type; 
		}
		
		public int getImageID(){
			return imageID; 
		}
		
		public Point getObjectPosition(){
			return objectPosition; 
		}
		
		public void setType(String type){
			this.type = type; 
		}
		
		public void setImageId(int imageID){
			this.imageID = imageID; 
		}
		
		public void setObjectPosition(Point objectPosition){
			this.objectPosition = objectPosition; 
		}
}
