package kabalpackage;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.awt.image.*;
import javax.imageio.ImageIO;
import java.awt.image.VolatileImage;
import kabalpackage.*;
import kabalpackage.utilities.*;

/**
 * The main game panel where we lay out all the stacks at positions partly 
 * defined by the common layout variables contained in the class
 * LayoutVariables.
 *
 * @see kabalpackage.utilities.LayoutVariables
 */
public class GameArea extends JPanel {
    
    // Common layout variables used in this class.
    private final int SOLITAIRE_STACK_COUNT = LayoutVariables.SOLITAIRE_STACK_COUNT; 
    private final int FOUNDATION_COUNT = LayoutVariables.FOUNDATION_COUNT;
    private final int STACK_SPACING = LayoutVariables.STACK_SPACING;    
    private final int STACK_XPOS_START = LayoutVariables.MARGIN_LEFT;    
    private final int FOUNDATION_XPOS_START = LayoutVariables.FOUNDATION_XPOS_START;
    private final int STACK_YPOS_START = LayoutVariables.STACK_YPOS_START;
    private final int FOUNDATION_YPOS_START = LayoutVariables.FOUNDATION_YPOS_START;
    private final int CARD_WIDTH = LayoutVariables.CARD_WIDTH;    
    private final int CARD_HEIGHT = LayoutVariables.CARD_HEIGHT;
    private final int CARD_SPACING = LayoutVariables.CARD_SPACING;
    private final Color BACKGROUND_COLOR = LayoutVariables.BACKGROUND_COLOR;

    // The game condition (for Timer).
    private boolean GAME_STARTED = false;
    private boolean GAME_PAUSED = false;
    
    private int CARDS_TO_DEAL_COUNT = 3;

    // The CardImageMakers, responsible for retrieving the card images from file
    // and the strings containing the file names.
    private String cardsFile = "bondedcards.png";
    private CardImageMaker cim;
    private String cardsFile2 = "bondedcards-over.png";
    private CardImageMaker cim2;    

    // The deck and the stack for dealt cards.
    private Deck deck;
    private DealtCardsStack dealtCards;    

    // Array with references to the stacks
    private Stack[] stacks = new Stack[SOLITAIRE_STACK_COUNT+FOUNDATION_COUNT+1];

    // List of executed moves (for undo-functionality)
    private ArrayList<Move> moves = new ArrayList<Move>();

    // Timer
    private SolTimer timerComponent = new SolTimer();     

    // The thread which we run the timer on
    private Thread timerComponentThread;

    private HighScore highScore = null;

    // The background image
    private VolatileImage background;

    /**
     * Creates new instance of GameArea. Instantiates the two CardImageMakers.
     */
    public GameArea(){

        setLayout(null);
        setBackground(BACKGROUND_COLOR);
        setCursor(new Cursor(Cursor.HAND_CURSOR));   

        loadImages();
    }


    /**
     * Loads the images used for the cards and the GameArea background.
     */
    public void loadImages(){
        // Instantiate the CardImageMakers. If the images won't load, we exit
        // the entire program.
        try{
            cim = new CardImageMaker(cardsFile, CARD_WIDTH, CARD_HEIGHT);
            cim2 = new CardImageMaker(cardsFile2, CARD_WIDTH, CARD_HEIGHT);
        }
        catch(IOException IOe){
            JOptionPane.showMessageDialog(this, "Could not load card images!",
                    "Error!", JOptionPane.ERROR_MESSAGE);
            System.err.println("Could not load card images!");
            System.exit(1);
        }
        catch(IllegalArgumentException iae){
            JOptionPane.showMessageDialog(this, "Could not load card images!" +
                    "Exiting!", "Error!", JOptionPane.ERROR_MESSAGE);
            System.err.println("Could not load card images!");
            System.exit(1);
        }       

        // Load the standard background image
        try{	
            background = VolatileImageLoader.loadFromFile(
                    getClass().getResourceAsStream("images/bgpleasant.png"),
                    Transparency.OPAQUE );
        }
        catch(IOException ioe){
            JOptionPane.showMessageDialog(this, "Could not load background image!",
                    "Error!", JOptionPane.ERROR_MESSAGE);
            System.err.println("Could not load background image!");
        }
    }

    /**
     * This method changes the background image. We feed it an an int value,
     * which is the index of the requested background in the 
     * LayoutVariables.fileNames array.
     */
    public void changeBackground(int bgvalue){
        try{	
            background = VolatileImageLoader.loadFromFile(
                    getClass().getResourceAsStream("images/"
                    +LayoutVariables.fileNames[bgvalue]),
                    Transparency.OPAQUE );
        }
        catch(IOException ioe){
            JOptionPane.showMessageDialog(this, "Could not load background image!",
                    "Error!", JOptionPane.ERROR_MESSAGE);
            System.err.println("Could not load background image!");
        }
        repaint();
    }    

    private boolean SOUND_ON = true;
    /**
     * Turns soundeffects on and off
     */
    public void toggleSoundEffects(boolean bool){
        SOUND_ON = bool;
    }    

    /**
     * Plays soundeffect.
     */
    public void playSound(){
        SoundPlayer soundPlayer = new SoundPlayer();
        Thread soundThread = new Thread(soundPlayer);
        soundThread.start();
        soundPlayer.playSound();
    }    

    /**
     * Sets the number of cards to deal. Legal values are 1 and 3.
     */
    public void setCardsToDealCount(int cardCount){
        CARDS_TO_DEAL_COUNT = cardCount;
    }    

    /**
     * Starts a new game. The old contents (the old stacks and deck) are
     * removed and new ones are added.
     */    
    public void newGame(){

        removeAll();

        MouseHandler mh = new MouseHandler();

        // Vi create a new Deck and DealtCardsStack and add the appropriate
        // mouse listeners to them.
        deck = new Deck(cim, cim2);
        deck.addMouseListener(new DeckListener());
        dealtCards = new DealtCardsStack(deck);
        dealtCards.addMouseListener(mh);
        dealtCards.addMouseMotionListener(mh);      

        // Add the Deck and DealtCardsStack at the correct positions.
        deck.setBounds(STACK_XPOS_START, FOUNDATION_YPOS_START,
                CARD_WIDTH, CARD_HEIGHT);
        add(deck);
        dealtCards.setBounds(STACK_XPOS_START+100, FOUNDATION_YPOS_START, 
                CARD_WIDTH+(2*LayoutVariables.CARD_YSPACING), CARD_HEIGHT);
        add(dealtCards);        

        // Add the DealtCardsStack to the last position in the stack array.
        stacks[stacks.length-1] = dealtCards;

        // The Foundations.
        Foundation seqToAdd = null;
        for(int i=0; i<FOUNDATION_COUNT; i++){
            seqToAdd = new Foundation();
            seqToAdd.setBounds( FOUNDATION_XPOS_START+(STACK_SPACING*i)
                    +(i*CARD_WIDTH), FOUNDATION_YPOS_START, CARD_WIDTH, CARD_HEIGHT);
            seqToAdd.addMouseListener(mh);
            seqToAdd.addMouseMotionListener(mh);
            add(seqToAdd);
            stacks[i] = seqToAdd;
        }

        // The solitaire stacks.
        SolitaireStack toAdd = null;
        for(int i=0; i<SOLITAIRE_STACK_COUNT; i++){
            // Opprett ny KabalStack hvor vi tar "i+1" antall kort fra "deck"
            toAdd = new SolitaireStack(deck,i+1);
            toAdd.setBounds( STACK_XPOS_START+(STACK_SPACING*i)+(i*CARD_WIDTH),
                    STACK_YPOS_START, CARD_WIDTH, i*CARD_SPACING+CARD_HEIGHT  );
            toAdd.addMouseListener(mh);
            toAdd.addMouseMotionListener(mh);

            add(toAdd);
            stacks[FOUNDATION_COUNT+i] = toAdd;
        }

        // We turn all the cards left in the deck.
        for(int i=0; i<deck.getCardCount(); i++){
            deck.getCardAt(i).setTurned();
        }

        // We have not started playing yet.
        this.setGameStarted(false);
        
        // Timer. If there is a thread already (we have started a new game),
        // we will reuse this - we only reset the time value to 0. We don't
        // want a lot of threads floating around which we don't need.
        if(timerComponentThread != null) resetTimer();
        else{
            timerComponent.setBounds(0,0, LayoutVariables.WINDOW_WIDTH ,23);
            timerComponentThread = new Thread(timerComponent);
            timerComponentThread.start();
        }
        add(timerComponent);        

        repaint();
    }

    /**
     * Sets the game to started.
     */
    public void setGameStarted(Boolean b){
        GAME_STARTED = b;
    }

    /**
     * Returns whether or not the game has started.
     */
    public boolean gameHasStarted(){
        return GAME_STARTED;
    } 

    /**
     * Returns whether or not the game is paused.
     */
    public boolean gameIsPaused(){
        return GAME_PAUSED;
    }

    /**
     * Resumes the timer.
     *
     * @see kabalpackage.utilities.SolTimer#resumeTimer()
     */
    public void resumeTimer(){
       GAME_PAUSED = false;
       timerComponent.resumeTimer();
    }     

    /**
     * Pauses the timer.
     *
     * @see kabalpackage.utilities.SolTimer#pauseTimer()
     */
    public void pauseTimer(){
       GAME_PAUSED = true;
       timerComponent.pauseTimer();
    }

    /**
     * Resets the timer.
     *
     * @see kabalpackage.utilities.SolTimer#resetTimer()
     */
    public void resetTimer(){
        timerComponent.resetTimer();
    }

    /**
     * This at present has limited functionality, and is therefore not fully
     * implemented. The intention is that a call to this method will revert
     * our last move.
     */
    public void undoMove(){
        if(moves.size() > 0){
            moves.get(moves.size()-1).undoMove();
            moves.remove(moves.size()-1);
            repaint();
        }
    }

    
    private boolean GAME_OVER = false;
    /**
     * Checks whether or not the game is over. If it is (if all
     * Foundations are full), we remove the mouse listeners, stop the timer
     * and display a new HighScore window.
     */
    public void gameOver(){
        // Find out the number of full SequentialStacks.
        int fullCount = 0;
        for(Stack stack : stacks){
            if(stack instanceof Foundation){
                if(stack.isFull()) fullCount++;
            }
        }

        // If they're all full  ..
        if(fullCount == 4){
            // .. remove the mouse listeners
            for(Stack stack : stacks){
                if(stack instanceof Foundation){
                    stack.removeMouseListener(stack.getMouseListeners()[0]);
                    stack.removeMouseMotionListener(
                            stack.getMouseMotionListeners()[0]);
                }
            }

            // .. create new HighScore object and stop the timer.
            timerComponent.gameOver();
            GAME_OVER = true;
            highScore = new HighScore(timerComponent.getTime());
        }
    }
    
    public boolean isGameOver(){
        return GAME_OVER;
    }

    /**
     * Presents the highscore table.
     */
    public void presentHighScore() {
        highScore = new HighScore(-1);
    }

    /**
     * This method compares each card in a stack which is available for
     * moving, to the top card in each of the other stacks, to find all the
     * legal moves you can currently do. The legal moves are added to a
     * string and sent to a new JOptionPane.showMessageDialog().
     */
    public void hint(){
        
        // If the game is over, we don't bother to check anything
        if(isGameOver()){
            JOptionPane.showMessageDialog(this, "Start new game",
                    "Possible moves", JOptionPane.PLAIN_MESSAGE);
            return;
        }
        
        // List to temporarily hold the cards that are available for moving.
        ArrayList<Card> TMP_TURNED_CARDS;        

        // ArrayList where we put the legal moves.
        ArrayList<String> possibleMoves = new ArrayList<String>();
        
        for(Stack SRC_STACK : stacks){
            if(SRC_STACK != null && !SRC_STACK.isEmpty()){
                TMP_TURNED_CARDS = SRC_STACK.getAvailableCards();
                if(TMP_TURNED_CARDS != null){
                    for(Card TMP_CARD : TMP_TURNED_CARDS){
                        for(Stack DST_STACK : stacks){
                            if(DST_STACK != null && !SRC_STACK.equals(DST_STACK) && DST_STACK.isValidMove(TMP_CARD)
                                    && !(DST_STACK instanceof Foundation && !TMP_CARD.equals(SRC_STACK.getTopCard()) )){                                

                                if(!DST_STACK.isEmpty()){
                                    Card DST_CARD = DST_STACK.getTopCard();
                                    possibleMoves.add("You can move " + TMP_CARD.getName() + " of " + TMP_CARD.getType()
                                            + " to " + DST_CARD.getName() + " of " + DST_CARD.getType() + ".");
                                }
                                else{
                                    if(SRC_STACK instanceof Foundation && DST_STACK.getType().equals(SRC_STACK.getType()) ){
                                        break;
                                    }
                                    possibleMoves.add("You can move " + TMP_CARD.getName() + " of " + TMP_CARD.getType()
                                            + " to empty " + DST_STACK.getType() + ".");
                                    if(SRC_STACK instanceof SolitaireStack && DST_STACK.getType().equals(SRC_STACK.getType()) ){
                                        break;
                                    }
                                }
                            }
                        }
                    }
                } 
            }
        }

        
        String stringOut = "";
        if(!possibleMoves.isEmpty()){
            for(String string : possibleMoves){
                stringOut += "\n" + string;
            }
        }
        else stringOut = "Deal new cards from the deck";

        JOptionPane.showMessageDialog(this, stringOut, "Possible moves",
                JOptionPane.PLAIN_MESSAGE);
        
        
    } 

    /**
     * Draws the background of the GameArea.
     */
    protected void paintComponent(Graphics graphics) {
        Graphics2D g = (Graphics2D)graphics.create();
        
        if(background != null){
            g.drawImage(background, 0, 0, null);
        }
        // If no background could be loaded, we paint a green, one
        // color background
        else{
            g.setColor(LayoutVariables.BACKGROUND_COLOR);
            g.fillRect(0, 0, LayoutVariables.WINDOW_WIDTH, 
                    LayoutVariables.WINDOW_HEIGHT);
        }
        // Draw black, diagonal lines
        for(int i=0; i<LayoutVariables.WINDOW_WIDTH; i++){
            g.setColor(Color.BLACK);
            g.drawLine(0,i*4, i*4, 0);
        }
        g.dispose();
    }

    /**
     * Changes the card images.
     *
     * @param pic1 The filename of the regular image
     * @param pic2 The filename of the highlighting image
     */
    void changeImage(String pic1, String pic2) {
        
        cim.changeImage(pic1);
        cim2.changeImage(pic2);
        
        for(Card card : deck.getAllCards() ){

            String type = card.getType();
            int number = card.getNumber();

            try{
                VolatileImage back = VolatileImageLoader.loadFromBufferedImage(
                        cim.getCardBack(), Transparency.TRANSLUCENT );
                VolatileImage image = VolatileImageLoader.loadFromBufferedImage(
                        cim.cropToCard(type, number-1), Transparency.TRANSLUCENT );
                VolatileImage over = VolatileImageLoader.loadFromBufferedImage(
                        cim2.cropToCard(type, number-1), Transparency.TRANSLUCENT );

                card.setImage(back, image, over);
            }
            catch(Exception e){ 
                System.err.println("Could not change card style");                
                e.printStackTrace();
            }
        }
        
        repaint();
    }


    final private class MouseHandler extends MouseInputAdapter {
        
        // The card we have clicked.
        Card CLICKED_CARD;
        // The stack we are trying to move.
        Stack SRC_STACK;
        // The stack we are moving to.
        Stack DST_STACK;
        // The temporary stack we are dragging around.
        private Stack TMP_STACK;
        // Temporary list containing the cards we are trying to move.
        private ArrayList<Card> TMP_LIST = new ArrayList<Card>();
        // Point in stack.
	private Point p;
        // Point in GameArea.
        private Point pp;

        // Set in mousePressed. Decides what the mouseDragged and
        // mouseReleased methods can do.
        private boolean CLICK_OK = false;       



	public void mouseMoved(MouseEvent e) {
            p = e.getPoint();
            pp = SwingUtilities.convertPoint(SRC_STACK, e.getPoint(),
                    GameArea.this);
        }

	public void mousePressed(MouseEvent e) {

            // Set the source stack
            SRC_STACK = (Stack)e.getSource();

            // We only allow clicks on non-empty stacks with the first 
            // (usually the left) mouse button.
            if(e.getButton() != MouseEvent.BUTTON1 || SRC_STACK.isEmpty() ){
                CLICK_OK = false;
                return;
            }           

            // We again find the point in GameArea.
            pp = SwingUtilities.convertPoint(SRC_STACK, p, GameArea.this);

            // Figure out which card we are clicking.
            CLICKED_CARD = (Card)SRC_STACK.getComponentAt(p);

            // Make a list of cards that we wish to move.
            TMP_LIST = SRC_STACK.getAvailableCardsAt(CLICKED_CARD);

            // Set whether or not the click is legal. We can not click on
            // a card that has not yet been turned.
            if(CLICKED_CARD != null && CLICKED_CARD.isTurned() && 
                    !(SRC_STACK instanceof DealtCardsStack) ){
                CLICK_OK = true;
            }

            // In DealtCardsStack we only allow moving the top card.
            else if(SRC_STACK instanceof DealtCardsStack && CLICKED_CARD != null
                    && CLICKED_CARD.equals(SRC_STACK.getTopCard()) ){
                CLICK_OK = true;
            }
            else{
                CLICK_OK = false;
                return;
            }
            
            // Start timer
            if(!gameHasStarted()){
                resumeTimer();
                setGameStarted(true);
            }
            if(gameHasStarted() && gameIsPaused()){
                resumeTimer();
            }

            
            // Animation

            // We make a temporary stack to move around on the screen
            if(SRC_STACK instanceof SolitaireStack){
                TMP_STACK = new SolitaireStack(TMP_LIST);
            }
            if(SRC_STACK instanceof Foundation){
                TMP_STACK = new Foundation(TMP_LIST);
            }
            if(SRC_STACK instanceof DealtCardsStack){
                TMP_LIST.get(0).setLocation(0,0);
                TMP_STACK = new Foundation(TMP_LIST);
            }

            // Change the postition of the temporary stack so that it the
            // transition is seamless.
            TMP_STACK.setBounds((int)CLICKED_CARD.getBounds().getX(),
                    (int)CLICKED_CARD.getBounds().getY(),79,700);
            GameArea.this.add(TMP_STACK, 0);

            int yShift = 0;
            if(SRC_STACK instanceof DealtCardsStack){
                DealtCardsStack tmp = (DealtCardsStack)SRC_STACK;
                yShift = tmp.getCardCount()-1;
            } 

            TMP_STACK.transform(new Point((int)(pp.getX()
                    + CLICKED_CARD.getBounds().getX() )
                    + yShift*LayoutVariables.CARD_YSPACING, (int)(pp.getY()
                    + CLICKED_CARD.getBounds().getY()) ), p );           

            SRC_STACK.hideCards(TMP_LIST); 
        }


	public void mouseDragged(MouseEvent e) {

            if(!CLICK_OK) return;

            pp = SwingUtilities.convertPoint(SRC_STACK, e.getPoint(),GameArea.this);

            GameArea.this.setCursor(new Cursor(Cursor.MOVE_CURSOR));

            // Animation
            int yShift = 0;
            if(SRC_STACK instanceof DealtCardsStack){
                DealtCardsStack tmp = (DealtCardsStack)SRC_STACK;
                yShift = tmp.getCardCount()-1;
            }

            Point newLocation = new Point((int)(pp.getX()
                    -CLICKED_CARD.getBounds().getX())
                    + yShift*LayoutVariables.CARD_YSPACING, (int)(pp.getY()
                    +CLICKED_CARD.getBounds().getY()) );
            TMP_STACK.transform( newLocation, p );
            

            // Card highlighting
            
            Point tp;

            // For each component
            for(Stack stack : stacks){
                // Translate point in GameArea to point in stack
                tp = SwingUtilities.convertPoint(GameArea.this, pp, stack);

                // If the stack contains tp and is not the stack which we are
                // moving, and we're not trying to drag more than one card 
                // to a Foundation ..
                if(stack.contains(tp) && !stack.equals(SRC_STACK) 
                        && !(stack instanceof Foundation && TMP_LIST.size() > 1)){
                    // If the desination stack is not highlighted and we have
                    // a valid move, then we highlight
                    if(!stack.isHighlighted()
                            && stack.isValidMove(CLICKED_CARD)){
                        stack.highlight(true);
                    }
                }

                // If the stack doesn't contain the point and it has been
                // highlighted, we remove the highlighting.
                else if(stack instanceof SolitaireStack 
                        || stack instanceof Foundation){
                    
                    if(stack.isHighlighted() ){
                        stack.highlight(false);
                    }
                }
            }
	}	


	public void mouseClicked(MouseEvent e){

           // Get number of clicks
            int clickCount = e.getClickCount();

            
            // If we've got a double click and the click is ok
            if(clickCount == 2 && CLICK_OK){

                // Start timer
                if(!gameHasStarted()){
                    resumeTimer();
                    setGameStarted(true);
                }
                if(gameHasStarted() && gameIsPaused()){
                    resumeTimer();
                }

                // Add the card to the first Foundation which is either empty
                // or already contains a card which is legal to move this 
                // card to.
                for(Stack stack : GameArea.this.stacks){
                    if(stack instanceof Foundation){
                        if(stack.isValidMove(CLICKED_CARD)
                                && SRC_STACK.getTopCard().equals(CLICKED_CARD)){                            

                            ArrayList<Card> cards = new ArrayList<Card>();
                            cards.add(CLICKED_CARD);
                            stack.addCards(cards);
                            SRC_STACK.removeCards(cards);                             

                            // Play sound if it's enabled
                            if(GameArea.this.SOUND_ON){
                                playSound();
                            }

                            if(SRC_STACK instanceof DealtCardsStack){
                                moves.clear();
                            }
                            else{
                                moves.add(new Move(SRC_STACK, stack, TMP_LIST));
                            }

                            gameOver();
                            return;
                        }
                    }
                }
            } 
	}        

	public void mouseReleased(MouseEvent e){

            // We don't allow this to run if the click is either not OK, or
            // the button clicked is not the first (left) mouse button.
            if(e.getButton() != MouseEvent.BUTTON1 || !CLICK_OK) return;            

            // Remove temporary stack and repaint the game area.
            GameArea.this.remove(TMP_STACK);
            GameArea.this.repaint();

            // Change mouse cursor back to HAND_CURSOR.
            GameArea.this.setCursor(new Cursor(Cursor.HAND_CURSOR));

            // Set destination stack
            try{
                DST_STACK = (Stack)GameArea.this.getComponentAt(pp);
            }
            // If we drop the cards on something that's not a stack, we put the
            // cards back in the source stack and don't continue the method.
            catch(ClassCastException cce){
                SRC_STACK.showCards(TMP_LIST);
                return;
            }

            // If the move is valid, we go through with the move.
            if(DST_STACK != null && DST_STACK.isValidMove(CLICKED_CARD) && 
                    !(DST_STACK instanceof Foundation && TMP_LIST.size() > 1)){                

                // Play sound if it's enabled
                if(GameArea.this.SOUND_ON){
                    playSound();
                }                

                // Remove the cards from the source stack
                SRC_STACK.removeCards(TMP_LIST);

                // Remove highlighting in the destination stack
                DST_STACK.highlight(false);                      

                // Add the cards to the destination stack
                DST_STACK.addCards(TMP_LIST);

                // If we made a move to a Foundation, we check if the game
                // is over.
                if(DST_STACK instanceof Foundation){
                    gameOver();
                }

                // Add move to moves list. If we add a new card from the
                // DealtCardsStack we are not allowed to undo the move or
                // any of the previous moves. We do this partly because it
                // makes some sense, but mostly because it would take some
                // time to implement the opposite behaviour, and time is
                // something we don't have a lot of at the current time.
                if(SRC_STACK instanceof DealtCardsStack){
                    moves.clear();
                }
                else{
                    moves.add(new Move(SRC_STACK, DST_STACK, TMP_LIST));
                }
            }

            // If the move is not valid, we put the cards back in the source
            // stack.
            else{
                SRC_STACK.showCards(TMP_LIST);
            }
	}
    }   

    final private class DeckListener implements MouseListener{

        private boolean CLICK_OK = false;

        public void mousePressed(MouseEvent e){
            CLICK_OK = true;
        }

        public void mouseReleased(MouseEvent e){            

            if(!CLICK_OK) return;
            
            // If the deck is emptied, we set it to appear empty and we
            // remove this mouse listener as there's no point in being able
            // to click it anymore.
            if(deck.isEmpty()){
                deck.showAsEmpty();
                deck.removeMouseListener(this);
                return;
            }
            

            if(GameArea.this.SOUND_ON){
                playSound();
            }            

            // Start timer
            if(!gameHasStarted()){
                resumeTimer();
                setGameStarted(true);
            }
            if(gameHasStarted() && gameIsPaused()){
                resumeTimer();
            }

            // Add new cards from the deck in the DealtCardsStack
            dealtCards.addNewCardsFromDeck(CARDS_TO_DEAL_COUNT);
        }

        public void mouseClicked(MouseEvent e){}
        public void mouseEntered(MouseEvent e){}
        public void mouseExited(MouseEvent e){}
    }
}
