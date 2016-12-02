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
 * Class representing event of some item being equipped by
 * some actor.
 */
public class ItemEquipped extends Event
{
    private static final int SIZE = SizeOf.INT // actor id 
        + SizeOf.INT // new actor TS 
        + ItemDescription.sizeOf() // item
        + SizeOf.BYTE // item position
    ;

    private int actorId;
    private int newActorTS;
    private ItemDescription itemDesc;
    private PhysicalSlotType position;

    private ItemEquipped()
    {
        super(EventType.ITEM_EQUIPPED);
    }

    /**
     * Constructor.
     * @param actorId
     *            id of actor that equipped item
     * @param newActorTS
     *            new TS which should be set to actor after
     *            equipping item
     * @param item
     *            equipped item
     * @param position
     *            position on which item should be equipped
     */
    public ItemEquipped(int actorId,
                        int newActorTS,
                        Item item,
                        PhysicalSlotType position)
    {
        this();
        assert position != PhysicalSlotType.INVENTORY;
        this.actorId = actorId;
        this.newActorTS = newActorTS;
        this.itemDesc = new ItemDescription(item);
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

        Item i = itemDesc.getItem(world, dataRequestSink);

        if (!a.equipItem(i, position))
            return false;

        if (a.getSlowTS() < newActorTS)
            a.setSlowTS(newActorTS);

        return true;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "ItemEquipped: item: " + itemDesc.getId() + " actorId: "
            + actorId;
    }

    /** {@inheritDoc} */
    @Override
    protected boolean decodeParams(Buffer buffer)
    {
        actorId = buffer.decodeInt();
        newActorTS = buffer.decodeInt();
        itemDesc = new ItemDescription(buffer);
        position = buffer.decodeEnum(PhysicalSlotType.class);
        return true;
    }

    /** {@inheritDoc} */
    @Override
    protected boolean encodeParams(Buffer buffer)
    {
        buffer.encode(actorId);
        buffer.encode(newActorTS);
        itemDesc.encode(buffer);
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
        return new ItemEquipped();
    }

    /** {@inheritDoc} */
    @Override
    public Set<Integer> getAffectedActors()
    {
        return Collections.singleton(actorId);
    }

    /**
     * Returns equipped item id.
     * @return equipped item id.
     */
    public int getItemId()
    {
        return itemDesc.getId();
    }
}
