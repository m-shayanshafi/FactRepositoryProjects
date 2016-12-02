package kabalpackage.utilities;

import java.io.*;
import java.awt.*;
import java.awt.image.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import kabalpackage.*;

/**
 * Generates pictures for all the cards. Extracts single card images from
 * a large image containing all the cards.
 */
public class CardImageMaker extends JPanel {    

    //The image containing all the cards.
    private BufferedImage cardsBuffer;
    //The card width.
    private int x;
    // The card height.
    private int y;    

    /**
     * Creates new instance of CardImageMaker.
     *
     * @param cardsImage The filename of the image we wish to use.
     * @param x  The width of a single card.
     * @param y  The height of a single card.
     */
    public CardImageMaker(String cardsImage, int x, int y)  throws IOException{
        
	this.x = x;
	this.y = y;       

        cardsBuffer = ImageIO.read(getClass().getResourceAsStream("images/"
                + cardsImage));
    }    

    /**
     * Returns a BufferedImage of the back of the cards. Useful only for the
     * Deck class.
     */
    public BufferedImage getCardBack(){
        return cardsBuffer.getSubimage(2*x, 4*y, x, y);
    }   

    /**
     * Returns a BufferedImage of the requested card.
     *
     * @param type The type of card (clubs, diamonds, hearts, spades) we want.
     * @param number The number value of the card we want.
     */
    public BufferedImage cropToCard(String type, int number){
	BufferedImage ret = new BufferedImage(x,y, BufferedImage.TRANSLUCENT);

	if(type.equals("clubs")) ret = cardsBuffer.getSubimage(number*x,0,x,y);
	if(type.equals("diamonds")) ret = cardsBuffer.getSubimage(number*x,y,x,y);
	if(type.equals("hearts")) ret = cardsBuffer.getSubimage(number*x,2*y,x,y);
	if(type.equals("spades")) ret = cardsBuffer.getSubimage(number*x,3*y,x,y);
	if(type.equals("back")) ret = cardsBuffer.getSubimage(number*x,4*y,x,y);	

	return ret;
    }

    public void changeImage(String pic) {
        try{
            cardsBuffer = null;
            cardsBuffer = ImageIO.read( getClass().getResourceAsStream("images/" + pic) );
        }
        catch(Exception e){
            System.err.println("FUCK!");
        }
    }
}

