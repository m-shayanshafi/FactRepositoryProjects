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

import pl.org.minions.stigma.game.map.MapType;
import pl.org.minions.stigma.globals.Position;

/**
 * Checks passability of {@link MapType}. Does not check
 * whether or not someone is currently standing on tile.
 */
public class MapTypePassable extends Passable
{
    private MapType type;

    /**
     * Constructor.
     * @param type
     *            map type used for reference by
     *            {@link #isPassable(Position)}.
     */
    public MapTypePassable(MapType type)
    {
        this.type = type;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isPassable(Position p)
    {
        return type.getTerrainType(p).isPassable();
    }
}
