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
package pl.org.minions.stigma.game.item;

import java.util.EnumSet;
import java.util.Set;

/**
 * Enumeration representing information about in which
 * Equipment slot type item can be put.
 */
public enum LogicalSlotType
{
    ONE_HAND(PhysicalSlotType.RIGHT_HAND, PhysicalSlotType.LEFT_HAND),
    TWO_HANDS(new PhysicalSlotType[]
              { PhysicalSlotType.RIGHT_HAND, PhysicalSlotType.LEFT_HAND, },
              new PhysicalSlotType[]
              { PhysicalSlotType.RIGHT_HAND, PhysicalSlotType.LEFT_HAND, }),
    SHIELD(PhysicalSlotType.LEFT_HAND, PhysicalSlotType.RIGHT_HAND),
    FULL_HELMET(new PhysicalSlotType[]
    { PhysicalSlotType.HEAD }, new PhysicalSlotType[]
    { PhysicalSlotType.FACE }),
    OPEN_HELMET(PhysicalSlotType.HEAD),
    MASK(PhysicalSlotType.FACE),
    AMULET(PhysicalSlotType.NECK),
    RING(PhysicalSlotType.FINGER_1,
         PhysicalSlotType.FINGER_2,
         PhysicalSlotType.FINGER_3,
         PhysicalSlotType.FINGER_4,
         PhysicalSlotType.FINGER_5,
         PhysicalSlotType.FINGER_6,
         PhysicalSlotType.FINGER_7,
         PhysicalSlotType.FINGER_8),
    GLOVES(PhysicalSlotType.HANDS),
    ARMOR(PhysicalSlotType.TORSO),
    TROUSERS(PhysicalSlotType.LEGS),
    BOOTS(PhysicalSlotType.FEET),
    ROBE(new PhysicalSlotType[]
    { PhysicalSlotType.TORSO }, new PhysicalSlotType[]
    { PhysicalSlotType.LEGS }),
    FULL_ARMOR(new PhysicalSlotType[]
    { PhysicalSlotType.TORSO }, new PhysicalSlotType[]
    { PhysicalSlotType.LEGS, PhysicalSlotType.HANDS, PhysicalSlotType.FEET, }),
    NO_SLOT_TYPE;

    private static final LogicalSlotType[] VALUES_ARRAY = values();
    private Set<PhysicalSlotType> availableSlots;
    private Set<PhysicalSlotType> blockedSlots;

    private LogicalSlotType(PhysicalSlotType... availableSlots)
    {
        this(availableSlots, null);
    }

    private LogicalSlotType(PhysicalSlotType[] availableSlots,
                            PhysicalSlotType[] blockedSlots)
    {
        if (availableSlots != null)
            this.availableSlots =
                    EnumSet.of(PhysicalSlotType.INVENTORY, availableSlots);
        else
            this.availableSlots = EnumSet.of(PhysicalSlotType.INVENTORY);

        if (blockedSlots != null && blockedSlots.length > 0)
            this.blockedSlots = EnumSet.of(blockedSlots[0], blockedSlots);
        else
            this.blockedSlots = EnumSet.noneOf(PhysicalSlotType.class);
    }

    /**
     * Returns enum value for given ordinal.
     * @param ordinal
     *            requested ordinal
     * @return enum value for given ordinal.
     */
    public static LogicalSlotType getForOrdinal(int ordinal)
    {
        return VALUES_ARRAY[ordinal];
    }

    /**
     * Returns number of values in enum.
     * @return number of values in enum.
     */
    public static int valuesCount()
    {
        return VALUES_ARRAY.length;
    }

    /**
     * Returns list of available physical slots for selected
     * logic slot (list includes INVENTORY slot).
     * @return list of available physical slots
     */
    public Set<PhysicalSlotType> getAvailablePhysicalSlots()
    {
        return availableSlots;
    }

    /**
     * Returns list of blocked physical slots for selected
     * logic slot.
     * @return list of blocked physical slots
     */
    public Set<PhysicalSlotType> getBlockedPhysicalSlots()
    {
        return blockedSlots;
    }
}
