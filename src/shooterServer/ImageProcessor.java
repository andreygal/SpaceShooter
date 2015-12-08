package shooterServer;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageProcessor {
	
		private File players, enemies, bullets; 
		public static Image[] PlayerShip; 
		public static Image[] EnemyShip; 
		public static Image[] Bullet; 

		
	ImageProcessor(){
		players = new File("././resource/Players");
		enemies = new File("././resource/Enemies");
		bullets = new File("././resource/Bullets");

		// declare arrays to hold resized sprites
		PlayerShip = getResizedImages(players);
		EnemyShip  = getResizedImages(enemies);
		Bullet     = getResizedImages(bullets);
		}
		

		//for testing
//		for (int i = 0; i < PlayerShip.length; i++) {
//			System.out.println(PlayerShip[i]);
//		}

	public static Image[] getResizedImages(File imageDir) {
		// resize images and store them in respective arrays
		File[] imageList = imageDir.listFiles();
		Image[] images = new Image[imageList.length];
		int numOfSprites = imageList.length;
		System.out.println(numOfSprites);

		if (numOfSprites != 0) {
			for (int i = 0; i < numOfSprites; i++) {
				Image sprite = null;
				try {
					sprite = ImageIO.read(imageList[i]);
				} catch (IOException e) {
					e.getStackTrace();
					System.out.println("error: please provide a valid image file for the game object.");
				}
				// resize to 1/4 of the original area. limited the resizing to
				// the source constructor.
				int resizedspritewidth = sprite.getWidth(null) / 2;
				int resizedspriteheight = sprite.getHeight(null) / 2;
				images[i] = sprite.getScaledInstance((resizedspritewidth), (resizedspriteheight), Image.SCALE_SMOOTH);
				System.out.println(imageList[i]);
			}
		}

		return images;
	}
	
	public static Dimension[] getImageDimensions(Image[] resizedImages){
		
		Dimension[] dimensions = new Dimension[resizedImages.length]; 
		
		for(int i=0; i<resizedImages.length; i++){
			dimensions[i] = new Dimension(resizedImages[i].getWidth(null), resizedImages[i].getHeight(null));
		}
		
		return dimensions; 
	}
	
	
	
}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	