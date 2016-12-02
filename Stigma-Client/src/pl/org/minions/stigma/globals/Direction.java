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
package pl.org.minions.stigma.globals;

/**
 * Describes move direction.
 */
public enum Direction
{
    N, NE, E, SE, S, SW, W, NW, NONE;

    // it's optimization, because Enum#values() calls clone()
    private static final Direction[] VALUES_ARRAY = values();

    /**
     * Returns direction for given ordinal number.
     * @param id
     *            ordinal of requested object
     * @return direction for given ordinal number
     */
    public static Direction getForOridinal(int id)
    {
        return VALUES_ARRAY[id];
    }

    /**
     * Returns same like {@link #values()} function, but
     * without making copy. It is more efficient, yet user
     * is responsible for not modification of returned
     * array.
     * @return array with all possible enum values
     */
    public static Direction[] getValuesArray()
    {
        return VALUES_ARRAY;
    }

    /**
     * Returns a direction opposite to the specified.
     * <p>
     * For {@link #NONE} returns {@link #NONE}.
     * @param direction
     *            base direction
     * @return opposite direction
     */
    public static Direction getOpposite(Direction direction)
    {
        switch (direction)
        {
            case E:
                return W;
            case N:
                return S;
            case NE:
                return SW;
            case NW:
                return SE;
            case S:
                return N;
            case SE:
                return NW;
            case SW:
                return NE;
            case W:
                return E;
            case NONE:
                return NONE;
            default:
                return null;
        }
    }

    /**
     * Returns a direction opposite this one.
     * @return the opposite direction
     * @see #getOpposite(Direction)
     */
    public Direction getOpposite()
    {
        return getOpposite(this);
    }
}
