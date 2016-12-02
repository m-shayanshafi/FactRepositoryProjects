/*
 * GameController.java
 *
 * Subject to the apache license v. 2.0
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * @author william@xylophones.net
 */

package net.xylophones.micro.game.mb.controller;

import net.xylophones.micro.game.mb.BlockGenerator;
import net.xylophones.micro.game.mb.mvc.board.BoardController;
import net.xylophones.micro.game.mb.mvc.board.BoardListener;
import net.xylophones.micro.game.mb.mvc.board.BoardModel;
import net.xylophones.micro.game.mb.mvc.status.StatusController;
import net.xylophones.micro.game.mb.mvc.status.StatusListener;
import net.xylophones.micro.game.mb.mvc.status.StatusModel;

import java.util.Vector;
import java.util.Enumeration;
import net.xylophones.micro.db.SimpleStore;

/**
 * Main game controller - creates models and sub-controllers
 *
 * @author william@xylophones.net
 */
public class GameController implements BoardListener, StatusListener {

    private BoardController boardController;

    private StatusController statusController;
    
    private BlockGenerator generator = new BlockGenerator();
    
    private int nextTetrad = 0;
    
    private int timer = 0;
    
    private int maxDownInterval = 11;
    
    private int downInterval = maxDownInterval - 1;
    
    private Vector listeners = new Vector();
    
    private BoardModel boardModel;
    
    private StatusModel statusModel;
    
    private SimpleStore dataStore;

    /**
     * 
     * Creates a new instance of GameController 
     * 
     * 
     * @param BoardController boardController
     * @param StatusController statusController
     */
    public GameController ( SimpleStore dataStore ) {
        
        boardModel = new BoardModel(10, 20);
        boardController = new BoardController(boardModel);
        
        statusModel = new StatusModel();
        statusController = new StatusController(statusModel);
        
        //this.boardModel = boardModel;
        //this.boardController = boardController;
        boardController.addListener(this);
        
        //this.statusModel = statusModel;
        //this.statusController = statusController;
        statusController.addListener(this);
        
        this.dataStore = dataStore;
        
        statusModel.setHighScore( getStoredHighScore() );
        
        generateNextTetrad();
    }   
    
    public void boardEvent(int event) {
        switch( event ) {
            case BoardListener.EVENT_LINE_SINGLE:
                statusController.command(StatusController.COMMAND_SCORE_SINGLE);
                notifyListeners(GameListener.EVENT_LINE_CLEARED);
                break;
                
            case BoardListener.EVENT_LINE_DOUBLE:
                statusController.command(StatusController.COMMAND_SCORE_DOUBLE);
                notifyListeners(GameListener.EVENT_LINE_CLEARED);
                break;
                
            case BoardListener.EVENT_LINE_TRIPLE:
                statusController.command(StatusController.COMMAND_SCORE_TRIPLE);
                notifyListeners(GameListener.EVENT_LINE_CLEARED);
                break;
                
            case BoardListener.EVENT_LINE_TETRIS:
                statusController.command(StatusController.COMMAND_SCORE_TETRIS);
                notifyListeners(GameListener.EVENT_LINE_CLEARED);
                break;
                
            case BoardListener.EVENT_BLOCK_LANDED:

                boardModel.setTetrad(nextTetrad);
                boardController.command(BoardController.COMMAND_PREPARE_BLOCK );
                generateNextTetrad(); 
                statusModel.setNextTetrad( nextTetrad );

                break;
                
            case BoardListener.EVENT_BOARD_FULL:
                notifyListeners(GameListener.EVENT_GAME_OVER);
                highScoreCheck();
                reset();
                break;
        }
    }
    
    public void statusEvent(int event) {
        switch(event) {
            case StatusListener.EVENT_NEW_LEVEL:
                downInterval = maxDownInterval - statusModel.getLevel();
                break;
                
            case StatusListener.EVENT_SCORE:
                break;
        }
    }
    
    private void highScoreCheck() {
        int highScore = getStoredHighScore();

        if ( statusModel.getScore() > highScore  ) {
            setStoredHighScore( statusModel.getScore() );
            statusModel.setHighScore( statusModel.getScore() );
        }
    }
    
    private void setStoredHighScore(int score) {       
        dataStore.put( "highScore", String.valueOf(score) );
    }
    
    private int getStoredHighScore() {
        int highScore = 0;
        String highScoreString = dataStore.get("highScore");
        
        if ( highScoreString != null ) {
            highScore = Integer.parseInt(highScoreString);
        }
        
        return highScore;
    }
    
    private void generateNextTetrad() {
        nextTetrad = generator.randomTetrad();
    }
    
    public void tick() {
        timer++;
        if ( timer % downInterval == 0 ) {
            boardController.command(BoardController.COMMAND_DOWN);
        }
    }
    
    public void reset() {
        statusController.command(StatusController.COMMAND_RESET);
        boardController.command(BoardController.COMMAND_RESET);
        
        generateNextTetrad();
        boardModel.setTetrad(nextTetrad);
        boardController.command(BoardController.COMMAND_PREPARE_BLOCK );
        
        generateNextTetrad(); 
        statusModel.setNextTetrad( nextTetrad );       
    }
           
    public void addListener(GameListener listener) {
        listeners.addElement(listener);
    }

    public void removeListener(GameListener listener) {
        listeners.removeElement(listener);
    }

    public void notifyListeners(int event) {
        GameListener listener;
        Enumeration enumeration = listeners.elements();
        
        while (enumeration.hasMoreElements()) {
            listener = (GameListener) enumeration.nextElement();
            listener.gameEvent(event);
        }
    }

    public BoardModel getBoardModel() {
        return boardModel;
    }

    public StatusModel getStatusModel() {
        return statusModel;
    }

    public BoardController getBoardController() {
        return boardController;
    }

    public StatusController getStatusController() {
        return statusController;
    }
}
