package org.gcs.robot;

import java.awt.Point;
import java.util.Observable;
import java.util.Random;
import static java.awt.event.KeyEvent.*;
import static java.lang.Math.*;

/**
 * The RCModel class models the game board as a two dimensional array.
 * The array contains <i>height</i> rows and <i>width</i> columns.
 * Public constants indicate the meaning of an array element; a
 * value of two or more represents a collision. The player's
 * position is a Point representing the current row & column.
 *
 * The player's moves are handled by the {@link #move(int) move()}
 * methods. Overloaded versions of {@link #move(int, int) move()}
 * are used for mouse input.
 *
 * @author John B. Matthews
 */
public class RCModel extends Observable {

    /** The smallest permitted board is 8x8. */
    public static final int MINTILE = 8;

    /** This cell contains an electified post */
    public static final int POST = -2;

    /** This cell contains a bomb */
    public static final int BOMB = -1;

    /** This cell is empty */
    public static final int EMPTY = 0;

    /** This cell contains a robot */
    public static final int ROBOT = 1;

    /** This cell is a collision among two or more robots */
    public static final int WRECK = 2;

    private static final int NW    = VK_NUMPAD7;
    private static final int NORTH = VK_NUMPAD8;
    private static final int NE    = VK_NUMPAD9;
    private static final int WEST  = VK_NUMPAD4;
    private static final int STAY  = VK_NUMPAD5;
    private static final int EAST  = VK_NUMPAD6;
    private static final int SW    = VK_NUMPAD1;
    private static final int SOUTH = VK_NUMPAD2;
    private static final int SE    = VK_NUMPAD3;
    private static final int JUMP  = VK_NUMPAD0;
    
    private int [][] board; // the game board
    private int pRow, pCol; // player's row & column
    private int width;
    private int height;
    private int level;
    private int liveRobots;
    private int deadRobots;
    private int safeJumps;
    private Random random = new Random(System.currentTimeMillis());

    /**
     * Construct a game board with the given dimensions.
     *
     * @param width board width measured in tiles
     * @param height board height measured in tiles
     */
    public RCModel(int width, int height) {
        newGame(width, height);
    }

    /**
     * Resize the game board array.
     * Notification is deferred to give observers an opportunity
     * to adjust. The observer may call initLevel() when ready.
     * 
     * @param width board width measured in tiles
     * @param height board height measured in tiles
     */
    public void resizeArray(int width, int height) {
        newGame(width, height);
    }

    // initialization common to constructor and resize()
    private void newGame(int width, int height) {
        this.width = max(width, MINTILE);
        this.height = max(height, MINTILE);
        this.board = new int [this.height][this.width];
    }

    /** Reset the game and initialize the first level. */
    public void resetGame() {
        this.level = RCPrefs.DEFAULT_LEVEL;
        this.deadRobots = RCPrefs.DEFAULT_SCORE;
        this.safeJumps = RCPrefs.DEFAULT_JUMPS;
        initLevel();
    }

    /**
     * Restore the game and initialize the current level.
     * @param level the previous level
     * @param score the previous score
     * @param jumps the previous jumps
     */
    public void restoreGame(int level, int score, int jumps) {
        this.level = level;
        this.deadRobots = score;
        this.safeJumps = jumps;
        initLevel();
    }

    /** Advance to the next level and initialize it. */
    public void newLevel() {
        this.level++;
        this.safeJumps++;
        initLevel();
    }

    /** Initialize the current level and notify any observers. */
    public void initLevel() { 
        int row, col;
        // clean house
        for (int i = 0; i < height; i++) 
            for (int j = 0; j < width; j++) 
                board[i][j] = EMPTY;
        // scatter some ROBOTS
        int botCount = min(level + 5, 2 * max(width, height));
        liveRobots = botCount;
        while (botCount > 0) {
            row = randomRow();
            col = randomCol();
            if (board[row][col] == EMPTY) {
                board[row][col] = ROBOT;
                botCount--;
            }
        }
        // now POSTS
        int postCount = randomInt(
            min(width, height) + 5, 2 * (max(width, height) + 5));
        while (postCount > 0) {
            row = randomRow();
            col = randomCol();
            if (board[row][col] == EMPTY) {
                board[row][col] = POST;
                postCount--;
            }
        }
        // and BOMBS
        int bombCount = randomInt(width / 5, width / 5 + 2);
        while (bombCount > 0) {
            row = randomRow();
            col = randomCol();
            if (board[row][col] == EMPTY) {
                board[row][col] = BOMB;
                bombCount--;
            }
        }
        // finally, our hero
        do {
            pRow = randomRow();
            pCol = randomCol();
        } while (board[pRow][pCol] != 0);
        this.setChanged();
        this.notifyObservers();
    }

    /** Return the curent game level. */
    public int getLevel() { return level; }

    /** Return the curent score. */
    public int getDeadRobots() { return deadRobots; }

    /** Return the number of robots left on this level. */
    public int getLiveRobots() { return liveRobots; }

    /** Return the number of safe jumps left. */
    public int getSafeJumps() { return safeJumps; }

    /** Return the game board array. */
    public int [][] getBoard() { return board; }

    /** Return the game board Width in Tiles. */
    public int getWidth() { return width; }

    /** Return the game board Height in Tiles. */
    public int getHeight() { return height; }

    /**
     * Return the player's current position as a <code>java.awt.Point</code>.
     * This point represents a two-dimensional array index:
     * <code>point.x</code> holds the column index and
     * <code>point.y</code> holds the row index.
     *
     * @return the player's board position in row, column order
     */
    public Point getPosition() { return new Point(pCol, pRow); }

    /** Return true if the player has died; false otherwise. */
    public boolean lost() { return board[pRow][pCol] > EMPTY; }

    /** Return true if the player has won; false otherwise. */
    public boolean won() { return countBots(board) == 0; }

    /**
     * Move in the given direction. Non-numeric values are ignored.
     *
     * @param key a numeric keypad key code (96-105)
     * @return true if a safe move was possible
     */
    public boolean move(int key) {
        int oldRow = pRow; 
        int oldCol = pCol;
        switch(key) {
            case NW:    pRow--; pCol--; break;
            case NORTH: pRow--;         break;
            case NE:    pRow--; pCol++; break; 
            case WEST:          pCol--; break;
            case STAY:                  break;
            case EAST:          pCol++; break;
            case SW:    pRow++; pCol--; break;
            case SOUTH: pRow++;         break;
            case SE:    pRow++; pCol++; break;
            case JUMP:  jump(); return false;
            default: return false;
        }
        if (safe(pRow, pCol)) {
            moveBots();
            return true;
        }
        else { // restore original position
            pRow = oldRow;
            pCol = oldCol;
            return false;
        }
    }

    /**
     * Move toward the specified row and column.
     *
     * @param row the given row
     * @param col the given column
     * @return true if a safe move was possible
     */
    public boolean move(int row, int col) {
        if      (row <  pRow && col <  pCol) return move(NW);
        else if (row <  pRow && col == pCol) return move(NORTH);
        else if (row <  pRow && col >  pCol) return move(NE);
        else if (row == pRow && col <  pCol) return move(WEST);
        else if (row == pRow && col == pCol) return move(STAY);
        else if (row == pRow && col >  pCol) return move(EAST);
        else if (row >  pRow && col <  pCol) return move(SW);
        else if (row >  pRow && col == pCol) return move(SOUTH);
        else if (row >  pRow && col >  pCol) return move(SE);
        return false;
    }

    /** Move by jumping to a random location.*/
    public void move() {
        jump();
    }

    private void jump() {
        if (safeJumps > 0) {
            do {
                pRow = randomRow();
                pCol = randomCol();
            } while (!safe(pRow, pCol));
            safeJumps--;
        } else {
            pRow = randomRow();
            pCol = randomCol();
        }
        moveBots();
    }

    /** Move the robots; cull the deceased; award prizes. */
    private void moveBots() {
        int rRow; // robot's destination row
        int rCol; // robot's destination column
        int dest; // destination content
        int [][] clone = new int [height][width];
        for (int i = 0; i < height; i++) 
            System.arraycopy(board[i], 0, clone[i], 0, width);

        for (int i = 0; i < height; i++) 
            for (int j = 0; j < width; j++) 
                if (clone[i][j] == ROBOT) {
                    rRow = i;
                    if (i < pRow) rRow++;
                    else if (i > pRow) rRow--;
                    rCol = j;
                    if (j < pCol) rCol++;
                    else if (j > pCol) rCol--;
                    board[i][j]--;
                    dest = board[rRow][rCol];
                    if (dest == BOMB) safeJumps++;
                    if (dest > POST) board[rRow][rCol]++;
                }
        int newBots = countBots(board);
        int oldBots = countBots(clone);
        deadRobots += oldBots - newBots;
        liveRobots = newBots;

        this.setChanged();
        this.notifyObservers();
    }

    /** Count the number of robots on the specified board. */
    private int countBots(int [][] b) {
        int result = 0;
        for (int i = 0; i < b.length; i++)
            for (int j = 0; j < b[0].length; j++) 
                if (b[i][j] == ROBOT) result++;
        return result;
    }

    /** Return false if (row, col) is off board, occupied or threatened */
    private boolean safe(int row, int col) {
        if (offBoard(row, col)) return false;
        if (board[row][col] != EMPTY) return false;
        for (int i = row - 1; i <= row + 1; i++) 
            for (int j = col - 1; j <= col + 1; j++)
                if (offBoard(i, j)) continue;
                else if (board[i][j] == ROBOT) return false;
        return true;
    }

    private boolean offBoard(int r, int c) {
        return r < 0 || r >= height || c < 0 || c >= width;
    }

    private int randomInt(int lo, int hi) {
        return lo + random.nextInt(hi - lo + 1);
    }

    private int randomRow() {
        return random.nextInt(height);
    }

    private int randomCol() {
        return random.nextInt(width);
    }

}
