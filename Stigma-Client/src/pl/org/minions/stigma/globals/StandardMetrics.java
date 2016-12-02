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

import java.awt.geom.Point2D;

import pl.org.minions.stigma.globals.Distance.Metric;

/**
 * Contains most commonly used implementations of
 * {@link Metric} interface.
 */
public enum StandardMetrics implements Metric
{
    STEPS
    {
        /** {@inheritDoc} */
        @Override
        public int getDistance(Position from, Position to)
        {
            return Math.max(Math.abs(from.getX() - to.getX()),
                            Math.abs(from.getY() - to.getY()));
        }
    },
    MANHATTAN
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public int getDistance(Position from, Position to)
        {
            final int dx = from.getX() - to.getX();
            final int dy = from.getY() - to.getY();

            return Math.abs(dx) + Math.abs(dy);
        };
    },
    EUCLIDEAN
    {
        /** {@inheritDoc} */
        @Override
        public int getDistance(Position from, Position to)
        {
            return (int) Math.round(Point2D.distance(from.getX(),
                                                     from.getY(),
                                                     to.getX(),
                                                     to.getY()));
        }
    };

    /**
     * Creates a new {@link Distance} instance with
     * specified value.
     * @param value
     *            distance value
     * @return distance
     */
    public Distance createDistance(int value)
    {
        return new Distance(value, this);
    }
}
