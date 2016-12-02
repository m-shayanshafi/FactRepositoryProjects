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
package jmines.view.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.MissingResourceException;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.plaf.basic.BasicBorders;

import jmines.control.listeners.MouseListenerForGamePanel;
import jmines.model.GameBoard;
import jmines.model.Tile;
import jmines.model.TilesShapeUnsupportedException;
import jmines.model.events.GameBoardEvent;
import jmines.model.events.GameBoardListener;
import jmines.view.persistence.Configuration;

/**
 * The panel in which the game is drawn and the mouse events listened.
 *
 * @author Zleurtor
 */
public class GamePanel extends JPanel implements GameBoardListener {

    static {
        final int basis = 16;

        final List<Image> tmp = new ArrayList<Image>();
        for (int i = 0; i < basis; i++) {
            tmp.add(Configuration.getInstance().getImage(
                    Configuration.PREFIX_IMG_TILES,
                    Integer.toString(i)));
        }
        tmp.add(Configuration.getInstance().getImage(
                Configuration.PREFIX_IMG_TILES,
                Configuration.SUFFIX_FLAG));
        tmp.add(Configuration.getInstance().getImage(
                Configuration.PREFIX_IMG_TILES,
                Configuration.SUFFIX_MARK));
        tmp.add(Configuration.getInstance().getImage(
                Configuration.PREFIX_IMG_TILES,
                Configuration.SUFFIX_MINE));
        tmp.add(Configuration.getInstance().getImage(
                Configuration.PREFIX_IMG_TILES,
                Configuration.SUFFIX_EXPLODED));
        tmp.add(Configuration.getInstance().getImage(
                Configuration.PREFIX_IMG_TILES,
                Configuration.SUFFIX_NO_MINE));

        Image[] tmpArray = new Image[tmp.size()];
        tmpArray = tmp.toArray(tmpArray);
        SPRITES = tmpArray;
    }

    //==========================================================================
    // Static attributes
    //==========================================================================
    /**
     * The unique serial version identifier.
     */
    private static final long serialVersionUID = -2965820654953600202L;
    /**
     * An array of images representing all the sprites that can be used in the
     * game (the numbers, flag, mark, mine etc.).
     */
    private static final Image[] SPRITES;
    /**
     * The index of the flag in the previous array.
     */
    private static final int INDEX_FLAG = Configuration.getInstance().getInt(Configuration.KEY_TILES_FLAG);
    /**
     * The index of the mark in the previous array.
     */
    private static final int INDEX_MARK = Configuration.getInstance().getInt(Configuration.KEY_TILES_MARK);
    /**
     * The index of the mine in the previous array.
     */
    private static final int INDEX_MINE = Configuration.getInstance().getInt(Configuration.KEY_TILES_MINE);
    /**
     * The index of the exploded mine in the previous array.
     */
    private static final int INDEX_EXPLODED = Configuration.getInstance().getInt(Configuration.KEY_TILES_EXPLODED);
    /**
     * The index of the no mine sprite in the previous array.
     */
    private static final int INDEX_NO_MINE = Configuration.getInstance().getInt(Configuration.KEY_TILES_NO_MINE);

    //==========================================================================
    // Attributes
    //==========================================================================
    /**
     * The object who is in charge of all the business calculations.
     */
    private final GameBoard gameBoard;
    /**
     * The width (in pixels) of a tile.
     */
    private final int tilesWidth;
    /**
     * The height (in pixels) of a tile.
     */
    private final int tilesHeight;
    /**
     * The buffer in which the panel is first painted.
     */
    private BufferedImage buffer;
    /**
     * The buffer in which the panel background is first painted.
     */
    private BufferedImage backgroundBuffer;
    /**
     * The graphics of the panel buffer.
     */
    private Graphics2D bufferGraphics;
    /**
     * The graphics of the panel background buffer.
     */
    private Graphics2D backgroundBufferGraphics;
    /**
     * The JMines main panel.
     */
    private MainPanel mainPanel;
    /**
     * The set of currently pressed buttons.<br/>
     * Each point contained in this collection correspond to a tile: The x
     * coordinate is the number of the column containing the tile, and the y
     * coordinate is the number of the line containing the tile.
     */
    private Collection<Point> currentlyPressed = new ArrayList<Point>();
    /**
     * The currently overflown button.<br/>
     * The x coordinate is the number of the column containing the tile, and
     * the y coordinate is the number of the line containing the tile.
     */
    private Point currentlyOverflown = null;
    /**
     * The object who listen the mouse events.
     */
    private final MouseListenerForGamePanel mouseListener;
    /**
     * The current difficulty (Configuration.DIFFICULTY_BEGINNER,
     * Configuration.DIFFICULTY_INTERMEDIATE,
     * Configuration.DIFFICULTY_EXPERT or
     * Configuration.DIFFICULTY_CUSTOM).
     */
    private String difficulty;
    /**
     * Tell whether or not the game has been lost.
     */
    private boolean lost = false;
    /**
     * Tell whether or not the game has been won.
     */
    private boolean won = false;
    /**
     * The clicked tile that is responsible of the defeat.
     */
    private Tile defeatTile = null;
    /**
     * The color of the buttons.
     */
    private Color buttonsColor;
    /**
     * The transparency ratio for the highlighted and unlighted parts of the
     * buttons.
     */
    private final int transparency = 85;
    /**
     * The "mask" to apply to the highlighted part of the buttons.
     */
    private Color brighter = new Color(Color.WHITE.getRed(), Color.WHITE.getGreen(), Color.WHITE.getBlue(), transparency);
    /**
     * The "mask" to apply to the unlighted part of the buttons.
     */
    private Color darker = new Color(Color.BLACK.getRed(), Color.BLACK.getGreen(), Color.BLACK.getBlue(), transparency);
    /**
     * The look and feel dependent color of the buttons.
     */
    private Color lookAndFeelDependentButtonsColor;
    /**
     * Tell whether or not the game panel is painted with colors or with gray
     * scales.
     */
    private boolean colored;
    /**
     * Tell whether or not the display is antialiased.
     */
    private boolean antialiased;
    /**
     * Tell whether or not a "shadow" has to follow the cursor.
     */
    private boolean shadowed;

    //==========================================================================
    // Constructors
    //==========================================================================
    /**
     * Construct a new game panel using a given configuration and JMines main
     * panel.
     *
     * @param newMainPanel The JMines main panel.
     */
    public GamePanel(final MainPanel newMainPanel) {
        super(false);
        Configuration configuration = Configuration.getInstance();

        setBorder(BasicBorders.getTextFieldBorder());

        GameBoard tmp = null;

        byte tilesShape = GameBoard.SHAPE_UNDEFINED;
        if (configuration.getString(Configuration.KEY_USER_SHAPE).equals(Configuration.SHAPE_TRIANGULAR)) {
            tilesShape = GameBoard.SHAPE_TRIANGULAR;
        } else if (configuration.getString(Configuration.KEY_USER_SHAPE).equals(Configuration.SHAPE_TRIANGULAR_14)) {
            tilesShape = GameBoard.SHAPE_TRIANGULAR_14;
        } else if (configuration.getString(Configuration.KEY_USER_SHAPE).equals(Configuration.SHAPE_SQUARE)) {
            tilesShape = GameBoard.SHAPE_SQUARE;
        } else if (configuration.getString(Configuration.KEY_USER_SHAPE).equals(Configuration.SHAPE_PENTAGONAL)) {
            tilesShape = GameBoard.SHAPE_PENTAGONAL;
        } else if (configuration.getString(Configuration.KEY_USER_SHAPE).equals(Configuration.SHAPE_HEXAGONAL)) {
            tilesShape = GameBoard.SHAPE_HEXAGONAL;
        } else if (configuration.getString(Configuration.KEY_USER_SHAPE).equals(Configuration.SHAPE_OCTOSQUARE)) {
            tilesShape = GameBoard.SHAPE_OCTOSQUARE;
        } else if (configuration.getString(Configuration.KEY_USER_SHAPE).equals(Configuration.SHAPE_PARQUET)) {
            tilesShape = GameBoard.SHAPE_PARQUET;
        }

        try {
            String tmpDifficulty = configuration.getString(Configuration.KEY_USER_DIFFICULTY);

            int width = configuration.getInt(tmpDifficulty + Configuration.DOT + Configuration.SUFFIX_WIDTH);
            int height = configuration.getInt(tmpDifficulty + Configuration.DOT + Configuration.SUFFIX_HEIGHT);
            int mines = configuration.getInt(tmpDifficulty + Configuration.DOT + Configuration.SUFFIX_MINES);

            setDifficulty(tmpDifficulty);
            tmp = new GameBoard(tilesShape, width, height, mines);
        } catch (TilesShapeUnsupportedException e) {
            JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", Configuration.getInstance().getText(Configuration.KEY_TITLE_ERROR), JOptionPane.ERROR_MESSAGE);
        } catch (MissingResourceException e) {
            // We are in custom difficulty
            String tmpDifficulty = configuration.getString(Configuration.KEY_USER_DIFFICULTY);
            tmpDifficulty = tmpDifficulty.substring(tmpDifficulty.indexOf(Configuration.COMA) + 1);

            int width = Integer.parseInt(tmpDifficulty.substring(0, tmpDifficulty.indexOf(Configuration.COMA)));
            tmpDifficulty = tmpDifficulty.substring(tmpDifficulty.indexOf(Configuration.COMA) + 1);
            int height = Integer.parseInt(tmpDifficulty.substring(0, tmpDifficulty.indexOf(Configuration.COMA)));
            tmpDifficulty = tmpDifficulty.substring(tmpDifficulty.indexOf(Configuration.COMA) + 1);
            int mines = Integer.parseInt(tmpDifficulty);

            setDifficulty(Configuration.DIFFICULTY_CUSTOM);
            try {
                tmp = new GameBoard(tilesShape, width, height, mines);
            } catch (TilesShapeUnsupportedException e1) {
                JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", Configuration.getInstance().getText(Configuration.KEY_TITLE_ERROR), JOptionPane.ERROR_MESSAGE);
            }
        }

        gameBoard = tmp;
        gameBoard.addGameBoardListener(this);

        tilesWidth = configuration.getInt(Configuration.KEY_TILES_WIDTH);
        tilesHeight = configuration.getInt(Configuration.KEY_TILES_HEIGHT);

        mainPanel = newMainPanel;

        // Add the listeners
        mouseListener = new MouseListenerForGamePanel(mainPanel);
        addMouseListener(mouseListener);
        addMouseMotionListener(mouseListener);

        // Set the buttons color
        buttonsColor = getBackground();
        lookAndFeelDependentButtonsColor = getBackground();

        // Create the buffer
        buffer = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        backgroundBuffer = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        bufferGraphics = (Graphics2D) buffer.getGraphics();
        backgroundBufferGraphics = (Graphics2D) backgroundBuffer.getGraphics();
    }

    //==========================================================================
    // Getters
    //==========================================================================
    /**
     * Returns the object who is in charge of all the business calculations.
     *
     * @return The object who is in charge of all the business calculations.
     */
    public final GameBoard getGameBoard() {
        return gameBoard;
    }

    /**
     * Returns the currently overflown button coordinates.
     *
     * @param newCurrentlyOverflown The currently overflown button coordinates.
     */
    public final void setCurrentlyOverflown(final Point newCurrentlyOverflown) {
        this.currentlyOverflown = newCurrentlyOverflown;
    }

    /**
     * Returns the mouse listener.
     *
     * @return The mouse listener.
     */
    public final MouseListenerForGamePanel getMouseListener() {
        return mouseListener;
    }

    /**
     * Returns the current difficulty.
     *
     * @return The current difficulty.
     */
    public final String getDifficulty() {
        return difficulty;
    }

    /**
     * Tell whether or not the game has been lost.
     *
     * @return True if the game has been lost, false otherwise.
     */
    public final boolean isLost() {
        return lost;
    }

    /**
     * Tell whether or not the game has been won.
     *
     * @return True if the game has been won, false otherwise.
     */
    public final boolean isWon() {
        return won;
    }

    /**
     * Returns the look and feel dependent color of the buttons.
     *
     * @return The look and feel dependent color of the buttons.
     */
    public final Color getLookAndFeelDependentButtonsColor() {
        return lookAndFeelDependentButtonsColor;
    }

    /**
     * Tell whether or not the display is antialiased.
     *
     * @return True if the display is antialiased, false otherwise.
     */
    public final boolean isAntialiased() {
        return antialiased;
    }

    /**
     * Tell whether or not a "shadow" has to follow the cursor.
     * @return the shadowed
     */
    public final boolean isShadowed() {
        return shadowed;
    }

    //==========================================================================
    // Setters
    //==========================================================================
    /**
     * Allows to set a new value to the difficulty.
     *
     * @param newDifficulty The new value for the difficulty.
     */
    public final void setDifficulty(final String newDifficulty) {
        this.difficulty = newDifficulty;
    }

    /**
     * Returns the color of the buttons.
     *
     * @param newButtonsColor The color of the buttons.
     */
    public final void setButtonsColor(final Color newButtonsColor) {
        this.buttonsColor = newButtonsColor;
    }

    /**
     * Allows to change the coloring of the panel.
     *
     * @param newColored If true this panel will be painted with colors, else
     *                   it will be painted using gray scales.
     */
    final void setColored(final boolean newColored) {
        this.colored = newColored;

        if (buffer != null) {
            Object bufferAntialiasing = RenderingHints.VALUE_ANTIALIAS_OFF;
            Object backgroundBufferAntialiasing = RenderingHints.VALUE_ANTIALIAS_OFF;
            if (bufferGraphics != null && backgroundBufferGraphics != null) {
                bufferAntialiasing = bufferGraphics.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
                backgroundBufferAntialiasing = backgroundBufferGraphics.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
            }

            if (colored) {
                buffer = new BufferedImage(buffer.getWidth(), buffer.getHeight(), BufferedImage.TYPE_INT_ARGB);
                backgroundBuffer = new BufferedImage(buffer.getWidth(), buffer.getHeight(), BufferedImage.TYPE_INT_ARGB);
            } else {
                buffer = new BufferedImage(buffer.getWidth(), buffer.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
                backgroundBuffer = new BufferedImage(buffer.getWidth(), buffer.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
            }

            bufferGraphics = (Graphics2D) buffer.getGraphics();
            backgroundBufferGraphics = (Graphics2D) backgroundBuffer.getGraphics();
            bufferGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, bufferAntialiasing);
            backgroundBufferGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, backgroundBufferAntialiasing);

            paintBackground(null);
            repaint();
        }
    }

    /**
     * Allows to enable/disable the antialiasing.
     *
     * @param newAntialiased If true the antiliasing will be enabled, disabled if
     *                       false.
     */
    public final void setAntialiased(final boolean newAntialiased) {
        this.antialiased = newAntialiased;

        if (bufferGraphics != null) {
            if (antialiased) {
                bufferGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                repaint();
            } else {
                bufferGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
                repaint();
            }
        }
    }

    /**
     * Allows to enable/disable the shadow.
     *
     * @param newShadowed If true the shadow will be enabled, disabled if
     *                    false.
     */
    public final void setShadowed(final boolean newShadowed) {
        this.shadowed = newShadowed;
        repaint();
    }

    //==========================================================================
    // Inherited methods
    //==========================================================================
    /**
     * the called method when the game board is initialized.
     *
     * @param evt The event object relating the event that occurred.
     * @see jmines.model.events.GameBoardListener#initialized(jmines.model.events.GameBoardEvent)
     */
    public final void initialized(final GameBoardEvent evt) {
        mainPanel.setSaveVideoEnabled(false);
        mainPanel.setPlayingVideo(false);

        evt.getSource().initializePolygons(tilesWidth, tilesHeight);

        // Compute the game panel needed size
        int w = 0;
        int h = 0;
        for (int l = 0; l < gameBoard.getHeight(); l++) {
            for (int c = 0; c < gameBoard.getWidth(); c++) {
                if (getPolygons()[l][c] != null) {
                    Rectangle bounds = getPolygons()[l][c].getBounds();
                    if (bounds.getX() + bounds.getWidth() > w) {
                        w = (int) (bounds.getX() + bounds.getWidth());
                    }
                    if (bounds.getY() + bounds.getHeight() > h) {
                        h = (int) (bounds.getY() + bounds.getHeight());
                    }
                }
            }
        }
        int widthWithoutBorder = w + 1;
        int heightWithoutBorder = h + 1;
        int width = widthWithoutBorder;
        int height = heightWithoutBorder;
        if (getBorder() != null) {
            width += getBorder().getBorderInsets(this).left + getBorder().getBorderInsets(this).right;
            height += getBorder().getBorderInsets(this).top + getBorder().getBorderInsets(this).bottom;
        }
        Dimension dimension = new Dimension(width, height);
        setPreferredSize(dimension);
        setMinimumSize(dimension);
        setMaximumSize(dimension);
        setSize(dimension);

        if (colored) {
            buffer = new BufferedImage(widthWithoutBorder, heightWithoutBorder, BufferedImage.TYPE_INT_ARGB);
            backgroundBuffer = new BufferedImage(widthWithoutBorder, heightWithoutBorder, BufferedImage.TYPE_INT_ARGB);
        } else {
            buffer = new BufferedImage(widthWithoutBorder, heightWithoutBorder, BufferedImage.TYPE_BYTE_GRAY);
            backgroundBuffer = new BufferedImage(widthWithoutBorder, heightWithoutBorder, BufferedImage.TYPE_BYTE_GRAY);
        }

        Object antialiasingValue = RenderingHints.VALUE_ANTIALIAS_OFF;
        if (isAntialiased()) {
            antialiasingValue = RenderingHints.VALUE_ANTIALIAS_ON;
        }
        bufferGraphics = (Graphics2D) buffer.getGraphics();
        bufferGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, antialiasingValue);

        backgroundBufferGraphics = (Graphics2D) backgroundBuffer.getGraphics();
        backgroundBufferGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        paintBackground(null);

        mainPanel.getTimer().cancel();
        mainPanel.getTopPanel().getTimePanel().setNumber(0);
        mainPanel.getTopPanel().getFlagsPanel().setNumber(evt.getSource().getNumberOfMines());

        lost = false;
        won = false;
        defeatTile = null;
        mainPanel.setLoaded(false);

        if (mainPanel.isSoundEnabled()) {
            mainPanel.getAudioPlayer().playNew();
        }

        mouseListener.initialize();

        mainPanel.getRobot().initialize();
        repaint();
    }

    /**
     * The called method when the user loose the game.
     *
     * @param evt The event object relating the event that occurred.
     * @see jmines.model.events.GameBoardListener#defeat(jmines.model.events.GameBoardEvent)
     */
    public final void defeat(final GameBoardEvent evt) {
        if (mainPanel.isSaveVideoEnabled()) {
            mainPanel.getTopPanel().getSmileyButton().setEnabled(false);
        }

        for (int l = 0; l < gameBoard.getHeight(); l++) {
            for (int c = 0; c < gameBoard.getWidth(); c++) {
                if (gameBoard.getTile(l, c) != null
                        && ((!gameBoard.isOpen(l, c) && gameBoard.isContainingMine(l, c) && !gameBoard.isFlagged(l, c))
                                || (gameBoard.isFlagged(l, c) && !gameBoard.isContainingMine(l, c)))) {
                    gameBoard.getTile(l, c).setOpen(true);
                }
            }
        }

        lost = true;
        mainPanel.getTimer().cancel();
        mainPanel.manageSmiley();

        if (mainPanel.isSoundEnabled()) {
            mainPanel.getAudioPlayer().playDefeat();
        }

        paintBackground(evt.getTile());
        repaint();

        if (mainPanel.isSaveVideoEnabled()) {
            mainPanel.saveVideo();
        }
    }

    /**
     * The called method when the user win the game.
     *
     * @param evt The event object relating the event that occurred.
     * @see jmines.model.events.GameBoardListener#victory(jmines.model.events.GameBoardEvent)
     */
    public final void victory(final GameBoardEvent evt) {
        if (mainPanel.isSaveVideoEnabled()) {
            mainPanel.getTopPanel().getSmileyButton().setEnabled(false);
        }

        won = true;
        mainPanel.getTimer().cancel();
        mainPanel.manageSmiley();
        mainPanel.getTopPanel().getFlagsPanel().setNumber(0);

        if (mainPanel.isSoundEnabled()) {
            mainPanel.getAudioPlayer().playVictory();
        }

        repaint();
        mainPanel.manageBestTimes();

        if (mainPanel.isSaveVideoEnabled()) {
            mainPanel.saveVideo();
        }
    }

    /**
     * The method used to paint all the game panel.
     *
     * @param g The graphics in which the panel has to be painted.
     * @see javax.swing.JComponent#paint(java.awt.Graphics)
     */
    @Override
    public final void paint(final Graphics g) {
        if (bufferGraphics == null) {
            return;
        }

        paintBorder(g);

        // Draw the grid in the buffer
        bufferGraphics.drawImage(backgroundBuffer, 0, 0, this);

        // Draw the buttons in the buffer
        for (int l = 0; l < gameBoard.getHeight(); l++) {
            for (int c = 0; c < gameBoard.getWidth(); c++) {
                if (!gameBoard.isOpen(l, c))  {
                    if (!currentlyPressed.contains(new Point(c, l)) || gameBoard.getTile(l, c).isFlagged()) {
                        paintButton(l, c);
                    } else if (currentlyPressed.contains(new Point(c, l))) {
                        paintButtonBackground(l, c);
                    }
                }
            }
        }

        // Draw the buffer to the screen
        int left = 0;
        int top = 0;
        if (getBorder() != null) {
            left = getBorder().getBorderInsets(this).left;
            top = getBorder().getBorderInsets(this).top;
        }
        g.drawImage(buffer, left, top, this);
    }

    /**
     * Change the background color of this panel and all its sub panels.
     *
     * @param bg The new color to use for the panel background.
     * @see javax.swing.JComponent#setBackground(java.awt.Color)
     */
    @Override
    public final void setBackground(final Color bg) {
        super.setBackground(bg);
        paintBackground(null);
    }

    //==========================================================================
    // Static methods
    //==========================================================================

    //==========================================================================
    // Methods
    //==========================================================================
    /**
     * Returns the polygons representing the buttons of this game board.
     *
     * @return The polygons representing the buttons of this game board.
     */
    public final Polygon[][] getPolygons() {
        return gameBoard.getPolygons();
    }

    /**
     * Return the coordinates of the tile corresponding to the button at given
     * line and column.
     *
     * @param l The line of the button we want the tile coordinates.
     * @param c The column of the button we want the tile coordinates.
     * @return The coordinates of the tile corresponding to the button at given
     *         line and column.
     */
    private Point getTilesCoordinates(final int l, final int c) {
        final int three = 3;

        if (getPolygons()[l][c] == null) {
            return null;
        }

        Polygon polygon = (Polygon) getPolygons()[l][c];

        int left = polygon.getBounds().x;
        int top = polygon.getBounds().y;
        int width = polygon.getBounds().width;
        int height = polygon.getBounds().height;

        int x = 0;
        int y = 0;

        switch (gameBoard.getTilesShape()) {
        case GameBoard.SHAPE_TRIANGULAR:
            x = Math.round((left + (width / 2f)) - (tilesWidth / 2f));
            y = 0;
            if ((l + c) % 2 == 0) {
                y = top;
            } else {
                y = top + height - tilesHeight;
            }

            return new Point(x, y);
        case GameBoard.SHAPE_TRIANGULAR_14:
            x = left;
            y = top;
            if ((l % three == 0 && c % three == 0)
                    || (l % three == 2 && c % three == 1)) {
                y = (y + height) - tilesHeight;
            } else if ((l % three == 0 && c % three == 1)
                    || (l % three == 2 && c % three == 2)) {
                x = (x + width) - tilesWidth;
            } else if ((l % three == 1 && c % three == 2)
                    || (l % three == 2 && c % three == 0)) {
                x = (x + width) - tilesWidth;
                y = (y + height) - tilesHeight;
            }

            return new Point(x, y);
        case GameBoard.SHAPE_SQUARE:
            x = left + 1;
            y = top + 1;

            return new Point(x, y);
        case GameBoard.SHAPE_PENTAGONAL:
            x = Math.round(left + (width / 2f) - (tilesWidth / 2f));
            y = Math.round(top + (height / 2f) - (tilesHeight / 2f));
            if ((l % three == 0 && c % three == 1)
                    || (l % three == 1 && c % three == 2)) {
                y = top + height - tilesHeight - 1;
            } else if ((l % three == 1 && c % three == 1)
                    || (l % three == 2 && c % three == 2)) {
                y = top + 1;
            } else if ((l % three == 0 && c % three == 0)
                    || (l % three == 2 && c % three == 1)) {
                x = left + 1;
            } else if ((l % three == 0 && c % three == 2)
                    || (l % three == 2 && c % three == 0)) {
                x = left + width - tilesWidth - 1;
            }

            return new Point(x, y);
        case GameBoard.SHAPE_HEXAGONAL:
        case GameBoard.SHAPE_OCTOSQUARE:
        case GameBoard.SHAPE_PARQUET:
            x = Math.round(left + (width / 2f) - (tilesWidth / 2f));
            y = Math.round(top + (height / 2f) - (tilesHeight / 2f));

            return new Point(x, y);
        default:
            return null;
        }
    }

    /**
     * The method used to paint the background grid and the mines.
     *
     * @param tile If different from null, the tile is the last clicked tile
     *             that is defeat cause.
     */
    public final void paintBackground(final Tile tile) {
        if (tile != null) {
            defeatTile = tile;
        }

        if (backgroundBuffer != null) {
            backgroundBufferGraphics.setColor(getBackground().darker());
            backgroundBufferGraphics.fillRect(0, 0, buffer.getWidth(), buffer.getHeight());

            // Paint the grid
            backgroundBufferGraphics.setColor(getBackground());
            for (int l = 0; l < getPolygons().length; l++) {
                for (int c = 0; c < getPolygons()[l].length; c++) {
                    if (getPolygons()[l][c] != null) {
                        backgroundBufferGraphics.fill(getPolygons()[l][c]);
                    }
                }
            }

            // Paint the mines
            Image exploded = SPRITES[INDEX_EXPLODED];
            Color explodedColor = new Color(((BufferedImage) exploded).getRGB(0, 0));

            for (int l = 0; l < gameBoard.getHeight(); l++) {
                for (int c = 0; c < gameBoard.getWidth(); c++) {
                    Point coords = getTilesCoordinates(l, c);

                    if (coords != null) {
                        try {
                            if (!gameBoard.isContainingMine(l, c) && !gameBoard.isFlagged(l, c)) {
                                backgroundBufferGraphics.drawImage(SPRITES[gameBoard.getNumberOfSurroundingMines(l, c)], coords.x, coords.y, this);
                            } else if (gameBoard.isOpen(l, c) && gameBoard.isContainingMine(l, c) && gameBoard.getTile(l, c) == defeatTile) {
                                backgroundBufferGraphics.setColor(explodedColor);
                                backgroundBufferGraphics.fill(getPolygons()[l][c]);
                                backgroundBufferGraphics.setColor(getBackground().darker());
                                backgroundBufferGraphics.draw(getPolygons()[l][c]);
                                backgroundBufferGraphics.drawImage(exploded, coords.x, coords.y, this);
                            } else if (gameBoard.isFlagged(l, c) && !gameBoard.isContainingMine(l, c)) {
                                backgroundBufferGraphics.drawImage(SPRITES[INDEX_NO_MINE], coords.x, coords.y, this);
                            } else {
                                backgroundBufferGraphics.drawImage(SPRITES[INDEX_MINE], coords.x, coords.y, this);
                            }
                        } catch (TilesShapeUnsupportedException e) {
                            JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " (" + e.getMessage() + ")", Configuration.getInstance().getText(Configuration.KEY_TITLE_ERROR), JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }

            backgroundBufferGraphics.setColor(getBackground().darker());
            for (int l = 0; l < getPolygons().length; l++) {
                for (int c = 0; c < getPolygons()[l].length; c++) {
                    if (getPolygons()[l][c] != null) {
                        backgroundBufferGraphics.draw(getPolygons()[l][c]);
                    }
                }
            }
        }
    }

    /**
     * The method used to paint a button. i.e. to paint the relief of the
     * button.
     *
     * @param l The line at which the button is located.
     * @param c The column at which the button is located.
     */
    public final void paintButton(final int l, final int c) {
        paintButtonBackground(l, c);

        switch (gameBoard.getTilesShape()) {
        case GameBoard.SHAPE_TRIANGULAR:
            paintTriangularRelief(l, c);
            break;
        case GameBoard.SHAPE_TRIANGULAR_14:
            paintTriangular14Relief(l, c);
            break;
        case GameBoard.SHAPE_SQUARE:
            paintSquareRelief(l, c);
            break;
        case GameBoard.SHAPE_PENTAGONAL:
            paintPentagonalRelief(l, c);
            break;
        case GameBoard.SHAPE_HEXAGONAL:
            paintHexagonalRelief(l, c);
            break;
        case GameBoard.SHAPE_OCTOSQUARE:
            paintOctosquareRelief(l, c);
            break;
        case GameBoard.SHAPE_PARQUET:
            paintParquetRelief(l, c);
            break;
        default:
            break;
        }
    }

    /**
     * The method used to paint the background of a button.
     *
     * @param l The line at which the button is located.
     * @param c The column at which the button is located.
     */
    public final void paintButtonBackground(final int l, final int c) {
        Point coords = getTilesCoordinates(l, c);

        if (coords != null) {
            Color tmpButtonsColor = this.buttonsColor;
            if (isShadowed() && currentlyOverflown != null && currentlyOverflown.x == c && currentlyOverflown.y == l) {
                tmpButtonsColor = tmpButtonsColor.darker();
            }

            bufferGraphics.setColor(tmpButtonsColor);
            bufferGraphics.fill(getPolygons()[l][c]);

            bufferGraphics.setColor(tmpButtonsColor.darker());
            bufferGraphics.draw(getPolygons()[l][c]);

            if (gameBoard.isFlagged(l, c)) {
                bufferGraphics.drawImage(SPRITES[INDEX_FLAG], coords.x + 1, coords.y + 1, this);
            }

            if (gameBoard.isMarked(l, c)) {
                bufferGraphics.drawImage(SPRITES[INDEX_MARK], coords.x + 1, coords.y + 1, this);
            }
        }
    }

    /**
     * The method used to paint a triangular button relief.
     *
     * @param l The line at which the button is located.
     * @param c The column at which the button is located.
     */
    private void paintTriangularRelief(final int l, final int c) {
        final int three = 3;
        final int four = 4;
        final int six = 6;

        Polygon polygon = (Polygon) getPolygons()[l][c];
        if (polygon != null) {
            Polygon unlighted = null;
            Polygon highlighted = null;

            if ((l + c) % 2 == 0) {
                highlighted = new Polygon(
                        new int[] {polygon.xpoints[0], polygon.xpoints[1], polygon.xpoints[1], polygon.xpoints[0] + three, polygon.xpoints[three] - three, polygon.xpoints[three]},
                        new int[] {polygon.ypoints[0], polygon.ypoints[1], polygon.ypoints[1] - three, polygon.ypoints[0] + 2, polygon.ypoints[three] + 2, polygon.ypoints[three]},
                        six);
                unlighted = new Polygon(
                        new int[] {polygon.xpoints[three], polygon.xpoints[2], polygon.xpoints[2], polygon.xpoints[three] - three},
                        new int[] {polygon.ypoints[three], polygon.ypoints[2], polygon.ypoints[2] - three, polygon.ypoints[three] + 2},
                        four);
            } else {
                unlighted = new Polygon(
                        new int[] {polygon.xpoints[0], polygon.xpoints[three], polygon.xpoints[2], polygon.xpoints[2], polygon.xpoints[three] - three, polygon.xpoints[0] + three},
                        new int[] {polygon.ypoints[0], polygon.ypoints[three], polygon.ypoints[2], polygon.ypoints[2] + three, polygon.ypoints[three] - 2, polygon.ypoints[0] - 2},
                        six);
                highlighted = new Polygon(
                        new int[] {polygon.xpoints[0], polygon.xpoints[1], polygon.xpoints[1], polygon.xpoints[0] + three},
                        new int[] {polygon.ypoints[0], polygon.ypoints[1], polygon.ypoints[1] + three, polygon.ypoints[0] - 2},
                        four);
            }

            if (unlighted != null && highlighted != null) {
                bufferGraphics.setColor(brighter);
                bufferGraphics.fill(highlighted);

                bufferGraphics.setColor(darker);
                bufferGraphics.fill(unlighted);
            }
        }
    }

    /**
     * The method used to paint a triangular button relief.
     *
     * @param l The line at which the button is located.
     * @param c The column at which the button is located.
     */
    private void paintTriangular14Relief(final int l, final int c) {
        final int three = 3;
        final int four = 4;
        final int six = 6;

        Polygon polygon = (Polygon) getPolygons()[l][c];
        if (polygon != null) {
            Polygon unlighted = null;
            Polygon highlighted = null;

            final int d = (int) Math.round(2d / (Math.sqrt(2) - 1));

            if ((l % three == 0 && c % three == 0)
                    || (l % three == 2 && c % three == 1)) {
                highlighted = new Polygon(
                        new int[] {polygon.xpoints[0], polygon.xpoints[1], polygon.xpoints[1] + 2, polygon.xpoints[0] + 2},
                        new int[] {polygon.ypoints[0], polygon.ypoints[1], polygon.ypoints[1] - 2, polygon.ypoints[0] + d},
                        four);
                unlighted = new Polygon(
                        new int[] {polygon.xpoints[1], polygon.xpoints[2], polygon.xpoints[2] - d, polygon.xpoints[1] + 2},
                        new int[] {polygon.ypoints[1], polygon.ypoints[2], polygon.ypoints[2] - 2, polygon.ypoints[1] - 2},
                        four);
            } else if ((l % three == 0 && c % three == 1)
                    || (l % three == 2 && c % three == 2)) {
                highlighted = new Polygon(
                        new int[] {polygon.xpoints[0], polygon.xpoints[0] + d, polygon.xpoints[2] - 2, polygon.xpoints[2]},
                        new int[] {polygon.ypoints[0], polygon.ypoints[0] + 2, polygon.ypoints[2] + 2, polygon.ypoints[2]},
                        four);
                unlighted = new Polygon(
                        new int[] {polygon.xpoints[1], polygon.xpoints[2], polygon.xpoints[2] - 2, polygon.xpoints[1] - 2},
                        new int[] {polygon.ypoints[1], polygon.ypoints[2], polygon.ypoints[2] + 2, polygon.ypoints[1] - d},
                        four);
            } else if ((l % three == 1 && c % three == 2)
                    || (l % three == 2 && c % three == 0)) {
                highlighted = new Polygon(
                        new int[] {polygon.xpoints[0], polygon.xpoints[2], polygon.xpoints[2] - 2, polygon.xpoints[0] + d},
                        new int[] {polygon.ypoints[0], polygon.ypoints[2], polygon.ypoints[2] + d, polygon.ypoints[0] - 2},
                        four);
                unlighted = new Polygon(
                        new int[] {polygon.xpoints[0], polygon.xpoints[1], polygon.xpoints[2], polygon.xpoints[2] - 2, polygon.xpoints[1] - 2, polygon.xpoints[0] + d},
                        new int[] {polygon.ypoints[0], polygon.ypoints[1], polygon.ypoints[2], polygon.ypoints[2] + d, polygon.ypoints[1] - 2, polygon.ypoints[0] - 2},
                        six);
            } else if ((l % three == 0 && c % three == 2)
                    || (l % three == 1 && c % three == 0)) {
                highlighted = new Polygon(
                        new int[] {polygon.xpoints[0], polygon.xpoints[1], polygon.xpoints[1] + 2, polygon.xpoints[0] + 2, polygon.xpoints[2] - d, polygon.xpoints[2]},
                        new int[] {polygon.ypoints[0], polygon.ypoints[1], polygon.ypoints[1] - d, polygon.ypoints[0] + 2, polygon.ypoints[2] + 2, polygon.ypoints[2]},
                        six);
                unlighted = new Polygon(
                        new int[] {polygon.xpoints[1], polygon.xpoints[2], polygon.xpoints[2] - d, polygon.xpoints[1] + 2},
                        new int[] {polygon.ypoints[1], polygon.ypoints[2], polygon.ypoints[2] + 2, polygon.ypoints[1] - d},
                        four);
            }

            if (unlighted != null && highlighted != null) {
                bufferGraphics.setColor(brighter);
                bufferGraphics.fill(highlighted);

                bufferGraphics.setColor(darker);
                bufferGraphics.fill(unlighted);
            }
        }
    }

    /**
     * The method used to paint a square button relief.
     *
     * @param l The line at which the button is located.
     * @param c The column at which the button is located.
     */
    private void paintSquareRelief(final int l, final int c) {
        final int three = 3;
        final int six = 6;

        Polygon polygon = (Polygon) getPolygons()[l][c];
        if (polygon != null) {
            Polygon unlighted = null;
            Polygon highlighted = null;

            unlighted = new Polygon(
                    new int[] {polygon.xpoints[2], polygon.xpoints[three], polygon.xpoints[three] - 2, polygon.xpoints[2] - 2, polygon.xpoints[1] + three, polygon.xpoints[1] + 1},
                    new int[] {polygon.ypoints[2], polygon.ypoints[three] + 1, polygon.ypoints[three] + three, polygon.ypoints[2] - 2, polygon.ypoints[1] - 2, polygon.ypoints[1]},
                    six);
            highlighted = new Polygon(
                    new int[] {polygon.xpoints[0] + 1, polygon.xpoints[1] + 1, polygon.xpoints[1] + three, polygon.xpoints[0] + three, polygon.xpoints[three] - three, polygon.xpoints[three] - 1},
                    new int[] {polygon.ypoints[0] + 1, polygon.ypoints[1] - 1, polygon.ypoints[1] - three, polygon.ypoints[0] + three, polygon.ypoints[three] + three, polygon.ypoints[three] + 1},
                    six);

            if (unlighted != null && highlighted != null) {
                bufferGraphics.setColor(darker);
                bufferGraphics.fill(unlighted);

                bufferGraphics.setColor(brighter);
                bufferGraphics.fill(highlighted);
            }
        }
    }

    /**
     * The method used to paint a pentagonal button relief.
     *
     * @param l The line at which the button is located.
     * @param c The column at which the button is located.
     */
    private void paintPentagonalRelief(final int l, final int c) {
        final int three = 3;
        final int four = 4;
        final int six = 6;
        final int eight = 8;

        Polygon polygon = (Polygon) getPolygons()[l][c];
        if (polygon != null) {
            Polygon unlighted = null;
            Polygon highlighted = null;

            if ((l % three == 0 && c % three == 1) || (l % three == 1 && c % three == 2)) {
                unlighted = new Polygon(
                        new int[] {polygon.xpoints[2], polygon.xpoints[three], polygon.xpoints[four], polygon.xpoints[four] - three, polygon.xpoints[three] - 2, polygon.xpoints[2] + 2},
                        new int[] {polygon.ypoints[2], polygon.ypoints[three], polygon.ypoints[four], polygon.ypoints[four] + 1, polygon.ypoints[three] - 2, polygon.ypoints[2] - 2},
                        six);

                highlighted = new Polygon(
                        new int[] {polygon.xpoints[0], polygon.xpoints[1], polygon.xpoints[2], polygon.xpoints[2] + 2, polygon.xpoints[1] + three, polygon.xpoints[0], polygon.xpoints[four] - three, polygon.xpoints[four]},
                        new int[] {polygon.ypoints[0], polygon.ypoints[1], polygon.ypoints[2], polygon.ypoints[2] - 2, polygon.ypoints[1] + 1, polygon.ypoints[0] + 2, polygon.ypoints[four] + 1, polygon.ypoints[four]},
                        eight);

            } else if ((l % three == 1 && c % three == 1) || (l % three == 2 && c % three == 2)) {
                unlighted = new Polygon(
                        new int[] {polygon.xpoints[0], polygon.xpoints[1], polygon.xpoints[2], polygon.xpoints[2] - 2, polygon.xpoints[1] - three, polygon.xpoints[0], polygon.xpoints[four] + three, polygon.xpoints[four]},
                        new int[] {polygon.ypoints[0], polygon.ypoints[1], polygon.ypoints[2], polygon.ypoints[2] + 2, polygon.ypoints[1] - 1, polygon.ypoints[0] - 2, polygon.ypoints[four] - 1, polygon.ypoints[four]},
                        eight);

                highlighted = new Polygon(
                        new int[] {polygon.xpoints[2], polygon.xpoints[three], polygon.xpoints[four], polygon.xpoints[four] + three, polygon.xpoints[three] + 2, polygon.xpoints[2] - 2},
                        new int[] {polygon.ypoints[2], polygon.ypoints[three], polygon.ypoints[four], polygon.ypoints[four] - 1, polygon.ypoints[three] + 2, polygon.ypoints[2] + 2},
                        six);
            } else if ((l % three == 0 && c % three == 0) || (l % three == 2 && c % three == 1)) {
                unlighted = new Polygon(
                        new int[] {polygon.xpoints[0], polygon.xpoints[1], polygon.xpoints[1] - 1, polygon.xpoints[0] - 2, polygon.xpoints[four] - 1, polygon.xpoints[three] + 2, polygon.xpoints[three], polygon.xpoints[four]},
                        new int[] {polygon.ypoints[0], polygon.ypoints[1], polygon.ypoints[1] + three, polygon.ypoints[0], polygon.ypoints[four] - three, polygon.ypoints[three] - 2, polygon.ypoints[three], polygon.ypoints[four]},
                        eight);

                highlighted = new Polygon(
                        new int[] {polygon.xpoints[1], polygon.xpoints[2], polygon.xpoints[three], polygon.xpoints[three] + 2, polygon.xpoints[2] + 2, polygon.xpoints[1] - 1},
                        new int[] {polygon.ypoints[1], polygon.ypoints[2], polygon.ypoints[three], polygon.ypoints[three] - 2, polygon.ypoints[2] + 2, polygon.ypoints[1] + three},
                        six);
            } else if ((l % three == 0 && c % three == 2) || (l % three == 2 && c % three == 0)) {
                unlighted = new Polygon(
                        new int[] {polygon.xpoints[1], polygon.xpoints[2], polygon.xpoints[three], polygon.xpoints[three] - 2, polygon.xpoints[2] - 2, polygon.xpoints[1] + 1},
                        new int[] {polygon.ypoints[1], polygon.ypoints[2], polygon.ypoints[three], polygon.ypoints[three] + 2, polygon.ypoints[2] - 2, polygon.ypoints[1] - three},
                        six);

                highlighted = new Polygon(
                        new int[] {polygon.xpoints[three], polygon.xpoints[four], polygon.xpoints[0], polygon.xpoints[1], polygon.xpoints[1] + 1, polygon.xpoints[0] + 2, polygon.xpoints[four] + 1, polygon.xpoints[three] - 2},
                        new int[] {polygon.ypoints[three], polygon.ypoints[four], polygon.ypoints[0], polygon.ypoints[1], polygon.ypoints[1] - three, polygon.ypoints[0], polygon.ypoints[four] + three, polygon.ypoints[three] + 2},
                        eight);
            }

            if (unlighted != null && highlighted != null) {
                bufferGraphics.setColor(darker);
                bufferGraphics.fill(unlighted);

                bufferGraphics.setColor(brighter);
                bufferGraphics.fill(highlighted);
            }
        }
    }

    /**
     * The method used to paint a hexagonal button relief.
     *
     * @param l The line at which the button is located.
     * @param c The column at which the button is located.
     */
    private void paintHexagonalRelief(final int l, final int c) {
        final int three = 3;
        final int four = 4;
        final int five = 5;
        final int eight = 8;

        Polygon polygon = (Polygon) getPolygons()[l][c];
        if (polygon != null) {
            Polygon unlighted = null;
            Polygon highlighted = null;

            unlighted = new Polygon(
                    new int[] {polygon.xpoints[2], polygon.xpoints[three], polygon.xpoints[four], polygon.xpoints[five], polygon.xpoints[five] + 1, polygon.xpoints[four] - 1, polygon.xpoints[three] - 2, polygon.xpoints[2] - 1},
                    new int[] {polygon.ypoints[2], polygon.ypoints[three], polygon.ypoints[four], polygon.ypoints[five], polygon.ypoints[five] - 2, polygon.ypoints[four] - 2, polygon.ypoints[three], polygon.ypoints[2] + 2},
                    eight);
            highlighted = new Polygon(
                    new int[] {polygon.xpoints[five], polygon.xpoints[0], polygon.xpoints[1], polygon.xpoints[2], polygon.xpoints[2] - 1, polygon.xpoints[1] + 1, polygon.xpoints[0] + 2, polygon.xpoints[five] + 1},
                    new int[] {polygon.ypoints[five], polygon.ypoints[0], polygon.ypoints[1], polygon.ypoints[2], polygon.ypoints[2] + 2, polygon.ypoints[1] + 2, polygon.ypoints[0], polygon.ypoints[five] - 2},
                    eight);

            if (unlighted != null && highlighted != null) {
                bufferGraphics.setColor(darker);
                bufferGraphics.fill(unlighted);

                bufferGraphics.setColor(brighter);
                bufferGraphics.fill(highlighted);
            }
        }
    }

    /**
     * The method used to paint an octogonal/square button relief.
     *
     * @param l The line at which the button is located.
     * @param c The column at which the button is located.
     */
    private void paintOctosquareRelief(final int l, final int c) {
        final int three = 3;
        final int four = 4;
        final int five = 5;
        final int six = 6;
        final int seven = 7;
        final int eight = 8;

        Polygon polygon = (Polygon) getPolygons()[l][c];
        if (polygon != null) {
            Polygon unlighted = null;
            Polygon highlighted = null;

            if ((l + c) % 2 == 0) {
                highlighted = new Polygon(
                        new int[] {polygon.xpoints[seven], polygon.xpoints[0], polygon.xpoints[1], polygon.xpoints[2], polygon.xpoints[2] - 1, polygon.xpoints[1] + 1, polygon.xpoints[0] + 2, polygon.xpoints[seven] + 2},
                        new int[] {polygon.ypoints[seven], polygon.ypoints[0], polygon.ypoints[1], polygon.ypoints[2], polygon.ypoints[2] + 2, polygon.ypoints[1] + 2, polygon.ypoints[0] + 1, polygon.ypoints[seven] - 1},
                        eight);
                unlighted = new Polygon(
                        new int[] {polygon.xpoints[three], polygon.xpoints[four], polygon.xpoints[five], polygon.xpoints[six], polygon.xpoints[six] + 1, polygon.xpoints[five] - 1, polygon.xpoints[four] - 2, polygon.xpoints[three] - 2},
                        new int[] {polygon.ypoints[three], polygon.ypoints[four], polygon.ypoints[five], polygon.ypoints[six], polygon.ypoints[six] - 2, polygon.ypoints[five] - 2, polygon.ypoints[four] - 1, polygon.ypoints[three] + 1},
                        eight);
            } else {
                unlighted = new Polygon(
                        new int[] {polygon.xpoints[2], polygon.xpoints[1], polygon.xpoints[1] - 2, polygon.xpoints[2] - 2, polygon.xpoints[three] + three, polygon.xpoints[three] + 1},
                        new int[] {polygon.ypoints[2], polygon.ypoints[1] + 1, polygon.ypoints[1] + three, polygon.ypoints[2] - 2, polygon.ypoints[three] - 2, polygon.ypoints[three]},
                        six);
                highlighted = new Polygon(
                        new int[] {polygon.xpoints[0] + 1, polygon.xpoints[three] + 1, polygon.xpoints[three] + three, polygon.xpoints[0] + three, polygon.xpoints[1] - three, polygon.xpoints[1] - 1},
                        new int[] {polygon.ypoints[0] + 1, polygon.ypoints[three] - 1, polygon.ypoints[three] - three, polygon.ypoints[0] + three, polygon.ypoints[1] + three, polygon.ypoints[1] + 1},
                        six);
            }

            if (unlighted != null && highlighted != null) {
                bufferGraphics.setColor(darker);
                bufferGraphics.fill(unlighted);

                bufferGraphics.setColor(brighter);
                bufferGraphics.fill(highlighted);
            }
        }
    }

    /**
     * The method used to paint a parquet type button relief.
     *
     * @param l The line at which the button is located.
     * @param c The column at which the button is located.
     */
    private void paintParquetRelief(final int l, final int c) {
        final int three = 3;
        final int six = 6;

        Polygon polygon = (Polygon) getPolygons()[l][c];
        if (polygon != null) {
            Polygon unlighted = null;
            Polygon highlighted = null;

            unlighted = new Polygon(
                    new int[] {polygon.xpoints[2], polygon.xpoints[1], polygon.xpoints[1] - 2, polygon.xpoints[2] - 2, polygon.xpoints[three] + three, polygon.xpoints[three] + 1},
                    new int[] {polygon.ypoints[2], polygon.ypoints[1] + 1, polygon.ypoints[1] + three, polygon.ypoints[2] - 2, polygon.ypoints[three] - 2, polygon.ypoints[three]},
                    six);
            highlighted = new Polygon(
                    new int[] {polygon.xpoints[0] + 1, polygon.xpoints[three] + 1, polygon.xpoints[three] + three, polygon.xpoints[0] + three, polygon.xpoints[1] - three, polygon.xpoints[1] - 1},
                    new int[] {polygon.ypoints[0] + 1, polygon.ypoints[three] - 1, polygon.ypoints[three] - three, polygon.ypoints[0] + three, polygon.ypoints[1] + three, polygon.ypoints[1] + 1},
                    six);

            if (unlighted != null && highlighted != null) {
                bufferGraphics.setColor(darker);
                bufferGraphics.fill(unlighted);

                bufferGraphics.setColor(brighter);
                bufferGraphics.fill(highlighted);
            }
        }
    }

    /**
     * Add a currently pressed button.
     *
     * @param point Coordinates of button which is actually being pressed.
     */
    public final void addCurrentlyPressed(final Point point) {
        currentlyPressed.add(point);
    }

    /**
     * Remove the coordinates of all actually pressed buttons.
     */
    public final void clearCurrentlyPressed() {
        currentlyPressed.clear();
    }
}
