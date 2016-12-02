package kabalpackage;

import java.awt.*;
import java.util.ArrayList;

/**
 * Abstract class containing abstract methods common to all stack types. 
 * Some methods may not be necessary in all stacks, where they might be empty,
 * return null or for instance in the case of isValidMove(), it will always
 * return false when called on a DealtCardsStack.
 */
abstract public class Stack extends javax.swing.JLayeredPane{
        
    abstract public String getType();
    abstract public boolean isEmpty();
    abstract public boolean isFull();
    
    abstract public Card getTopCard();
    abstract public ArrayList<Card> getAvailableCards();
    abstract public ArrayList<Card> getAvailableCardsAt(Card card);
    abstract public ArrayList<Card> getAllCards();
    
    abstract public void hideCards(ArrayList<Card> cards);
    abstract public void showCards(ArrayList<Card> cards);
    abstract public void addSingleCard(Card card);
    abstract public void removeSingleCard(Card card);    
    abstract public void addCards(ArrayList<Card> cards);
    abstract public void removeCards(ArrayList<Card> cards);
    
    abstract public boolean isValidMove(Card card);
    
    abstract public void highlight(boolean bool);
    abstract public boolean isHighlighted();
    
    /**
     * Transforms the position of the stack.
     */
    public final void transform(Point pp, Point p) {
	setLocation(pp.x-p.x, pp.y-p.y);
    }
}
