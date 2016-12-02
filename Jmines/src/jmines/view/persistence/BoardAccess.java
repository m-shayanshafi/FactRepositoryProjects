/*
 * This file is part of JMines.
 * Copyright (C) 2009 Zleurtor
 *
 * JMines is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JMines is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with JMines.  If not, see <http://www.gnu.org/licenses/>.
 */
package jmines.view.persistence;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JOptionPane;

import jmines.control.listeners.MouseListenerForGamePanel.Action;
import jmines.view.components.MainPanel;

/**
 * The class used to manage board files.
 *
 * @author Zleurtor
 */
public final class BoardAccess {

    //==========================================================================
    // Static attributes
    //==========================================================================
    /**
     * The mask used to save a mined cell.
     */
    public static final byte MASK_NULL = -1;
    /**
     * The mask used to save a mined cell.
     */
    public static final byte MASK_MINED = 1; /* 1 */
    /**
     * The mask used to save a opened cell.
     */
    public static final byte MASK_OPENED = 1 << 1; /* 2 */
    /**
     * The mask used to save a flagged cell.
     */
    public static final byte MASK_FLAGGED = 1 << 1 << 1; /* 4 */
    /**
     * The mask used to save a marked cell.
     */
    public static final byte MASK_MARKED = 1 << 1 << 1 << 1; /* 8 */

    //==========================================================================
    // Attributes
    //==========================================================================

    //==========================================================================
    // Constructors
    //==========================================================================
    /**
     * Construct a new Board Manager. Do nothing. Never used.
     */
    private BoardAccess() {
    }

    //==========================================================================
    // Getters
    //==========================================================================

    //==========================================================================
    // Setters
    //==========================================================================

    //==========================================================================
    // Inherited methods
    //==========================================================================

    //==========================================================================
    // Static methods
    //==========================================================================
    /**
     * Save a JMines Board file filled using the given main panel.
     *
     * @param mainPanel The main panel for which the board will be saved.
     * @param file The file to save the board in.
     * @throws IOException If an exception occurs while saving the board.
     */
    public static void saveBoard(final MainPanel mainPanel, final File file) throws IOException {
        // Open the file
        OutputStream stream = null;
        if (!file.exists()) {
            file.createNewFile();
        }

        try {
            stream = (OutputStream) Configuration.getInstance().getClassHuffmanFileOutputStream().getConstructor(File.class).newInstance(file);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", Configuration.getInstance().getText(Configuration.KEY_TITLE_ERROR), JOptionPane.ERROR_MESSAGE);
            return;
        } catch (SecurityException e) {
            JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", Configuration.getInstance().getText(Configuration.KEY_TITLE_ERROR), JOptionPane.ERROR_MESSAGE);
            return;
        } catch (InstantiationException e) {
            JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", Configuration.getInstance().getText(Configuration.KEY_TITLE_ERROR), JOptionPane.ERROR_MESSAGE);
            return;
        } catch (IllegalAccessException e) {
            JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", Configuration.getInstance().getText(Configuration.KEY_TITLE_ERROR), JOptionPane.ERROR_MESSAGE);
            return;
        } catch (InvocationTargetException e) {
            JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", Configuration.getInstance().getText(Configuration.KEY_TITLE_ERROR), JOptionPane.ERROR_MESSAGE);
            return;
        } catch (NoSuchMethodException e) {
            JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", Configuration.getInstance().getText(Configuration.KEY_TITLE_ERROR), JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Save
        if (mainPanel.getGamePanel().getDifficulty().equals(Configuration.DIFFICULTY_BEGINNER)) {
            stream.write(Action.DIFFICULTY_BEGINNER);
        } else if (mainPanel.getGamePanel().getDifficulty().equals(Configuration.DIFFICULTY_INTERMEDIATE)) {
            stream.write(Action.DIFFICULTY_INTERMEDIATE);
        } else if (mainPanel.getGamePanel().getDifficulty().equals(Configuration.DIFFICULTY_EXPERT)) {
            stream.write(Action.DIFFICULTY_EXPERT);
        } else if (mainPanel.getGamePanel().getDifficulty().equals(Configuration.DIFFICULTY_CUSTOM)) {
            stream.write(Action.DIFFICULTY_CUSTOM);
        }

        stream.write(mainPanel.getGamePanel().getGameBoard().getTilesShape());

        stream.write(mainPanel.getGamePanel().getGameBoard().getWidth());
        stream.write(mainPanel.getGamePanel().getGameBoard().getHeight());
        for (int l = 0; l < mainPanel.getGamePanel().getGameBoard().getHeight(); l++) {
            for (int c = 0; c < mainPanel.getGamePanel().getGameBoard().getWidth(); c++) {
                byte toSave = 0;

                if (mainPanel.getGamePanel().getGameBoard().getTile(l, c) != null) {
                    if (mainPanel.getGamePanel().getGameBoard().getTile(l, c).isContainingMine()) {
                        toSave |= MASK_MINED;
                    }
                    if (mainPanel.getGamePanel().getGameBoard().getTile(l, c).isOpen()) {
                        toSave |= MASK_OPENED;
                    }
                    if (mainPanel.getGamePanel().getGameBoard().getTile(l, c).isFlagged()) {
                        toSave |= MASK_FLAGGED;
                    }
                    if (mainPanel.getGamePanel().getGameBoard().getTile(l, c).isMarked()) {
                        toSave |= MASK_MARKED;
                    }
                } else {
                    toSave = MASK_NULL;
                }

                stream.write(toSave);
            }
        }

        // Close the file
        stream.flush();
        stream.close();
    }

    /**
     * Read the given file and return all the board data contained in.
     *
     * @param file The file to read.
     * @return All the board data contained in the given file
     * @throws IOException If an exception occurs while loading the board.
     */
    public static BoardData loadBoard(final File file) throws IOException {
        // Open the file
        InputStream stream = null;
        try {
            stream = (InputStream) Configuration.getInstance().getClassHuffmanFileInputStream().getConstructor(File.class).newInstance(file);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", Configuration.getInstance().getText(Configuration.KEY_TITLE_ERROR), JOptionPane.ERROR_MESSAGE);
            return null;
        } catch (SecurityException e) {
            JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", Configuration.getInstance().getText(Configuration.KEY_TITLE_ERROR), JOptionPane.ERROR_MESSAGE);
            return null;
        } catch (InstantiationException e) {
            JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", Configuration.getInstance().getText(Configuration.KEY_TITLE_ERROR), JOptionPane.ERROR_MESSAGE);
            return null;
        } catch (IllegalAccessException e) {
            JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", Configuration.getInstance().getText(Configuration.KEY_TITLE_ERROR), JOptionPane.ERROR_MESSAGE);
            return null;
        } catch (InvocationTargetException e) {
            JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", Configuration.getInstance().getText(Configuration.KEY_TITLE_ERROR), JOptionPane.ERROR_MESSAGE);
            return null;
        } catch (NoSuchMethodException e) {
            JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", Configuration.getInstance().getText(Configuration.KEY_TITLE_ERROR), JOptionPane.ERROR_MESSAGE);
            return null;
        }

        // Read the file
        byte difficulty = -1;
        byte width = -1;
        byte height = -1;
        int mines = 0;
        byte shape = -1;
        byte[][] grid = null;

        difficulty = (byte) stream.read();
        shape = (byte) stream.read();
        width = (byte) stream.read();
        height = (byte) stream.read();
        grid = new byte[height][width];
        for (int l = 0; l < height; l++) {
            for (int c = 0; c < width; c++) {
                grid[l][c] = (byte) stream.read();
                if (grid[l][c] != BoardAccess.MASK_NULL && (grid[l][c] & BoardAccess.MASK_MINED) != 0) {
                    mines++;
                }
            }
        }

        return new BoardData(difficulty, shape, width, height, grid, mines);
    }

    //==========================================================================
    // Inner classes
    //==========================================================================
    /**
     * A class used to wrap all the board data contained in a file.
     *
     * @author Zleurtor
     */
    public static final class BoardData {

        //======================================================================
        // Attributes
        //======================================================================
        /**
         * The difficulty of the grid.
         */
        private byte difficulty;
        /**
         * The buttons shape of the grid.
         */
        private byte shape;
        /**
         * The grid width.
         */
        private byte width;
        /**
         * The grid height.
         */
        private byte height;
        /**
         * The grid.
         */
        private byte[][] grid;
        /**
         * The number of mines contained in the grid.
         */
        private int mines;

        //======================================================================
        // Constructors
        //======================================================================
        /**
         * Construct a new board data wrapper.
         *
         * @param newDifficulty The difficulty of the grid.
         * @param newShape The buttons shape of the grid.
         * @param newWidth The grid width.
         * @param newHeight The grid height.
         * @param newGrid The grid.
         * @param newMines The number of mines contained in the grid.
         */
        public BoardData(final byte newDifficulty, final byte newShape, final byte newWidth, final byte newHeight, final byte[][] newGrid, final int newMines) {
            this.difficulty = newDifficulty;
            this.shape = newShape;
            this.width = newWidth;
            this.height = newHeight;
            this.grid = newGrid;
            this.mines = newMines;
        }

        //======================================================================
        // Getters
        //======================================================================
        /**
         * Returns the difficulty of the grid.
         *
         * @return The difficulty of the grid.
         */
        public byte getDifficulty() {
            return difficulty;
        }

        /**
         * Returns the buttons shape of the grid.
         *
         * @return The buttons shape of the grid.
         */
        public byte getShape() {
            return shape;
        }

        /**
         * Returns the grid width.
         *
         * @return The grid width.
         */
        public byte getWidth() {
            return width;
        }

        /**
         * Returns the grid height.
         *
         * @return The grid height.
         */
        public byte getHeight() {
            return height;
        }

        /**
         * Returns the grid.
         *
         * @return The grid.
         */
        public byte[][] getGrid() {
            return grid;
        }

        /**
         * Returns the number of mines contained in the grid.
         *
         * @return The number of mines contained in the grid.
         */
        public int getMines() {
            return mines;
        }
    }

}
