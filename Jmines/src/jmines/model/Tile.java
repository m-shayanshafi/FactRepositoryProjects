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

/**
 * The class used to do all the business calculations on the tiles.
 *
 * @author Zleurtor
 */
public class Tile {

    //==========================================================================
    // Static attributes
    //==========================================================================

    //==========================================================================
    // Attributes
    //==========================================================================
    /**
     * Tell whether or not this tile is containing a mine.
     */
    private boolean containingMine;
    /**
     * Tell whether or not this tile has been open.
     */
    private boolean open = false;
    /**
     * Tell whether or not this tile has been flagged.
     */
    private boolean flagged = false;
    /**
     * Tell whether or not this tile has been marked.
     */
    private boolean marked = false;

    //==========================================================================
    // Constructors
    //==========================================================================
    /**
     * Construct a new Tile saying if it has to contain a mine.
     *
     * @param newContainingMine Tell whether or not the constructed tile will
     *                          contain a mine.
     */
    public Tile(final boolean newContainingMine) {
        containingMine = newContainingMine;
    }

    //==========================================================================
    // Getters
    //==========================================================================
    /**
     * Returns whether or not this tile is containing a mine.
     *
     * @return True if this tile is containing mine, false otherwise.
     */
    public final boolean isContainingMine() {
        return containingMine;
    }

    /**
     * Returns whether or not this tile has been open.
     *
     * @return True if this mine has been open, false otherwise.
     */
    public final boolean isOpen() {
        return open;
    }

    /**
     * Returns whether or not this tile has been flagged.
     *
     * @return True if this mine has been flagged, false otherwise.
     */
    public final boolean isFlagged() {
        return flagged;
    }

    /**
     * Returns whether or not this tile has been marked.
     *
     * @return True if this mine has been marked, false otherwise.
     */
    public final boolean isMarked() {
        return marked;
    }

    //==========================================================================
    // Setters
    //==========================================================================
    /**
     * Allows to set a new value to tell if a mine is contained if this tile.
     *
     * @param newContainingMine The new value telling if a mine is contained in
     *                          this tile.
     */
    public final void setContainingMine(final boolean newContainingMine) {
        this.containingMine = newContainingMine;
    }

    /**
     * Allows to set a new value to tell if this tile has been opened.
     *
     * @param newOpen The new value telling if this tile has been opened.
     */
    public final void setOpen(final boolean newOpen) {
        this.open = newOpen;
    }

    /**
     * Allows to set a new value to tell if this tile has been flagged.
     *
     * @param newFlagged The new value telling if this tile has been flagged.
     */
    public final void setFlagged(final boolean newFlagged) {
        this.flagged = newFlagged;
    }

    /**
     * Allows to set a new value to tell if this tile has been marked.
     *
     * @param newMarked The new value telling if this tile has been marked.
     */
    public final void setMarked(final boolean newMarked) {
        this.marked = newMarked;
    }

    //==========================================================================
    // Inherited methods
    //==========================================================================
    /**
     * Returns a string representation of the object.
     *
     * @return A string representation of the object.
     * @see java.lang.Object#toString().
     */
    @Override
    public final String toString() {
        if (containingMine) {
            return "O";
        } else {
            return " ";
        }
    }

    //==========================================================================
    // Static methods
    //==========================================================================

    //==========================================================================
    // Methods
    //==========================================================================
}
