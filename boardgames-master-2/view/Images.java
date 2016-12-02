package view;

import java.awt.Image;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class Images {
	
//	public static final Image BACKGROUND = createImage("Images/background.gif");
	public static final Image PIECE_0 = createImage("Images/0.gif");
	public static final Image PIECE_1 = createImage("Images/1.gif");
	public static final Image PIECE_2 = createImage("Images/2.gif");
	public static final Image PIECE_3 = createImage("Images/3.gif");
	public static final Image PIECE_4 = createImage("Images/4.gif");
	public static final Image PIECE_5 = createImage("Images/5.gif");
	public static final Image PIECE_6 = createImage("Images/6.gif");
	public static final Image PIECE_7 = createImage("Images/7.gif");
	public static final Image PIECE_8 = createImage("Images/8.gif");
	public static final Image PIECE_9 = createImage("Images/9.gif");
	public static final Image PIECE_10 = createImage("Images/10.gif");
	public static final Image PIECE_11 = createImage("Images/11.gif");
	public static final Image PIECE_12 = createImage("Images/12.gif");
	public static final Image PIECE_13 = createImage("Images/13.gif");
	public static final Image PIECE_14 = createImage("Images/14.gif");
	public static final Image PIECE_15 = createImage("Images/15.gif");
	
	public static final Image SENET_1 = createImage("Images/Senet_1.png");
	public static final Image SENET_1_CLICKED = createImage("Images/Senet_1_clicked.png");
	public static final Image SENET_2 = createImage("Images/Senet_2.png");
	public static final Image SENET_2_CLICKED = createImage("Images/Senet_2_clicked.png");
	public static final Image GO = createImage("Images/go.png");
	public static final Image PASS = createImage("Images/pass.png");
	public static final Image OUT = createImage("Images/out.png");
	public static final Image HOUSE = createImage("Images/house.png");
	public static final Image UP = createImage("Images/up.png");
	public static final Image DOWN = createImage("Images/down.png");
	public static final Image ROLL_0 = createImage("Images/roll_0.png");
	public static final Image ROLL_1 = createImage("Images/roll_1.png");
	public static final Image ROLL_2 = createImage("Images/roll_2.png");
	public static final Image ROLL_3 = createImage("Images/roll_3.png");
	public static final Image ROLL_4 = createImage("Images/roll_4.png");
	public static final Image ROLL_5 = createImage("Images/roll_5.png");
	
	public static final Image BALL = createImage("Images/ball.png");
	public static final Image CCP_1 = createImage("Images/piece1.png");
	public static final Image CCP_2 = createImage("Images/piece2.png");
	public static final Image CCP_1_SELECTED = createImage("Images/piece1_clicked.png");
	public static final Image CCP_2_SELECTED = createImage("Images/piece2_clicked.png");
	
	public static final ImageIcon ICON_MANCALA = createImageIcon("Images/mancala.png");
	public static final ImageIcon ICON_SENET = createImageIcon("Images/senet.png");
	public static final ImageIcon ICON_CC = createImageIcon("Images/cc.png");
	
	
	/**
	 * Returns the image from the image file.
	 * @param path the relative path of the file
	 * @return the Image object
	 */
	private static Image createImage(String path) {
		Image image = null;
		try {
			URL imgURL = UI.class.getResource(path);
			image = ImageIO.read(imgURL);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return image;
	}

	/**
	 * Returns the image icon from the image file
	 * @param path the relative path of the file
	 * @return the icon object
	 */
	private static ImageIcon createImageIcon(String path){
		ImageIcon icon = null;
		try{
			URL imgURL = UI.class.getResource(path);
			icon = new ImageIcon(imgURL);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return icon;
	}
}


