/*
 * StatusModel.java
 *
 * Subject to the apache license v. 2.0
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * @author william@xylophones.net
 */

package net.xylophones.micro.game.mb.mvc.status;

import net.xylophones.micro.game.mb.Block;

/**
 *
 * @author william@xylophones.net
 */
public class StatusModel {
    
    private int level = 1;
    
    private int score = 0;
    
    private int highScore = 0;
    
    private int nextTetrad = Block.TETRAD_Z;
    
    private int totalLines = 0;
    
    /**
     * Creates a new instance of StatusModel
     */
    public StatusModel() {
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getHighScore() {
        return highScore;
    }

    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }

    public int getNextTetrad () {
        return nextTetrad;
    }

    public void setNextTetrad(int nextTetrad) {
        this.nextTetrad = nextTetrad;
    }

    public int getTotalLines() {
        return totalLines;
    }

    public void setTotalLines(int totalLines) {
        this.totalLines = totalLines;
    }
    
}
