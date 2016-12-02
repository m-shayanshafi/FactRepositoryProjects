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
package jmines.model.events;

import java.util.EventObject;

import jmines.model.GameBoard;
import jmines.model.Tile;

/**
 * The Event object relating the events that can occurs on a GameBoard object.
 *
 * @author Zleurtor
 */
public class GameBoardEvent extends EventObject {

    //==========================================================================
    // Static attributes
    //==========================================================================
    /**
     * The unique serial version identifier.
     */
    private static final long serialVersionUID = 9150036399898790058L;

    //==========================================================================
    // Attributes
    //==========================================================================
    /**
     * The source (the GameBoard) on which the event has occurred.
     */
    private final GameBoard source;
    /**
     * The last clicked tile.
     */
    private final Tile tile;

    //==========================================================================
    // Constructors
    //==========================================================================
    /**
     * Construct a new GameBoardEvent using a given GameBoard.
     *
     * @param newSource The source of the new created GameBoardEvent.
     * @param newTile The last clicked tile.
     */
    public GameBoardEvent(final GameBoard newSource, final Tile newTile) {
        super(newSource);
        this.source = newSource;
        this.tile = newTile;
    }

    //==========================================================================
    // Getters
    //==========================================================================
    /**
     * Returns the source of this GameBoardEvent.
     *
     * @return The source of this GameBoardEvent.
     * @see java.util.EventObject#getSource()
     */
    @Override
    public final GameBoard getSource() {
        return source;
    }

    /**
     * Returns the last clicked tile.
     *
     * @return The last clicked tile.
     */
    public final Tile getTile() {
        return tile;
    }

    //==========================================================================
    // Setters
    //==========================================================================

    //==========================================================================
    // Inherited methods
    //==========================================================================

    //==========================================================================
    // Static methods
    //==========================================================================

    //==========================================================================
    // Methods
    //==========================================================================
}
