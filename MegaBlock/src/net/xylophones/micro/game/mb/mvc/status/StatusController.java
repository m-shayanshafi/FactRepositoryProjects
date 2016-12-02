/*
 * StatusController.java
 *
 * Subject to the apache license v. 2.0
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * @author william@xylophones.net
 */

package net.xylophones.micro.game.mb.mvc.status;

import net.xylophones.micro.game.mb.Block;

import java.util.Vector;
import java.util.Enumeration;

/**
 *
 * @author william@xylophones.net
 */
public class StatusController {
    
    public static final int COMMAND_NEXT_BLOCK = 1;
    
    public static final int COMMAND_SCORE_SINGLE = 2;
    
    public static final int COMMAND_SCORE_DOUBLE = 3;
    
    public static final int COMMAND_SCORE_TRIPLE = 4;
    
    public static final int COMMAND_SCORE_TETRIS = 5;
    
    public static final int COMMAND_RESET = 6;

    private StatusModel model;
    
    private int numLevels = 10;
    
    private int numLinesLevel = 10;
    
    private Vector listeners = new Vector();
    
    /** 
     * Creates a new instance of StatusController 
     *
     * @param StatusModel model
     */
    public StatusController(StatusModel model) {
        this.model = model;
    }
    
    /**
     * Perform a command
     * 
     * @param int command
     */
    public void command(int command) {
        switch( command ) {
            case COMMAND_SCORE_SINGLE:
                int points = model.getLevel() * 40;
                model.setScore( model.getScore() + points );
                model.setTotalLines( model.getTotalLines() + 1 );
                notifyListeners(StatusListener.EVENT_SCORE);
                break;
                
            case COMMAND_SCORE_DOUBLE:
                points = model.getLevel() * 100;
                model.setScore( model.getScore() + points );
                model.setTotalLines( model.getTotalLines() + 2 );
                notifyListeners(StatusListener.EVENT_SCORE);
                break;
                
            case COMMAND_SCORE_TRIPLE:
                points = model.getLevel() * 300;
                model.setScore( model.getScore() + points );
                model.setTotalLines( model.getTotalLines() + 3 );
                notifyListeners(StatusListener.EVENT_SCORE);
                break;
                
            case COMMAND_SCORE_TETRIS:
                points = model.getLevel() * 1200;
                model.setScore( model.getScore() + points );
                model.setTotalLines( model.getTotalLines() + 4 );
                notifyListeners(StatusListener.EVENT_SCORE);
                break;
                
            case COMMAND_RESET:
                reset();
                break;

        }
        
        nextLevelCheck();
    }
    
    private void nextLevelCheck() {
        int level = 1 + (model.getTotalLines() / numLinesLevel);
        level = Math.min(numLevels, level);
        
        if (level != model.getLevel()) {
            model.setLevel( level );
            notifyListeners(StatusListener.EVENT_NEW_LEVEL);
        }
    }
    
    private void reset() {
        model.setLevel(1);
        model.setScore(0);
        model.setNextTetrad(Block.TETRAD_Z);
        model.setTotalLines(0);
    }

    public void addListener(StatusListener listener) {
        listeners.addElement(listener);
    }

    public void removeListener(StatusListener listener) {
        listeners.removeElement(listener);
    }

    public void notifyListeners(int event) {
        StatusListener listener;
        Enumeration enumeration = listeners.elements();
        
        while (enumeration.hasMoreElements()) {
            listener = (StatusListener) enumeration.nextElement();
            listener.statusEvent(event);
        }
    }
    
}
