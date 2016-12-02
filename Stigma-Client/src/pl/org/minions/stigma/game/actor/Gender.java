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

import pl.org.minions.utils.logger.Log;

/**
 * Enum representing gender of actor.
 */
public enum Gender
{
    Male, Female, Neutral;

    private static final Gender[] VALUES = Gender.class.getEnumConstants();

    /**
     * Returns enum value for given ordinal number.
     * @param ordinal
     *            ordinal of requested enum value
     * @return enum value for given ordinal number.
     */
    public static Gender getForOrdinal(byte ordinal)
    {
        if (ordinal < 0 || ordinal >= VALUES.length)
        {
            Log.logger.warn("Bad ordinal: " + ordinal);
            return null;
        }
        return VALUES[ordinal];
    }
}
