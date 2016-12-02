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
package jmines.model;

import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import jmines.model.events.GameBoardEvent;
import jmines.model.events.GameBoardListener;

/**
 * The class used to do all the business calculations.
 *
 * @author Zleurtor
 */
public class GameBoard {

    //==========================================================================
    // Static attributes
    //==========================================================================
    /**
     * The identifier for triangular grids.
     */
    public static final byte SHAPE_UNDEFINED = -1;
    /**
     * The identifier for triangular grids.
     */
    public static final byte SHAPE_TRIANGULAR = 3;
    /**
     * The identifier for triangular 14 grids.
     */
    public static final byte SHAPE_TRIANGULAR_14 = 14;
    /**
     * The identifier for square grids.
     */
    public static final byte SHAPE_SQUARE = 4;
    /**
     * The identifier for pentagonal grids.
     */
    public static final byte SHAPE_PENTAGONAL = 5;
    /**
     * The identifier for hexagonal grids.
     */
    public static final byte SHAPE_HEXAGONAL = 6;
    /**
     * The identifier for octogonal/square grids.
     */
    public static final byte SHAPE_OCTOSQUARE = 8;
    /**
     * The identifier for parquet grids.
     */
    public static final byte SHAPE_PARQUET = 'P';
    /**
     * All the supported shapes identifiers.
     */
    public static final Collection<Byte> SUPPORTED_SHAPES;
    /**
     * The filling ratio for each button shape that doesn't have a filling
     * ratio equals to 1.
     */
    private static final Map<Byte, Float> FILLING_RATIO;

    static {
        final float eight = 8;
        final float nine = 9;

        SUPPORTED_SHAPES = new ArrayList<Byte>();
        SUPPORTED_SHAPES.add(SHAPE_TRIANGULAR);
        SUPPORTED_SHAPES.add(SHAPE_TRIANGULAR_14);
        SUPPORTED_SHAPES.add(SHAPE_SQUARE);
        SUPPORTED_SHAPES.add(SHAPE_PENTAGONAL);
        SUPPORTED_SHAPES.add(SHAPE_HEXAGONAL);
        SUPPORTED_SHAPES.add(SHAPE_OCTOSQUARE);
        SUPPORTED_SHAPES.add(SHAPE_PARQUET);

        FILLING_RATIO = new HashMap<Byte, Float>();
        FILLING_RATIO.put(SHAPE_TRIANGULAR_14, eight / nine);
        FILLING_RATIO.put(SHAPE_PENTAGONAL, eight / nine);
        FILLING_RATIO.put(SHAPE_PARQUET, eight / nine);
    }

    //==========================================================================
    // Attributes
    //==========================================================================
    /**
     * The identifier for the type of grid.
     */
    private byte tilesShape;
    /**
     * The number of columns in the current grid.
     */
    private int width;
    /**
     * The number of lines in the current grid.
     */
    private int height;
    /**
     * The theorical number of mines contained in the current grid.
     */
    private int theoricalNumberOfMines;
    /**
     * The number of mines contained in the current grid taking in account the
     * filling ratio for the current shape.
     */
    private int numberOfMines;
    /**
     * An array containing a description of all tiles of the grid.
     */
    private Tile[][] tiles;
    /**
     * All the object listening for game board events.
     */
    private Collection<GameBoardListener> listeners = new ArrayList<GameBoardListener>();
    /**
     * Tell whether or not a tile can be marked.
     */
    private boolean marksAuthorized = true;
    /**
     * Tell whether or not the next tile opening is the first.
     */
    private boolean firstOpen = true;
    /**
     * The shapes of all the game panel buttons.
     */
    private Polygon[][] polygons;

    //==========================================================================
    // Constructors
    //==========================================================================
    /**
     * Construct a new GameBoard.
     *
     * @param newTilesShape The identifier for the shape of the tiles
     *                      (TRIANGULAR, SQUARE etc.).
     * @param newWidth The number of columns in the new grid.
     * @param newHeight The number of lines in the new grid.
     * @param newNumberOfMines The number of mines in the new grid.
     * @throws TilesShapeUnsupportedException If the given tiles shape is not
     *                                        supported (or not yet
     *                                        implemented).
     */
    public GameBoard(final byte newTilesShape, final int newWidth, final int newHeight, final int newNumberOfMines)
    throws TilesShapeUnsupportedException {
        if (!SUPPORTED_SHAPES.contains(newTilesShape)) {
            throw new TilesShapeUnsupportedException(newTilesShape);
        }

        tilesShape = newTilesShape;
        width = newWidth;
        height = newHeight;
        setNumberOfMines(newNumberOfMines, true);
    }

    //==========================================================================
    // Getters
    //==========================================================================
    /**
     * Returns the identifier used for the current tiles shape.
     *
     * @return The identifier used for the current tiles shape.
     */
    public final byte getTilesShape() {
        return tilesShape;
    }

    /**
     * Returns the number of columns in the current grid.
     *
     * @return The number of columns in the current grid.
     */
    public final int getWidth() {
        return width;
    }

    /**
     * Returns the number of lines in the current grid.
     *
     * @return The number of lines in the current grid.
     */
    public final int getHeight() {
        return height;
    }

    /**
     * Returns the number of mines contained in the current grid.
     *
     * @return The number of mines contained in the current grid.
     */
    public final int getNumberOfMines() {
        return numberOfMines;
    }

    /**
     * Tell whether or not the tiles marks are authorized.
     *
     * @return True if the tiles marks are authorized, false otherwise.
     */
    public final boolean isMarksAuthorized() {
        return marksAuthorized;
    }

    /**
     * Returns the polygons of all the game panel buttons.
     *
     * @return The polygons of all the game panel buttons.
     */
    public final Polygon[][] getPolygons() {
        return polygons;
    }

    //==========================================================================
    // Setters
    //==========================================================================
    /**
     * This allows the user to change the shape of the grid tiles.
     *
     * @param newTilesShape The new value for the tiles shape identifier.
     * @throws TilesShapeUnsupportedException If the given tiles shape
     *                                        identifier is not supported.
     */
    public final void setTilesShape(final byte newTilesShape)
    throws TilesShapeUnsupportedException {
        if (!SUPPORTED_SHAPES.contains(newTilesShape)) {
            throw new TilesShapeUnsupportedException(newTilesShape);
        }

        this.tilesShape = newTilesShape;
        setNumberOfMines(theoricalNumberOfMines, true);
    }

    /**
     * This allows the user to change the number of columns of the grid.
     *
     * @param newWidth The new number of columns in the grid.
     */
    public final void setWidth(final int newWidth) {
        this.width = newWidth;
    }

    /**
     * This allows the user to change the number of lines of the grid.
     *
     * @param newHeight The new number of lines in the grid.
     */
    public final void setHeight(final int newHeight) {
        this.height = newHeight;
    }

    /**
     * This allows the user to change the number of mines of the grid.
     *
     * @param newNumberOfMines The new number of mines contained in the grid.
     * @param computeFillingRatio Tell whether or not the number of mines has
     *                            to be computed using the filling ration of
     *                            the tiles shape.
     */
    public final void setNumberOfMines(final int newNumberOfMines, final boolean computeFillingRatio) {
        this.theoricalNumberOfMines = newNumberOfMines;
        if (FILLING_RATIO.containsKey(tilesShape) && computeFillingRatio) {
            this.numberOfMines = Math.round(theoricalNumberOfMines * FILLING_RATIO.get(tilesShape));
        } else {
            this.numberOfMines = theoricalNumberOfMines;
        }
    }

    /**
     * This allows the user to change the authorization for tiles marks.
     *
     * @param newMarksAuthorized the new value for tiles marks authorization.
     */
    public final void setMarksAuthorized(final boolean newMarksAuthorized) {
        this.marksAuthorized = newMarksAuthorized;
    }

    /**
     * This allows to change the first open boolean.
     *
     * @param newFirstOpen true if the next open will be the first, false
     *                  otherwise.
     */
    public final void setFirstOpen(final boolean newFirstOpen) {
        this.firstOpen = newFirstOpen;
    }

    //==========================================================================
    // Inherited methods
    //==========================================================================

    //==========================================================================
    // Static methods
    //==========================================================================
    /**
     * Calculate the maximum number of mines for a grid that has a size equals
     * to (width * height).
     *
     * @param shape The shape of the grid we want the maximum number of mines.
     * @param width The width of the grid we want the maximum number of mines.
     * @param height The height of the grid we want the maximum number of mines.
     * @return The maximum number of mines for a grid that has a size equals to
     *         (width * height).
     */
    public static int getMaxMines(final byte shape, final float width, final float height) {
        double w = width;
        double h = height;

        if (GameBoard.FILLING_RATIO.keySet().contains(shape)) {
            w = w * Math.sqrt(GameBoard.FILLING_RATIO.get(shape));
            h = h * Math.sqrt(GameBoard.FILLING_RATIO.get(shape));
        }

        return (int) Math.round((h - 1) * (w - 1));
    }

    //==========================================================================
    // Methods
    //==========================================================================
    /**
     * Initialize the cell polygons after a GameBoard initialization for
     * a given tile width and height. The tile is a rectangle that must be
     * inscribed in the polygon.
     *
     * @param tilesWidth The width of the tiles we have to initialize the
     *                   shapes for.
     * @param tilesHeight The height of the tiles we have to initialize the
     *                    shapes for.
     */
    public final void initializePolygons(final int tilesWidth, final int tilesHeight) {
        switch (getTilesShape()) {
        case SHAPE_TRIANGULAR:
            initializeTriangularPolygons(tilesWidth, tilesHeight);
            break;
        case SHAPE_TRIANGULAR_14:
            initializeTriangular14Polygons(tilesWidth, tilesHeight);
            break;
        case SHAPE_SQUARE:
            initializeSquarePolygons(tilesWidth, tilesHeight);
            break;
        case SHAPE_PENTAGONAL:
            initializePentagonalPolygons(tilesWidth, tilesHeight);
            break;
        case SHAPE_HEXAGONAL:
            initializeHexagonalPolygons(tilesWidth, tilesHeight);
            break;
        case SHAPE_OCTOSQUARE:
            initializeOctosquarePolygons(tilesWidth, tilesHeight);
            break;
        case SHAPE_PARQUET:
            initializeParquetPolygons(tilesWidth, tilesHeight);
            break;
        default:
            break;
        }
    }

    /**
     * Initialize the cell polygons after a GameBoard initialization for
     * a given tile width and height. The tile is a rectangle that must be
     * inscribed in the polygon.
     *
     * @param tilesWidth The width of the tiles we have to initialize the
     *                   shapes for.
     * @param tilesHeight The height of the tiles we have to initialize the
     *                    shapes for.
     */
    private void initializeTriangularPolygons(final int tilesWidth, final int tilesHeight) {
        final int three = 3;
        final int four = 4;

        polygons = new Polygon[getHeight()][getWidth()];
        double lx = ((2 * tilesHeight) + (Math.sqrt(three) * tilesWidth)) / Math.sqrt(three);
        double ly = (Math.sqrt(three) / 2) * lx;
        for (int l = 0; l < getHeight(); l++) {
            for (int c = 0; c < getWidth(); c++) {
                if ((l + c) % 2 == 0) {
                    int x1 = (int) Math.round(((lx) * (c / 2)) + ((lx / 2) * (c % 2)));
                    int y1 = (int) Math.round((ly) * (l));
                    int x2 = (int) Math.floor(x1 + (lx / 2));
                    int y2 = (int) Math.round(y1 + ly);
                    int x3 = (int) Math.ceil(x1 + (lx / 2));
                    int y3 = (int) Math.round(y1 + ly);
                    int x4 = (int) Math.round(x1 + lx);
                    int y4 = y1;

                    polygons[l][c] = new Polygon(
                            new int[] {x1, x2, x3, x4},
                            new int[] {y1, y2, y3, y4},
                            four);
                } else {
                    int x1 = (int) Math.round(((lx) * (c / 2)) + ((lx / 2) * (c % 2)));
                    int y1 = (int) Math.round(((ly) * (l)) + ly);
                    int x2 = (int) Math.floor(x1 + (lx / 2));
                    int y2 = (int) Math.round(y1 - ly);
                    int x3 = (int) Math.ceil(x1 + (lx / 2));
                    int y3 = (int) Math.round(y1 - ly);
                    int x4 = (int) Math.round(x1 + lx);
                    int y4 = y1;

                    polygons[l][c] = new Polygon(
                            new int[] {x1, x2, x3, x4},
                            new int[] {y1, y2, y3, y4},
                            four);
                }
            }
        }
    }

    /**
     * Initialize the cell polygons after a GameBoard initialization for
     * a given tile width and height. The tile is a rectangle that must be
     * inscribed in the polygon.
     *
     * @param tilesWidth The width of the tiles we have to initialize the
     *                   shapes for.
     * @param tilesHeight The height of the tiles we have to initialize the
     *                    shapes for.
     */
    private void initializeTriangular14Polygons(final int tilesWidth, final int tilesHeight) {
        final int three = 3;
        final int four = 4;

        polygons = new Polygon[getHeight()][getWidth()];
        for (int l = 0; l < getHeight(); l++) {
            for (int c = 0; c < getWidth(); c++) {
                if ((l % three == 0 && c % three == 0)
                        || (l % three == 2 && c % three == 1)) {
                    int x = ((c / three) * (four * tilesWidth)) + ((c % three) * tilesWidth);
                    int y = ((l / three) * (four * tilesHeight)) + ((l % three) * tilesHeight);
                    if (l % three == 2 && c % three == 1) {
                        x += tilesWidth;
                    }
                    polygons[l][c] = new Polygon(
                            new int[] {x, x, x + (2 * tilesWidth)},
                            new int[] {y, y + (2 * tilesHeight), y + (2 * tilesHeight)},
                            three);
                } else if ((l % three == 0 && c % three == 1)
                        || (l % three == 2 && c % three == 2)) {
                    int x = ((c / three) * (four * tilesWidth)) + (((c % three) - 1) * tilesWidth);
                    int y = ((l / three) * (four * tilesHeight)) + ((l % three) * tilesHeight);
                    if (l % three == 2 && c % three == 2) {
                        x += tilesWidth;
                    }
                    polygons[l][c] = new Polygon(
                            new int[] {x, x + (2 * tilesWidth), x + (2 * tilesWidth)},
                            new int[] {y, y + (2 * tilesHeight), y},
                            three);
                } else if ((l % three == 0 && c % three == 2)
                        || (l % three == 1 && c % three == 0)) {
                    int x = ((c / three) * (four * tilesWidth)) + ((c % three) * tilesWidth);
                    int y = ((l / three) * (four * tilesHeight)) + ((l % three) * tilesHeight);
                    if (l % three == 1 && c % three == 0) {
                        y += tilesHeight;
                    }
                    polygons[l][c] = new Polygon(
                            new int[] {x, x, x + (2 * tilesWidth)},
                            new int[] {y, y + (2 * tilesHeight), y},
                            three);
                } else if ((l % three == 1 && c % three == 2)
                        || (l % three == 2 && c % three == 0)) {
                    int x = ((c / three) * (four * tilesWidth)) + ((c % three) * tilesWidth);
                    int y = ((l / three) * (four * tilesHeight)) + (((l % three) - 1) * tilesHeight);
                    if (l % three == 2 && c % three == 0) {
                        y += tilesHeight;
                    }
                    polygons[l][c] = new Polygon(
                            new int[] {x, x + (2 * tilesWidth), x + (2 * tilesWidth)},
                            new int[] {y + (2 * tilesHeight), y + (2 * tilesHeight), y},
                            three);
                }
            }
        }
    }

    /**
     * Initialize the cell polygons after a GameBoard initialization for
     * a given tile width and height. The tile is a rectangle that must be
     * inscribed in the polygon.
     *
     * @param tilesWidth The width of the tiles we have to initialize the
     *                   shapes for.
     * @param tilesHeight The height of the tiles we have to initialize the
     *                    shapes for.
     */
    private void initializeSquarePolygons(final int tilesWidth, final int tilesHeight) {
        final int four = 4;

        polygons = new Polygon[getHeight()][getWidth()];
        for (int l = 0; l < getHeight(); l++) {
            for (int c = 0; c < getWidth(); c++) {
                polygons[l][c] = new Polygon(
                        new int[] {(tilesWidth + 1) * c, (tilesWidth + 1) * c, ((tilesWidth + 1) * c) + (tilesWidth + 1), ((tilesWidth + 1) * c) + (tilesWidth + 1)},
                        new int[] {(tilesHeight + 1) * l, ((tilesHeight + 1) * l) + (tilesHeight + 1), ((tilesHeight + 1) * l) + (tilesHeight + 1), (tilesHeight + 1) * l},
                        four);
            }
        }
    }

    /**
     * Initialize the cell polygons after a GameBoard initialization for
     * a given tile width and height. The tile is a rectangle that must be
     * inscribed in the polygon.
     *
     * @param tilesWidth The width of the tiles we have to initialize the
     *                   shapes for.
     * @param tilesHeight The height of the tiles we have to initialize the
     *                    shapes for.
     */
    private void initializePentagonalPolygons(final int tilesWidth, final int tilesHeight) {
        final int three = 3;
        final int five = 5;
        final int eight = 8;

        polygons = new Polygon[getHeight()][getWidth()];
        final double w = tilesWidth;
        final double h = tilesHeight;
        final double eleven = 11d;
        final double h1 = (eleven / (double) eight) * tilesWidth;
        final double h2 = (eleven / (double) eight) * tilesHeight;

        for (int l = 0; l < getHeight(); l++) {
            for (int c = 0; c < getWidth(); c++) {
                Polygon shape = null;

                if (l % three == 0 && c % three == 1) {
                    int x1 = (int) Math.round((h2 + (w / 2d)) + (Math.floor(c / three) * (h2 + w + h2)));
                    int y1 = (int) Math.round(Math.floor(l / three) * (h1 + h1 + h));
                    int x2 = (int) Math.round(x1 - (((w / 2d) + h2) / 2d));
                    int y2 = (int) Math.round(y1 + ((h1 - (h / 2d)) / 2d));
                    int x3 = (int) Math.round(x1 - (w / 2d));
                    int y3 = (int) Math.round(y1 + h1);
                    int x4 = (int) Math.round(x1 + (w / 2d));
                    int y4 = (int) Math.round(y1 + h1);
                    int x5 = (int) Math.round(x1 + (((w / 2d) + h2) / 2d));
                    int y5 = (int) Math.round(y1 + ((h1 - (h / 2d)) / 2d));
                    shape = new Polygon(
                            new int[] {x1, x2, x3, x4, x5},
                            new int[] {y1, y2, y3, y4, y5},
                            five);
                } else if (l % three == 1 && c % three == 2) {
                    int x1 = (int) Math.round((h2 + w + h2) + (Math.floor(c / three) * (h2 + w + h2)));
                    int y1 = (int) Math.round((h1 - (h / 2d) + h) + (Math.floor(l / three) * (h1 + h1 + h)));
                    int x2 = (int) Math.round(x1 - (((w / 2d) + h2) / 2d));
                    int y2 = (int) Math.round(y1 + ((h1 - (h / 2d)) / 2d));
                    int x3 = (int) Math.round(x1 - (w / 2d));
                    int y3 = (int) Math.round(y1 + h1);
                    int x4 = (int) Math.round(x1 + (w / 2d));
                    int y4 = (int) Math.round(y1 + h1);
                    int x5 = (int) Math.round(x1 + (((w / 2d) + h2) / 2d));
                    int y5 = (int) Math.round(y1 + ((h1 - (h / 2d)) / 2d));
                    shape = new Polygon(
                            new int[] {x1, x2, x3, x4, x5},
                            new int[] {y1, y2, y3, y4, y5},
                            five);
                } else if (l % three == 1 && c % three == 1) {
                    int x1 = (int) Math.round((h2 + (w / 2d)) + (Math.floor(c / three) * (h2 + w + h2)));
                    int y1 = (int) Math.round(h1 + h1 + Math.floor(l / three) * (h1 + h1 + h));
                    int x2 = (int) Math.round(x1 + (((w / 2d) + h2) / 2d));
                    int y2 = (int) Math.round(y1 - ((h1 - (h / 2d)) / 2d));
                    int x3 = (int) Math.round(x1 + (w / 2d));
                    int y3 = (int) Math.round(y1 - h1);
                    int x4 = (int) Math.round(x1 - (w / 2d));
                    int y4 = (int) Math.round(y1 - h1);
                    int x5 = (int) Math.round(x1 - (((w / 2d) + h2) / 2d));
                    int y5 = (int) Math.round(y1 - ((h1 - (h / 2d)) / 2d));
                    shape = new Polygon(
                            new int[] {x1, x2, x3, x4, x5},
                            new int[] {y1, y2, y3, y4, y5},
                            five);
                } else if (l % three == 2 && c % three == 2) {
                    int x1 = (int) Math.round((h2 + w + h2) + (Math.floor(c / three) * (h2 + w + h2)));
                    int y1 = (int) Math.round(h1 + h1 + (h1 - (h / 2d) + h) + (Math.floor(l / three) * (h1 + h1 + h)));
                    int x2 = (int) Math.round(x1 + (((w / 2d) + h2) / 2d));
                    int y2 = (int) Math.round(y1 - ((h1 - (h / 2d)) / 2d));
                    int x3 = (int) Math.round(x1 + (w / 2d));
                    int y3 = (int) Math.round(y1 - h1);
                    int x4 = (int) Math.round(x1 - (w / 2d));
                    int y4 = (int) Math.round(y1 - h1);
                    int x5 = (int) Math.round(x1 - (((w / 2d) + h2) / 2d));
                    int y5 = (int) Math.round(y1 - ((h1 - (h / 2d)) / 2d));
                    shape = new Polygon(
                            new int[] {x1, x2, x3, x4, x5},
                            new int[] {y1, y2, y3, y4, y5},
                            five);
                } else if (l % three == 0 && c % three == 0) {
                    int x1 = (int) Math.round(h2 + (Math.floor(c / three) * (h2 + w + h2)));
                    int y1 = (int) Math.round(h1 + Math.floor(l / three) * (h1 + h1 + h));
                    int x2 = (int) Math.round(x1 - (((w / 2d) + h2) / 2) + (w / 2d));
                    int y2 = (int) Math.round(y1 - (((h1 - (h / 2d)) / 2d) + (h / 2)));
                    int x3 = (int) Math.round(x1 - h2);
                    int y3 = (int) Math.round(y1 - (h / 2d));
                    int x4 = (int) Math.round(x1 - h2);
                    int y4 = (int) Math.round(y1 + (h / 2d));
                    int x5 = (int) Math.round(x1 - (((w / 2d) + h2) / 2) + (w / 2d));
                    int y5 = (int) Math.round(y1 + (((h1 - (h / 2d)) / 2d) + (h / 2)));
                    shape = new Polygon(
                            new int[] {x1, x2, x3, x4, x5},
                            new int[] {y1, y2, y3, y4, y5},
                            five);
                } else if (l % three == 2 && c % three == 1) {
                    int x1 = (int) Math.round(h2 + h2 + (w / 2d) + (Math.floor(c / three) * (h2 + w + h2)));
                    int y1 = (int) Math.round(h1 + h1 + (h / 2d) + Math.floor(l / three) * (h1 + h1 + h));
                    int x2 = (int) Math.round(x1 - (((w / 2d) + h2) / 2) + (w / 2d));
                    int y2 = (int) Math.round(y1 - (((h1 - (h / 2d)) / 2d) + (h / 2)));
                    int x3 = (int) Math.round(x1 - h2);
                    int y3 = (int) Math.round(y1 - (h / 2d));
                    int x4 = (int) Math.round(x1 - h2);
                    int y4 = (int) Math.round(y1 + (h / 2d));
                    int x5 = (int) Math.round(x1 - (((w / 2d) + h2) / 2) + (w / 2d));
                    int y5 = (int) Math.round(y1 + (((h1 - (h / 2d)) / 2d) + (h / 2)));
                    shape = new Polygon(
                            new int[] {x1, x2, x3, x4, x5},
                            new int[] {y1, y2, y3, y4, y5},
                            five);
                } else if (l % three == 0 && c % three == 2) {
                    int x1 = (int) Math.round((h2 + w) + (Math.floor(c / three) * (h2 + w + h2)));
                    int y1 = (int) Math.round(h1 + Math.floor(l / three) * (h1 + h1 + h));
                    int x2 = (int) Math.round(x1 + (((w / 2d) + h2) / 2) - (w / 2d));
                    int y2 = (int) Math.round(y1 + (((h1 - (h / 2d)) / 2d) + (h / 2)));
                    int x3 = (int) Math.round(x1 + h2);
                    int y3 = (int) Math.round(y1 + (h / 2d));
                    int x4 = (int) Math.round(x1 + h2);
                    int y4 = (int) Math.round(y1 - (h / 2d));
                    int x5 = (int) Math.round(x1 + (((w / 2d) + h2) / 2) - (w / 2d));
                    int y5 = (int) Math.round(y1 - (((h1 - (h / 2d)) / 2d) + (h / 2)));
                    shape = new Polygon(
                            new int[] {x1, x2, x3, x4, x5},
                            new int[] {y1, y2, y3, y4, y5},
                            five);
                } else if (l % three == 2 && c % three == 0) {
                    int x1 = (int) Math.round((w / 2d) + (Math.floor(c / three) * (h2 + w + h2)));
                    int y1 = (int) Math.round(h1 + h1 + (h / 2d) + Math.floor(l / three) * (h1 + h1 + h));
                    int x2 = (int) Math.round(x1 + (((w / 2d) + h2) / 2) - (w / 2d));
                    int y2 = (int) Math.round(y1 + (((h1 - (h / 2d)) / 2d) + (h / 2)));
                    int x3 = (int) Math.round(x1 + h2);
                    int y3 = (int) Math.round(y1 + (h / 2d));
                    int x4 = (int) Math.round(x1 + h2);
                    int y4 = (int) Math.round(y1 - (h / 2d));
                    int x5 = (int) Math.round(x1 + (((w / 2d) + h2) / 2) - (w / 2d));
                    int y5 = (int) Math.round(y1 - (((h1 - (h / 2d)) / 2d) + (h / 2)));
                    shape = new Polygon(
                            new int[] {x1, x2, x3, x4, x5},
                            new int[] {y1, y2, y3, y4, y5},
                            five);
                }

                polygons[l][c] = shape;
            }
        }
    }

    /**
     * Initialize the cell polygons after a GameBoard initialization for
     * a given tile width and height. The tile is a rectangle that must be
     * inscribed in the polygon.
     *
     * @param tilesWidth The width of the tiles we have to initialize the
     *                   shapes for.
     * @param tilesHeight The height of the tiles we have to initialize the
     *                    shapes for.
     */
    private void initializeHexagonalPolygons(final int tilesWidth, final int tilesHeight) {
        final int three = 3;
        final int four = 4;
        final int six = 6;

        polygons = new Polygon[getHeight()][getWidth()];
        double dx = (tilesWidth / four) + (tilesHeight / (four * Math.sqrt(three))) + 1;
        double dy = ((Math.sqrt(three) * tilesWidth) / four) + (tilesHeight / four) + 1;
        for (int l = 0; l < getHeight(); l++) {
            for (int c = 0; c < getWidth(); c++) {
                int x1 = (int) Math.round((three * dx) * c);
                int y1 = (int) Math.round(((2 * dy - 1) * l) + dy + ((c % 2) * dy));
                int x2 = (int) Math.round(x1 + dx);
                int y2 = (int) Math.round(y1 - dy);
                int x3 = (int) Math.round(x1 + (three * dx));
                int y3 = (int) Math.round(y1 - dy);
                int x4 = (int) Math.round(x1 + (four * dx));
                int y4 = y1;
                int x5 = (int) Math.round(x1 + (three * dx));
                int y5 = (int) Math.round(y1 + dy);
                int x6 = (int) Math.round(x1 + dx);
                int y6 = (int) Math.round(y1 + dy);

                polygons[l][c] = new Polygon(new int[] {x1, x2, x3, x4, x5, x6},
                        new int[] {y1, y2, y3, y4, y5, y6},
                        six);
            }
        }
    }

    /**
     * Initialize the cell polygons after a GameBoard initialization for
     * a given tile width and height. The tile is a rectangle that must be
     * inscribed in the polygon.
     *
     * @param tilesWidth The width of the tiles we have to initialize the
     *                   shapes for.
     * @param tilesHeight The height of the tiles we have to initialize the
     *                    shapes for.
     */
    private void initializeOctosquarePolygons(final int tilesWidth, final int tilesHeight) {
        final int four = 4;
        final int eight = 8;

        polygons = new Polygon[getHeight()][getWidth()];

        final int octogonalWidth = (int) Math.round(((tilesWidth / Math.sqrt(2)) * 2) + tilesWidth);
        final int octogonalHeight = (int) Math.round(((tilesHeight / Math.sqrt(2)) * 2) + tilesHeight);

        for (int l = 0; l < getHeight(); l++) {
            for (int c = 0; c < getWidth(); c++) {
                final int x = (int) ((Math.floor(c / 2d) * (octogonalWidth + 1)) + (Math.ceil(c / 2d) * tilesWidth) + ((c % 2) * ((tilesWidth / Math.sqrt(2)) + 1)));
                final int y = (int) ((Math.floor(l / 2d) * (octogonalHeight + 1)) + (Math.ceil(l / 2d) * tilesHeight) + ((l % 2) * ((tilesHeight / Math.sqrt(2)) + 1)));

                if ((l + c) % 2 == 0) {
                    polygons[l][c] = new Polygon(new int[] {x, (int) Math.round(x + (tilesWidth / Math.sqrt(2))), (int) Math.round(x + (tilesWidth / Math.sqrt(2))) + tilesWidth, x + octogonalWidth, x + octogonalWidth, (int) Math.round(x + (tilesWidth / Math.sqrt(2))) + tilesWidth, (int) Math.round(x + (tilesWidth / Math.sqrt(2))), x},
                            new int[] {(int) Math.round(y + (tilesHeight / Math.sqrt(2))), y, y, (int) Math.round(y + (tilesHeight / Math.sqrt(2))), (int) Math.round(y + (tilesHeight / Math.sqrt(2))) + tilesHeight, y + octogonalHeight, y + octogonalHeight, (int) Math.round(y + (tilesHeight / Math.sqrt(2))) + tilesHeight},
                            eight);
                } else {
                    polygons[l][c] = new Polygon(new int[] {(int) Math.round(x + (tilesWidth / Math.sqrt(2))), (int) Math.round(x + (tilesWidth / Math.sqrt(2)) + tilesWidth), (int) Math.round(x + (tilesWidth / Math.sqrt(2)) + tilesWidth), (int) Math.round(x + (tilesWidth / Math.sqrt(2)))},
                            new int[] {(int) Math.round(y + (tilesHeight / Math.sqrt(2))), (int) Math.round(y + (tilesHeight / Math.sqrt(2))), (int) Math.round(y + (tilesHeight / Math.sqrt(2)) + tilesHeight), (int) Math.round(y + (tilesHeight / Math.sqrt(2)) + tilesHeight)},
                            four);
                }
            }
        }
    }

    /**
     * Initialize the cell polygons after a GameBoard initialization for
     * a given tile width and height. The tile is a rectangle that must be
     * inscribed in the polygon.
     *
     * @param tilesWidth The width of the tiles we have to initialize the
     *                   shapes for.
     * @param tilesHeight The height of the tiles we have to initialize the
     *                    shapes for.
     */
    private void initializeParquetPolygons(final int tilesWidth, final int tilesHeight) {
        final int three = 3;
        final int four = 4;

        polygons = new Polygon[getHeight()][getWidth()];

        final int horizontalWidth = tilesWidth * 2;
        final int horizontalHeight = tilesHeight;
        final int verticalWidth = tilesWidth;
        final int verticalHeight = tilesHeight * 2;

        for (int l = 0; l < getHeight(); l++) {
            for (int c = 0; c < getWidth(); c++) {
                Polygon shape = null;

                if (l % three == 0 && c % three == 0) {
                    // Vertical first tiles
                    int x = (four * (c / three)) * tilesWidth;
                    int y = (four * (l / three)) * tilesHeight;
                    shape = new Polygon(
                            new int[] {x, x + verticalWidth, x + verticalWidth, x},
                            new int[] {y, y, y + verticalHeight, y + verticalHeight},
                            four);
                } else if (l % three == 2 && c % three == 1) {
                    // Vertical first tiles
                    int x = ((four * (c / three)) + 2) * tilesWidth;
                    int y = ((four * (l / three)) + 2) * tilesHeight;
                    shape = new Polygon(
                            new int[] {x, x + verticalWidth, x + verticalWidth, x},
                            new int[] {y, y, y + verticalHeight, y + verticalHeight},
                            four);
                } else if (l % three == 0 && c % three == 1) {
                    // Vertical second tiles
                    int x = ((four * (c / three)) + 1) * tilesWidth;
                    int y = (four * (l / three)) * tilesHeight;
                    shape = new Polygon(
                            new int[] {x, x + verticalWidth, x + verticalWidth, x},
                            new int[] {y, y, y + verticalHeight, y + verticalHeight},
                            four);
                } else if (l % three == 2 && c % three == 2) {
                    // Vertical second tiles
                    int x = ((four * (c / three)) + three) * tilesWidth;
                    int y = ((four * (l / three)) + 2) * tilesHeight;
                    shape = new Polygon(
                            new int[] {x, x + verticalWidth, x + verticalWidth, x},
                            new int[] {y, y, y + verticalHeight, y + verticalHeight},
                            four);
                } else if (l % three == 0 && c % three == 2) {
                    // Horizontal first tiles
                    int x = ((four * (c / three)) + 2) * tilesWidth;
                    int y = (four * (l / three)) * tilesHeight;
                    shape = new Polygon(
                            new int[] {x, x + horizontalWidth, x + horizontalWidth, x},
                            new int[] {y, y, y + horizontalHeight, y + horizontalHeight},
                            four);
                } else if (l % three == 1 && c % three == 0) {
                    // Horizontal first tiles
                    int x = (four * (c / three)) * tilesWidth;
                    int y = ((four * (l / three)) + 2) * tilesHeight;
                    shape = new Polygon(
                            new int[] {x, x + horizontalWidth, x + horizontalWidth, x},
                            new int[] {y, y, y + horizontalHeight, y + horizontalHeight},
                            four);
                } else if (l % three == 1 && c % three == 2) {
                    // Horizontal second tiles
                    int x = ((four * (c / three)) + 2) * tilesWidth;
                    int y = ((four * (l / three)) + 1) * tilesHeight;
                    shape = new Polygon(
                            new int[] {x, x + horizontalWidth, x + horizontalWidth, x},
                            new int[] {y, y, y + horizontalHeight, y + horizontalHeight},
                            four);
                } else if (l % three == 2 && c % three == 0) {
                    // Horizontal second tiles
                    int x = (four * (c / three)) * tilesWidth;
                    int y = ((four * (l / three)) + three) * tilesHeight;
                    shape = new Polygon(
                            new int[] {x, x + horizontalWidth, x + horizontalWidth, x},
                            new int[] {y, y, y + horizontalHeight, y + horizontalHeight},
                            four);
                }

                polygons[l][c] = shape;
            }
        }
    }

    /**
     * This method is launched after a game board initialization.
     */
    private void fireInitializedEvent() {
        GameBoardEvent evt = new GameBoardEvent(this, null);
        for (GameBoardListener listener : listeners) {
            listener.initialized(evt);
        }
    }

    /**
     * This method is launched when a mine has been discovered.
     *
     * @param tile The last clicked tile.
     */
    private void fireDefeatEvent(final Tile tile) {
        GameBoardEvent evt = new GameBoardEvent(this, tile);
        for (GameBoardListener listener : listeners) {
            listener.defeat(evt);
        }
    }

    /**
     * This method is launched when all non mined tiles have been opened.
     *
     * @param tile The last clicked tile.
     */
    private void fireVictoryEvent(final Tile tile) {
        GameBoardEvent evt = new GameBoardEvent(this, tile);
        for (GameBoardListener listener : listeners) {
            listener.victory(evt);
        }
    }

    /**
     * Initialize the grid.
     */
    public final void initialize() {
        final int three = 3;

        tiles = new Tile[height][width];

        for (int i = 0; i < numberOfMines; i++) {
            int l, c;
            do {
                l = (int) Math.floor(Math.random() * tiles.length);
                c = (int) Math.floor(Math.random() * tiles[l].length);
            } while (tiles[l][c] != null
                    || (tilesShape == SHAPE_TRIANGULAR_14 && l % three == 1 && c % three == 1)
                    || (tilesShape == SHAPE_PENTAGONAL && l % three == 1 && c % three == 0)
                    || (tilesShape == SHAPE_PARQUET && l % three == 1 && c % three == 1));
            tiles[l][c] = new Tile(true);
        }

        for (int l = 0; l < tiles.length; l++) {
            for (int c = 0; c < tiles[l].length; c++) {
                if (tiles[l][c] == null
                        && !(tilesShape == SHAPE_TRIANGULAR_14 && l % three == 1 && c % three == 1)
                        && !(tilesShape == SHAPE_PENTAGONAL && l % three == 1 && c % three == 0)
                        && !(tilesShape == SHAPE_PARQUET && l % three == 1 && c % three == 1)) {
                    tiles[l][c] = new Tile(false);
                }
            }
        }

        firstOpen = true;

        fireInitializedEvent();
    }

    /**
     * Add a listener for the game board events.
     *
     * @param listener The object that has to be added to the set of listening
     *                 objects.
     */
    public final synchronized void addGameBoardListener(final GameBoardListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    /**
     * Returns the tile located at given coordinates.
     *
     * @param l The line of the tile to return.
     * @param c The column of the tile to return.
     * @return The tile located at the given coordinates.
     */
    public final Tile getTile(final int l, final int c) {
        return tiles[l][c];
    }

    /**
     * Returns the coordinates of a given tile.
     *
     * @param tile The tile we want the coordinates.
     * @return The coordinates of the given tile.
     */
    public final Point getTileCoordinates(final Tile tile) {
        Point ret = null;

        for (int l = 0; l < tiles.length; l++) {
            for (int c = 0; c < tiles[l].length; c++) {
                if (tiles[l][c] == tile) {
                    ret = new Point(c, l);
                }
            }
        }

        return ret;
    }

    /**
     * Search the neighbors of a given tile.
     *
     * @param tile The tile we want the neighbors.
     * @return A collection of tiles that are the neighbors of the given tile.
     * @throws TilesShapeUnsupportedException If the tiles shape of the current
     *                                        grid is not supported.
     */
    public final Collection<Tile> getNeighborhood(final Tile tile)
    throws TilesShapeUnsupportedException {
        if (tile == null || (tile.isContainingMine() && tile.isOpen())) {
            return new ArrayList<Tile>();
        }

        for (int l = 0; l < tiles.length; l++) {
            for (int c = 0; c < tiles[l].length; c++) {
                if (tiles[l][c] == tile) {
                    return getNeighborhood(l, c);
                }
            }
        }

        return null;
    }

    /**
     * Search the neighbors of a tile located at given coordinates.
     *
     * @param l The line of the shape we want the neighbors.
     * @param c The column of the shape we want the neighbors.
     * @return A collection of tiles that are the neighbors of the given tile.
     * @throws TilesShapeUnsupportedException If the tiles shape of the current
     *                                        grid is not supported.
     */
    public final Collection<Tile> getNeighborhood(final int l, final int c)
    throws TilesShapeUnsupportedException {
        switch (tilesShape) {
        case SHAPE_TRIANGULAR:
            return getTriangularNeighborhood(l, c);
        case SHAPE_TRIANGULAR_14:
            return getTriangular14Neighborhood(l, c);
        case SHAPE_SQUARE:
            return getSquareNeighborhood(l, c);
        case SHAPE_PENTAGONAL:
            return getPentagonalNeighborhood(l, c);
        case SHAPE_HEXAGONAL:
            return getHexagonalNeighborhood(l, c);
        case SHAPE_OCTOSQUARE:
            return getOctosquareNeighborhood(l, c);
        case SHAPE_PARQUET:
            return getParquetNeighborhood(l, c);
        default:
            throw new TilesShapeUnsupportedException(tilesShape);
        }
    }

    /**
     * Search the neighbors of a given triangular tile.
     *
     * @param l The line of the shape we want the neighbors.
     * @param c The column of the shape we want the neighbors.
     * @return A collection of tiles that are the neighbors of the given tile.
     */
    private Collection<Tile> getTriangularNeighborhood(final int l, final int c) {
        Collection<Tile> ret = new ArrayList<Tile>();

        if (l > 0) {
            if (c > 0) {
                ret.add(tiles[l - 1][c - 1]);
            }
            ret.add(tiles[l - 1][c]);
            if (c < tiles[l].length - 1) {
                ret.add(tiles[l - 1][c + 1]);
            }
        }
        if (c > 1) {
            ret.add(tiles[l][c - 2]);
        }
        if (c > 0) {
            ret.add(tiles[l][c - 1]);
        }
        if (c < tiles[l].length - 1) {
            ret.add(tiles[l][c + 1]);
        }
        if (c < tiles[l].length - 2) {
            ret.add(tiles[l][c + 2]);
        }
        if (l < tiles.length - 1) {
            if (c > 0) {
                ret.add(tiles[l + 1][c - 1]);
            }
            ret.add(tiles[l + 1][c]);
            if (c < tiles[l].length - 1) {
                ret.add(tiles[l + 1][c + 1]);
            }
        }

        if ((l + c) % 2 == 0) {
            if (l > 0) {
                if (c > 1) {
                    ret.add(tiles[l - 1][c - 2]);
                }
                if (c < tiles[l].length - 2) {
                    ret.add(tiles[l - 1][c + 2]);
                }
            }
        } else {
            if (l < tiles.length - 1) {
                if (c > 1) {
                    ret.add(tiles[l + 1][c - 2]);
                }
                if (c < tiles[l].length - 2) {
                    ret.add(tiles[l + 1][c + 2]);
                }
            }
        }

        return ret;
    }

    /**
     * Search the neighbors of a given triangular tile.
     *
     * @param l The line of the shape we want the neighbors.
     * @param c The column of the shape we want the neighbors.
     * @return A collection of tiles that are the neighbors of the given tile.
     */
    private Collection<Tile> getTriangular14Neighborhood(final int l, final int c) {
        final int three = 3;

        Collection<Tile> ret = new ArrayList<Tile>();

        if (l % three == 0 && c % three == 0) {
            if (l > 1) {
                ret.add(tiles[l - 2][c]);
            }
            if (l > 0) {
                if (c > 1) {
                    ret.add(tiles[l - 1][c - 2]);
                }
                if (c > 0) {
                    ret.add(tiles[l - 1][c - 1]);
                }
                ret.add(tiles[l - 1][c]);
            }
            if (c > 0) {
                ret.add(tiles[l][c - 1]);
            }
            if (c < tiles[l].length - 1) {
                ret.add(tiles[l][c + 1]);
            }
            if (c < tiles[l].length - 1) {
                ret.add(tiles[l][c + 2]);
            }
            if (l < tiles.length - 1) {
                if (c > 0) {
                    ret.add(tiles[l + 1][c - 1]);
                }
                if (c < tiles[l + 1].length - 1) {
                    ret.add(tiles[l + 1][c]);
                }
                if (c < tiles[l + 1].length - 2) {
                    ret.add(tiles[l + 1][c + 2]);
                }
            }
            if (l < tiles.length - 2) {
                if (c > 0) {
                    ret.add(tiles[l + 2][c - 1]);
                }
                if (l < tiles.length - 2) {
                    ret.add(tiles[l + 2][c]);
                }
                if (l < tiles.length - 2 && c < tiles[l + 2].length - 1) {
                    ret.add(tiles[l + 2][c + 1]);
                }
                if (l < tiles.length - 2 && c < tiles[l + 2].length - 2) {
                    ret.add(tiles[l + 2][c + 2]);
                }
            }
        } else if (l % three == 2 && c % three == 1) {
            if (l > 1) {
                if (c > 0) {
                    ret.add(tiles[l - 2][c - 1]);
                }
                if (l > 1) {
                    ret.add(tiles[l - 2][c]);
                }
                if (l > 1 && c < tiles[l - 2].length - 1) {
                    ret.add(tiles[l - 2][c + 1]);
                }
            }
            if (l > 0) {
                if (c > 0) {
                    ret.add(tiles[l - 1][c - 1]);
                }
                if (l > 0 && c < tiles[l - 1].length - 1) {
                    ret.add(tiles[l - 1][c + 1]);
                }
                if (l > 0 && c < tiles[l - 1].length - 2) {
                    ret.add(tiles[l - 1][c + 2]);
                }
            }
            if (c > 0) {
                ret.add(tiles[l][c - 1]);
            }
            if (c < tiles[l].length - 1) {
                ret.add(tiles[l][c + 1]);
            }
            if (c < tiles[l].length - 2) {
                ret.add(tiles[l][c + 2]);
            }
            if (l < tiles.length - 1) {
                ret.add(tiles[l + 1][c]);
                if (c < tiles[l + 1].length - 1) {
                    ret.add(tiles[l + 1][c + 1]);
                }
                if (c < tiles[l + 1].length - 2) {
                    ret.add(tiles[l + 1][c + 2]);
                }
                if (c < tiles[l + 1].length - three) {
                    ret.add(tiles[l + 1][c + three]);
                }
            }
            if (l < tiles.length - 2 && c < tiles[l + 2].length - 1) {
                ret.add(tiles[l + 2][c + 1]);
            }
        } else if (l % three == 0 && c % three == 1) {
            if (l > 1) {
                if (c > 0) {
                    ret.add(tiles[l - 2][c - 1]);
                }
            }

            if (l > 0) {
                if (c > 2) {
                    ret.add(tiles[l - 1][c - three]);
                }
                if (c > 1) {
                    ret.add(tiles[l - 1][c - 2]);
                }
                if (c > 0) {
                    ret.add(tiles[l - 1][c - 1]);
                }
                ret.add(tiles[l - 1][c]);
            }

            if (c > 1) {
                ret.add(tiles[l][c - 2]);
            }
            if (c > 0) {
                ret.add(tiles[l][c - 1]);
            }
            if (c < tiles[l].length - 1) {
                ret.add(tiles[l][c + 1]);
            }

            if (l < tiles.length - 1) {
                if (c > 1) {
                    ret.add(tiles[l + 1][c - 2]);
                }
                if (c > 0) {
                    ret.add(tiles[l + 1][c - 1]);
                }
                if (c < tiles[l + 1].length - 1) {
                    ret.add(tiles[l + 1][c + 1]);
                }
            }

            if (l < tiles.length - 2) {
                if (c > 0) {
                    ret.add(tiles[l + 2][c - 1]);
                }
                ret.add(tiles[l + 2][c]);
                if (c < tiles[l + 2].length - 1) {
                    ret.add(tiles[l + 2][c + 1]);
                }
            }
        } else if (l % three == 2 && c % three == 2) {
            if (l > 1) {
                if (c > 1) {
                    ret.add(tiles[l - 2][c - 2]);
                }
                if (c > 0) {
                    ret.add(tiles[l - 2][c - 1]);
                }
                ret.add(tiles[l - 2][c]);
                if (c < tiles[l - 2].length - 1) {
                    ret.add(tiles[l - 2][c + 1]);
                }
            }

            if (l > 0) {
                if (c > 1) {
                    ret.add(tiles[l - 1][c - 2]);
                }
                ret.add(tiles[l - 1][c]);
                if (c < tiles[l - 1].length - 1) {
                    ret.add(tiles[l - 1][c + 1]);
                }
            }

            if (c > 1) {
                ret.add(tiles[l][c - 2]);
            }
            if (c > 0) {
                ret.add(tiles[l][c - 1]);
            }
            if (c < tiles[l].length - 1) {
                ret.add(tiles[l][c + 1]);
            }

            if (l < tiles.length - 1) {
                ret.add(tiles[l + 1][c]);
                if (c < tiles[l + 1].length - 1) {
                    ret.add(tiles[l + 1][c + 1]);
                }
                if (c < tiles[l + 1].length - 2) {
                    ret.add(tiles[l + 1][c + 2]);
                }
            }

            if (l < tiles.length - 2) {
                ret.add(tiles[l + 2][c]);
            }
        } else if (l % three == 1 && c % three == 2) {
            if (l > 2) {
                if (c < tiles[l - three].length - 1) {
                    ret.add(tiles[l - three][c + 1]);
                }
            }

            if (l > 1) {
                if (c > 0) {
                    ret.add(tiles[l - 2][c - 1]);
                }
                ret.add(tiles[l - 2][c]);
                if (c < tiles[l - 2].length - 1) {
                    ret.add(tiles[l - 2][c + 1]);
                }
            }

            if (l > 0) {
                if (c > 1) {
                    ret.add(tiles[l - 1][c - 2]);
                }
                if (c > 0) {
                    ret.add(tiles[l - 1][c - 1]);
                }
                ret.add(tiles[l - 1][c]);
                if (c < tiles[l - 1].length - 1) {
                    ret.add(tiles[l - 1][c + 1]);
                }
                if (c < tiles[l - 1].length - 2) {
                    ret.add(tiles[l - 1][c + 2]);
                }
            }

            if (c > 1) {
                ret.add(tiles[l][c - 2]);
            }
            if (c < tiles[l].length - 1) {
                ret.add(tiles[l][c + 1]);
            }

            if (l < tiles.length - 1) {
                if (c > 1) {
                    ret.add(tiles[l + 1][c - 2]);
                }
                if (c > 0) {
                    ret.add(tiles[l + 1][c - 1]);
                }
                ret.add(tiles[l + 1][c]);
            }
        } else if (l % three == 2 && c % three == 0) {
            if (l > 1) {
                ret.add(tiles[l - 2][c]);
                if (c < tiles[l - 2].length - 1) {
                    ret.add(tiles[l - 2][c + 1]);
                }
                if (c < tiles[l - 2].length - 2) {
                    ret.add(tiles[l - 2][c + 2]);
                }
            }

            if (l > 0) {
                ret.add(tiles[l - 1][c]);
                if (c < tiles[l - 1].length - 2) {
                    ret.add(tiles[l - 1][c + 2]);
                }
            }

            if (c > 1) {
                ret.add(tiles[l][c - 2]);
            }
            if (c > 0) {
                ret.add(tiles[l][c - 1]);
            }
            if (c < tiles[l].length - 1) {
                ret.add(tiles[l][c + 1]);
            }
            if (c < tiles[l].length - 2) {
                ret.add(tiles[l][c + 2]);
            }

            if (l < tiles.length - 1) {
                if (c > 0) {
                    ret.add(tiles[l + 1][c - 1]);
                }
                ret.add(tiles[l + 1][c]);
                if (c < tiles[l + 1].length - 1) {
                    ret.add(tiles[l + 1][c + 1]);
                }
                if (c < tiles[l + 1].length - 2) {
                    ret.add(tiles[l + 1][c + 2]);
                }
            }

            if (l < tiles.length - 2) {
                if (c > 0) {
                    ret.add(tiles[l + 2][c - 1]);
                }
            }
        } else if (l % three == 0 && c % three == 2) {
            if (l > 1) {
                if (c < tiles[l - 2].length - 1) {
                    ret.add(tiles[l - 2][c + 1]);
                }
            }

            if (l > 0) {
                if (c > 1) {
                    ret.add(tiles[l - 1][c - 2]);
                }
                if (c > 0) {
                    ret.add(tiles[l - 1][c - 1]);
                }
                ret.add(tiles[l - 1][c]);
                if (c < tiles[l - 1].length - 1) {
                    ret.add(tiles[l - 1][c + 1]);
                }
            }

            if (c > 1) {
                ret.add(tiles[l][c - 2]);
            }
            if (c > 0) {
                ret.add(tiles[l][c - 1]);
            }
            if (c < tiles[l].length - 1) {
                ret.add(tiles[l][c + 1]);
            }
            if (c < tiles[l].length - 2) {
                ret.add(tiles[l][c + 2]);
            }

            if (l < tiles.length - 1) {
                if (c > 1) {
                    ret.add(tiles[l + 1][c - 2]);
                }
                ret.add(tiles[l + 1][c]);
            }

            if (l < tiles.length - 2) {
                if (c > 1) {
                    ret.add(tiles[l + 2][c - 2]);
                }
                if (c > 0) {
                    ret.add(tiles[l + 2][c - 1]);
                }
                ret.add(tiles[l + 2][c]);
            }
        } else if (l % three == 1 && c % three == 0) {
            if (l > 0) {
                ret.add(tiles[l - 1][c]);
                if (c < tiles[l - 1].length - 1) {
                    ret.add(tiles[l - 1][c + 1]);
                }
                if (c < tiles[l - 1].length - 2) {
                    ret.add(tiles[l - 1][c + 2]);
                }
            }

            if (c > 0) {
                ret.add(tiles[l][c - 1]);
            }
            if (c < tiles[l].length - 2) {
                ret.add(tiles[l][c + 2]);
            }

            if (l < tiles.length - 1) {
                if (c > 1) {
                    ret.add(tiles[l + 1][c - 2]);
                }
                if (c > 0) {
                    ret.add(tiles[l + 1][c - 1]);
                }
                ret.add(tiles[l + 1][c]);
                if (c < tiles[l + 1].length - 1) {
                    ret.add(tiles[l + 1][c + 1]);
                }
                if (c < tiles[l + 1].length - 2) {
                    ret.add(tiles[l + 1][c + 2]);
                }
            }

            if (l < tiles.length - 2) {
                if (c > 0) {
                    ret.add(tiles[l + 2][c - 1]);
                }
                ret.add(tiles[l + 2][c]);
                if (c < tiles[l + 2].length - 1) {
                    ret.add(tiles[l + 2][c + 1]);
                }
            }

            if (l < tiles.length - three) {
                if (c > 0) {
                    ret.add(tiles[l + three][c - 1]);
                }
            }
        }

        return ret;
    }

    /**
     * Search the neighbors of a given square tile.
     *
     * @param l The line of the shape we want the neighbors.
     * @param c The column of the shape we want the neighbors.
     * @return A collection of tiles that are the neighbors of the given tile.
     */
    private Collection<Tile> getSquareNeighborhood(final int l, final int c) {
        Collection<Tile> ret = new ArrayList<Tile>();

        if (l > 0) {
            if (c > 0) {
                ret.add(tiles[l - 1][c - 1]);
            }
            ret.add(tiles[l - 1][c]);
            if (c < tiles[l].length - 1) {
                ret.add(tiles[l - 1][c + 1]);
            }
        }

        if (c > 0) {
            ret.add(tiles[l][c - 1]);
        }
        if (c < tiles[l].length - 1) {
            ret.add(tiles[l][c + 1]);
        }

        if (l < tiles.length - 1) {
            if (c > 0) {
                ret.add(tiles[l + 1][c - 1]);
            }
            ret.add(tiles[l + 1][c]);
            if (c < tiles[l].length - 1) {
                ret.add(tiles[l + 1][c + 1]);
            }
        }

        return ret;
    }

    /**
     * Search the neighbors of a given pentagonal tile.
     *
     * @param l The line of the shape we want the neighbors.
     * @param c The column of the shape we want the neighbors.
     * @return A collection of tiles that are the neighbors of the given tile.
     */
    private Collection<Tile> getPentagonalNeighborhood(final int l, final int c) {
        Collection<Tile> ret = new ArrayList<Tile>();
        final int three = 3;

        if (l % three == 0 && c % three == 1) {
            if (l > 0) {
                if (c > 1) {
                    ret.add(tiles[l - 1][c - 2]);
                }
                if (c > 0) {
                    ret.add(tiles[l - 1][c - 1]);
                }
                ret.add(tiles[l - 1][c]);
                if (c < tiles[l - 1].length - 1) {
                    ret.add(tiles[l - 1][c + 1]);
                }
            }

            if (c > 0) {
                ret.add(tiles[l][c - 1]);
            }
            if (c < tiles[l].length - 1) {
                ret.add(tiles[l][c + 1]);
            }

            if (l < tiles.length - 1) {
                ret.add(tiles[l + 1][c]);
            }
        } else if (l % three == 1 && c % three == 2) {
            if (l > 0) {
                ret.add(tiles[l - 1][c]);
                if (c < tiles[l - 1].length - 1) {
                    ret.add(tiles[l - 1][c + 1]);
                }
            }

            if (c > 0) {
                ret.add(tiles[l][c - 1]);
            }
            if (c < tiles[l].length - 2) {
                ret.add(tiles[l][c + 2]);
            }

            if (l < tiles.length - 1) {
                if (c > 0) {
                    ret.add(tiles[l + 1][c - 1]);
                }
                ret.add(tiles[l + 1][c]);
                if (c < tiles[l + 1].length - 1) {
                    ret.add(tiles[l + 1][c + 1]);
                }
            }
        } else if (l % three == 1 && c % three == 1) {
            if (l > 0) {
                if (c > 0) {
                    ret.add(tiles[l - 1][c - 1]);
                }
                ret.add(tiles[l - 1][c]);
                if (c < tiles[l - 1].length - 1) {
                    ret.add(tiles[l - 1][c + 1]);
                }
            }

            if (c > 1) {
                ret.add(tiles[l][c - 2]);
            }
            if (c < tiles[l].length - 1) {
                ret.add(tiles[l][c + 1]);
            }

            if (l < tiles.length - 1) {
                if (c > 0) {
                    ret.add(tiles[l + 1][c - 1]);
                }
                ret.add(tiles[l + 1][c]);
            }
        } else if (l % three == 2 && c % three == 2) {
            if (l > 0) {
                ret.add(tiles[l - 1][c]);
            }

            if (c > 0) {
                ret.add(tiles[l][c - 1]);
            }
            if (c < tiles[l].length - 1) {
                ret.add(tiles[l][c + 1]);
            }

            if (l < tiles.length - 1) {
                if (c > 0) {
                    ret.add(tiles[l + 1][c - 1]);
                }
                ret.add(tiles[l + 1][c]);
                if (c < tiles[l + 1].length - 1) {
                    ret.add(tiles[l + 1][c + 1]);
                }
                if (c < tiles[l + 1].length - 2) {
                    ret.add(tiles[l + 1][c + 2]);
                }
            }
        } else if (l % three == 0 && c % three == 0) {
            if (l > 0) {
                if (c > 0) {
                    ret.add(tiles[l - 1][c - 1]);
                }
                ret.add(tiles[l - 1][c]);
            }

            if (c > 0) {
                ret.add(tiles[l][c - 1]);
            }
            if (c < tiles[l].length - 1) {
                ret.add(tiles[l][c + 1]);
            }

            if (l < tiles.length - 1) {
                if (c > 0) {
                    ret.add(tiles[l + 1][c - 1]);
                }
                ret.add(tiles[l + 1][c]);
                if (c < tiles[l + 1].length - 1) {
                    ret.add(tiles[l + 1][c + 1]);
                }
            }

            if (l < tiles.length - 2) {
                ret.add(tiles[l + 2][c]);
            }
        } else if (l % three == 2 && c % three == 1) {
            if (l > 1) {
                if (c < tiles[l - 2].length - 1) {
                    ret.add(tiles[l - 2][c + 1]);
                }
            }

            if (l > 0) {
                ret.add(tiles[l - 1][c]);
                if (c < tiles[l - 1].length - 1) {
                    ret.add(tiles[l - 1][c + 1]);
                }
            }

            if (c > 0) {
                ret.add(tiles[l][c - 1]);
            }
            if (c < tiles[l].length - 1) {
                ret.add(tiles[l][c + 1]);
            }

            if (l < tiles.length - 1) {
                ret.add(tiles[l + 1][c]);
                if (c < tiles[l].length - 1) {
                    ret.add(tiles[l + 1][c + 1]);
                }
            }
        } else if (l % three == 0 && c % three == 2) {
            if (l > 0) {
                if (c > 0) {
                    ret.add(tiles[l - 1][c - 1]);
                }
                ret.add(tiles[l - 1][c]);
            }

            if (c > 0) {
                ret.add(tiles[l][c - 1]);
            }
            if (c < tiles[l].length - 1) {
                ret.add(tiles[l][c + 1]);
            }

            if (l < tiles.length - 1) {
                if (c > 0) {
                    ret.add(tiles[l + 1][c - 1]);
                }
                ret.add(tiles[l + 1][c]);
            }

            if (l < tiles.length - 2) {
                if (c > 0) {
                    ret.add(tiles[l + 2][c - 1]);
                }
            }
        } else if (l % three == 2 && c % three == 0) {
            if (l > 1) {
                ret.add(tiles[l - 2][c]);
            }

            if (l > 0) {
                if (c > 0) {
                    ret.add(tiles[l - 1][c - 1]);
                }
                if (c < tiles[l - 1].length - 1) {
                    ret.add(tiles[l - 1][c + 1]);
                }
            }

            if (c > 0) {
                ret.add(tiles[l][c - 1]);
            }
            if (c < tiles[l].length - 1) {
                ret.add(tiles[l][c + 1]);
            }

            if (l < tiles.length - 1) {
                ret.add(tiles[l + 1][c]);
                if (c < tiles[l].length - 1) {
                    ret.add(tiles[l + 1][c + 1]);
                }
            }
        }

        return ret;
    }

    /**
     * Search the neighbors of a given hexagonal tile.
     *
     * @param l The line of the shape we want the neighbors.
     * @param c The column of the shape we want the neighbors.
     * @return A collection of tiles that are the neighbors of the given tile.
     */
    private Collection<Tile> getHexagonalNeighborhood(final int l, final int c) {
        Collection<Tile> ret = new ArrayList<Tile>();

        if (l > 0) {
            ret.add(tiles[l - 1][c]);
        }
        if (c > 0) {
            ret.add(tiles[l][c - 1]);
        }
        if (c < tiles[l].length - 1) {
            ret.add(tiles[l][c + 1]);
        }
        if (l < tiles.length - 1) {
            ret.add(tiles[l + 1][c]);
        }

        if (c % 2 == 0) {
            if (l > 0) {
                if (c > 0) {
                    ret.add(tiles[l - 1][c - 1]);
                }
                if (c < tiles[l].length - 1) {
                    ret.add(tiles[l - 1][c + 1]);
                }
            }
        } else {
            if (l < tiles.length - 1) {
                if (c > 0) {
                    ret.add(tiles[l + 1][c - 1]);
                }
                if (c < tiles[l].length - 1) {
                    ret.add(tiles[l + 1][c + 1]);
                }
            }
        }
        return ret;
    }

    /**
     * Search the neighbors of a given octogonal/square tile.
     *
     * @param l The line of the shape we want the neighbors.
     * @param c The column of the shape we want the neighbors.
     * @return A collection of tiles that are the neighbors of the given tile.
     */
    private Collection<Tile> getOctosquareNeighborhood(final int l, final int c) {
        Collection<Tile> ret = new ArrayList<Tile>();

        if ((l + c) % 2 == 0) {
            if (l > 0) {
                if (c > 0) {
                    ret.add(tiles[l - 1][c - 1]);
                }
                ret.add(tiles[l - 1][c]);
                if (c < tiles[l].length - 1) {
                    ret.add(tiles[l - 1][c + 1]);
                }
            }

            if (c > 0) {
                ret.add(tiles[l][c - 1]);
            }
            if (c < tiles[l].length - 1) {
                ret.add(tiles[l][c + 1]);
            }

            if (l < tiles.length - 1) {
                if (c > 0) {
                    ret.add(tiles[l + 1][c - 1]);
                }
                ret.add(tiles[l + 1][c]);
                if (c < tiles[l].length - 1) {
                    ret.add(tiles[l + 1][c + 1]);
                }
            }
        } else {
            if (l > 0) {
                ret.add(tiles[l - 1][c]);
            }

            if (c > 0) {
                ret.add(tiles[l][c - 1]);
            }
            if (c < tiles[l].length - 1) {
                ret.add(tiles[l][c + 1]);
            }

            if (l < tiles.length - 1) {
                ret.add(tiles[l + 1][c]);
            }
        }
        return ret;
    }

    /**
     * Search the neighbors of a given parquet type tile.
     *
     * @param l The line of the shape we want the neighbors.
     * @param c The column of the shape we want the neighbors.
     * @return A collection of tiles that are the neighbors of the given tile.
     */
    private Collection<Tile> getParquetNeighborhood(final int l, final int c) {
        Collection<Tile> ret = new ArrayList<Tile>();
        final int three = 3;

        if (l % three == 0 && c % three == 0) {
            // First vertical tiles
            if (l > 0) {
                if (c > 0) {
                    ret.add(tiles[l - 1][c - 1]);
                }
                ret.add(tiles[l - 1][c]);
            }

            if (c > 0) {
                ret.add(tiles[l][c - 1]);
            }
            if (c < tiles[l].length - 1) {
                ret.add(tiles[l][c + 1]);
            }

            if (l < tiles.length - 1) {
                if (c > 0) {
                    ret.add(tiles[l + 1][c - 1]);
                }
                ret.add(tiles[l + 1][c]);
            }

            if (l < tiles.length - 2) {
                if (c > 0) {
                    ret.add(tiles[l + 2][c - 1]);
                }
            }
        } else if (l % three == 2 && c % three == 1) {
            // First vertical tiles
            if (l > 1) {
                ret.add(tiles[l - 2][c]);
            }

            if (l > 0) {
                if (c > 0) {
                    ret.add(tiles[l - 1][c - 1]);
                }
                if (c < tiles[l - 1].length - 1) {
                    ret.add(tiles[l - 1][c + 1]);
                }
            }

            if (c > 0) {
                ret.add(tiles[l][c - 1]);
            }
            if (c < tiles[l].length - 1) {
                ret.add(tiles[l][c + 1]);
            }

            if (l < tiles.length - 1) {
                ret.add(tiles[l + 1][c]);
                if (c < tiles[l + 1].length - 1) {
                    ret.add(tiles[l + 1][c + 1]);
                }
            }
        } else if (l % three == 0 && c % three == 1) {
            // Second vertical tiles
            if (l > 0) {
                if (c > 0) {
                    ret.add(tiles[l - 1][c - 1]);
                }
                ret.add(tiles[l - 1][c]);
            }

            if (c > 0) {
                ret.add(tiles[l][c - 1]);
            }
            if (c < tiles[l].length - 1) {
                ret.add(tiles[l][c + 1]);
            }

            if (l < tiles.length - 1) {
                if (c > 0) {
                    ret.add(tiles[l + 1][c - 1]);
                }
                if (c < tiles[l + 1].length - 1) {
                    ret.add(tiles[l + 1][c + 1]);
                }
            }

            if (l < tiles.length - 2) {
                ret.add(tiles[l + 2][c]);
            }
        } else if (l % three == 2 && c % three == 2) {
            // Second vertical tiles
            if (l > 1) {
                if (c < tiles[l - 2].length - 1) {
                    ret.add(tiles[l - 2][c + 1]);
                }
            }

            if (l > 0) {
                ret.add(tiles[l - 1][c]);
                if (c < tiles[l - 1].length - 1) {
                    ret.add(tiles[l - 1][c + 1]);
                }
            }

            if (c > 0) {
                ret.add(tiles[l][c - 1]);
            }
            if (c < tiles[l].length - 1) {
                ret.add(tiles[l][c + 1]);
            }

            if (l < tiles.length - 1) {
                ret.add(tiles[l + 1][c]);
                if (c < tiles[l + 1].length - 1) {
                    ret.add(tiles[l + 1][c + 1]);
                }
            }
        } else if (l % three == 0 && c % three == 2) {
            // First horizontal tiles
            if (l > 0) {
                if (c > 1) {
                    ret.add(tiles[l - 1][c - 2]);
                }
                if (c > 0) {
                    ret.add(tiles[l - 1][c - 1]);
                }
                ret.add(tiles[l - 1][c]);
                if (c < tiles[l - 1].length - 1) {
                    ret.add(tiles[l - 1][c + 1]);
                }
            }

            if (c > 0) {
                ret.add(tiles[l][c - 1]);
            }
            if (c < tiles[l].length - 1) {
                ret.add(tiles[l][c + 1]);
            }

            if (l < tiles.length - 1) {
                ret.add(tiles[l + 1][c]);
            }
        } else if (l % three == 1 && c % three == 0) {
            // First horizontal tiles
            if (l > 0) {
                ret.add(tiles[l - 1][c]);
                if (c < tiles[l - 1].length - 1) {
                    ret.add(tiles[l - 1][c + 1]);
                }
            }

            if (c > 0) {
                ret.add(tiles[l][c - 1]);
            }
            if (c < tiles[l].length - 2) {
                ret.add(tiles[l][c + 2]);
            }

            if (l < tiles.length - 1) {
                if (c > 0) {
                    ret.add(tiles[l + 1][c - 1]);
                }
                ret.add(tiles[l + 1][c]);
                if (c < tiles[l + 1].length - 1) {
                    ret.add(tiles[l + 1][c + 1]);
                }
            }
        } else if (l % three == 1 && c % three == 2) {
            // Second horizontal tiles
            if (l > 0) {
                if (c > 0) {
                    ret.add(tiles[l - 1][c - 1]);
                }
                ret.add(tiles[l - 1][c]);
                if (c < tiles[l - 1].length - 1) {
                    ret.add(tiles[l - 1][c + 1]);
                }
            }

            if (c > 1) {
                ret.add(tiles[l][c - 2]);
            }
            if (c < tiles[l].length - 1) {
                ret.add(tiles[l][c + 1]);
            }

            if (l < tiles.length - 1) {
                if (c > 0) {
                    ret.add(tiles[l + 1][c - 1]);
                }
                ret.add(tiles[l + 1][c]);
            }
        } else if (l % three == 2 && c % three == 0) {
            // Second horizontal tiles
            if (l > 0) {
                ret.add(tiles[l - 1][c]);
            }

            if (c > 0) {
                ret.add(tiles[l][c - 1]);
            }
            if (c < tiles[l].length - 1) {
                ret.add(tiles[l][c + 1]);
            }

            if (l < tiles.length - 1) {
                if (c > 0) {
                    ret.add(tiles[l + 1][c - 1]);
                }
                ret.add(tiles[l + 1][c]);
                if (c < tiles[l + 1].length - 1) {
                    ret.add(tiles[l + 1][c + 1]);
                }
                if (c < tiles[l + 1].length - 2) {
                    ret.add(tiles[l + 1][c + 2]);
                }
            }
        }

        return ret;
    }

    /**
     * Compute the number of mines surrounding a given tile.
     *
     * @param tile The tile we want the number of surrounding mines.
     * @return The number of mines surrounding the given tile.
     * @throws TilesShapeUnsupportedException If the tiles shape of the current
     *                                        grid is not supported.
     */
    public final int getNumberOfSurroundingMines(final Tile tile)
    throws TilesShapeUnsupportedException {
        if (tile == null) {
            return -1;
        }

        for (int l = 0; l < tiles.length; l++) {
            for (int c = 0; c < tiles[l].length; c++) {
                if (tiles[l][c] == tile) {
                    return getNumberOfSurroundingMines(l, c);
                }
            }
        }

        return -1;
    }

    /**
     * Compute the number of mines surrounding a tile located at given
     * coordinates.
     *
     * @param l The line of the tile we want the number of surrounding mines.
     * @param c The column of the tile we want the number of surrounding mines.
     * @return The number of mines surrounding the tile located at the given
     *         coordinates.
     * @throws TilesShapeUnsupportedException If the tiles shape of the current
     *                                        grid is not supported.
     */
    public final int getNumberOfSurroundingMines(final int l, final int c)
    throws TilesShapeUnsupportedException {
        int sum = 0;
        for (Tile tmp : getNeighborhood(l, c)) {
            if (tmp != null && tmp.isContainingMine()) {
                sum++;
            }
        }

        return sum;
    }

    /**
     * Tell whether or not the tile located at given coordinates is containing
     * a mine.
     *
     * @param l The line of the tile we want to know if it is containing a
     *          mine.
     * @param c The column of the tile we want to know if it is containing a
     *          mine.
     * @return true if the tile located at given coordinates is containing
     *         a mine, false otherwise.
     */
    public final boolean isContainingMine(final int l, final int c) {
        return tiles[l][c] != null && tiles[l][c].isContainingMine();
    }

    /**
     * Tell whether or not the tile located at given coordinates is open.
     *
     * @param l The line of the tile we want to know if it is open.
     * @param c The column of the tile we want to know if it is open.
     * @return true if the tile located at given coordinates is open, false
     *         otherwise.
     */
    public final boolean isOpen(final int l, final int c) {
        return tiles[l][c] == null || tiles[l][c].isOpen();
    }

    /**
     * Tell whether or not the tile located at given coordinates is flagged.
     *
     * @param l The line of the tile we want to know if it is flagged.
     * @param c The column of the tile we want to know if it is flagged.
     * @return true if the tile located at given coordinates is flagged, false
     *         otherwise.
     */
    public final boolean isFlagged(final int l, final int c) {
        return tiles[l][c].isFlagged();
    }

    /**
     * Tell whether or not the tile located at given coordinates is marked.
     *
     * @param l The line of the tile we want to know if it is flagged.
     * @param c The column of the tile we want to know if it is flagged.
     * @return true if the tile located at given coordinates is flagged, false
     *         otherwise.
     */
    public final boolean isMarked(final int l, final int c) {
        return tiles[l][c].isMarked();
    }

    /**
     * Open the given tile.<br/>
     * This method can fire the defeat event (if the
     * opening tile is containing a mine) or a victory event (if the opening
     * tile is the last non mined tile).<br/>
     * If the tile is the first open and it is containing a mine, then the mine
     * is moved to an empty tile.
     *
     * @param tile The tile want to open.
     * @return true if the tile is the first open, false otherwise.
     */
    public final boolean open(final Tile tile) {
        if (firstOpen) {
            firstOpen = false;
            if (tile.isContainingMine() && numberOfMines < width * height) {
                tile.setContainingMine(false);

                int l, c;
                do {
                    l = (int) Math.floor(Math.random() * tiles.length);
                    c = (int) Math.floor(Math.random() * tiles[l].length);
                } while (tiles[l][c] == null || tiles[l][c].isContainingMine());
                tiles[l][c].setContainingMine(true);
            }
            tile.setOpen(true);

            return true;
        } else {
            tile.setOpen(true);
            checkGameEnd(tile);

            return false;
        }
    }

    /**
     * Check if the game has been won.
     *
     * @param tile The last clicked tile.
     */
    public final void checkGameEnd(final Tile tile) {
        if (tile != null && tile.isContainingMine()) {
            fireDefeatEvent(tile);
        } else {
            int nbClosed = 0;
            for (int l = 0; l < tiles.length; l++) {
                for (int c = 0; c < tiles[l].length; c++) {
                    if (tiles[l][c] != null && !tiles[l][c].isOpen()) {
                        nbClosed++;
                    }
                }
            }
            if (nbClosed == numberOfMines) {
                for (int l = 0; l < tiles.length; l++) {
                    for (int c = 0; c < tiles[l].length; c++) {
                        if (tiles[l][c] != null && !tiles[l][c].isFlagged() && !tiles[l][c].isOpen()) {
                            tiles[l][c].setFlagged(true);
                        }
                    }
                }

                fireVictoryEvent(tile);
            }
        }
    }
}

