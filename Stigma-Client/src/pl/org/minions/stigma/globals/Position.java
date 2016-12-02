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

import java.awt.Point;
import java.util.HashMap;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import pl.org.minions.utils.logger.Log;

/**
 * Class representing map position of object in game.
 * Position on map has two rectangular coordinates - x
 * (horizontal) and y (vertical).
 */
@XmlRootElement(name = "pos")
@XmlType(propOrder = {})
public class Position
{
    private static final int SIZE = SizeOf.SHORT + SizeOf.SHORT;
    private short x;

    private short y;

    /**
     * Default constructor needed by JAXB.
     */
    public Position()
    {
        x = -1;
        y = -1;
    }

    /**
     * Copy constructor. Creates new position based on other
     * object. Remember to use it instead of coping
     * references when result should be two different
     * positions.
     * @param other
     *            source position
     */
    public Position(Position other)
    {
        x = other.x;
        y = other.y;
    }

    /**
     * Creates new Position.
     * @param x
     *            x (horizontal) coordinate
     * @param y
     *            y (vertical) coordinate
     */
    public Position(short x, short y)
    {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns size in bytes needed to encode this class.
     * @return size in bytes needed to encode this class.
     */
    public static int sizeOf()
    {
        return SIZE;
    }

    /**
     * Returns copy of position.
     * @see #Position(Position)
     * @return copy of this position
     */
    public Position deepCopy()
    {
        return new Position(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o)
    {
        if (o == null)
            return false;
        if (o == this)
            return true;
        if (o instanceof Position)
        {
            Position p = (Position) o;
            if (p.getX() != getX())
                return false;
            return p.getY() == getY();
        }
        else
            return false;
    }

    /**
     * Finds direction between this position and position b.
     * @param b
     *            second position
     * @return direction from this position to position b or
     *         {@link Direction#NONE} if positions are not
     *         neighbors
     */
    public Direction getDirection(Position b)
    {
        int dx = b.x - x;
        int dy = b.y - y;
        switch (dx)
        {
            case 1:
            {
                switch (dy)
                {
                    case 1:
                        return Direction.SE;
                    case 0:
                        return Direction.E;
                    case -1:
                        return Direction.NE;
                    default:
                        return Direction.NONE;
                }
            }
            case 0:
                switch (dy)
                {
                    case 1:
                        return Direction.S;
                    case -1:
                        return Direction.N;
                    case 0:
                    default:
                        return Direction.NONE;
                }
            case -1:
                switch (dy)
                {
                    case 1:
                        return Direction.SW;
                    case 0:
                        return Direction.W;
                    case -1:
                        return Direction.NW;
                    default:
                        return Direction.NONE;
                }
            default:
                return Direction.NONE;
        }
    }

    /**
     * Returns horizontal coordinate.
     * @return x (horizontal) coordinate
     */
    public short getX()
    {
        return this.x;
    }

    /**
     * Returns vertical coordinate.
     * @return y (vertical) coordinate
     */
    public short getY()
    {
        return this.y;
    }

    /**
     * Used for {@link HashMap HashMap} etc. It creates hash
     * code unique for position represented by object not
     * for object itself. Two different objects with same
     * position should have the same hash code.
     * @return hash code unique for position represented by
     *         object not for object itself
     */
    @Override
    public int hashCode()
    {
        return (x << Short.SIZE) + y;
    }

    /**
     * Checks whether b position is neighbor of this
     * position.
     * @param b
     *            reference position
     * @return true if is neighbor false otherwise
     */
    public boolean isNeighbor(Position b)
    {
        return this.getDirection(b) != Direction.NONE;
    }

    /**
     * Returns new position, translated from original by one
     * point in direction given as argument. Does not modify
     * original position.
     * @param dir
     *            direction in which position should be
     *            translated
     * @return new position, translated in given direction
     */
    public Position newPosition(Direction dir)
    {
        short newX = getX();
        short newY = getY();

        switch (dir)
        {
            case N:
                --newY;
                break;
            case NE:
                ++newX;
                --newY;
                break;
            case E:
                ++newX;
                break;
            case SE:
                ++newX;
                ++newY;
                break;
            case S:
                ++newY;
                break;
            case SW:
                --newX;
                ++newY;
                break;
            case W:
                --newX;
                break;
            case NW:
                --newX;
                --newY;
                break;
            case NONE:
                break;
            default:
                Log.logger.error("Unknown direction");
                break;
        }
        return new Position(newX, newY);
    }

    /**
     * Returns new position, translated from original by
     * given distance. Does not modify original position.
     * @param dx
     *            distance in positive x direction
     * @param dy
     *            distance in positive y direction
     * @return new position, translated by given distance
     */
    public Position newPosition(short dx, short dy)
    {
        return new Position((short) (x + dx), (short) (y + dy));
    }

    /**
     * Sets horizontal coordinate.
     * @param x
     *            (horizontal) coordinate
     */
    @XmlAttribute(name = "x", required = true)
    public void setX(short x)
    {
        this.x = x;
    }

    /**
     * Sets vertical coordinate.
     * @param y
     *            (vertical) coordinate
     */
    @XmlAttribute(name = "y", required = true)
    public void setY(short y)
    {
        this.y = y;
    }

    /**
     * Returns a {@link Point} with exactly the same
     * coordinates as this position.
     * <p>
     * This is a helper method equivalent to {@code new
     * Point(position.getX(), position.getY())}.
     * @return point that corresponds to this position
     */
    public Point toPoint()
    {
        return new Point(x, y);
    }

    /**
     * Returns string representation of position.
     * @return string representation of position
     */
    @Override
    public String toString()
    {
        return "(" + x + "," + y + ")";
    }
}
