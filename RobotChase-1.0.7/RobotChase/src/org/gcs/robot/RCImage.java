package org.gcs.robot;

import java.awt.Image;
import java.awt.Color;
import java.awt.Toolkit;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 * The RCImage class supplies images to the {@link org.gcs.robot.RCView
 * RCView} view. Each set is a single instance containing the images used
 * in play. There are three sets, each having a common image type: png, gif
 * or jpg. The images are assumed to have a uniform aspect ratio, defined
 * in {@link org.gcs.robot.RCTile RCTile}.
 *
 * @see RCTile
 * @author John B. Matthews
 **/
public final class RCImage {
    /** A player's image */
    static Image Me;

    /** A blank background tile */
    static Image Blank;

    /** A robots' image */
    static Image Robot;

    /** A collision image */
    static Image Wreck;

    /** A decesaed player's image */
    static Image Dead;

    /** A bomb's image */
    static Image Bomb;

    /** An elctric post's image */
    static Image Post;

    /** Theme color */
    public static final Color borderColor = new Color(192, 0, 0);

    private static String current = RCPrefs.getTileSet();
    private static RCImage instance = new RCImage(current);

    /** private constructor */
    private RCImage (String suffix) {
       makeImageSet(suffix);
    };

    /** make a set of tile images. */
    private static void makeImageSet(String suffix) {
        Me    = getImage("/images/me" + suffix);
        Blank = getImage("/images/blank" + suffix);
        Robot = getImage("/images/robot" + suffix);
        Wreck = getImage("/images/wreck" + suffix);
        Dead  = getImage("/images/dead" + suffix);
        Bomb  = getImage("/images/bomb" + suffix);
        Post  = getImage("/images/post" + suffix);
    }

    /** Load a tile image by name. */
    private static Image getImage(String name) {
        // ClassLoader required for jar access
        URL file = RCImage.class.getResource(name);
        if (file != null)
            return Toolkit.getDefaultToolkit().createImage(file);
        else {
            Object [] messages = {"Unable to locate " + name + "!"};
            JOptionPane.showMessageDialog(null,
                messages, "I/O Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
        return null;
    }

    /** Return the current set of images. */
    public static RCImage getSet() { return instance; }
    
   /**
     * Create a scaled ImageIcon from the given image.
     * @param image the image to scale
     * @param w desired width
     * @param h desired height
     * @return a scaled ImageIcon
     */
    public static ImageIcon scale(Image image, int w, int h){
        Image scaled = image.getScaledInstance(w, h, Image.SCALE_DEFAULT);
        return new ImageIcon(scaled);
    }
    
    /** Return the next set of images in the series. */
    public static RCImage getNextSet() {
        if (".png".equals(current)) current = ".gif";
        else if (".gif".equals(current)) current = ".jpg";
        else if (".jpg".equals(current)) current = ".png";
        makeImageSet(current);
        RCPrefs.putTileSet(current);
        return instance;
    }

}