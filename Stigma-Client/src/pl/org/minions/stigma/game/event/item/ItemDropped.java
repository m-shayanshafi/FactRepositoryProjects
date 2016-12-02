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

import java.util.Collections;
import java.util.Set;

import pl.org.minions.stigma.game.actor.Actor;
import pl.org.minions.stigma.game.command.data.sink.DataRequestSinks;
import pl.org.minions.stigma.game.event.Event;
import pl.org.minions.stigma.game.event.EventType;
import pl.org.minions.stigma.game.item.Item;
import pl.org.minions.stigma.game.map.MapInstance;
import pl.org.minions.stigma.game.world.World;
import pl.org.minions.stigma.globals.SizeOf;
import pl.org.minions.stigma.network.Buffer;

/**
 * Class representing event of some item being dropped by
 * some actor.
 */
public class ItemDropped extends Event
{
    private static final int SIZE = ItemDescription.sizeOf() // item
        + SizeOf.INT // actor id
    ;

    private ItemDescription itemDesc;
    private int actorId;

    private ItemDropped()
    {
        super(EventType.ITEM_DROPPED);
    }

    /**
     * Constructor.
     * @param item
     *            dropped item
     * @param actorId
     *            id of actor who dropped item
     */
    public ItemDropped(Item item, int actorId)
    {
        this();
        this.itemDesc = new ItemDescription(item);
        this.actorId = actorId;
    }

    /**
     * Factory method for creation of empty objects of this
     * class. Needed for decoding.
     * @return empty instance of this class
     */
    public static ItemDropped create()
    {
        return new ItemDropped();
    }

    /**
     * Returns itemId.
     * @return itemId
     */
    public int getItemId()
    {
        return itemDesc.getId();
    }

    /**
     * Returns actorId.
     * @return actorId
     */
    public int getActorId()
    {
        return actorId;
    }

    /** {@inheritDoc} */
    @Override
    public boolean apply(World world, DataRequestSinks dataRequestSink)
    {
        // we should have seen this actor if we see him dropping something
        Actor a = world.getActor(actorId);
        if (a == null)
            return false;

        Item i = itemDesc.getItem(world, dataRequestSink);

        if (i.isOnGround())
            return false;

        if (!a.removeItem(i))
            return false;

        MapInstance map = world.getMap(a.getMapId(), a.getMapInstanceNo());

        if (map == null)
            return false;

        MapInstance.Segment segment =
                map.getSegmentForPosition(a.getPosition());

        if (segment == null)
            return false;

        i.setMapId(a.getMapId());
        i.setMapInstanceNo(a.getMapInstanceNo());
        i.setPosition(a.getPosition());

        segment.putItem(i);

        return true;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "ItemDropped: item: " + getItemId() + " actorId: " + actorId;
    }

    /** {@inheritDoc} */
    @Override
    protected boolean decodeParams(Buffer buffer)
    {
        itemDesc = new ItemDescription(buffer);
        actorId = buffer.decodeInt();
        return true;
    }

    /** {@inheritDoc} */
    @Override
    protected boolean encodeParams(Buffer buffer)
    {
        itemDesc.encode(buffer);
        buffer.encode(actorId);
        return true;
    }

    /** {@inheritDoc} */
    @Override
    protected int innerGetParamsLength()
    {
        return SIZE;
    }

    /** {@inheritDoc} */
    @Override
    public Set<Integer> getAffectedActors()
    {
        return Collections.singleton(actorId);
    }

}
