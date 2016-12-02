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
package pl.org.minions.stigma.globals;

import java.math.BigDecimal;

/**
 * Utility class for calculating seconds from turns.
 */
public final class TurnCalc
{

    private TurnCalc()
    {
    }

    /**
     * Returns number of milliseconds equivalent to given
     * number of turns.
     * @param turns
     *            number of turns
     * @return duration of given number of turns in
     *         milliseconds
     */
    public static int turns2millis(int turns)
    {
        return GlobalConfig.globalInstance().getMillisecondsPerTurn() * turns;
    }

    /**
     * Returns number of seconds equivalent to given number
     * of turns.
     * @param turns
     *            number of turns
     * @return duration of given number of turns in seconds
     */
    public static double turns2seconds(int turns)
    {
        // CHECKSTYLE:OFF
        return turns2millis(turns) / 1000.;
        // CHECKSTYLE:ON
    }

    /**
     * Returns number of seconds equivalent to given number
     * of turns. {@link BigDecimal} representation allows
     * precise operations.
     * @param turns
     *            number of turns
     * @return duration of given number of turns in seconds
     */
    public static BigDecimal turns2secondsDec(int turns)
    {
        // CHECKSTYLE:OFF
        return BigDecimal.valueOf(turns2millis(turns), 3);
        // CHECKSTYLE:ON
    }

    /**
     * Returns string with number of seconds equivalent to
     * given number of turns. String will be nicely
     * formatted and with precision to second decimal place.
     * @param turns
     *            number of turns
     * @return representation of duration of given number of
     *         turns in seconds
     */
    public static String turns2secondsStr(int turns)
    {
        // CHECKSTYLE:OFF
        int ms = turns2millis(turns);
        int rest = ms % 10 >= 5 ? 1 : 0;
        ms /= 10;
        ms += rest;
        return BigDecimal.valueOf(ms, 2).toString();
        // CHECKSTYLE:ON
    }

}
