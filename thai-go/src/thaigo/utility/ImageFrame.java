package thaigo.utility;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JWindow;

/** A Frame for a image.
 * 
 * @author Nol
 *
 */
public class ImageFrame extends JWindow {

	JLabel imageHolder;
	
	/** Loads a new image and add it to the frame.
	 * 
	 * @param path Path of the image
	 * @param imgWidth Width of image
	 * @param imgHeight Height of image
	 */
	public ImageFrame(String path, int imgWidth, int imgHeight){
		super();
		ImageIcon icon = (new ImageLoader(path, imgWidth, imgHeight)).getImageIcon();
		imageHolder = new JLabel( icon );
		add(imageHolder);
	}
	
	/** Gets the label that hold the image.
	 * 
	 * @return Label that contains the image 
	 */
	public JLabel getImageLabel(){
		return imageHolder;
	}
}
