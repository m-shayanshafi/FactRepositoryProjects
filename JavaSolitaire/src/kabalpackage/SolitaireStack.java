package kabalpackage;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import kabalpackage.*;
import kabalpackage.utilities.LayoutVariables;

/**
 * The solitaire stack, where we do most of the playing.
 */
public class SolitaireStack extends Stack{
    
    private String type = "solitaire stack";
    
    // The cards currently in the stack
    private ArrayList<Card> cards = new ArrayList<Card>();
    
    // Layout variables
    private final int CARD_SPACING = LayoutVariables.CARD_SPACING;
    private final int CARD_WIDTH = LayoutVariables.CARD_WIDTH;
    private final int CARD_HEIGHT = LayoutVariables.CARD_HEIGHT;
    
    /**
     * Creates new instance of SolitaireStack.
     *
     * @param deck  The deck from which we fetch the cards
     * @param cardcount The number of cards to add to this stack (and 
     * remove from deck)
     */
    public SolitaireStack(Deck deck, int cardcount) {
        
        Card tmpCard;
        for(int i=0; i<cardcount; i++){
            
            // Add cards at appropriate positions in the stack
            tmpCard = deck.getCardAt(0);
            tmpCard.setBounds(0, i*CARD_SPACING, CARD_WIDTH, CARD_HEIGHT);
            this.add(tmpCard, 0);
            cards.add(tmpCard);
            
            // Turn the last card
            if(i >= cardcount-1) tmpCard.setTurned();
            
            // Remove card from the deck
            deck.removeCard(deck.getCardAt(0));
            
        }
    }
    
    /**
     * Creates a new instance of SolitaireStack with the given collection of
     * cards. Used when we make a temporary stack to move around on the screen.
     */
    public SolitaireStack(ArrayList<Card> cardsIn){
        this.setBackground(null);
        this.setLayout(null);
        
        Card tmpCardSrc;
        Card tmpCardDst;
        for(int i=0; i<cardsIn.size(); i++){
            tmpCardSrc = cardsIn.get(i);
            tmpCardDst = tmpCardSrc.makeCopy();
            this.cards.add(tmpCardDst);
            
            tmpCardDst.setBounds(0, i*CARD_SPACING, CARD_WIDTH, CARD_HEIGHT);
            this.add(tmpCardDst, 0);
        }
        
    }
    
    public SolitaireStack(){ }
    
    
    /**
     * Returns an ArrayList containing the given card and all the turned 
     * cards that are "attached" to it. We use this method to know what cards
     * we want to move and of course which cards to add to the temporary stacks.
     */
    public ArrayList<Card> getAvailableCardsAt(Card card){
        // Create the list to be returned
        ArrayList<Card> ret = new ArrayList<Card>();
        
        // If the card is not turned, we return null.
        if(!card.isTurned()) {
            return null;
        }
        
        // Find the index of the card        
        int cardIdx = cards.lastIndexOf(card);
                
        // Add the cards on the above indexes to the list
        if (cardIdx > -1) {
            for(int i=0; i<cards.size()-cardIdx; i++) {
                ret.add(cards.get(cardIdx+i ));
            }
        }
        
        // Return the list
        return ret;
    }
    
    /**
     * Returns the type of the stack.
     */
    public String getType(){
        return type;
    }
    
    
    /**
     * Returns a list of cards in the stack that are currently turned. These
     * cards are legible for moving. Used by the hint method in GameArea.
     */
    public ArrayList<Card> getAvailableCards(){
        //Oppretter en listen vi skal returnere.
        ArrayList<Card> ret = new ArrayList<Card>();
        
        for(int i=0; i<cards.size(); i++){
            if(cards.get(i).isTurned()){
                ret.add(cards.get(i));
            }
        }
        if(ret.size() < 1) return null;
        return ret;
    }
    
    
    /**
     * Returns the top card in the stack.
     */
    public Card getTopCard(){
        return cards.get(cards.size()-1);
    }
    
    /**
     * Does nothing in this type of stack. The only reason for it being there
     * is that it is an inherited abstract method.
     */
    public void addSingleCard(Card card){}
    
    
    
    /**
     * Adds a collection of cards to the stack at the appropriate locations.
     */
    public void addCards(ArrayList<Card> c){
               
        int topCardYPos = 0;
        if(!this.isEmpty()){
            
            // For each new card that we add, we must find the vertical
            // starting position of the current/previous top card, that is if
            // the stack is not empty.
            topCardYPos = (int)this.getTopCard().getBounds().getY();
            int i = 1;
            for(Card card : c){
                // Legg det til i arraylista og i LayeredPane
                card.setBounds(0, topCardYPos+(i*CARD_SPACING), CARD_WIDTH,
                        CARD_HEIGHT);
                this.cards.add( cards.size(), card );
                this.add(card,0);
                i++;
            }
        }
        // If the stack is empty we start adding the cards at point 0,0.
        else{
            this.cards.clear();
            int i = 0;
            for(Card card : c){
                // Legg det til i arraylista og i LayeredPane
                card.setBounds(0, topCardYPos+(i*CARD_SPACING), CARD_WIDTH,
                        CARD_HEIGHT);
                this.cards.add( cards.size(), card );
                this.add(card,0);
                i++;
            }
        }        
        trimToSize();
    }
       
    
    /**
     * Removes multiple given cards from the stack.
     */
    public void removeCards(ArrayList<Card> c){
        this.cards.removeAll(c);        
        this.cards.trimToSize();
        for(Card card : c){
            this.remove(card);
        }
        
        if(this.cards.size() > 0){
            SolitaireStack.this.getTopCard().setTurned();
        }
        trimToSize();
    }
    
    /**
     * Does nothing in this type of stack. The only reason for it being there
     * is that it is an inherited abstract method.
     */
    public void removeSingleCard(Card card){}
    
    
    /**
     * Returns whether or not the given card can be moved onto the top card
     * of this stack.
     */
    public boolean isValidMove(Card card){
        if(this.isEmpty() && card.getNumber() == 13) return true;
        if(!this.isEmpty() && card.getNumber() == this.getTopCard().getNumber()-1){
            if(this.getTopCard().getType().equals(card.getType()) ) return false;
            if( (this.getTopCard().getType().equals("diamonds") || this.getTopCard().getType().equals("hearts") ) &&
                    ( card.getType().equals("clubs") || card.getType().equals("spades") )  )
                return true;
            if( (this.getTopCard().getType().equals("clubs") || this.getTopCard().getType().equals("spades") ) &&
                    ( card.getType().equals("diamonds") || card.getType().equals("hearts") )  )
                return true;
        }
        return false;
    }
    
    /**
     * Returns whether or not the stack is empty.
     */
    public boolean isEmpty(){
        return cards.isEmpty();
    }
    
    /**
     * Returns whether or not the stack is full.
     */
    public boolean isFull(){ return false; }
    
        
    /**
     * Removes the given cards from the deck, but only from the GUI component.
     * A reference to the cards will still be there. Used during animation.
     */
    public void hideCards(ArrayList<Card> cards){
        for(Card c : cards){
            remove(c);
        }
    }
    
    /**
     * Same as hideCards(), only this one re-adds the cards.
     */
    public void showCards(ArrayList<Card> cards){
        int i = 1;
        for(Card c : cards){
            int topCardYPos = (int)getTopCard().getBounds().getY();
            c.getBounds().setLocation(0, topCardYPos+(i*CARD_SPACING));
            add(c, 0);
            i++;
        }
    }
    
    /**
     * Trims the visual size of the stack to accomodate the current number 
     * of cards.
     */
    public void trimToSize(){
        if(cards.size() != 0)
            this.setSize(CARD_WIDTH, CARD_HEIGHT 
                    + (this.cards.size()*CARD_SPACING)-CARD_SPACING );
        else
            this.setSize(CARD_WIDTH, CARD_HEIGHT);
    }
    
    // Variable indicating whether or not the stack or its top card
    // is hightlighted
    private boolean HIGHLIGHT = false;
    
    /**
     * (De)highlights the stack or the top card in the stack.
     */
    public void highlight(boolean bool){
        HIGHLIGHT = bool;
        if(this.isEmpty()){
            repaint();
        }
        else{
            getTopCard().highlight(bool);
            getTopCard().repaint();
        }
    }
    
    /**
     * Returns whether or not the stack or its top card is hightlighted
     */
    public boolean isHighlighted(){
        return HIGHLIGHT;
    }
    
    /**
     * Draws the stack background.
     */
    protected void paintComponent(Graphics graphics) {
        Graphics2D g = (Graphics2D)graphics.create();
        
        if(!HIGHLIGHT){
            g.setColor(LayoutVariables.PLACEHOLDER_COLOR);
            g.setComposite(LayoutVariables.PLACEHOLDER_ALPHA);
            g.fillRect(0, 0, LayoutVariables.CARD_WIDTH, 
                    LayoutVariables.CARD_HEIGHT);
        }
        else{
            g.setColor(LayoutVariables.PLACEHOLDER_COLOR);
            g.setComposite(LayoutVariables.PLACEHOLDER_ALPHA_HIGHLIGHT);
            g.fillRect(0, 0, LayoutVariables.CARD_WIDTH, 
                    LayoutVariables.CARD_HEIGHT);
        }
        g.dispose();
    }
    
    public ArrayList<Card> getAllCards(){
        return this.cards;
    }
    
}