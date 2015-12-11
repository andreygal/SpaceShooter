package shooterServer;
/**
 * @author Andrey Galper 
 * The ImageProcessor class is responsible for handling the game's 
 * graphical resources. It reaches into directories specified by File
 * objects, extracts the images, resizes them to fit the game window,
 * and obtain's the dimensions of each image to be used later by 
 * the GameLauncher class.  
 */

import java.awt.Dimension;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageProcessor {
	//to store the images
	private File players, enemies, bullets; 
	public static Image[] PlayerShip; 
	public static Image[] EnemyShip; 
	public static Image[] Bullet; 

	//constructor initializes the respective arrays 	
	public ImageProcessor(){
		players = new File("././resource/Players");
		enemies = new File("././resource/Enemies");
		bullets = new File("././resource/Bullets");

		// declare arrays to hold resized sprites
		PlayerShip = getResizedImages(players);
		EnemyShip  = getResizedImages(enemies);
		Bullet     = getResizedImages(bullets);
	}


	//test loop
	//		for (int i = 0; i < PlayerShip.length; i++) {
	//			System.out.println(PlayerShip[i]);
	//		}
	/**
	 * Obtains image file from a given directory 
	 * @param imageDir is the directory holding the images 
	 * @return an array holding resized images for use by the game
	 */
	public static Image[] getResizedImages(File imageDir) {
		// resize images and store them in respective arrays
		File[]  imageList = imageDir.listFiles();
		Image[] images = new Image[imageList.length];
		int     numOfSprites = imageList.length;
		
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
	/**
	 *	Calculates the dimensions of images in a given array of resized sprites.
	 *	Image is linked to its dimensions via the array index 
	 * @param resizedImages
	 * @return an array containing dimensions.
	 */
	public static Dimension[] getImageDimensions(Image[] resizedImages){

		Dimension[] dimensions = new Dimension[resizedImages.length]; 

		for(int i=0; i<resizedImages.length; i++){
			dimensions[i] = new Dimension(resizedImages[i].getWidth(null), resizedImages[i].getHeight(null));
		}
		
		return dimensions; 
	}



}


