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
package pl.org.minions.stigma.game;

/**
 * Interface for objects used in 'time-stamp protocol'.
 * @param <T>
 *            class implementing this interface - for proper
 *            method signatures
 */
public interface TimestampedObject<T extends TimestampedObject<T>>
{
    /**
     * Compares two objects and tells whether or not
     * synchronization is needed.
     * @param other
     *            possibly newer object (partially complete)
     * @return {@code true} when synchronization is needed
     *         (other object is newer and we need to
     *         synchronize other data of it).
     */
    boolean compareTS(T other);

    /**
     * Returns object's id.
     * @return object's id
     */
    int getId();

    /**
     * Returns newest known time-stamp of object.
     * @return newest known time-stamp of object.
     */
    int getNewestTS();
}
