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
package pl.org.minions.stigma.server.item;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import pl.org.minions.stigma.databases.parsers.Parsable;
import pl.org.minions.stigma.databases.xml.XmlDbElem;
import pl.org.minions.stigma.game.item.Item;
import pl.org.minions.stigma.game.map.MapInstance;
import pl.org.minions.stigma.game.map.MapType;
import pl.org.minions.stigma.globals.Position;
import pl.org.minions.stigma.server.managers.WorldManager;
import pl.org.minions.utils.Version;
import pl.org.minions.utils.logger.Log;

/**
 * Represents static item layer over map. Items will be put
 * on given position. This layer type is rather not very
 * reusable.
 */
@XmlRootElement(name = "staticItems")
@XmlType(propOrder = {})
public class StaticItems implements ItemGenerator, XmlDbElem, Parsable
{
    private short id;
    private boolean modified;
    private Map<Position, ListWrapper> itemsMap =
            new HashMap<Position, ListWrapper>();
    private String version = Version.FULL_VERSION;

    // This wrapper is needed, cause JAXB have problems with Map<K,List<V>> 
    private static class ListWrapper
    {
        private List<ItemDescription> list = new LinkedList<ItemDescription>();

        public ListWrapper()
        {
        }

        public ListWrapper(List<ItemDescription> list)
        {
            // this look strange but it removes warnings for functions needed only by JAXB
            this();
            setList(list);
        }

        public List<ItemDescription> getList()
        {
            return list;
        }

        @XmlElement(name = "item")
        public void setList(List<ItemDescription> list)
        {
            this.list = list;
        }
    }

    /**
     * Constructor.
     */
    public StaticItems()
    {
    }

    /** {@inheritDoc} */
    @Override
    public boolean putItems(WorldManager worldManager, MapInstance map)
    {
        MapType t = map.getType();
        if (t == null)
        {
            Log.logger.error("Not fully initialized map passed to StaticItemLayer");
            return false;
        }

        for (Map.Entry<Position, ListWrapper> entry : itemsMap.entrySet())
        {
            Position p = entry.getKey();
            for (ItemDescription desc : entry.getValue().getList())
            {
                if (t.getTile(p).isPassable())
                {
                    Item i =
                            ItemBuilder.buildItem(worldManager,
                                                  desc,
                                                  t.getId(),
                                                  map.getInstanceNo(),
                                                  p);
                    if (i != null)
                        worldManager.addItem(i);
                    else
                    {
                        Log.logger.warn("Problem with adding item with descirpiton: "
                            + desc.toString());
                        return false;
                    }
                }
                else
                    Log.logger.warn("Bad position for item: " + p + " on map: "
                        + t.getId());
            }
        }
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public short getId()
    {
        return id;
    }

    /** {@inheritDoc} */
    @Override
    public void clearModified()
    {
        for (Map.Entry<Position, ListWrapper> entry : itemsMap.entrySet())
            for (ItemDescription desc : entry.getValue().getList())
                desc.clearModified();
        modified = false;

    }

    /** {@inheritDoc} */
    @Override
    public boolean isModified()
    {
        if (modified)
            return true;

        for (Map.Entry<Position, ListWrapper> entry : itemsMap.entrySet())
            for (ItemDescription desc : entry.getValue().getList())
                if (desc.isModified())
                    return true;

        return false;
    }

    /** {@inheritDoc} */
    @Override
    public void setModified()
    {
        modified = true;
    }

    /** {@inheritDoc} */
    @Override
    public String getVersion()
    {
        return version;
    }

    /**
     * Sets version - needed for verifying parsing.
     * @param version
     *            new version
     */
    @XmlAttribute(name = "version", required = true)
    public void setVersion(String version)
    {
        this.version = version;
    }

    /**
     * Sets id of this layer.
     * @param id
     *            new id
     */
    @XmlAttribute(name = "id", required = true)
    public final void setId(short id)
    {
        this.id = id;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isGood()
    {
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("[StaticItems: id=");
        builder.append(id);
        builder.append('\n');
        for (Map.Entry<Position, ListWrapper> entry : itemsMap.entrySet())
        {
            builder.append(entry.getKey());
            builder.append(" -> (");
            for (ItemDescription desc : entry.getValue().getList())
            {
                builder.append(desc);
                builder.append(",");
            }
            builder.append(")\n");
        }
        builder.append("]");
        return builder.toString();
    }

    /**
     * Adds item on given position.
     * @param p
     *            position to add item to.
     * @param desc
     *            description of added item
     */
    public void addItem(Position p, ItemDescription desc)
    {
        List<ItemDescription> list = getItems(p);
        if (list == null)
        {
            list = new LinkedList<ItemDescription>();
            itemsMap.put(p, new ListWrapper(list));
        }
        list.add(desc);
    }

    /**
     * Returns all items on given position. May return
     * {@code null}.
     * @param p
     *            position of requested items
     * @return all items on given position
     */
    public List<ItemDescription> getItems(Position p)
    {
        ListWrapper w = itemsMap.get(p);
        if (w != null)
            return w.getList();
        return null;
    }

    /**
     * Removes given item from given position.
     * @param p
     *            position to remove from
     * @param desc
     *            item to remove
     */
    public void removeItem(Position p, ItemDescription desc)
    {
        List<ItemDescription> list = getItems(p);
        if (list != null)
            list.remove(desc);
    }

    /**
     * Removes all items from given position.
     * @param p
     *            position to remove items from
     */
    public void removeAllItems(Position p)
    {
        itemsMap.remove(p);
    }

    /**
     * Returns itemsMap. Needed by JAXB.
     * @return itemsMap
     */
    public Map<Position, ListWrapper> getItemsMap()
    {
        return itemsMap;
    }

    /**
     * Sets new value of itemsMap. Needed by JAXB.
     * @param itemsMap
     *            the itemsMap to set
     */
    public void setItemsMap(Map<Position, ListWrapper> itemsMap)
    {
        this.itemsMap = itemsMap;
    }

}
