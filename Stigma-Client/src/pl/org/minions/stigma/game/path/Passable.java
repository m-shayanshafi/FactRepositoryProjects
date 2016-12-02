/**
 *   Stigma - Multiplayer online RPG - http://stigma.sourceforge.net
 *   Copyright (C) 2005-2009 Minions Studio
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *   
 */
package pl.org.minions.stigma.game.path;

import java.util.Collection;

import pl.org.minions.stigma.globals.Position;

/**
 * Interface used to determine whether specific positions or
 * positions list is passable (passable means that if no
 * entities where on this map player would be able to step
 * on this position), or whether it is just blocked (blocked
 * means that someone/something is blocking position).
 */
public abstract class Passable
{
    /**
     * Checks if every position in given collection is
     * passable.
     * @param positions
     *            positions to check
     * @return {@code true} only when all positions in
     *         collection are passable
     */
    public final boolean areAllPassable(Collection<Position> positions)
    {
        for (Position p : positions)
            if (!isPassable(p))
                return false;
        return true;
    }

    /**
     * Checks if every given position is passable.
     * @param positions
     *            positions to check
     * @return {@code true} only when all given positions
     *         are passable
     */
    public final boolean areAllPassable(Position... positions)
    {
        for (Position p : positions)
            if (!isPassable(p))
                return false;
        return true;
    }

    /**
     * Checks if tile at given position is passable.
     * @param p
     *            position for which passable should be
     *            checked
     * @return {@code true} if actor can be moved to given
     *         position
     */
    public abstract boolean isPassable(Position p);
}
