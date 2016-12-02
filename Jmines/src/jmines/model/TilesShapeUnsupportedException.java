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

import jmines.view.persistence.Configuration;

/**
 * The Exception used when the tiles shape given to a GameBoard is not
 * supported (or not implemented).
 *
 * @author Zleurtor
 */
public class TilesShapeUnsupportedException extends Exception {

    //==========================================================================
    // Static attributes
    //==========================================================================
    /**
     * The unique serial version identifier.
     */
    private static final long serialVersionUID = -1861806806357836404L;

    //==========================================================================
    // Attributes
    //==========================================================================
    /**
     * The tiles shape that caused this Exception.
     */
    private final byte tilesShape;

    //==========================================================================
    // Constructors
    //==========================================================================
    /**
     * Construct a new TilesShapeUnsupportedException using a given tiles shape.
     *
     * @param newTilesShape The tiles shape that caused this Exception.
     */
    public TilesShapeUnsupportedException(final byte newTilesShape) {
        super(newTilesShape + " is an unsupported tiles shape.", null);
        tilesShape = newTilesShape;
    }

    //==========================================================================
    // Getters
    //==========================================================================
    /**
     * Returns the tiles shape that caused this Exception.
     *
     * @return The tiles shape that caused this Exception.
     */
    public final byte getTilesShape() {
        return tilesShape;
    }

    //==========================================================================
    // Setters
    //==========================================================================

    //==========================================================================
    // Inherited methods
    //==========================================================================
    /**
     * Returns the detail message string of this Exception.
     *
     * @return The detail message string of this throwable.
     * @see java.lang.Throwable#getMessage()
     */
    @Override
    public final String getMessage() {
        return Configuration.getInstance().getConfigurableText(Configuration.KEY_ERROR_SHAPEUNSUPPORTED, new String[] {Byte.toString(getTilesShape())});
    }

    //==========================================================================
    // Static methods
    //==========================================================================

    //==========================================================================
    // Methods
    //==========================================================================
}
