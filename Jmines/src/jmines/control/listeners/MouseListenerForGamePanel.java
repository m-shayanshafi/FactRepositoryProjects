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
package jmines.control.listeners;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.event.MouseInputListener;

import jmines.model.Tile;
import jmines.model.TilesShapeUnsupportedException;
import jmines.view.components.MainPanel;
import jmines.view.persistence.Configuration;

/**
 * The listener to use to listen the mouse event and mouse moves in the game
 * panel.
 *
 * @author Zleurtor
 */
public class MouseListenerForGamePanel implements MouseInputListener {

    //==========================================================================
    // Static attributes
    //==========================================================================
    /**
     * The color for the cheat pixel when the mouse has moved to a mined tile.
     */
    public static final Color MINED_COLOR = Color.BLACK;
    /**
     * The color for the cheat pixel when the mouse has moved to a not mined
     * tile.
     */
    public static final Color NOT_MINED_COLOR = Color.WHITE;

    //==========================================================================
    // Attributes
    //==========================================================================
    /**
     * Its value is true if the left button is currently pressed, false
     * otherwise.
     */
    private boolean leftButtonPressed = false;
    /**
     * Its value is true if the right button is currently pressed, false
     * otherwise.
     */
    private boolean rightButtonPressed = false;
    /**
     * Its value is true if the right and left button are currently pressed,
     * false otherwise.
     */
    private boolean twoButtonsPressed = false;
    /**
     * The main panel for which the events occur.
     */
    private final MainPanel mainPanel;
    /**
     * The list in which the game mouse actions are stored.
     */
    private final List<Action> stored;

    //==========================================================================
    // Constructors
    //==========================================================================
    /**
     * Construct a new mouse listener.
     *
     * @param newMainPanel The main panel for which the events occur.
     */
    public MouseListenerForGamePanel(final MainPanel newMainPanel) {
        this.mainPanel = newMainPanel;
        stored = new ArrayList<Action>();
    }

    //==========================================================================
    // Getters
    //==========================================================================
    /**
     * Tell whether or not the left button is currently pressed.
     *
     * @return true if the left button is currently pressed, false otherwise.
     */
    public final boolean isLeftButtonPressed() {
        return leftButtonPressed;
    }

    /**
     * Returns the list in which the game mouse actions are stored.
     *
     * @return The list in which the game mouse actions are stored.
     */
    public final List<Action> getStored() {
        return stored;
    }

    //==========================================================================
    // Setters
    //==========================================================================

    //==========================================================================
    // Inherited methods
    //==========================================================================
    /**
     * Called method when the mouse is clicked.
     *
     * @param evt The event object relating the event that occurred.
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    public final void mouseClicked(final MouseEvent evt) {
        if (mainPanel.getGamePanel().isLost() || mainPanel.getGamePanel().isWon() || mainPanel.isPlayingVideo()) {
            return;
        }
    }

    /**
     * Called method when the mouse is moved into the game panel.
     *
     * @param evt The event object relating the event that occurred.
     * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
     */
    public final void mouseEntered(final MouseEvent evt) {
        if (mainPanel.getGamePanel().isLost() || mainPanel.getGamePanel().isWon() || mainPanel.isPlayingVideo()) {
            return;
        }
    }

    /**
     * Called method when the mouse is moved out of the game panel.
     *
     * @param evt The event object relating the event that occurred.
     * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
     */
    public final void mouseExited(final MouseEvent evt) {
        if (mainPanel.getGamePanel().isLost() || mainPanel.getGamePanel().isWon() || mainPanel.isPlayingVideo()) {
            return;
        }

        mainPanel.getGamePanel().clearCurrentlyPressed();
        mainPanel.getGamePanel().repaint();

        leftButtonPressed = false;
        rightButtonPressed = false;
        twoButtonsPressed = false;

        mainPanel.getGamePanel().setCurrentlyOverflown(null);
        mainPanel.manageSmiley();
    }

    /**
     * Called method when the mouse is pressed.
     *
     * @param evt The event object relating the event that occurred.
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     */
    public final void mousePressed(final MouseEvent evt) {
        if (mainPanel.getGamePanel().isLost() || mainPanel.getGamePanel().isWon() || mainPanel.isPlayingVideo()) {
            return;
        }

        Point over = getTileCoordinates(evt);
        if (over == null) {
            return;
        }

        int l = (int) over.getY();
        int c = (int) over.getX();

        if (evt.getButton() == MouseEvent.BUTTON1) {
            leftButtonPressed = true;
            mainPanel.getGamePanel().addCurrentlyPressed(over);
            mainPanel.getGamePanel().repaint();
        } else if (evt.getButton() == MouseEvent.BUTTON3) {
            rightButtonPressed = true;
        }

        if (leftButtonPressed) {
            mainPanel.getGamePanel().setCurrentlyOverflown(null);
        }

        if (leftButtonPressed && rightButtonPressed) {
            twoButtonsPressed = true;

            mainPanel.getGamePanel().clearCurrentlyPressed();
            mainPanel.getGamePanel().addCurrentlyPressed(over);
            try {
                for (Tile tile : mainPanel.getGamePanel().getGameBoard().getNeighborhood(l, c)) {
                    if (tile != null && !tile.isOpen() && !tile.isFlagged() && !tile.isMarked()) {
                        mainPanel.getGamePanel().addCurrentlyPressed(mainPanel.getGamePanel().getGameBoard().getTileCoordinates(tile));
                    }
                }
            } catch (TilesShapeUnsupportedException e) {
                JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", Configuration.getInstance().getText(Configuration.KEY_TITLE_ERROR), JOptionPane.ERROR_MESSAGE);
            }
            mainPanel.getGamePanel().repaint();
        } else if (rightButtonPressed) {
            Tile tile = mainPanel.getGamePanel().getGameBoard().getTile(l, c);
            if (!tile.isFlagged() && !tile.isMarked() && !tile.isOpen()) {
                tile.setFlagged(true);
                if (mainPanel.isSoundEnabled()) {
                    mainPanel.getAudioPlayer().playFlag();
                }
                store(Action.ID_FLAG, tile);
                mainPanel.getTopPanel().getFlagsPanel().setNumber(mainPanel.getTopPanel().getFlagsPanel().getNumber() - 1);
            } else if (tile.isFlagged() && !tile.isOpen() && mainPanel.getGamePanel().getGameBoard().isMarksAuthorized()) {
                tile.setFlagged(false);
                store(Action.ID_UNFLAG, tile);
                tile.setMarked(true);
                store(Action.ID_MARK, tile);
                mainPanel.getTopPanel().getFlagsPanel().setNumber(mainPanel.getTopPanel().getFlagsPanel().getNumber() + 1);
            } else if (tile.isFlagged() && !tile.isOpen() && !mainPanel.getGamePanel().getGameBoard().isMarksAuthorized()) {
                tile.setFlagged(false);
                tile.setMarked(false);
                store(Action.ID_UNFLAG, tile);
                mainPanel.getTopPanel().getFlagsPanel().setNumber(mainPanel.getTopPanel().getFlagsPanel().getNumber() + 1);
            } else if (tile.isMarked() && !tile.isOpen()) {
                tile.setMarked(false);
                store(Action.ID_UNMARK, tile);
            }
            mainPanel.getGamePanel().repaint();
        }

        mainPanel.manageSmiley();
    }

    /**
     * Called method when the mouse is released.
     *
     * @param evt The event object relating the event that occurred.
     * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
     */
    public final void mouseReleased(final MouseEvent evt) {
        if (mainPanel.getGamePanel().isLost() || mainPanel.getGamePanel().isWon() || mainPanel.isPlayingVideo()) {
            return;
        }

        Point over = getTileCoordinates(evt);
        if (over == null) {
            mainPanel.getGamePanel().clearCurrentlyPressed();
            mainPanel.getGamePanel().repaint();
            return;
        }

        int l = (int) over.getY();
        int c = (int) over.getX();

        if (evt.getButton() == MouseEvent.BUTTON1 && !twoButtonsPressed) {
            open(l, c);
            leftButtonPressed = false;
            mainPanel.getGamePanel().clearCurrentlyPressed();
            mainPanel.getGamePanel().repaint();
        } else if (evt.getButton() == MouseEvent.BUTTON1) {
            leftButtonPressed = false;
            mainPanel.manageSmiley();
        } else if (evt.getButton() == MouseEvent.BUTTON3) {
            rightButtonPressed = false;
        }

        if (twoButtonsPressed && (evt.getButton() == MouseEvent.BUTTON1 || evt.getButton() == MouseEvent.BUTTON3)) {
            try {
                if (mainPanel.getGamePanel().getGameBoard().isOpen(l, c)) {
                    int nbFlagged = 0;
                    for (Tile tile : mainPanel.getGamePanel().getGameBoard().getNeighborhood(l, c)) {
                        if (tile != null && tile.isFlagged()) {
                            nbFlagged++;
                        }
                    }
                    if (nbFlagged == mainPanel.getGamePanel().getGameBoard().getNumberOfSurroundingMines(l, c)) {
                        for (Tile tile : mainPanel.getGamePanel().getGameBoard().getNeighborhood(l, c)) {
                            open(tile);
                        }
                    }
                }
            } catch (TilesShapeUnsupportedException e) {
                JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", Configuration.getInstance().getText(Configuration.KEY_TITLE_ERROR), JOptionPane.ERROR_MESSAGE);
            }

            twoButtonsPressed = false;
            if (!mainPanel.getGamePanel().isLost() && !mainPanel.getGamePanel().isWon()) {
                mainPanel.getTopPanel().setPlayIcon();
            }

            mainPanel.getGamePanel().clearCurrentlyPressed();
            mainPanel.getGamePanel().repaint();
        }

        checkCheat(evt);
        mainPanel.manageSmiley();
    }

    /**
     * Called method when the mouse is dragged.
     *
     * @param evt The event object relating the event that occurred.
     * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
     */
    public final void mouseDragged(final MouseEvent evt) {
        if (mainPanel.getGamePanel().isLost() || mainPanel.getGamePanel().isWon() || mainPanel.isPlayingVideo()) {
            return;
        }

        Point over = getTileCoordinates(evt);
        if (over == null) {
            mainPanel.getGamePanel().clearCurrentlyPressed();
            mainPanel.getGamePanel().repaint();
            return;
        }

        int l = (int) over.getY();
        int c = (int) over.getX();

        if (leftButtonPressed && !rightButtonPressed) {
            mainPanel.getGamePanel().clearCurrentlyPressed();
            mainPanel.getGamePanel().addCurrentlyPressed(over);
            mainPanel.getGamePanel().repaint();
        } else if (twoButtonsPressed) {
            mainPanel.getGamePanel().clearCurrentlyPressed();
            mainPanel.getGamePanel().addCurrentlyPressed(over);
            try {
                for (Tile tile : mainPanel.getGamePanel().getGameBoard().getNeighborhood(l, c)) {
                    if (tile != null && !tile.isOpen()) {
                        mainPanel.getGamePanel().addCurrentlyPressed(mainPanel.getGamePanel().getGameBoard().getTileCoordinates(tile));
                    }
                }
            } catch (TilesShapeUnsupportedException e) {
                JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", Configuration.getInstance().getText(Configuration.KEY_TITLE_ERROR), JOptionPane.ERROR_MESSAGE);
            }
            mainPanel.getGamePanel().repaint();
        }

        mainPanel.manageSmiley();
    }

    /**
     * Called method when the mouse is moved.
     *
     * @param evt The event object relating the event that occurred.
     * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
     */
    public final void mouseMoved(final MouseEvent evt) {
        if (mainPanel.getGamePanel().isLost() || mainPanel.getGamePanel().isWon() || mainPanel.isPlayingVideo()) {
            return;
        }

        if (!leftButtonPressed && !rightButtonPressed) {
            mainPanel.getGamePanel().setCurrentlyOverflown(getTileCoordinates(evt));
            mainPanel.getGamePanel().repaint();
        } else {
            mainPanel.getGamePanel().setCurrentlyOverflown(null);
        }

        checkCheat(evt);
    }

    //==========================================================================
    // Static methods
    //==========================================================================

    //==========================================================================
    // Methods
    //==========================================================================
    /**
     * Compute the coordinates of the tile on which an event occurred.
     *
     * @param evt The event object relating the event that occurred.
     * @return The point containing the coordinates of the tile on which an
     *         event occurred.
     */
    private Point getTileCoordinates(final MouseEvent evt) {
        Point ret = null;

        int x = evt.getX();
        int y = evt.getY();
        if (mainPanel.getGamePanel().getBorder() != null) {
            x -= mainPanel.getGamePanel().getBorder().getBorderInsets(mainPanel.getGamePanel()).left;
            y -= mainPanel.getGamePanel().getBorder().getBorderInsets(mainPanel.getGamePanel()).top;
        }

        for (int l = 0; l < mainPanel.getGamePanel().getPolygons().length; l++) {
            for (int c = 0; c < mainPanel.getGamePanel().getPolygons()[l].length; c++) {
                if (mainPanel.getGamePanel().getPolygons()[l][c] != null && mainPanel.getGamePanel().getPolygons()[l][c].contains(x, y)) {
                    ret = new Point(c, l);
                }
            }
        }

        return ret;
    }

    /**
     * Open the tile located at given coordinates.
     *
     * @param l The line containing the tile to open.
     * @param c The column containing the tile to open.
     */
    private void open(final int l, final int c) {
        Tile tile = mainPanel.getGamePanel().getGameBoard().getTile(l, c);
        open(tile);
    }


    /**
     * Open a given tile and (recursively) its neighbors.
     *
     * @param tile The tile to open.
     */
    private void open(final Tile tile) {
        mainPanel.manageSmiley();

        if (tile != null && !tile.isOpen() && !tile.isFlagged()) {
            store(Action.ID_OPEN, tile);

            if (mainPanel.getGamePanel().getGameBoard().open(tile)) {
                mainPanel.getGamePanel().paintBackground(null);
                mainPanel.getTimer().start();
            }

            if (mainPanel.isSoundEnabled()) {
                mainPanel.getAudioPlayer().playOpen();
            }

            try {
                int surrounding = mainPanel.getGamePanel().getGameBoard().getNumberOfSurroundingMines(tile);
                if (surrounding == 0 && !tile.isContainingMine()) {
                    Collection<Tile> neighborhood = mainPanel.getGamePanel().getGameBoard().getNeighborhood(tile);
                    for (Tile tmp : neighborhood) {
                        open(tmp);
                    }
                }
            } catch (TilesShapeUnsupportedException e) {
                JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", Configuration.getInstance().getText(Configuration.KEY_TITLE_ERROR), JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Store the action click on the given tile.
     *
     * @param id The action identifier.
     * @param tile The tile on which the user has clicked.
     */
    private void store(final byte id, final Tile tile)  {
        byte line = -1;
        byte column = -1;

        for (byte l = 0; l < mainPanel.getGamePanel().getGameBoard().getHeight(); l++) {
            for (byte c = 0; c < mainPanel.getGamePanel().getGameBoard().getWidth(); c++) {
                if (mainPanel.getGamePanel().getGameBoard().getTile(l, c) == tile) {
                    line = l;
                    column = c;
                }
            }
        }

        if (line != -1 && column != -1) {
            stored.add(new Action(id, line, column));
        }
    }

    /**
     * Check the color of the cheat pixel.
     *
     * @param evt The event object relating the event that occurred.
     */
    private void checkCheat(final MouseEvent evt) {
        Point coords = getTileCoordinates(evt);
        if (coords != null && mainPanel.getGamePanel().getGameBoard().isContainingMine(coords.y, coords.x)) {
            mainPanel.getCheatPixel().changeColor(MINED_COLOR);
        } else {
            mainPanel.getCheatPixel().changeColor(NOT_MINED_COLOR);
        }
    }

    /**
     * Initialize the listener setting the leftButtonPressed,
     * rightButtonPressed and TwoButtonsPressed to false.
     */
    public final void initialize() {
        leftButtonPressed = false;
        rightButtonPressed = false;
        twoButtonsPressed = false;

        stored.clear();
        Action.setStartDate(System.currentTimeMillis());
    }

    //==========================================================================
    // Inner classes
    //==========================================================================
    /**
     * The class used to store a game mouse action.
     *
     * @author Zleurtor
     */
    public static final class Action {

        //======================================================================
        // Static attributes
        //======================================================================
        /**
         * The date at which the actual game has started.
         */
        private static long startDate;
        /**
         * The identifier for the opening action.
         */
        public static final byte ID_OPEN = 0;
        /**
         * The identifier for the flaging action.
         */
        public static final byte ID_FLAG = 1;
        /**
         * The identifier for the marking action.
         */
        public static final byte ID_MARK = 2;
        /**
         * The identifier for the unflaging action.
         */
        public static final byte ID_UNFLAG = 3;
        /**
         * The identifier for the unmarking action.
         */
        public static final byte ID_UNMARK = 4;
        /**
         * The beginner difficulty.
         */
        public static final byte DIFFICULTY_BEGINNER = 0;
        /**
         * The intermediate difficulty.
         */
        public static final byte DIFFICULTY_INTERMEDIATE = 1;
        /**
         * The expert difficulty.
         */
        public static final byte DIFFICULTY_EXPERT = 2;
        /**
         * The custom difficulty.
         */
        public static final byte DIFFICULTY_CUSTOM = 3;

        //======================================================================
        // Attributes
        //======================================================================
        /**
         * The date at which this action has occured.
         */
        private final long date;
        /**
         * The identifier of the action.
         */
        private final byte id;
        /**
         * The line at which this action has occured.
         */
        private final byte line;
        /**
         * The column at which this action has occured.
         */
        private final byte column;

        //======================================================================
        // Constructors
        //======================================================================
        /**
         * Construct a new action using given id, line and column.
         *
         * @param newId The identifier of the action that occurred.
         * @param newLine The line at which this action has occured.
         * @param newColumn The column at which this action has occured.
         */
        public Action(final byte newId, final byte newLine, final byte newColumn) {
            date = System.currentTimeMillis();
            id = newId;
            line = newLine;
            column = newColumn;
        }

        /**
         * Construct a new action using given in, line, column and date.
         *
         * @param newId The identifier of the action that occurred.
         * @param newLine The line at which this action has occured.
         * @param newColumn The column at which this action has occured.
         * @param newDate The date when the action occured.
         */
        public Action(final byte newId, final byte newLine, final byte newColumn, final long newDate) {
            id = newId;
            line = newLine;
            column = newColumn;
            date = newDate;
        }

        //======================================================================
        // Getters
        //======================================================================
        /**
         * Returns the date at which the actual game has started.
         *
         * @return The date at which the actual game has started.
         */
        public static long getStartDate() {
            return startDate;
        }

        /**
         * Returns the date at which this action has occured.
         *
         * @return The date at which this action has occured.
         */
        public long getDate() {
            return date;
        }

        /**
         * Returns the identifier of the action.
         *
         * @return The identifier of the action.
         */
        public byte getId() {
            return id;
        }

        /**
         * Returns the line at which this action has occured.
         *
         * @return The line at which this action has occured.
         */
        public byte getLine() {
            return line;
        }

        /**
         * Returns the column at which this action has occured.
         *
         * @return The column at which this action has occured.
         */
        public byte getColumn() {
            return column;
        }

        //======================================================================
        // Setters
        //======================================================================
        /**
         * Used to set a new value for the date at which the actual game has
         * started.
         *
         * @param newStartDate The new value for the date at which the actual game has
         *                     started.
         */
        public static void setStartDate(final long newStartDate) {
            startDate = newStartDate;
        }

        //======================================================================
        // Inherited methods
        //======================================================================
        /**
         * Returns a string representation of the object.
         *
         * @return A string representation of the object.
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return "[" + line + ", " + column + "] @ " + date;
        }
    }

}
