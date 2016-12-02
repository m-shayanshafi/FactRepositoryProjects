/*
 * BoardView.java
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

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.TiledLayer;

import net.xylophones.micro.game.mb.Block;

/**
 * A class for rendering a {@code BoardModel} onto a 
 * {@code Graphics} instnace
 *
 * @author william@xylophones.net
 */
public class BoardView
 {

    /**
     * The layer representing the board
     */
    private TiledLayer background;

    /**
     * The layer representing the block that the user is 
     * currently controlling
     */
    private TiledLayer userBlock;

    /**
     * The model of the board
     */
    private BoardModel model;

    /**
     * The width in pixels of one cell
     */
    private int cellSize;
    
    /**
     * The x ordinate of the top-left corner where this
     * view will be rendered when painted
     */
    private int x;
    
    /**
     * The y ordinate of the top-left corner where this
     * view will be rendered when painted
     */
    private int y;
    
    /**
     * The width of the rendered board in pixels
     */
    private int width = 0;
    
    /**
     * The height of the rendered board in pixels
     */
    private int height = 0;
    
    /**
     * The background colour of the board
     */
    private int backgroundColor = 0x0000AA;
    
    /*
     * The indexes of the TiledLayer image cells for each tetrad
     */
    /*
    private static final int[] tetradImageIndexes = {
        // TETRAD_I
        1,
        // TETRAD_J
        2,
        // TETRAD_L
        3,
        // TETRAD_O
        4,
        // TETRAD_S
        5,
        // TETRAD_T
        6,
        // TETRAD_Z
    };
    */

    /**
     * <p>
     * Paint the board
     * </p>
     *
     * <p>
     * This method just delegates to the {@code TiledLayer} instances
     * </p>
     * 
     * @param g
     */
    public void paint(Graphics g) {
        // paint the background of the board in the appropriate position
        
        updateBoardLayer();
        updateBlockLayer();
        
        g.setColor(backgroundColor);
        g.fillRect(x, y, width, height);
        
        background.paint(g);
        userBlock.paint(g);
    }


    /**
     * <p>
     * Creates a new instance of BoardView   
     * </p>
     * 
     * <p>
     * The {@code tiles} parameter is an image which is used to
     * build {@code TiledLayer} instances which are used for 
     * rendering the {@code BoardModel}. The image must contain
     * eight differnet square cells, each of which is used to colour
     * one tetrad type. 
     * </p>
     * 
     * <p>
     * The width & height of a cell is defined by the height of the {@code tiles}
     * Image. Therefore, the width and height (in pixels) of the full board is 
     * defined to be the height of {@code tiles} x num cells high and the width 
     * is defined to be {@code tiles} x num cells wide.
     * </p>
     * 
     * @param model	The model that will be rendered
     * @param tiles	An image containing the tiles
     */
    public BoardView (BoardModel model, Image tiles) {
        this.model = model;

        cellSize = tiles.getHeight();

        background = new TiledLayer(model.getNumCellsWide(), 
                                    model.getNumCellsHigh(), 
                                    tiles, cellSize, cellSize);

        userBlock = new TiledLayer(4, 4, tiles, cellSize, cellSize);
        
        width  = (model.getNumCellsWide()) * cellSize;
        height = (model.getNumCellsHigh()) * cellSize;
    }

    /**
     * Update the block shape to represent the current tetrad and rotation
     */
    private void updateBlockLayer() {
        boolean[][] shape = Block.getShape( model.getTetrad(), model.getRotation() );
        
        // get the index of the tile in the TiledLayer
        // which is correct for the tetrad that is in play.
        int tileIndex = model.getTetrad() + 1; //tetradImageIndexes[model.getTetrad()];
        
        for (int i=0 ; i<4 ; i++) {
            for (int j=0 ; j<4 ; j++) {
                if (shape[i][j]) {
                    userBlock.setCell(i, j, tileIndex);
                } else {
                    userBlock.setCell(i, j, 0);
                }
            }
        }
        
        userBlock.setPosition( cellSize*model.getBlockCellX(), 
                               cellSize*model.getBlockCellY() );
    }
    
    /**
     * 
     */
    private void updateBoardLayer() {
        for (int y=0 ; y<model.getNumCellsHigh() ; y++) {
           for (int x=0 ; x<model.getNumCellsWide() ; x++) {
               if (model.getCellContents(x, y) == BoardModel.BLOCK_NULL) {
                   background.setCell(x, y, 0);
               } else if (model.getCellContents(x, y) != BoardModel.BLOCK_OFFSCREEN ) {
                    try {
                        background.setCell(x, y, model.getCellContents(x, y) + 1);
                    } catch (Exception e) {
                    }
                }
            }
        }
    }
    
    /**
     * Set the x,y pixel position of the view
     *
     * @param x
     * @param y
     */
    public void setPosition(int x, int y) {
        this.x = x;
        
        this.y = y;
    }
    
    /**
     * Get the width of the full board area in pixels
     * 
     * @return
     */
    public int getWidth() {
        return width;
    }
    
    /**
     * Get the height of the full board area in pixels
     * 
     * @return
     */
    public int getHeight() {
        return height;
    }
}
