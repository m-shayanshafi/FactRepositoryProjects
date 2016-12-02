package thaigo.utility;

import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;

/**
 * Utility class for creating image, supports file in jar package.
 * 
 * @author Nol Pasurapunya 5510546018
 * @version 2013.04.19
 *
 */
public class ImageLoader {
	
	/** Image dump. */
	private ImageIcon icon;
	
	/**
	 * Constructor for making image object by getting path of image file.
	 * @param path path of image file
	 */
	public ImageLoader(String path) {
		this(path ,40, 40);
	}
	
	/**
	 * Constructor for making image object by getting path of image file
	 * and assign size of image.
	 * @param path path of image file
	 * @param width width of image object
	 * @param height height of image object
	 */
	public ImageLoader(String path, int width, int height) {
		ClassLoader loader = this.getClass().getClassLoader();
		URL url = loader.getResource(path);
		icon = new ImageIcon(url);
		Image img = icon.getImage() ;  
		Image newimg = img.getScaledInstance( width, height, Image.SCALE_SMOOTH ) ;  
		icon = new ImageIcon( newimg );
	}
	
	/** 
	 * Get image as <code>ImageIcon</code>.
	 * @return image as <code>ImageIcon</code>
	 */
	public ImageIcon getImageIcon() {
		return icon;
	}
	
	/** 
	 * Get image as <code>Image</code>.
	 * @return image as <code>Image</code>
	 */
	public Image getImage(){
		return icon.getImage();
	}
}
