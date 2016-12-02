package kabalpackage;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import kabalpackage.*;
import kabalpackage.utilities.LayoutVariables;

/**
 * Stack showing the cards currently dealt from the deck.
 */
public class DealtCardsStack extends Stack{
    
    private String type = "dealt cards";
    private Deck deck;
    
    // Common layout variables used in this class
    private final int CARD_WIDTH = LayoutVariables.CARD_WIDTH;
    private final int CARD_HEIGHT = LayoutVariables.CARD_HEIGHT;
    private final int CARD_YSPACING = LayoutVariables.CARD_YSPACING;
        
    // List containing the cards currently in the stack.
    private ArrayList<Card> cards = new ArrayList<Card>();
    
    
    /**
     * Creates a new instance of DealtCardsStack
     */
    public DealtCardsStack(Deck deck) {
        this.deck = deck;
    }
    
    /**
     * Returns the type of the stack.
     */
    public String getType(){
        return type;
    }
    
    /**
     * Returns whether or not the stack is empty.
     */
    public boolean isEmpty(){
        return cards.size() == 0;
    }
    
    /**
     * Returns whether or not the stack is full.
     */
    public boolean isFull(){
        return cards.size() == 3;
    }
    
    /**
     * Returns the current number of cards in the stack.
     */
    public int getCardCount(){
        return cards.size();
    }
    
    /**
     * Returns the card currently at the top of the stack.
     */
    public Card getTopCard(){
        return cards.get(cards.size()-1);
    }
    
    /**
     * This method is used by the hint method in GameArea to figure
     * out which cards in the stack is available for comparison with the top
     * card in the other stacks. Here it only returns the top card as only
     * that card can be moved.
     */
    public ArrayList<Card> getAvailableCards(){
        ArrayList<Card> ret = new ArrayList<Card>();
        ret.add(getTopCard());
        return ret;
    }
    
    /**
     * Returns ArrayList containing the top card as we can only move one 
     * card at a time from this type of stack.
     */
    public ArrayList<Card> getAvailableCardsAt(Card card){
        ArrayList<Card> ret = new ArrayList<Card>();
        ret.add(card);
        return ret;
    }
    
    /**
     * Removes given collection of cards, but only in the GUI part. References
     * the cards will still be in the stack. 
     */
    public void hideCards(ArrayList<Card> cards){
        this.remove(cards.get(0));
    }
    
    /**
     * Adds given collection of cards to the GUI element of this class.
     */
    public void showCards(ArrayList<Card> cards){
        cards.get(0).setLocation( this.cards.size()*CARD_YSPACING
                -CARD_YSPACING, 0 );
        this.add(cards.get(0), 0);
    }
    
    /**
     * Adds a single card to the stack.
     */
    public void addSingleCard(Card card){
        this.add(card);
        cards.add(card);
    }
    
    /**
     * Remove a single card from the stack.
     */
    public void removeSingleCard(Card card){
        this.remove(card);
        cards.remove(card);
    }
    
    /**
     * Adds a given collection of cards to the stack.
     */
    public void addCards(ArrayList<Card> c){
        int i=0;
        for(Card card : c){
            this.cards.add(card);
            card.setBounds( 2*CARD_YSPACING, 0, CARD_WIDTH, CARD_HEIGHT);
            this.add(card, 0);
            i++;
        }
        trimToSize();
    }
    
    /**
     * Removes a given collection of cards from the Stack.
     */
    public void removeCards(ArrayList<Card> cards){
        this.cards.removeAll(cards);
        deck.removeCards(cards);
        
        this.cards.trimToSize();
        for(Card card : cards){
            this.remove(card);
        }
        trimToSize();
    }
    
    /**
     * Adds new cards from the deck.
     */
    public void addNewCardsFromDeck(int cardCount){

        // Retrieves new cards from the deck.
        ArrayList<Card> cardsToAdd = deck.getCards(cardCount);
        
        // If we retrieved no cards, we don't continue this method.
        if(cardsToAdd == null) return;
        
        // Removes the cards to give room to new ones.
        this.removeAll();
        this.cards.clear();
        
        // Adds the new cards.
        for(int i=0; i<cardsToAdd.size(); i++){
            cardsToAdd.get(i).setBounds( i*CARD_YSPACING, 0, CARD_WIDTH, CARD_HEIGHT);
            this.cards.add(cardsToAdd.get(i));
            this.add(cardsToAdd.get(i), 0);
        }
        trimToSize();
    }
    
    /**
     * Trims the visual size of the stack to accomodate the current
     * number of cards.
     */
    public void trimToSize(){
        if(cards.size() != 0)
            this.setSize(CARD_WIDTH+(this.cards.size()*CARD_YSPACING)
                    -CARD_YSPACING, CARD_HEIGHT );
        else
            this.setSize(CARD_WIDTH, CARD_HEIGHT);
    }
    
    /**
     * Returns whether or not a move to this stack is valid. In this case,
     * we don't allow any moves - we can only move FROM this stack. Therefore
     * this method always returns false here.
     *
     * @param card  The card we want to place here.
     */
    public boolean isValidMove(Card card){
        return false;
    }
    
    
    
    public void highlight(boolean bool){}
    public boolean isHighlighted(){
        return false;
    }
    
    
    /**
     * Draws the stack background.
     */
    protected void paintComponent(Graphics graphics) {
        Graphics2D g = (Graphics2D)graphics.create();
        g.setColor(LayoutVariables.PLACEHOLDER_COLOR);
        g.setComposite(LayoutVariables.PLACEHOLDER_ALPHA);
        g.fillRect(0, 0, LayoutVariables.CARD_WIDTH,
                LayoutVariables.CARD_HEIGHT);
        g.dispose();
    }
    
    public ArrayList<Card> getAllCards(){
        return this.cards;
    }
}
