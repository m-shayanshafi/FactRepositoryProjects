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
package pl.org.minions.stigma.game;

import pl.org.minions.stigma.game.actor.Actor;

/**
 * Provides functions and constant variables used in various
 * calculations during game.
 */
public abstract class MechanicInternals
{
    private static final int MAX_STAMINA_BASE = 16;
    private static final int MAX_STAMINA_STRENGTH_FACTOR = 2;
    private static final int MAX_STAMINA_WILLPOWER_FACTOR = 4;

    private static final int MAX_HEALTH_BASE = 16;
    private static final int MAX_HEALT_STRENGTH_FACTOR = 4;
    private static final int MAX_HEALTH_WILLPOWER_FACTOR = 2;

    private static final short MAX_LOAD_FACTOR = 10;

    private static final int XP_REQUIREMENT_BASE = 80;
    private static final int XP_REQUIREMENT_INCREMENT = 20;

    private MechanicInternals()
    {
    }

    /**
     * Calculates amount of experience required to advance
     * to the next level, while on given <tt>level</tt>.
     * @param level
     *            current level of an {@link Actor}
     * @return amount of experience points required to
     *         advance
     */
    public static int getNextLevelXPRequirement(byte level)
    {
        return XP_REQUIREMENT_BASE + level * XP_REQUIREMENT_INCREMENT;
    }

    /**
     * Calculates max load basing on primary attributes of
     * given {@link Actor}.
     * @param actor
     *            actor to calculate max load for
     * @return max carried load
     */
    public static short getBaseMaxLoad(Actor actor)
    {
        return (short) (MAX_LOAD_FACTOR * actor.getStrength());
    }

    /**
     * Calculate max health basing on primary attributes of
     * given {@link Actor}.
     * @param actor
     *            actor to calculate max health for
     * @return
     *         max health
     */
    public static short getBaseMaxHealth(Actor actor)
    {
        return (short) (MAX_HEALTH_BASE + MAX_HEALT_STRENGTH_FACTOR
            * actor.getStrength() + MAX_HEALTH_WILLPOWER_FACTOR
            * actor.getWillpower());
    }

    /**
     * Calculate max stamina basing on primary attributes of
     * given {@link Actor}.
     * @param actor
     *            actor to calculate max stamina for
     * @return
     *         max stamina
     */
    public static short getBaseMaxStamina(Actor actor)
    {
        return (short) (MAX_STAMINA_BASE + MAX_STAMINA_STRENGTH_FACTOR
            * actor.getStrength() + MAX_STAMINA_WILLPOWER_FACTOR
            * actor.getWillpower());
    }
}
