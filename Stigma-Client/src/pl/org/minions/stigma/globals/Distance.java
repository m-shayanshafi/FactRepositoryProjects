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
 * Instances of this class represent distance between two
 * {@link Position positions} on a map according to a given
 * metric.
 */
public class Distance implements Comparable<Distance>
{
    /**
     * A distance metric.
     * @see StandardMetrics
     */
    public static interface Metric
    {
        /**
         * Returns distance between two positions according
         * to this metric.
         * @param from
         *            first position
         * @param to
         *            second position
         * @return distance value
         */
        int getDistance(Position from, Position to);
    }

    private Metric metric;
    private int value;

    /**
     * Constructor.
     * @param from
     *            first position
     * @param to
     *            second position
     * @param metric
     *            distance metric
     */
    public Distance(Position from, Position to, Metric metric)
    {
        this.metric = metric;
        this.value = metric.getDistance(from, to);
    }

    /**
     * Constructor.
     * @param value
     *            distance value
     * @param metric
     *            distance metric
     */
    public Distance(int value, Metric metric)
    {
        this.metric = metric;
        this.value = value;
    }

    /**
     * Returns metric.
     * @return metric
     */
    public Metric getMetric()
    {
        return metric;
    }

    /**
     * Returns value.
     * @return value
     */
    public int getValue()
    {
        return value;
    }

    /** {@inheritDoc} */
    @Override
    public int compareTo(Distance o)
    {
        if (o.metric != this.metric)
            throw new IllegalArgumentException("Attempted comparison of distances using different metrics.");
        return value - o.value;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "distance(" + value + " " + metric + ")";
    }
}
