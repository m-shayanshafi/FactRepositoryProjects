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
import pl.org.minions.stigma.game.event.item.ItemUnEquipped;
import pl.org.minions.stigma.game.item.Item;
import pl.org.minions.stigma.game.item.PhysicalSlotType;
import pl.org.minions.stigma.game.map.MapInstance;
import pl.org.minions.stigma.game.map.MapInstance.Segment;
import pl.org.minions.stigma.game.world.ExtendedWorld;
import pl.org.minions.stigma.globals.SizeOf;
import pl.org.minions.stigma.network.Buffer;

/**
 * Un-Equip command - request to un-equip item from actor.
 */
public class UnEquip extends Command
{
    private static final int SIZE = SizeOf.BYTE;

    private PhysicalSlotType position;

    private UnEquip()
    {
        super(CommandType.UNEQUIP);
    }

    /**
     * Constructor.
     * @param position
     *            position to un-equip item from
     */
    public UnEquip(PhysicalSlotType position)
    {
        this();
        this.position = position;
    }

    /**
     * Factory method for creation of empty objects of this
     * class. Needed for decoding.
     * @return empty instance of this class
     */
    public static UnEquip create()
    {
        return new UnEquip();
    }

    /** {@inheritDoc} */
    @Override
    protected boolean commandSpecificDecode(Buffer buffer)
    {
        this.position = buffer.decodeEnum(PhysicalSlotType.class);
        return true;
    }

    /** {@inheritDoc} */
    @Override
    protected boolean commandSpecificEncode(Buffer buffer)
    {
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

        Item i = a.getEquipedItems().get(position);

        if (i == null)
            return false;

        // must be un-equipped from main slot 
        if (!i.getType()
              .getEquipementSlot()
              .getAvailablePhysicalSlots()
              .contains(position))
            return false;

        MapInstance map = world.getMap(a.getMapId(), a.getMapInstanceNo());

        if (map == null)
            return false;

        MapInstance.Segment segment =
                map.getSegmentForPosition(a.getPosition());

        if (segment == null)
            return false;

        affectedSegments.add(segment);
        output.add(new ItemUnEquipped(a.getId(), world.getTurnNumber(),

        position));
        return true;
    }

    /** {@inheritDoc} */
    @Override
    protected boolean paramsEquals(Command cmd)
    {
        UnEquip other = (UnEquip) cmd;
        return other.position == this.position;
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
        return "UnEquip: " + position;
    }

}
