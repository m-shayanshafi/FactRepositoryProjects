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
package pl.org.minions.stigma.game.world.server;

import java.util.List;

import pl.org.minions.stigma.databases.map.MapDB;
import pl.org.minions.stigma.game.map.MapInstance;
import pl.org.minions.stigma.game.map.MapType;
import pl.org.minions.stigma.game.map.TerrainSet;
import pl.org.minions.stigma.game.world.ExtendedWorld;
import pl.org.minions.stigma.server.ServerConfig;
import pl.org.minions.utils.logger.Log;

/**
 * Class representing game's world. It has full support for
 * instances and commands execution.
 */
public class ExtendedWorldImpl extends ExtendedWorld
{
    private final int maxInstances =
            ServerConfig.globalInstance().getMaxMapInstances();
    private int instancesCount;
    private volatile int turnNumber = 1;
    private int currentTurnNumber;

    /**
     * Creates world using given databases.
     * @param mapDB
     *            database with map types used by this world
     */
    public ExtendedWorldImpl(MapDB mapDB)
    {
        super(mapDB);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MapInstance createMap(short typeId, short instanceNo)
    {
        MapType type = getMapDB().getMapType(typeId);
        if (type == null)
        {
            Log.logger.warn("Null map type for given id: " + typeId);
            return null;
        }
        TerrainSet terrainSet =
                getMapDB().getTerrainSet(type.getTerrainSetId());
        if (terrainSet == null)
        {
            Log.logger.warn("Null terrainSet for given id: "
                + type.getTerrainSetId());
            return null;
        }
        if (instancesCount == maxInstances)
        {
            Log.logger.warn("Reached maximum instance amount");
            return null;
        }

        ++instancesCount;
        Log.logger.debug("New map instance: map type=" + typeId
            + " instanceNo=" + instanceNo);
        MapInstance m = new MapInstance(instanceNo, type, terrainSet);
        m = innerCreateMap(m, typeId);
        notifyAll(m);
        return m;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public short findFreeInstanceNo(short mapType)
    {
        List<MapInstance> list = getMaps().get(mapType);
        if (list == null)
            return 0;
        short i = 0;
        for (MapInstance m : list)
            if (m == null)
                return i;
            else
                ++i;
        return (short) list.size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MapInstance findReadyInstance(short mapType)
    {
        List<MapInstance> list = getMaps().get(mapType);
        if (list == null)
            return null;
        for (MapInstance m : list)
        {
            if (m.isReady())
                return m;
        }
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public int getTurnNumber()
    {
        return currentTurnNumber;
    }

    /** {@inheritDoc} */
    @Override
    public synchronized void incrementTurnNumber() throws ArithmeticException
    {
        if (++turnNumber < 0)
            throw new ArithmeticException("Turn counter moved after 'last turn'");
    }

    /** {@inheritDoc} */
    @Override
    public synchronized void markTurn()
    {
        currentTurnNumber = turnNumber;
    }
}
