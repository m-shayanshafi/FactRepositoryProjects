package kw.texasholdem.util;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.imageio.ImageIO;

import kw.texasholdem.tool.Card;

/**
 * Utility class responsible for retrieving resource files.
 * 
 * @author Ken Wu
 */
public abstract class ResourceManager {
    
    private static final String IMAGE_PATH_FORMAT = "/images/card_%s.png"; 
    
    public static ImageIcon getCardImage(Card card) {
        // Use image order, which is different from value order.
        int sequenceNr = card.getSuit() * Card.NO_OF_RANKS + card.getRank();
        String sequenceNrString = String.valueOf(sequenceNr);
        if (sequenceNrString.length() == 1) {
            sequenceNrString = "0" + sequenceNrString;
        }
        String path = String.format(IMAGE_PATH_FORMAT, sequenceNrString);
        return getIcon(path);
    }

    
    public static ImageIcon getIcon(String path) {
        URL url = ResourceManager.class.getResource(path);
        if (url != null) {
            return new ImageIcon(url);
        } else {
            throw new RuntimeException("Resource file not found: " + path);
        }
    }
    
    public static Image getImage(String path) {
        
    	Image img = null;
		try {
			img = ImageIO.read(ResourceManager.class.getResource(path));
		} catch (IOException e) {
			throw new RuntimeException("Resource file not found: " + path);
		}
    	return img;
        
    }

    
}
