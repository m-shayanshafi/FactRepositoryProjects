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
package pl.org.minions.stigma.pathfinding;

import pl.org.minions.stigma.game.path.Passable;
import pl.org.minions.stigma.game.path.Path;
import pl.org.minions.stigma.globals.Distance;
import pl.org.minions.stigma.globals.Position;

/**
 * Interface for path-finding algorithm. PathFinder should
 * be assigned to particular passable entity.
 */
public interface PathFinder
{
    /**
     * Returns found path between given position.
     * @param start
     *            start position
     * @param target
     *            target position
     * @return path between given positions
     */
    Path generatePath(Position start, Position target);

    /**
     * Returns found path between given position.
     * @param start
     *            start position
     * @param target
     *            target position
     * @param range
     *            how many fields from target algorithm
     *            shall end generation path
     * @return path between given start position and 'range'
     *         from target
     */
    Path generatePath(Position start, Position target, Distance range);

    /**
     * Returns passable associated with this path finder.
     * @return passable
     */
    Passable getPassable();
}
