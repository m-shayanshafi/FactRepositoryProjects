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
package pl.org.minions.stigma.game.command.data.sink;

/**
 * Class representing sink for requests for synchronization
 * data of given object type.
 * @param <T>
 *            type for which requests will be stored in this
 *            sink
 */
public interface DataRequestSink<T>
{
    /**
     * Adds object for synchronization.
     * @param o
     *            object which should be added to sink
     */
    void add(T o);

    /**
     * Compares newest TS of object with given and adds for
     * synchronization if current is older.
     * @param currentObject
     *            currently known object
     * @param newestTS
     *            newest known TS of this object
     */
    void compareAndAdd(T currentObject, int newestTS);

    /**
     * Compares objects and adds {@code currentObject} for
     * synchronization if any is older.
     * @param currentObject
     *            currently known object
     * @param newObject
     *            object which contains new TS
     */
    void compareAndAdd(T currentObject, T newObject);

    /**
     * Adds object for forced synchronization. Should be
     * used for example when synchronizing new object.
     * @param object
     *            object which should be added to sink
     */
    void forceAdd(T object);

    /**
     * Returns {@code true} when sink is empty.
     * @return {@code true} when sink is empty
     */
    boolean isEmpty();
}
