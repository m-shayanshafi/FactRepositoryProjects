package kabalpackage;

import java.io.IOException;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.JComponent;
import java.awt.geom.*;
import kabalpackage.utilities.VolatileImageLoader;

/**
 * The card class.
 */
public class Card extends JComponent {

    private BufferedImage image;
    private BufferedImage backImage = null; // The back of the card
    private BufferedImage turnedImage = null; // The picture side
    private BufferedImage overImage = null; // The mouse over image
    
    // The card type.
    private String type;
    // The card number value and its width and height.
    private int number, w, h;
    
    // Variables indicating whether or not the card is turned or highlighted
    private boolean IS_TURNED = false;
    private boolean HIGHLIGHTED = false;

    private VolatileImage vimage = null;
    private VolatileImage backVimImage = null;
    private VolatileImage turnedVimImage = null;
    private VolatileImage overVimImage = null;
    
    /**
     * Creates new instance of card.
     *
     * @param type Card type (hearts, diamond, clubs, spades)
     * @param number The value of the card where 1 is ace and 13 king
     * @param backImage The image we want to use as the back side
     * @param turnedImage The image we want to use as the picture side
     * @param overImage The image we want to use for mouse over action
     * @param w width of the card
     * @param h height of the card
     */
    public Card(String type, int number, BufferedImage backImage, 
            BufferedImage turnedImage, BufferedImage overImage, int w, int h){
	this.type = type;
	this.number = number;
	this.backImage = backImage;
        this.image = backImage;
	this.turnedImage = turnedImage;
        this.overImage = overImage;
	this.w = w;
	this.h = h;
        
        // Create the VolatileImages
        try{
            backVimImage = VolatileImageLoader.loadFromBufferedImage(
                    backImage, Transparency.TRANSLUCENT);
            vimage = VolatileImageLoader.loadFromBufferedImage(
                    backImage, Transparency.TRANSLUCENT);
            turnedVimImage = VolatileImageLoader.loadFromBufferedImage(
                    turnedImage, Transparency.TRANSLUCENT);
            overVimImage = VolatileImageLoader.loadFromBufferedImage(
                    overImage, Transparency.TRANSLUCENT);
        }
        catch(IOException ioe){
            System.err.println("Could not convert from BufferedImage " +
                    "to VolatileImage");
        }
    }
    
    public Card(VolatileImage turnedVimImage){
        this.vimage = turnedVimImage;
    }
    
    /**
     * Sets the card images to the desired images.
     */
    public void setImage(VolatileImage back, VolatileImage image, VolatileImage over){
        try{
            backVimImage = back;
            turnedVimImage = image;
            overVimImage = over;
            
            if(IS_TURNED) vimage = turnedVimImage;
            else vimage = backVimImage;
        }
        catch(Exception e){
            System.err.println("FUCK!!");
        }
        this.repaint();
    }
    
    /**
     * Makes a deep copy of the card in question. Used when we make a
     * temporary stack, so as to not having to worry about changing the
     * bounds/location of the cards when we put them into the temp stack and
     * then back into the source or destination stacks.
     */
    public Card makeCopy(){
        return new Card(vimage);
    }

    /**
     * Turns the card with the picture side up.
     */
    public void setTurned(){
	IS_TURNED = true;
	vimage = turnedVimImage;
    }
    
    /**
     * Defaces the card.
     */
    public void defaceCard(){
        IS_TURNED = false;
        vimage = backVimImage;
    }
    
    /**
     * (De)highligts the card.
     *
     * @param bool true = highlighted, false = normal
     */
    public void highlight(Boolean bool){
        if(bool){
            vimage = overVimImage;
            HIGHLIGHTED = true;
        }
        if(!bool){
            vimage = turnedVimImage;
            HIGHLIGHTED = false;
        }
        repaint();
    }
    
    /**
     * Returns whether or not the card has been highlighted.
     */
    public boolean isHighLighted(){
        return HIGHLIGHTED;
    }
    
    /**
     * Returns whether or not the card is turned.
     */
    public boolean isTurned(){
	return IS_TURNED;
    }
    
    /**
     * Returns a readable name for the card value. Used by the hint method
     * in GameArea.
     *
     * @see kabalpackage.GameArea#hint()
     */
    public String getName(){
        if(number == 1) return "ace";
        if(number == 13) return "king";
        if(number == 12) return "queen";
        if(number == 11) return "jack";
        else return "" + number;
    }

    /**
     *  Returns the card type.
     */
    public String getType(){
	return type;
    }
    
    /**
     * Returns the card value.
     */
    public int getNumber(){
	return number;
    }

    public String toString(){
	return getName() + " " + number;
    }
    

    
    /**
     * Paints the card image onto the component.
     */
    protected void paintComponent(Graphics graphics) {
        Graphics g = graphics.create();
	g.drawImage(vimage, 0, 0, null);        
        g.dispose();        
    }
    
}
