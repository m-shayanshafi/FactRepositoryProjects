package kabalpackage.utilities;

import java.awt.*;
import java.awt.image.*;
import javax.imageio.ImageIO;
import java.awt.image.VolatileImage;
import java.io.*;
import kabalpackage.*;

/**
 * VolatileImageLoader does as the name implies. It loads a picture file
 * into a BufferedImage which it then draws onto a new VolatileImage. Only
 * contains static methods. We use VolatileImages as they are hardware
 * accelerated by default.
 */
public class VolatileImageLoader {
    
    /**
     * Creates an empty VolatileImage with the given size and transparency
     *
     * @param width Width of the image
     * @param height Height of the image
     * @param transparency The transparency of the image
     */
    public static VolatileImage createVolatileImage(int width,
            int height, int transparency) {	
        
	GraphicsEnvironment ge = 
                GraphicsEnvironment.getLocalGraphicsEnvironment();
	GraphicsConfiguration gc = 
                ge.getDefaultScreenDevice().getDefaultConfiguration();
	VolatileImage image = null;
	Graphics2D g = null;
	
	image = gc.createCompatibleVolatileImage(width, height, transparency);
 
	int valid = image.validate(gc);
	
	if (valid == VolatileImage.IMAGE_INCOMPATIBLE) {
		image = gc.createCompatibleVolatileImage(width, height,
                        transparency);
		return image;
                
	}
	
	return image;
    }
    
    
    /**
     * The method you call when you want to create a new VolatileImage 
     * from file.
     *
     * @param picStream The InputStream for the image file
     * @param transparency The Transparency of the new image
     */
    public static VolatileImage loadFromFile(InputStream picStream, 
            int transparency) throws IOException{
        
	GraphicsEnvironment ge = 
                GraphicsEnvironment.getLocalGraphicsEnvironment();
	GraphicsConfiguration gc = 
                ge.getDefaultScreenDevice().getDefaultConfiguration();
	
	// Loads the image from a file using ImageIO.
	BufferedImage bimage = ImageIO.read( picStream );
	
	// Creates the volatile image
	VolatileImage vimage = createVolatileImage(bimage.getWidth(),
                bimage.getHeight(), transparency);
	
	Graphics2D g = null;
	try {
		g = vimage.createGraphics();
		g.drawImage(bimage,null,0,0);
	} finally {	
		g.dispose();
	}
	
	return vimage;
    }
    
    /**
     * Creates a VolatileImage from a BufferedImage.
     *
     * @param image The image you want to convert
     * @param transparency The desired transparency of the image
     */
    public static VolatileImage loadFromBufferedImage(BufferedImage image,
            int transparency) throws IOException{
        
	GraphicsEnvironment ge = 
                GraphicsEnvironment.getLocalGraphicsEnvironment();
	GraphicsConfiguration gc = 
                ge.getDefaultScreenDevice().getDefaultConfiguration();
	
	// Loads the image from a file using ImageIO.
	BufferedImage bimage = image;
	
	// Creates the volatile image
	VolatileImage vimage = createVolatileImage(bimage.getWidth(),
                bimage.getHeight(), transparency);
	
	Graphics2D g = null;
	try {
		g = vimage.createGraphics();
                g.setComposite(AlphaComposite.Src);
		g.drawImage(bimage,null,0,0);
	} finally {	
		g.dispose();
	}
	
	return vimage;
    }    
}
