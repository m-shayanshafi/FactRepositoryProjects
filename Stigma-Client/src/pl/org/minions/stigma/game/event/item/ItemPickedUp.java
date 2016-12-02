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
import pl.org.minions.stigma.game.item.PhysicalSlotType;
import pl.org.minions.stigma.game.map.MapInstance;
import pl.org.minions.stigma.game.world.World;
import pl.org.minions.stigma.globals.SizeOf;
import pl.org.minions.stigma.network.Buffer;

/**
 * Class representing event of some item being picked up by
 * some actor.
 */
public class ItemPickedUp extends Event
{
    private static final int SIZE = SizeOf.INT + SizeOf.INT;

    private int itemId;
    private int actorId;

    private ItemPickedUp()
    {
        super(EventType.ITEM_PICKED_UP);
    }

    /**
     * Constructor.
     * @param itemId
     *            id of picked up item
     * @param actorId
     *            id of actor who picked up item
     */
    public ItemPickedUp(int itemId, int actorId)
    {
        this();
        this.itemId = itemId;
        this.actorId = actorId;
    }

    /**
     * Factory method for creation of empty objects of this
     * class. Needed for decoding.
     * @return empty instance of this class
     */
    public static ItemPickedUp create()
    {
        return new ItemPickedUp();
    }

    /**
     * Returns itemId.
     * @return itemId
     */
    public int getItemId()
    {
        return itemId;
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
        // no TS check because we must already seen item that is picked up

        Item i = world.getItem(itemId);
        if (i == null)
            return false;

        Actor a = world.getActor(actorId);
        if (a == null)
            return false;

        if (!i.isOnGround() || i.getMapId() != a.getMapId()
            || i.getMapInstanceNo() != a.getMapInstanceNo()
            || !i.getPosition().equals(a.getPosition()))
            return false;

        MapInstance map = world.getMap(i.getMapId(), i.getMapInstanceNo());

        if (map == null)
            return false;

        MapInstance.Segment segment =
                map.getSegmentForPosition(i.getPosition());

        if (segment == null)
            return false;

        segment.removeItem(i);
        a.addItem(i, PhysicalSlotType.INVENTORY);

        return true;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "ItemPickedUp: item: " + itemId + " actorId: " + actorId;
    }

    /** {@inheritDoc} */
    @Override
    protected boolean decodeParams(Buffer buffer)
    {
        itemId = buffer.decodeInt();
        actorId = buffer.decodeInt();
        return true;
    }

    /** {@inheritDoc} */
    @Override
    protected boolean encodeParams(Buffer buffer)
    {
        buffer.encode(itemId);
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
