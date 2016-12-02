package kabalpackage;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import kabalpackage.*;
import kabalpackage.utilities.LayoutVariables;

/**
 * The stack where we add cards in sequence.
 */
public class Foundation extends Stack {
    
    private String type = "foundation";
    
    // The cards currently in the stack
    private ArrayList<Card> cards = new ArrayList<Card>();
    
    /**
     * Creates a new instance of an empty Foundation.
     */
    public Foundation() {
    }
    
    /**
     * Creates an instance of Foundation already containing a given card.
     * This is used when we make a temporary stack when moving from either a
     * Foundation or a DealtCardsStack.
     */
    public Foundation(ArrayList<Card> c){
        for(Card card : c){
            this.add(card);
        }
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
        return cards.isEmpty();
    }
    
    /**
     * Returns whether or not the stack is full.
     */
    public boolean isFull(){
        return cards.size() == 13;
    }
        
    /**
     * Returns the top card.
     */
    public Card getTopCard(){
        if(cards.size() > 0)
            return cards.get(cards.size()-1);
        else return null;
    }
    
    /**
     * Returns the top card as ArrayList.
     */
    public ArrayList<Card> getAvailableCards(){
        ArrayList<Card> ret = new ArrayList<Card>();
        ret.add(getTopCard());
        return ret;
    }
    
    /**
     * Returns the top card as ArrayList.
     */
    public ArrayList<Card> getAvailableCardsAt(Card card){
        ArrayList<Card> ret = new ArrayList<Card>();
        ret.add(card);
        return ret;
    }
    
    /**
     * Adds a single card.
     */
    public void addSingleCard(Card card){
        this.cards.add(card);
        card.setBounds(0,0, LayoutVariables.CARD_WIDTH, 
                LayoutVariables.CARD_HEIGHT);
        this.add(card, 0);
    }
    
    /**
     * Adds multiple cards.
     */
    public void addCards(ArrayList<Card> c){
        for(Card card : c){
            this.cards.add(card);
            card.setBounds(0,0, LayoutVariables.CARD_WIDTH, 
                    LayoutVariables.CARD_HEIGHT);
            this.add(card, 0);
        }
    }

    /**
     * Removes multiple cards.
     */
    public void removeCards(ArrayList<Card> c){
        this.cards.removeAll(c);
        
        this.cards.trimToSize();
        for(Card card : c){
            this.remove(card);
        }
    }
    
    /**
     * Removes a single card.
     */
    public void removeSingleCard(Card card){
        this.cards.remove(card);
        this.remove(card);
        
        this.repaint();
    }
    
    /**
     * Removes the given cards from the foundation, but only from the GUI
     * component. A reference to the cards will still be there. Used during
     * animation.
     */
    public void hideCards(ArrayList<Card> card){
        removeSingleCard(card.get(0));
    }
    /**
     * Same as hideCards(), only this one re-adds the cards.
     */
    public void showCards(ArrayList<Card> card){
        addSingleCard(card.get(0));
    }
    
    /**
     * Returns whether or not the given card can be moved onto the top card
     * of this stack.
     */
    public boolean isValidMove(Card card){
        if(this.isEmpty() && card.getNumber() == 1) return true;
        if(!this.isEmpty() && card.getType().equals(this.getTopCard().getType() ) ){
            if(card.getNumber() == this.getTopCard().getNumber()+1){
                return true;
            }
        }
        return false;
    }

    // Variable indicating whether or not the stack is hightlighted
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
