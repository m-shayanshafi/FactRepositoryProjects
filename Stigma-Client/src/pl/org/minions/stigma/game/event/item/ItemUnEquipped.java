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
import pl.org.minions.stigma.game.world.World;
import pl.org.minions.stigma.globals.SizeOf;
import pl.org.minions.stigma.network.Buffer;

/**
 * Class representing event of some item being un-equipped
 * by some actor.
 */
public class ItemUnEquipped extends Event
{
    private static final int SIZE = SizeOf.INT // actor id 
        + SizeOf.INT // new actor TS 
        + SizeOf.BYTE // item position
    ;

    private int actorId;
    private int newActorTS;
    private PhysicalSlotType position;

    private Item unequippedItem;

    private ItemUnEquipped()
    {
        super(EventType.ITEM_UNEQUIPPED);
    }

    /**
     * Constructor.
     * @param actorId
     *            id of actor that un-equipped item
     * @param newActorTS
     *            new TS which should be set to actor after
     *            un-equipping item
     * @param position
     *            position from which item should be
     *            un-equipped
     */
    public ItemUnEquipped(int actorId, int newActorTS, PhysicalSlotType position)
    {
        this();
        this.actorId = actorId;
        this.newActorTS = newActorTS;
        this.position = position;
    }

    /** {@inheritDoc} */
    @Override
    public boolean apply(World world, DataRequestSinks dataRequestSink)
    {
        Actor a = world.getActor(actorId);

        // we should have seen this actor if we see him equipping something
        if (a == null)
            return false;
        if (dataRequestSink != null)
            dataRequestSink.getActorSink().compareAndAdd(a, newActorTS);

        unequippedItem = a.unEquipItem(position);
        if (unequippedItem == null)
            return false;

        if (a.getSlowTS() < newActorTS)
            a.setSlowTS(newActorTS);
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "ItemUnEquipped: position: " + position + " actorId: " + actorId;
    }

    /** {@inheritDoc} */
    @Override
    protected boolean decodeParams(Buffer buffer)
    {
        actorId = buffer.decodeInt();
        newActorTS = buffer.decodeInt();
        position = buffer.decodeEnum(PhysicalSlotType.class);
        return true;
    }

    /** {@inheritDoc} */
    @Override
    protected boolean encodeParams(Buffer buffer)
    {
        buffer.encode(actorId);
        buffer.encode(newActorTS);
        buffer.encode(position);
        return true;
    }

    /** {@inheritDoc} */
    @Override
    protected int innerGetParamsLength()
    {
        return SIZE;
    }

    /**
     * Returns new empty instance of this class. Needed for
     * decoding.
     * @return new empty instance
     */
    public static Event create()
    {
        return new ItemUnEquipped();
    }

    /** {@inheritDoc} */
    @Override
    public Set<Integer> getAffectedActors()
    {
        return Collections.singleton(actorId);
    }

    /**
     * Returns unequipped. Available only after applying
     * this event.
     * @return unequipped
     */
    public Item getUnequippedItem()
    {
        return unequippedItem;
    }

}
