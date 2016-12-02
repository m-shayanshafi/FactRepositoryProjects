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
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import jmines.control.listeners.MouseListenerForGamePanel.Action;
import jmines.view.components.MainPanel;

/**
 * The class used to manage video files.
 *
 * @author Zleurtor
 */
public final class VideoAccess {

    //==========================================================================
    // Static attributes
    //==========================================================================

    //==========================================================================
    // Attributes
    //==========================================================================

    //==========================================================================
    // Constructors
    //==========================================================================
    /**
     * Construct a new Video Manager. Do nothing. Never used.
     */
    private VideoAccess() {
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
     * Save a JMines Video file filled using the given list of game actions.
     *
     * @param actions The List of game actions to save.
     * @param mainPanel The main panel for which the video will be saved.
     * @param file The file to save the video in.
     * @throws IOException If an exception occurs while saving the video.
     */
    public static void saveVideo(final List<Action> actions, final MainPanel mainPanel, final File file) throws IOException {
        // Open the file
        OutputStream stream = null;
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            stream = (OutputStream) Configuration.getInstance().getClassHuffmanFileOutputStream().getConstructor(File.class).newInstance(file);
        } catch (InstantiationException e) {
            JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", Configuration.getInstance().getText(Configuration.KEY_TITLE_ERROR), JOptionPane.ERROR_MESSAGE);
            return;
        } catch (IllegalAccessException e) {
            JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", Configuration.getInstance().getText(Configuration.KEY_TITLE_ERROR), JOptionPane.ERROR_MESSAGE);
            return;
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", Configuration.getInstance().getText(Configuration.KEY_TITLE_ERROR), JOptionPane.ERROR_MESSAGE);
            return;
        } catch (SecurityException e) {
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
        int size = ((Long.SIZE + Byte.SIZE + Byte.SIZE + Byte.SIZE) * actions.size()) / Byte.SIZE;

        ByteBuffer buffer = ByteBuffer.allocate(size);
        for (Action action : actions) {
            buffer.putLong(action.getDate() - Action.getStartDate());
            buffer.put(action.getId());
            buffer.put(action.getLine());
            buffer.put(action.getColumn());
        }

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
                    if (mainPanel.getGamePanel().getGameBoard().getTile(l, c) != null && mainPanel.getGamePanel().getGameBoard().getTile(l, c).isContainingMine()) {
                        stream.write(1);
                    } else {
                        stream.write(0);
                    }
                }
            }

            for (int i = 0; i < buffer.capacity(); i++) {
                stream.write(buffer.get(i));
            }

            stream.flush();
            stream.close();
    }

    /**
     * Read the given file and return all the video data contained in.
     *
     * @param file The file to read.
     * @return All the video data contained in the given file
     * @throws IOException If an exception occurs while loading the video.
     */
    public static VideoData loadVideo(final File file) throws IOException {
        // Open the file
        InputStream stream = null;
        try {
            stream = (InputStream) Configuration.getInstance().getClassHuffmanFileInputStream().getConstructor(File.class).newInstance(file);
        } catch (InstantiationException e) {
            JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", Configuration.getInstance().getText(Configuration.KEY_TITLE_ERROR), JOptionPane.ERROR_MESSAGE);
        } catch (IllegalAccessException e) {
            JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", Configuration.getInstance().getText(Configuration.KEY_TITLE_ERROR), JOptionPane.ERROR_MESSAGE);
        } catch (InvocationTargetException e) {
            JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", Configuration.getInstance().getText(Configuration.KEY_TITLE_ERROR), JOptionPane.ERROR_MESSAGE);
        } catch (NoSuchMethodException e) {
            JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", Configuration.getInstance().getText(Configuration.KEY_TITLE_ERROR), JOptionPane.ERROR_MESSAGE);
        }

        if (stream == null) {
            return null;
        }

        // Read the file
        byte difficulty = -1;
        byte width = -1;
        byte height = -1;
        int mines = 0;
        byte shape = -1;
        byte[][] grid = null;
        ByteBuffer buffer = null;

        difficulty = (byte) stream.read();
        shape = (byte) stream.read();
        width = (byte) stream.read();
        height = (byte) stream.read();
        grid = new byte[height][width];
        for (int l = 0; l < height; l++) {
            for (int c = 0; c < width; c++) {
                grid[l][c] = (byte) stream.read();
                if (grid[l][c] == 1) {
                    mines++;
                }
            }
        }

        buffer = ByteBuffer.allocate(stream.available());
        byte[] array = new byte[stream.available()];
        stream.read(array);
        stream.close();
        for (byte b : array) {
            buffer.put(b);
        }
        buffer.rewind();

        // Store the actions
        final List<Action> actions = new ArrayList<Action>();
        while (buffer.position() < buffer.limit()) {
            long date = buffer.getLong();
            byte id = buffer.get();
            byte line = buffer.get();
            byte column = buffer.get();

            Action action = new Action(id, line, column, date);
            actions.add(action);
        }

        return new VideoData(difficulty, shape, width, height, grid, mines, actions);
    }

    //==========================================================================
    // Inner classes
    //==========================================================================
    /**
     * A class used to wrap all the video data contained in a file.
     *
     * @author Zleurtor
     */
    public static final class VideoData {

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
        /**
         * All the actions that have to be replayed.
         */
        private List<Action> actions;

        //======================================================================
        // Constructors
        //======================================================================
        /**
         * Construct a new video data wrapper.

         * @param newDifficulty The difficulty of the grid.
         * @param newShape The buttons shape of the grid.
         * @param newWidth The grid width.
         * @param newHeight The grid height.
         * @param newGrid The grid.
         * @param newMines The number of mines contained in the grid.
         * @param newActions All the actions that have to be replayed.
         */
        public VideoData(final byte newDifficulty, final byte newShape, final byte newWidth, final byte newHeight, final byte[][] newGrid, final int newMines, final List<Action> newActions) {
            this.difficulty = newDifficulty;
            this.shape = newShape;
            this.width = newWidth;
            this.height = newHeight;
            this.grid = newGrid;
            this.mines = newMines;
            this.actions = newActions;
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

        /**
         * Returns all the actions that have to be replayed.
         *
         * @return All the actions that have to be replayed.
         */
        public List<Action> getActions() {
            return actions;
        }

    }
}
