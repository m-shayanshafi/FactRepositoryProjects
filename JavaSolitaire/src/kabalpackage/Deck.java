package kabalpackage;
import java.awt.geom.Ellipse2D;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.image.*;
import java.util.ArrayList;
import java.util.Collections;
import kabalpackage.*;
import kabalpackage.utilities.CardImageMaker;
import kabalpackage.utilities.LayoutVariables;

/** 
 * The class responsible for creating all the card objects and holding the
 * remaining cards after the others have been placed in SolitaireStacks.
 */
public class Deck extends JLabel implements Serializable{
    
    // The imagemakers which are used to extract the card images
    private CardImageMaker cim;
    private CardImageMaker cim2;
    
    // The names of the card types.
    private final String[] types = {"hearts", "clubs", "diamonds", "spades"};
    // Array to contain the new cards.
    private Card[][] deck = new Card[4][13];
    // ArrayList for the new cards. For easy removal when added to stacks.
    private ArrayList<Card> deckArrayList = new ArrayList<Card>();
    
    // ArrayList with all cards, also those removed, for easy card pic changes
    private ArrayList<Card> allCards = new ArrayList<Card>();
    
    // Common layout variables used in this class
    private final int CARD_WIDTH = LayoutVariables.CARD_WIDTH;
    private final int CARD_HEIGHT = LayoutVariables.CARD_HEIGHT;
    private final int STACK_SPACING = LayoutVariables.STACK_SPACING;
    
    
    private BufferedImage[][] images = new BufferedImage[4][13];
    
    
    /**
     * Creates new instance of Deck with new cards.
     *
     * @param cim   The CardImageMaker for the regular card images
     * @param cim2  The CardImageMaker for the mouse over card images
     */
    public Deck(CardImageMaker cim, CardImageMaker cim2) {
        
        this.setBackground(null);
        this.setLayout(null);
	
	this.cim = cim;
        this.cim2 = cim2;
	
        
	BufferedImage image = cim.cropToCard("back", 2);        
	BufferedImage turnedImage;
        BufferedImage overImage;        
	String type = null;	
        // Create all the card objects
	for(int i=0; i<4; i++){
	    
	    if(i==0) type = "hearts";
	    if(i==1) type = "clubs";
	    if(i==2) type = "diamonds";
	    if(i==3) type = "spades";
	    
	    for(int j=0; j<13; j++){
		turnedImage = cim.cropToCard(type, j);
                overImage = cim2.cropToCard(type, j);
		deck[i][j] = new Card(types[i], j+1, image, turnedImage,
                        overImage, LayoutVariables.CARD_WIDTH,
                        LayoutVariables.CARD_HEIGHT);
	    }
	}
        
        // Move cards to ArrayList for easy removal and shuffling.
        for(int i=0; i<4; i++){
            for(int j=0; j<13; j++){
                deckArrayList.add(deck[i][j]);
                allCards.add(deck[i][j]);
            }
	}
        
        // Shuffle the cards.
        Collections.shuffle(deckArrayList);
    }  

    /**
     * Returns whether or not the deck is empty.
     */
    public boolean isEmpty(){
        return deckArrayList.isEmpty();
    }
    
    /**
     * Returns the number of cards left in the deck.
     */
    public int getCardCount(){
        return deckArrayList.size();
    }
    
    /**
     * Returns the card at a given index.
     */
    public Card getCardAt(int idx){
        return deckArrayList.get(idx);
    }
    
    
    /**
     * Returns all 52 cards
     */
    public ArrayList<Card> getAllCards(){
        return allCards;
    }
    
    
    /**
     * Removes a given card
     */
    public void removeCard(Card c){
        this.remove(c);
        deckArrayList.remove(c);
    }
    
    /**
     * Removes multiple cards.
     */
    public void removeCards(ArrayList<Card> c){
        for(Card card : c){
            deckArrayList.remove(card);
            this.remove(card);
            this.repaint();
        }
        // As long as we're not at the first card in the list, we decrease
        // idx by 1, so that we get the correct cards when we call getCards()
        // again.
        if(idx >0) idx -= 1;
    }
    
    /**
     * Adds multiple cards.
     */
    public void addCards(ArrayList<Card> cards){
        for(Card card : cards){
            deckArrayList.add(card);
        }
    }
    
    /**
     * The current index for getCards() and removeCards()
     */
    private int idx = 0;
    /**
     * Returns new cards
     */    
    public ArrayList<Card> getCards(int cardCount) {
        
        // If index has been set to 0, which means we've reached the end of
        // the deck, we set the deck to appear as if it's full.
        if(idx == 0) this.showAsFull();
        
        // Number of cards to return.
        int i=0;
        
        if(cardCount == 3){
            // We find the number of available cards left in the deck, when at a
            // the current index.
            if(idx <= deckArrayList.size() - 3) i = 3;
            else if(idx == deckArrayList.size() - 2) i = 2;
            else if(idx == deckArrayList.size() -  1) i = 1;
            // If the deck is empty, we do not attempt to return any cards, as
            // there are none to be returned.
            else return null;
        }
        else{
            i = cardCount;
        }
        
        // Create the list of cards to be returned.
        ArrayList<Card> ret = new ArrayList<Card>();
        
        // Adds the correct cards to the list.
        for(int j = idx; j < idx+i; j++){
            ret.add(deckArrayList.get(j));
        }
        // Increase the index by 1 so that the next time we call this method,
        // we'll get the next cards in the list.
        idx += i;
        
        // When we've reached the end of the deck, we set the index to 0,
        // meaning we start at the first card the next time the method is
        // called.
        if(idx == deckArrayList.size() ){
            this.showAsEmpty();
            idx = 0;
        }
        
        // Returns the list.
        return ret;
    }
    
    
    /**
     * Variable indicating whether or not the deck is empty. This is only used
     * to let the paintComponent method know what to draw */
    private boolean IS_EMPTY = false;
    
    /**
     * Sets the deck to appear empty.
     */
    public void showAsEmpty(){
        IS_EMPTY = true;
        this.repaint();
    }
    
    /**
     * Sets the deck to appear full.
     */
    public void showAsFull(){
        IS_EMPTY = false;
        this.repaint();
    }
    
    /**
     * Overrides the superclass paintComponent method. What it draws depends on
     * the value of IS_EMPTY. By default this value is false, but can be set by
     * the methods showAsEmpty() and showAsFull().
     */
    protected void paintComponent(Graphics graphics) {
        Graphics2D g = (Graphics2D)graphics.create();
        
        if(!IS_EMPTY){
            BufferedImage image = cim.getCardBack();            
            g.drawImage(image, 0, 0, null);
        }
        else{
            g.setColor(LayoutVariables.PLACEHOLDER_COLOR);
            g.setComposite(LayoutVariables.PLACEHOLDER_ALPHA);
            g.fillRect(0, 0, LayoutVariables.CARD_WIDTH,
                    LayoutVariables.CARD_HEIGHT);
        }
        g.dispose();
    }
}
