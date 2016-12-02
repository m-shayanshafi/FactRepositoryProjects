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
import java.util.Map;

import pl.org.minions.stigma.databases.map.MapDB;
import pl.org.minions.stigma.game.actor.Actor;
import pl.org.minions.stigma.game.item.Item;
import pl.org.minions.stigma.game.map.MapInstance;
import pl.org.minions.stigma.game.map.MapType;
import pl.org.minions.stigma.game.map.TerrainSet;
import pl.org.minions.utils.collections.CollectionFactory;
import pl.org.minions.utils.logger.Log;

/**
 * Class representing game's world. World is made from map
 * instances and actors.
 */
public class World
{
    private Map<Integer, Actor> actors =
            CollectionFactory.getFactory().createHashMap();
    private Map<Integer, Item> items =
            CollectionFactory.getFactory().createHashMap();
    private Map<Short, List<MapInstance>> maps =
            CollectionFactory.getFactory().createHashMap();

    private MapDB mapDB;

    /**
     * Creates world using given databases.
     * @param mapDB
     *            database with map types used by this world
     */
    public World(MapDB mapDB)
    {
        this.mapDB = mapDB;
    }

    /**
     * Adds actor to game world. Does not put it on map or
     * anything. It just adds it as "actor in game".
     * @param a
     *            new actor
     */
    public final void addActor(Actor a)
    {
        actors.put(a.getId(), a);
    }

    /**
     * Adds item to game world. Does not put it on map or
     * anything. It just adds it as "item in game".
     * @param i
     *            new actor
     */
    public final void addItem(Item i)
    {
        items.put(i.getId(), i);
    }

    /**
     * Creates new instance of map of given map type, using
     * given terrain set.
     * @param instanceNo
     *            new instance number
     * @param typeId
     *            map type id for new instance
     * @return newly created map
     */
    public MapInstance createMap(short typeId, short instanceNo)
    {
        MapType type = getMapDB().getMapType(typeId);
        TerrainSet terrainSet = null;
        if (type == null)
            Log.logger.info("Creating map with <null> map type: " + typeId);
        else
            terrainSet = getMapDB().getTerrainSet(type.getTerrainSetId());
        MapInstance m = new MapInstance(instanceNo, type, terrainSet);
        return innerCreateMap(m, typeId);
    }

    /**
     * Removes given instance of map from world. Does not
     * "destroy" it, just disconnects.
     * @param typeId
     *            type of map to remove
     * @param instanceNo
     *            instance number of map to remove
     */
    public void dropMap(short typeId, short instanceNo)
    {
        List<MapInstance> list = maps.get(typeId);
        if (list == null)
            return;
        if (list.size() >= instanceNo)
            return;
        list.set(instanceNo, null);
    }

    /**
     * Returns actor for given id or {@code null} if no such
     * actor is present in game.
     * @param id
     *            actor id
     * @return actor for given id
     */
    public final Actor getActor(int id)
    {
        return actors.get(id);
    }

    /**
     * Returns item for given id or {@code null} if no such
     * item is present in game.
     * @param id
     *            item id
     * @return item for given id
     */
    public final Item getItem(int id)
    {
        return items.get(id);
    }

    /**
     * Returns map of given map type id and instance number,
     * or {@code null} if no such instance is available.
     * @param id
     *            map type id
     * @param instanceNo
     *            instance number
     * @return map for given parameters or {@code null}
     */
    public MapInstance getMap(short id, short instanceNo)
    {
        List<MapInstance> list = maps.get(id);
        if (list != null && instanceNo < list.size())
            return list.get(instanceNo);
        return null;
    }

    /**
     * Returns map type database used by this world.
     * @return map type database used by this world.
     */
    public final MapDB getMapDB()
    {
        return mapDB;
    }

    /**
     * Returns collection of maps.
     * @return collection of maps
     */
    protected java.util.Map<Short, List<MapInstance>> getMaps()
    {
        return maps;
    }

    /**
     * Returns current turn number.
     * @return current turn number
     */
    public int getTurnNumber()
    {
        return 0;
    }

    /**
     * Inserts map properly in inner structures.
     * @param m
     *            map to add
     * @param typeId
     *            id of map type (in case map type was
     *            {@code null}
     * @return added map
     */
    protected final MapInstance innerCreateMap(MapInstance m, short typeId)
    {
        List<MapInstance> list = getMaps().get(typeId);
        if (list == null)
            list = new LinkedList<MapInstance>();
        for (int i = list.size(); i <= m.getInstanceNo(); ++i)
            list.add(null);
        list.set(m.getInstanceNo(), m);
        getMaps().put(typeId, list);
        return m;
    }

    /**
     * Removes actor from game. Does not remove it from map
     * or anything, it will just no longer be available via
     * {@link #getActor(int)} method.
     * @param id
     *            id of actor to remove
     */
    public final void removeActor(int id)
    {
        actors.remove(id);
    }

    /**
     * Removes item from game. Does not remove it from map
     * or anything, it will just no longer be available via
     * {@link #getItem(int)} method.
     * @param id
     *            id of item to remove
     */
    public final void removeItem(int id)
    {
        items.remove(id);
    }

}
