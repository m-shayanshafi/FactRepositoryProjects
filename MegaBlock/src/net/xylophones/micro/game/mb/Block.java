/*
 * Block.java
 * 
 * Copyright 2007 William Robertson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package net.xylophones.micro.game.mb;

import net.xylophones.micro.game.mb.geometry.Point;

/**
 * <p>
 * A class which defines the shapes of each tetrad in each
 * possible rotation and provides methods for determining certain 
 * characteristics of each shape. 
 * </p>
 * 
 * <p>
 * Each possible tetrad has a constant value 
 * {@code TETRAD_I, TETRAD_J, TETRAD_L} etc. 
 * </p>
 * 
 * <p>
 * Each possible tetrad rotation constant value 
 * - one of {@code ROTATION_0, ROTATION_90, ROTATION_180} or {@code ROTATION_270}
 * </p>  
 * 
 * <p>
 * Combined, a tertad and a rotation have a <i>shape</i>. A shape
 * is a 4x4 array of booleans, where {@code true} values represent a filled
 * space and a {@code false} values repeesent a space. 
 * </p>  
 * 
 * <p>
 * e.g. The J tetrad in the {@code ROTATION_0} position <br />
 * <tt>
 * tfff<br />
 * tttf<br />
 * ffff<br />
 * ffff
 * </tt>
 * </p>
 * 
 *
 * @author william@xylophones.net
 */
public class Block {
    
	/**
	 * The I tetrad
	 */
    public static final int TETRAD_I = 0;
    
	/**
	 * The J tetrad
	 */
    public static final int TETRAD_J = 1;
    
	/**
	 * The L tetrad
	 */
    public static final int TETRAD_L = 2;
    
	/**
	 * The O tetrad
	 */
    public static final int TETRAD_O = 3;
    
	/**
	 * The S tetrad
	 */
    public static final int TETRAD_S = 4;
    
	/**
	 * The T tetrad
	 */
    public static final int TETRAD_T = 5;
    
	/**
	 * The Z tetrad
	 */
    public static final int TETRAD_Z = 6;
    
	/**
	 * 0 degree rotation
	 */
    public static final int ROTATION_0 = 0;
    
	/**
	 * 90 degree rotation
	 */
    public static final int ROTATION_90 = 1;
    
	/**
	 * 180 degree rotation
	 */
    public static final int ROTATION_180 = 2;
    
	/**
	 * 270 degree rotation
	 */
    public static final int ROTATION_270 = 3;
    
    /**
     * All possible tetrads
     */
    private static final int[] possibleTetrads = {
        TETRAD_I, TETRAD_J, TETRAD_L, TETRAD_O, TETRAD_S, TETRAD_T, TETRAD_Z
    };

    /*
     * All possible transforms
     */
    /*
    private static final int[] possibleTransforms = {
        ROTATION_0, ROTATION_90, ROTATION_180, ROTATION_270
    };
    */

    /**
     * <p>
     * Holds positions for the blocks of the tetrads in various rotations
     * </p>
     * 
     * <p>
     * They are not 'real' rotations, so it is easiest to store them than 
     * to calculate them on-the-fly.
     * 
     * <p>
     * The positions are stored as arrays of integer arrays - 
     * </p>
     */
    private static final boolean[][][][] tetradShapes = {
        { // ROTATION_0
            // TETRAD_I
            {
                {false, true, false, false},
                {false, true, false, false},
                {false, true, false, false},
                {false, true, false, false}, 
            // TETRAD_J
            }, {
                {true , false, false, false},
                {true , true , true , false},
                {false, false, false, false},
                {false, false, false, false}, 
            // TETRAD_L
            }, {
                {false, false, true , false},
                {true , true , true , false},
                {false, false, false, false},
                {false, false, false, false}, 
            // TETRAD_O
            }, {
                {true , true , false, false},
                {true , true , false, false},
                {false, false, false, false},
                {false, false, false, false}, 
            // TETRAD_S
            }, {
                {false, true , true , false},
                {true , true , false, false},
                {false, false, false, false},
                {false, false, false, false}, 
            // TETRAD_T
            }, {
                {false, true , false, false},
                {true , true , true , false},
                {false, false, false, false},
                {false, false, false, false}, 
            // TETRAD_Z
            }, {
                {true , true , false, false},
                {false, true , true , false},
                {false, false, false, false},
                {false, false, false, false}, 
            },
        }, { // ROTATION_90
            // TETRAD_I
            {
                {false, false, false, false},
                {true , true , true , true },
                {false, false, false, false},
                {false, false, false, false}, 
            // TETRAD_J
            }, {
                {false, true , false, false},
                {false, true , false, false},
                {true , true , false, false},
                {false, false, false, false}, 
            // TETRAD_L
            }, {
                {true , true , false, false},
                {false, true , false, false},
                {false, true , false, false},
                {false, false, false, false}, 
            // TETRAD_O
            }, {
                {true , true , false, false},
                {true , true , false, false},
                {false, false, false, false},
                {false, false, false, false}, 
            // TETRAD_S
            }, {
                {true , false, false, false},
                {true , true , false, false},
                {false, true , false, false},
                {false, false, false, false}, 
            // TETRAD_T
            }, {
                {false, true , false, false},
                {true , true , false, false},
                {false, true , false, false},
                {false, false, false, false}, 
            // TETRAD_Z
            }, {
                {false, true , false, false},
                {true , true , false, false},
                {true , false, false, false},
                {false, false, false, false},  
            },
        }, { // ROTATION_180
            // TETRAD_I
            {
                {false, true, false, false},
                {false, true, false, false},
                {false, true, false, false},
                {false, true, false, false}, 
            // TETRAD_J
            }, {
                {false, false, false, false},
                {true , true , true , false},
                {false, false, true , false},
                {false, false, false, false}, 
            // TETRAD_L
            }, {
                {false, false, false, false},
                {true , true , true , false},
                {true , false, false, false},
                {false, false, false, false}, 
            // TETRAD_O
            }, {
                {true , true , false, false},
                {true , true , false, false},
                {false, false, false, false},
                {false, false, false, false}, 
            // TETRAD_S
            }, {
                {false, true , true , false},
                {true , true , false, false},
                {false, false, false, false},
                {false, false, false, false}, 
            // TETRAD_T
            }, {
                {false, false, false, false},
                {true , true , true , false},
                {false, true , false, false},
                {false, false, false, false}, 
            // TETRAD_Z
            }, {
                {true , true , false, false},
                {false, true , true , false},
                {false, false, false, false},
                {false, false, false, false}, 
            },
        }, { // ROTATION_270
            // TETRAD_I
            {
                {false, false, false, false},
                {true , true , true , true },
                {false, false, false, false},
                {false, false, false, false}, 
            // TETRAD_J
            }, {
                {false, true , true , false},
                {false, true , false, false},
                {false, true , false, false},
                {false, false, false, false}, 
            // TETRAD_L
            }, {
                {false, true , false, false},
                {false, true , false, false},
                {false, true , true , false},
                {false, false, false, false}, 
            // TETRAD_O
            }, {
                {true , true , false, false},
                {true , true , false, false},
                {false, false, false, false},
                {false, false, false, false}, 
            // TETRAD_S
            }, {
                {true , false, false, false},
                {true , true , false, false},
                {false, true , false, false},
                {false, false, false, false}, 
            // TETRAD_T
            }, {
                {false, true , false, false},
                {false, true , true , false},
                {false, true,  false, false},
                {false, false, false, false}, 
            // TETRAD_Z
            }, {
                {false, true , false, false},
                {true , true , false, false},
                {true , false, false, false},
                {false, false, false, false},  
            },
        },
    };

    /**
     * Private constructor - just static methods, so no
     * instantiation
     */
    private Block() {
    }

    /**
     * Get an array of all possible tetrad identifiers
     * 
     * @return
     */
    public static int[] getPossibleTetrads () {
        return possibleTetrads;
    }

    /**
     * Get the shape of a tetrad based on the tetrad identifier
     * and a rotation
     */
    public static boolean[][] getShape(int tetrad, int rotation) {
        return tetradShapes[rotation][tetrad];
    }

    /**
     * <p>
     * Get the leftmost cells of the block.
     * </p>
     * 
     * <p>
     * Returns an array of {@code Point} objects holding the
     * position of each filled cell in the first column from
     * the left which has a filled cell.
     * </p>
     * 
     * <p>
     * The coordinates in the {@code Point} objects are the
     * location in the tetrad shape
     * </p>
     * 
     * @param tetrad
     * @param rotation
     *
     * @return 
     */
    public static Point[] firstLeftCells(int tetrad, int rotation) {
        boolean[][] shape = getShape(tetrad, rotation);

        Point[] tmpPoints = new Point[4];

        int numFound = 0;

        for (int y=3 ; y>=0 ; y--) {
            for (int x=0 ; x<=3 ; x++) {
                if (shape[x][y]) {
                    tmpPoints[numFound] = new Point(x, y);
                    numFound++;
                    break;
                }
            }
        }
        
        Point[] points = new Point[numFound];
        System.arraycopy(tmpPoints, 0, points, 0, numFound);
        
        return points;
    }
    
    /**
     * <p>
     * Get the rightmost cells of the block.
     * </p>
     * 
     * <p>
     * Returns an array of {@code Point} objects holding the
     * position of each filled cell in the first column from
     * the right which has a filled cell.
     * </p>
     * 
     * <p>
     * The coordinates in the {@code Point} objects are the
     * location in the tetrad shape
     * </p>
     * 
     * @param tetrad
     * @param rotation
     *
     * @return 
     */
    public static Point[] firstRightCells(int tetrad, int transform) {
        boolean[][] shape = getShape(tetrad, transform);

        Point[] tmpPoints = new Point[4];

        int numFound = 0;

        for (int y=3 ; y>=0 ; y--) {
            for (int x=3 ; x>=0 ; x--) {
                if (shape[x][y]) {
                    tmpPoints[numFound] = new Point(x, y);
                    numFound++;
                    break;
                }
            }
        }

        Point[] points = new Point[numFound];
        System.arraycopy(tmpPoints, 0, points, 0, numFound);
        
        return points;
    }
    
    /**
     * <p>
     * Get the filled cells in the bottom-most row which
     * has filled cells 
     * </p>
     * 
     * <p>
     * Returns an array of {@code Point} objects holding the
     * position of each filled cell in the first column from
     * the right which has a filled cell.
     * </p>
     * 
     * <p>
     * The coordinates in the {@code Point} objects are the
     * location in the tetrad shape
     * </p>
     * 
     * @param tetrad
     * @param rotation
     *
     * @return 
     */
    public static Point[] firstBottomCells(int tetrad, int transform) {
        boolean[][] shape = getShape(tetrad, transform);

        Point[] tmpPoints = new Point[4];

        int numFound = 0;

        for (int x=3 ; x>=0 ; x--) {
            for (int y=3 ; y>=0 ; y--) {
                if (shape[x][y]) {
                    tmpPoints[numFound] = new Point(x, y);
                    numFound++;
                    break;
                }
            }
        }

        Point[] points = new Point[numFound];
        System.arraycopy(tmpPoints, 0, points, 0, numFound);
        
        return points;
    }

}
