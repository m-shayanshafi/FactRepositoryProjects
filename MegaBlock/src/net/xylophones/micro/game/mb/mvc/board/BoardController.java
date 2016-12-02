/*
 * BoardController.java
 *
 * Subject to the apache license v. 2.0
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * @author william@xylophones.net
 */

package net.xylophones.micro.game.mb.mvc.board;

import java.util.Enumeration;
import java.util.Vector;

import net.xylophones.micro.game.mb.Block;
import net.xylophones.micro.game.mb.geometry.Point;

/**
 *
 * @author william@xylophones.net
 */
public class BoardController {

    public static final int COMMAND_LEFT = 1;

    public static final int COMMAND_RIGHT = 2;

    public static final int COMMAND_DOWN = 3;

    public static final int COMMAND_ROTATE = 4;
    
    public static final int COMMAND_PREPARE_BLOCK = 5;
    
    public static final int COMMAND_RESET = 6;

    private static final int MOVE_LEFT = 1;

    private static final int MOVE_RIGHT = 2;

    private static final int MOVE_DOWN = 3;

    private BoardModel model;
    
    private Vector listeners = new Vector();

    /**
     * Creates a new instance of BoardController 
     */
    public BoardController (BoardModel model) {
        this.model = model;
    }

    public void command(int command) {
        switch(command) {
            case COMMAND_LEFT:
                if (canMoveLeft(model.getTetrad(), model.getRotation())) {
                    moveBlock(MOVE_LEFT);
                }
                break;
                
            case COMMAND_RIGHT:
                if (canMoveRight(model.getTetrad(), model.getRotation())) {
                    moveBlock(MOVE_RIGHT);
                }
                break;
                
            case COMMAND_ROTATE:
                if (canRotateBlock(model.getTetrad(), model.getRotation())) {
                    rotateBlock();
                }
                break;
                
            case COMMAND_DOWN:
                if ( canMoveDown(model.getTetrad(), model.getRotation()) ) {
                    moveBlock(MOVE_DOWN);
                } else {
                    landBlock();
                }

                break;
                
            case COMMAND_PREPARE_BLOCK:
                prepareBlock();               
                break;
                
            case COMMAND_RESET:
                resetModel();
                break;
        }
    }

    private void resetModel() {
        model.clearBoard();
        model.setBlockCellX( model.getNumCellsWide() / 2 );
        model.setRotation(Block.ROTATION_0);
    }
    
    private void prepareBlock() {
        model.setBlockCellX( model.getNumCellsWide() / 2 );
        model.setRotation(Block.ROTATION_0);

        // determine what the y index should be depending on the shape of the piece
        Point[] firstBottomCells = Block.firstBottomCells(model.getTetrad(), model.getRotation());

        model.setBlockCellY(-4);
        int maxY = -100;

        for (int i=0 ; i<firstBottomCells.length ; i++) {
            maxY = Math.max(maxY, firstBottomCells[i].getY());
        }
        model.setBlockCellY( model.getBlockCellY() + 3 - maxY );

        if ( canMoveDown(model.getTetrad(), model.getRotation()) ) {
            moveBlock(MOVE_DOWN);
        } else {
            notifyListeners(BoardListener.EVENT_BOARD_FULL);
        }
    }

    private boolean canMoveLeft(int tetrad, int transform) {
        Point[] rightCells = Block.firstLeftCells(tetrad, transform);

        boolean canMove = true;

        // check to see if there is a space below & if it is blocked

        for(int i=0 ; i<rightCells.length ; i++) {
            int cellXPosition = rightCells[i].getX() + model.getBlockCellX();
            int cellYPosition = rightCells[i].getY() + model.getBlockCellY();

            if ( cellXPosition + model.getBlockCellX() < 0 ) {
                canMove = false;
                break;
            } else if ( model.getCellContents(cellXPosition - 1, cellYPosition)  != BoardModel.BLOCK_NULL ) {
                canMove = false;
                break;
            }
        }
        
        return canMove;
    }
    
    private boolean canMoveRight(int tetrad, int transform) {
        Point[] rightCells = Block.firstRightCells(tetrad, transform);
        boolean canMove = true;

        // check to see if there is a space below & if it is blocked

        for(int i=0 ; i<rightCells.length ; i++) {
            int cellXPosition = rightCells[i].getX() + model.getBlockCellX();
            int cellYPosition = rightCells[i].getY() + model.getBlockCellY();

            if ( cellXPosition + 1 >= model.getNumCellsWide() ) {
                canMove = false;
                break;
            } else if ( model.getCellContents(cellXPosition + 1, cellYPosition) != BoardModel.BLOCK_NULL ) {
                canMove = false;
                break;
            }
        }
        
        return canMove;
    }
    
    private boolean canMoveDown(int tetrad, int transform) {
        Point[] bottomCells = Block.firstBottomCells(tetrad, transform);
        boolean canMove = true;

        // check to see if there is a space below or if it is blocked

        for(int i=0 ; i<bottomCells.length ; i++) {
            int cellXPosition = bottomCells[i].getX() + model.getBlockCellX();
            int cellYPosition = bottomCells[i].getY() + model.getBlockCellY();

            if ( cellYPosition >= model.getNumCellsHigh()-1 ) {
                canMove = false;
                break;
            } else if ( model.getCellContents(cellXPosition, cellYPosition + 1)  != BoardModel.BLOCK_NULL
                        && model.getCellContents(cellXPosition, cellYPosition + 1) != BoardModel.BLOCK_OFFSCREEN ) {
                canMove = false;
                break;
            }
        }
        
        return canMove;
    }

    private void moveBlock(int direction) {
        switch(direction) {
            case MOVE_LEFT:
                model.setBlockCellX(model.getBlockCellX() - 1);
                break;
            case MOVE_RIGHT:
                model.setBlockCellX(model.getBlockCellX() + 1);
                break;
            case MOVE_DOWN:
                model.setBlockCellY(model.getBlockCellY() + 1);
                break;
        }
    }

    private boolean canRotateBlock(int tetrad, int transform) {
        boolean[][] shape = Block.getShape( model.getTetrad(), determineNextTransform() );

        for (int x=0 ; x<4 ; x++) {
            for (int y=0 ; y<4 ; y++) {
                if (shape[x][y]) {
                    int cellXPosition = x + model.getBlockCellX();
                    int cellYPosition = y + model.getBlockCellY();
                    
                    if ( model.getCellContents(cellXPosition, cellYPosition) != BoardModel.BLOCK_NULL ) {
                        return false;
                    }
                }
            }
        }
        
        return true;
    }

    private void rotateBlock() {
        model.setRotation( determineNextTransform() );
    }
    
    /**
     * Check if the block is landing & if so then land it
     */
    private void landBlock() {
        boolean[][] shape = Block.getShape( model.getTetrad(), model.getRotation() );

        for (int x=0 ; x<4 ; x++) {
            for (int y=0 ; y<4 ; y++) {
                if (shape[x][y]) {
                    int cellXPosition = x + model.getBlockCellX();
                    int cellYPosition = y + model.getBlockCellY();

                    model.setCellContents(cellXPosition, cellYPosition, model.getTetrad());
                }
            }
        }
        
        notifyListeners(BoardListener.EVENT_BLOCK_LANDED);
        
        linesCheck();
    }

    public void addListener(BoardListener listener) {
        listeners.addElement(listener);
    }

    public void removeListener(BoardListener listener) {
        listeners.removeElement(listener);
    }

    public void notifyListeners(int event) {
        BoardListener listener;
        Enumeration enumeration = listeners.elements();
        
        while (enumeration.hasMoreElements()) {
            listener = (BoardListener) enumeration.nextElement();
            listener.boardEvent(event);
        }
    }

    /**
     * @return int
     */
    private int determineNextTransform() {
        return (model.getRotation() + 1) % 4;
    }
    
    /**
     * Check the board for complete lines & handle them
     * 
     * return the number of lines handled
     *
     * @return
     */
    private int linesCheck() {
        int numCleared = 0;
        
        for (int y=model.getNumCellsHigh()-1 ; y>=0 ; y--) {
            if ( checkClearRow(y) ) {
                // didn't call continue so clear the line
                clearRow(y);
                // test the same line again by incrementing y counter
                y++;
                // increment our cleared line counter
                numCleared++;
            }
        }
                
        switch(numCleared) {
            case 4:
                notifyListeners(BoardListener.EVENT_LINE_TETRIS);
                break;
            case 3:
                notifyListeners(BoardListener.EVENT_LINE_TRIPLE);
                break;
            case 2:
                notifyListeners(BoardListener.EVENT_LINE_DOUBLE);
                break;
            case 1:
                notifyListeners(BoardListener.EVENT_LINE_SINGLE);
                break;
        }
        
        return numCleared;
    }
    
    private boolean checkClearRow(int y) {
        for (int x=0 ; x<model.getNumCellsWide() ; x++) {
            if ( model.getCellContents(x, y) == BoardModel.BLOCK_NULL
                 || model.getCellContents(x, y) == BoardModel.BLOCK_OFFSCREEN ) {
                return false;
            }
        }
        
        return true;
    }
    
    private void clearRow(int y) {
        for (int Y=y ; Y>=0 ; Y-- ) {
            for (int x=0 ; x<model.getNumCellsWide() ; x++) {
                if ( model.getCellContents(x, Y-1) != BoardModel.BLOCK_OFFSCREEN ) {
                    model.setCellContents(x, Y, model.getCellContents(x, Y-1));
                }
            }
        }
    }
}
