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

import java.util.EventListener;

/**
 * Interface to implement to intercept GameBoardEvents.
 *
 * @author Zleurtor
 */
public interface GameBoardListener extends EventListener {

    //==============================================================================
    // Inherited methods
    //==============================================================================

    //==============================================================================
    // Static methods
    //==============================================================================

    //==============================================================================
    // Methods
    //==============================================================================
    /**
     * Called method when the GameBoard is initialized (or re-initialized).
     *
     * @param evt The event object relating the event that occurred.
     */
    void initialized(final GameBoardEvent evt);

    /**
     * Called method when the game has been lost.
     *
     * @param evt The event object relating the event that occurred.
     */
    void defeat(final GameBoardEvent evt);

    /**
     * Called method when the game has been won.
     *
     * @param evt The event object relating the event that occurred.
     */
    void victory(final GameBoardEvent evt);
}
