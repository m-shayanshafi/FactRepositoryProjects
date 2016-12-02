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
package pl.org.minions.stigma.network.messaging.auth;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import pl.org.minions.stigma.databases.item.ItemFactory;
import pl.org.minions.stigma.game.actor.Actor;
import pl.org.minions.stigma.game.actor.DamageType;
import pl.org.minions.stigma.game.actor.Gender;
import pl.org.minions.stigma.game.actor.Resistance;
import pl.org.minions.stigma.game.item.Item;
import pl.org.minions.stigma.game.item.PhysicalSlotType;
import pl.org.minions.stigma.game.item.type.ItemType.ItemKind;
import pl.org.minions.stigma.globals.SizeOf;
import pl.org.minions.stigma.network.Buffer;
import pl.org.minions.stigma.network.messaging.NetworkMessage;
import pl.org.minions.stigma.network.messaging.NetworkMessageType;

/**
 * Class representing message sent after client informed it
 * is ready for data. Message contains collection of actors
 * available for given account.
 */
public class LoginActorsList extends NetworkMessage
{
    private static final int CONST_ACTOR_SIZE = SizeOf.INT // id 
        + SizeOf.BYTE // gender
        + SizeOf.INT // experience 
        + SizeOf.BYTE // level
        + SizeOf.BYTE // strength
        + SizeOf.BYTE // willpower
        + SizeOf.BYTE // agility
        + SizeOf.BYTE // finesse
        + SizeOf.SHORT // safe map id
        + SizeOf.INT // money
        + DamageType.valuesCount() * (SizeOf.BYTE + SizeOf.SHORT) // resistances
    ;

    private static final int CONST_ITEM_SIZE = SizeOf.INT // id
        + SizeOf.BYTE // kind
        + SizeOf.SHORT // type
    ;

    private Collection<Actor> availableActors;
    private Collection<Integer> disabledActors;

    private LoginActorsList()
    {
        super(NetworkMessageType.LOGIN_ACTORS_LIST);
        availableActors = new LinkedList<Actor>();
        disabledActors = new LinkedList<Integer>();
    }

    /**
     * Constructor.
     * @param availableActors
     *            collection of available actors for account
     * @param disabledActors
     *            collection of currently disabled actors
     *            identifiers
     */
    public LoginActorsList(Collection<Actor> availableActors,
                           Collection<Integer> disabledActors)
    {
        this();
        this.availableActors = availableActors;
        this.disabledActors = disabledActors;
    }

    /**
     * Create empty object (needed for parsing network
     * message). Object state and information are unknown.
     * @return empty object
     */
    public static LoginActorsList create()
    {
        return new LoginActorsList();
    }

    /** {@inheritDoc} */
    @Override
    protected boolean decodeParams(Buffer buffer)
    {
        byte size = buffer.decodeByte();
        availableActors.clear();
        for (int i = 0; i < size; ++i)
        {
            Actor a = new Actor(buffer.decodeInt(), buffer.decodeString());
            if (a.getName() == null || a.getName().equals(""))
                return false;
            a.setGender(Gender.getForOrdinal(buffer.decodeByte()));
            a.setExperience(buffer.decodeInt());
            a.setLevel(buffer.decodeByte());
            a.setStrength(buffer.decodeByte());
            a.setWillpower(buffer.decodeByte());
            a.setAgility(buffer.decodeByte());
            a.setFinesse(buffer.decodeByte());

            a.firstRecalc(); // calculates other values

            a.setSafeMapId(buffer.decodeShort());

            final int archSize = buffer.decodeByte();
            for (int archIt = 0; archIt < archSize; ++archIt)
                a.getPersistenArchetypes().add(buffer.decodeShort());

            for (DamageType dmg : DamageType.getValuesArray())
            {
                Resistance r = a.getResistance(dmg);
                r.setRelative(buffer.decodeByte());
                r.setThreshold(buffer.decodeShort());
            }

            final int equipmentSize = buffer.decodeByte();
            Map<Integer, Item> decodedItems = new HashMap<Integer, Item>();
            for (int eqIt = 0; eqIt < equipmentSize; ++eqIt)
            {
                PhysicalSlotType slot =
                        PhysicalSlotType.getForOridinal(buffer.decodeByte());
                int id = buffer.decodeInt();
                ItemKind kind = ItemKind.getForOridinal(buffer.decodeByte());
                short type = buffer.decodeShort();

                Item item = decodedItems.get(id);
                if (item == null)
                {
                    item = ItemFactory.getInstance().getItem(id, type, kind);
                    decodedItems.put(id, item);
                }
                a.getEquipedItems().put(slot, item);
                item.setEquippedPosition(slot);
            }

            a.setMoney(buffer.decodeInt());

            // so this actor will NEVER be synchronized using TSes
            a.setFastTS(Integer.MAX_VALUE);
            a.setSlowTS(Integer.MAX_VALUE);
            availableActors.add(a);
        }

        size = buffer.decodeByte();
        disabledActors.clear();
        for (int i = 0; i < size; ++i)
        {
            disabledActors.add(buffer.decodeInt());
        }

        return true;
    }

    /** {@inheritDoc} */
    @Override
    protected boolean encodeParams(Buffer buffer)
    {
        buffer.encode((byte) availableActors.size());
        for (Actor a : availableActors)
        {
            buffer.encode(a.getId());
            buffer.encode(a.getName());
            buffer.encode((byte) a.getGender().ordinal());
            buffer.encode(a.getExperience());
            buffer.encode(a.getLevel());
            buffer.encode(a.getStrength());
            buffer.encode(a.getWillpower());
            buffer.encode(a.getAgility());
            buffer.encode(a.getFinesse());

            buffer.encode(a.getSafeMapId());

            buffer.encode((byte) a.getPersistenArchetypes().size());
            for (Short arch : a.getPersistenArchetypes())
                buffer.encode(arch);

            for (DamageType dmg : DamageType.getValuesArray())
            {
                Resistance r = a.getResistance(dmg);
                buffer.encode(r.getRelative());
                buffer.encode(r.getThreshold());
            }

            buffer.encode((byte) a.getEquipedItems().size());
            for (Map.Entry<PhysicalSlotType, Item> e : a.getEquipedItems()
                                                        .entrySet())
            {
                Item i = e.getValue();
                PhysicalSlotType slot = e.getKey();
                buffer.encode((byte) slot.ordinal());
                buffer.encode(i.getId());
                buffer.encode((byte) i.getKind().ordinal());
                buffer.encode(i.getType().getId());
            }

            buffer.encode(a.getMoney());
        }

        buffer.encode((byte) disabledActors.size());
        for (int i : disabledActors)
            buffer.encode(i);
        return true;
    }

    /**
     * Returns actors available for given account.
     * @return actors available for given account
     */
    public Collection<Actor> getAvailableActors()
    {
        return availableActors;
    }

    /**
     * Returns collection of identifiers of actors which are
     * not currently available.
     * @return collection of identifiers of actors which are
     *         not currently available.
     */
    public Collection<Integer> getDisabledActors()
    {
        return disabledActors;
    }

    /** {@inheritDoc} */
    @Override
    protected int innerGetParamsLength()
    {
        int size = SizeOf.BYTE;
        for (Actor a : availableActors)
        {
            size += CONST_ACTOR_SIZE;
            size += Buffer.stringBytesCount(a.getName());

            size += SizeOf.BYTE;
            size += SizeOf.SHORT * a.getPersistenArchetypes().size();

            size += SizeOf.BYTE;
            size += CONST_ITEM_SIZE * a.getEquipedItems().size();
        }
        size += SizeOf.BYTE;
        size += disabledActors.size() * SizeOf.INT;
        return size;
    }
}
