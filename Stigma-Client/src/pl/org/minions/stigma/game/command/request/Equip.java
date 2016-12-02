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
package pl.org.minions.stigma.game.command.request;

import java.util.List;
import java.util.Set;

import pl.org.minions.stigma.game.actor.Actor;
import pl.org.minions.stigma.game.command.Command;
import pl.org.minions.stigma.game.command.CommandType;
import pl.org.minions.stigma.game.data.WorldData;
import pl.org.minions.stigma.game.event.Event;
import pl.org.minions.stigma.game.event.item.ItemEquipped;
import pl.org.minions.stigma.game.item.Item;
import pl.org.minions.stigma.game.item.PhysicalSlotType;
import pl.org.minions.stigma.game.map.MapInstance;
import pl.org.minions.stigma.game.map.MapInstance.Segment;
import pl.org.minions.stigma.game.world.ExtendedWorld;
import pl.org.minions.stigma.globals.SizeOf;
import pl.org.minions.stigma.network.Buffer;

/**
 * Equip command - request to equip item from inventory.
 */
public class Equip extends Command
{
    private static final int SIZE = SizeOf.INT + SizeOf.BYTE;

    private int itemId;
    private PhysicalSlotType position;

    private Equip()
    {
        super(CommandType.EQUIP);
    }

    /**
     * Constructor.
     * @param itemId
     *            id of item to be equipped
     * @param position
     *            position to equip item to
     */
    public Equip(int itemId, PhysicalSlotType position)
    {
        this();
        assert position != PhysicalSlotType.INVENTORY;
        this.itemId = itemId;
        this.position = position;
    }

    /**
     * Factory method for creation of empty objects of this
     * class. Needed for decoding.
     * @return empty instance of this class
     */
    public static Equip create()
    {
        return new Equip();
    }

    /** {@inheritDoc} */
    @Override
    protected boolean commandSpecificDecode(Buffer buffer)
    {
        this.itemId = buffer.decodeInt();
        this.position = buffer.decodeEnum(PhysicalSlotType.class);
        return true;
    }

    /** {@inheritDoc} */
    @Override
    protected boolean commandSpecificEncode(Buffer buffer)
    {
        buffer.encode(itemId);
        buffer.encode(position);
        return true;
    }

    /** {@inheritDoc} */
    @Override
    protected int commandSpecificLength()
    {
        return SIZE;
    }

    /** {@inheritDoc} */
    @Override
    protected boolean innerExecute(ExtendedWorld world,
                                   List<Event> output,
                                   List<WorldData> dataForSender,
                                   Set<Segment> affectedSegments)
    {
        Actor a = world.getActor(getRequesterId());
        if (a == null)
            return false;

        Item i = world.getItem(itemId);
        if (i == null)
            return false;

        if (!a.getInventory().contains(i))
            return false;

        if (!a.canEquip(i, position))
            return false;

        MapInstance map = world.getMap(a.getMapId(), a.getMapInstanceNo());

        if (map == null)
            return false;

        MapInstance.Segment segment =
                map.getSegmentForPosition(a.getPosition());

        if (segment == null)
            return false;

        affectedSegments.add(segment);
        output.add(new ItemEquipped(a.getId(),
                                    world.getTurnNumber(),
                                    i,
                                    position));
        return true;
    }

    /** {@inheritDoc} */
    @Override
    protected boolean paramsEquals(Command cmd)
    {
        Equip other = (Equip) cmd;
        return other.itemId == this.itemId && other.position == this.position;
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
     * Returns position.
     * @return position
     */
    public PhysicalSlotType getPosition()
    {
        return position;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "Equip: " + itemId + " on " + position;
    }

}
