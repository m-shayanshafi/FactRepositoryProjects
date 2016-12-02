package kabalpackage.utilities;

import java.util.ArrayList;
import kabalpackage.*;

/**
 * Describes a move of cards between two stacks.
 */
public class Move {

    private Stack SRC_STACK;
    private Stack DST_STACK;
    private ArrayList<Card> CARDS_MOVED;
    
    /** 
     * Creates a new instance of Move.
     *
     * @param SRC_STACK  The source stack from which we moved the cards
     * @param DST_STACK  The destination stack where we moved the cards
     * @param CARDS_MOVED  The cards moved
     */
    public Move(Stack SRC_STACK, Stack DST_STACK, ArrayList<Card> CARDS_MOVED){
        
        this.SRC_STACK = SRC_STACK;
        this.DST_STACK = DST_STACK;
        this.CARDS_MOVED = CARDS_MOVED;
        
    }
    
    /**
     * Reverts this move.
     */
    public void undoMove(){
        if(SRC_STACK instanceof SolitaireStack && !SRC_STACK.isEmpty()){
            SRC_STACK.getTopCard().defaceCard();
        }
        SRC_STACK.addCards(CARDS_MOVED);
        DST_STACK.removeCards(CARDS_MOVED);
    }
}
