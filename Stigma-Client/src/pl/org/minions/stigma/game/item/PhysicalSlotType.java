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

/**
 * Enumeration which indicates position of equipped Item's
 * on character.
 */
public enum PhysicalSlotType
{
    INVENTORY,
    RIGHT_HAND,
    LEFT_HAND,
    HANDS,
    TORSO,
    LEGS,
    FEET,
    HEAD,
    FACE,
    NECK,
    FINGER_1,
    FINGER_2,
    FINGER_3,
    FINGER_4,
    FINGER_5,
    FINGER_6,
    FINGER_7,
    FINGER_8;

    private static final PhysicalSlotType[] VALUES_ARRAY = values();

    /**
     * Returns enum value for given ordinal.
     * @param ordinal
     *            requested ordinal
     * @return enum value for given ordinal.
     */
    public static PhysicalSlotType getForOridinal(int ordinal)
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

}
