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
package pl.org.minions.stigma.game.world;

import java.util.LinkedList;
import java.util.List;

import pl.org.minions.stigma.databases.map.MapDB;
import pl.org.minions.stigma.game.map.MapInstance;

/**
 * Extends world, adds method needed for execution of
 * commands.
 */
public abstract class ExtendedWorld extends World
{
    /**
     * Observer of map creation.
     */
    public static interface MapCreationObserver
    {
        /**
         * Will be called when new map is created.
         * @param newMap
         *            newly created map.
         */
        void notify(MapInstance newMap);
    }

    private List<MapCreationObserver> mapCreationObservers =
            new LinkedList<MapCreationObserver>();

    /**
     * Creates world using given databases.
     * @param mapDB
     *            database with map types used by this world
     */
    public ExtendedWorld(MapDB mapDB)
    {
        super(mapDB);
    }

    /**
     * Adds new observer.
     * @param observer
     *            new observer
     */
    public void addMapCreationObserver(MapCreationObserver observer)
    {
        mapCreationObservers.add(observer);
    }

    /**
     * Finds first free instance number. Free means it can
     * be used by {@link #createMap(short, short)} to create
     * new instance. Runs in linear time.
     * @param mapType
     *            map type for which instance number should
     *            be found
     * @return first free instance number
     */
    public abstract short findFreeInstanceNo(short mapType);

    /**
     * Finds first ready instance. Ready means it can accept
     * another actor. Runs in linear time.
     * @param mapType
     *            map type for which instance should be find
     * @return first ready instance or {@code null} if no
     *         such instance is available
     */
    public abstract MapInstance findReadyInstance(short mapType);

    /**
     * Increments turn number.
     * @throws ArithmeticException
     *             when turn number would exceed
     *             {@value java.lang.Integer#MAX_VALUE}
     *             (when turn ~200ms this will happen after
     *             ~16 years of work)
     */
    public abstract void incrementTurnNumber() throws ArithmeticException;

    /**
     * Marks begin of 'current turn'. Calling this function
     * will change return value of {@link #getTurnNumber()}
     * to actual value generated with use of
     * {@link #incrementTurnNumber()}.
     */
    public abstract void markTurn();

    /**
     * Notify all registered {@link MapCreationObserver}
     * about newly created map.
     * @param newMap
     *            newly created map
     */
    protected void notifyAll(MapInstance newMap)
    {
        for (MapCreationObserver o : mapCreationObservers)
            o.notify(newMap);
    }

    /**
     * Removes observer.
     * @param observer
     *            observer to remove
     */
    public void removeMapCreationObserver(MapCreationObserver observer)
    {
        mapCreationObservers.remove(observer);
    }

}
