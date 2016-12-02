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
package pl.org.minions.stigma.game.event.item;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.Set;

import pl.org.minions.stigma.game.command.data.sink.DataRequestSinks;
import pl.org.minions.stigma.game.event.Event;
import pl.org.minions.stigma.game.event.EventType;
import pl.org.minions.stigma.game.item.Item;
import pl.org.minions.stigma.game.map.MapInstance;
import pl.org.minions.stigma.game.world.World;
import pl.org.minions.stigma.globals.Position;
import pl.org.minions.stigma.globals.SizeOf;
import pl.org.minions.stigma.network.Buffer;
import pl.org.minions.utils.logger.Log;

/**
 * Class representing event of new item being added to map
 * (not dropped etc, just 'added').
 */
public class ItemAdded extends Event
{
    private static final int ITEM_CONST_SIZE = ItemDescription.sizeOf() // item description
        + SizeOf.SHORT // map id 
        + SizeOf.SHORT // map instance no
        + Position.sizeOf() // item position
    ;

    private ItemDescription itemDesc;
    private short mapId;
    private short mapInstanceNo;
    private Position position;

    private Item item;

    private ItemAdded()
    {
        super(EventType.ITEM_ADDED);
    }

    /**
     * Constructor.
     * @param item
     *            item to add to world
     */
    public ItemAdded(Item item)
    {
        this();
        this.itemDesc = new ItemDescription(item);
        this.mapId = item.getMapId();
        this.mapInstanceNo = item.getMapInstanceNo();
        this.position = item.getPosition();
        this.item = item;
    }

    /**
     * Creates empty object (needed for decoding).
     * @return empty object
     */
    public static ItemAdded create()
    {
        return new ItemAdded();
    }

    /**
     * Returns item added by this event. This method should
     * be called only after applying event.
     * @return item added by this event
     */
    public Item getItem()
    {
        return item;
    }

    /** {@inheritDoc} */
    @Override
    public boolean apply(World world, DataRequestSinks dataRequestSink)
    {
        if (Log.isTraceEnabled())
            Log.logger.trace(MessageFormat.format("Adding item: {0} to map id: {1} instance: {2}",
                                                  itemDesc.getId(),
                                                  mapId,
                                                  mapInstanceNo));
        MapInstance m = world.getMap(mapId, mapInstanceNo);
        if (m == null)
        {
            m = world.createMap(mapId, mapInstanceNo);
            if (m == null)
                return false;
        }

        MapInstance.Segment s = m.getSegmentForPosition(position);
        if (s == null)
            return false;

        if (item == null) // client - constructor no. 1
            item = itemDesc.getItem(world, dataRequestSink);
        else
            // server - constructor no. 2
            world.addItem(item);

        if (s.getItems(position) != null && s.getItems(position).contains(item))
            return false;

        item.setMapId(mapId);
        item.setMapInstanceNo(mapInstanceNo);
        item.setPosition(position);

        s.putItem(item);

        return true;
    }

    /** {@inheritDoc} */
    @Override
    protected boolean decodeParams(Buffer buffer)
    {
        this.itemDesc = new ItemDescription(buffer);
        this.mapId = buffer.decodeShort();
        this.mapInstanceNo = buffer.decodeShort();
        this.position = buffer.decodePosition();
        return true;
    }

    /** {@inheritDoc} */
    @Override
    protected boolean encodeParams(Buffer buffer)
    {
        itemDesc.encode(buffer);
        buffer.encode(mapId);
        buffer.encode(mapInstanceNo);
        buffer.encode(position);
        return true;
    }

    /** {@inheritDoc} */
    @Override
    protected int innerGetParamsLength()
    {
        return ITEM_CONST_SIZE;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return MessageFormat.format("ItemAdded: id:{0}, mapId:{1}, mapInstanceNo:{2}, position:{3}",
                                    itemDesc.getId(),
                                    mapId,
                                    mapInstanceNo,
                                    position);
    }

    /** {@inheritDoc} */
    @Override
    public Set<Integer> getAffectedActors()
    {
        return Collections.emptySet();
    }

}
