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
package pl.org.minions.stigma.game.actor;

/**
 * Damage types.
 */
public enum DamageType
{
    CUTTING, PIERCING, BLUNT, FROSTBITE, BURN, SHOCK, POISON;

    private static final DamageType[] VALUES_ARRAY = values();

    /**
     * Returns enum value for given ordinal.
     * @param ordinal
     *            requested ordinal
     * @return enum value for given ordinal.
     */
    public static DamageType getForOridinal(int ordinal)
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
     * Returns same like {@link #values()} function, but
     * without making copy. It is more efficient, yet user
     * is responsible for not modification of returned
     * array.
     * @return array with all possible enum values
     */
    public static DamageType[] getValuesArray()
    {
        return VALUES_ARRAY;
    }

}
