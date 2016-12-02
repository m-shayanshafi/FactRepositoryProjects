/*
 * BoardModel.java
 * 
 * Copyright 2007 William Robertson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package net.xylophones.micro.game.mb.mvc.board;

import net.xylophones.micro.game.mb.Block;

/**
 * This class holds all data relating to the current state
 * of the game board. This includes the current tetrad, the 
 * current 
 *
 * @author william@xylophones.net
 */
public class BoardModel {
    
    /**
     * X cell of block 
     */
    private int blockCellX;
    
    /**
     * Y cell of block 
     */
    private int blockCellY;
    
    /**
     * constant representing an empty cell
     */
    public final static int BLOCK_NULL = -1;
    
    /**
     * constant representing an cell offscreen
     */
    public final static int BLOCK_OFFSCREEN = -2;
    
    /**
     * Transform of the tetrad in play
     */
    private int rotation = Block.ROTATION_0;
    
    /**
     * Current tetrad in play
     */
    private int tetrad = Block.TETRAD_O;

    /**
     * Width of the board in cells
     */
    private int numCellsWide;
    
    /**
     * Height of the board in cells
     */
    private int numCellsHigh;
    
    /**
     * Holds the contents of each cell on the board
     */
    private int board[][];

    /**
     * Creates a new instance of BoardModel
     * 
     * @param numCellsWide
     * @param numCellsHigh
     */
    public BoardModel (int numCellsWide, int numCellsHigh) {
        this.numCellsWide = numCellsWide;
        this.numCellsHigh = numCellsHigh;
        
        board = new int[numCellsWide][numCellsHigh];
        
        clearBoard();
    }
    
    /**
     * Reset the board to only contain null (empty) cells 
     */
    public void clearBoard() {
        for (int i=0 ; i<numCellsWide ; i++) {
            for (int j=0 ; j<numCellsHigh ; j++) {
                board[i][j] = BLOCK_NULL;
            }
        }
    }
    
    /**
     * <p>
     * Get the current tetrad type
     * </p>
     * 
     * <p>
     * {@code Block.TETRAD_I, Block.TETRAD_J, Block.TETRAD_L} etc.
     * </p>
     * 
     * @return
     */
    public int getTetrad () {
        return tetrad;
    }

    /**
     * <p>
     * Set the current tetrad type
     * </p>
     * 
     * <p>
     * {@code Block.TETRAD_I, Block.TETRAD_J, Block.TETRAD_L} etc.
     * </p>
     * 
     * @param tetrad
     */
    public void setTetrad(int tetrad) {
        this.tetrad = tetrad;
    }

    /**
     * <p>
     * Get the rotation of the current tetrad
     * </p>
     * 
     * <p>
     * {@code ROTATION_0, ROTATION_90, ROTATION_180} or {@code ROTATION_270}
     * </p>
     * 
     * @return
     */
    public int getRotation() {
        return rotation;
    }

    /**
     * Set the rotation of the current tetrad
     * 
     * @param rotation
     *
     * @see getRotation()
     */
    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

    /**
     * Get the width of the board in cells
     * 
     * @return
     */
    public int getNumCellsWide() {
        return numCellsWide;
    }

    /**
     * Set the width of the board in cells
     * 
     * @param numCellsWide
     */
    public void setNumCellsWide(int numCellsWide) {
        this.numCellsWide = numCellsWide;
    }

    /**
     * Get the hieght of the board in cells
     * 
     * @return
     */
    public int getNumCellsHigh() {
        return numCellsHigh;
    }

    /**
     * Set the hieght of the board in cells
     * 
     * @param numCellsHigh
     */
    public void setNumCellsHigh(int numCellsHigh) {
        this.numCellsHigh = numCellsHigh;
    }

    /**
     * Get the x ordinate of the player's block, which
     * contains the tetrad shape. The x ordinate is the top 
     * leftmost cell of the block
     * 
     * @return
     */
    public int getBlockCellX() {
        return blockCellX;
    }

    /**
     * Set the x ordinate of the player's block, which
     * contains the tetrad shape. The x ordinate is the  
     * leftmost cell of the block
     * 
     * @param blockCellX
     */
    public void setBlockCellX(int blockCellX) {
        this.blockCellX = blockCellX;
    }

    /**
     * Get the y ordinate of the player's block, which
     * contains the tetrad shape. The y ordinate is the top 
     * cell of the block
     * 
     * @return
     */
    public int getBlockCellY() {
        return blockCellY;
    }

    /**
     * Set the y ordinate of the player's block, which
     * contains the tetrad shape. The y ordinate is the top 
     * cell of the block
     * 
     * @param blockCellY
     */
    public void setBlockCellY(int blockCellY) {
        this.blockCellY = blockCellY;
    }

    /**
     * Get the contents of a cell.
     * 
     * @param x
     * @param y
     *
     * @return
     *
     * @see setCellContents(int x, int y, int tetradType)
     */
    public int getCellContents(int x, int y) {
        if (x < 0 || x >=getNumCellsWide() || y<0 || y>=getNumCellsHigh()) {
            return BLOCK_OFFSCREEN;
        }
        
        return board[x][y];
    }
	
    /**
     * <p>
     * Set the contents of a cell 
     * </p>
     *
     * <p>
     * {@code BLOCK_NULL, BLOCK_OFFSCREEN} or one of the {@code Block.TETRAD_X} 
	 * constants  
     * </p>
     * 
     * @param x
     * @param y
     * @param tetradType
     * 
     * @return
     *
     * @see getCellContents(int x, int y)
     */
    public boolean setCellContents(int x, int y, int tetradType) {
        if (x < 0 || x >=getNumCellsWide() || y<0 || y>=getNumCellsHigh())  {
            return false;
        }
        
        board[x][y] = tetradType;
        return true;
    }
    
}
