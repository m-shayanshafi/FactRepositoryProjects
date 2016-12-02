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

import java.util.List;

/**
 * Abstract class used to build archetypes.
 */
public abstract class ArchetypeBuilder
{
    private ArchetypeBuilder()
    {
    }

    private static void accumulate(ArchetypeBase accumulator,
                                   ArchetypeBase archetype)
    {
        accumulator.setAgility((byte) (archetype.getAgility() + accumulator.getAgility()));
        accumulator.setFinesse((byte) (archetype.getFinesse() + accumulator.getFinesse()));
        accumulator.setStrength((byte) (archetype.getStrength() + accumulator.getStrength()));
        accumulator.setWillpower((byte) (archetype.getWillpower() + accumulator.getWillpower()));

        for (DamageType dmg : DamageType.getValuesArray())
        {
            Resistance resA = archetype.getResistance(dmg);
            Resistance resAcum = accumulator.getResistance(dmg);
            if (resA == null)
                continue;
            if (resAcum == null)
            {
                accumulator.getResistanceMap().put(dmg, resA.deepCopy());
                continue;
            }
            resAcum.setRelative((byte) (resAcum.getRelative() + resA.getRelative()));
            resAcum.setThreshold((short) (resAcum.getThreshold() + resA.getThreshold()));
        }

        accumulator.getProficiencies().addAll(archetype.getProficiencies());
    }

    private static void accumulate(ArchetypeBase acumulator,
                                   List<? extends ArchetypeBase> archetypes)
    {
        for (ArchetypeBase a : archetypes)
            accumulate(acumulator, a);
    }

    /**
     * Accumulates archetype, sets its
     * {@link Archetype#isAccumulated()} option.
     * @param acumulator
     *            archetype to accumulate
     * @param archetypes
     *            parents of given archetype
     */
    public static void accumulate(Archetype acumulator,
                                  List<? extends ArchetypeBase> archetypes)
    {
        accumulate((ArchetypeBase) acumulator, archetypes);
        acumulator.setAccumulated(true);
    }

    private static void checkConsistency(Actor a)
    {
        a.setAgility((byte) Math.max(1,
                                     Math.min(a.getAgility(), Byte.MAX_VALUE)));
        a.setFinesse((byte) Math.max(1,
                                     Math.min(a.getFinesse(), Byte.MAX_VALUE)));
        a.setStrength((byte) Math.max(1, Math.min(a.getStrength(),
                                                  Byte.MAX_VALUE)));
        a.setWillpower((byte) Math.max(1, Math.min(a.getWillpower(),
                                                   Byte.MAX_VALUE)));
    }

    /**
     * Accumulates actor. Sets random gender if none was
     * assigned from archetypes or base.
     * @param actor
     *            return archetype
     * @param archetypes
     *            list of archetypes which are accumulated
     *            in archetype
     */
    public static void accumulate(Actor actor,
                                  List<? extends ArchetypeBase> archetypes)
    {
        accumulate((ArchetypeBase) actor, archetypes);

        final double genderConst = 0.5;
        if (actor.getGender() == null)
            actor.setGender(Math.random() > genderConst ? Gender.Male
                                                       : Gender.Female);
        checkConsistency(actor);
    }

    /**
     * Adds archetypes to actors. If archetype is persistent
     * it is added to actors persistent list.
     * @param actor
     *            actor to which archetypes should be added
     * @param archetypes
     *            archetypes to be added to actor
     */
    public static void addArchetypes(Actor actor, Archetype... archetypes)
    {
        for (Archetype a : archetypes)
        {
            accumulate(actor, a);
            if (a.isPersistent())
                actor.getPersistenArchetypes().add(a.getId());
        }
        checkConsistency(actor);
    }

    /**
     * Removes aggregated info from archetype, leaves only
     * "delta".
     * @param acumulated
     *            accumulated archetype - will lose
     *            additional data
     * @param archetypes
     *            list of archetypes to remove from given
     *            archetype
     */
    public static void divide(ArchetypeBase acumulated,
                              List<? extends ArchetypeBase> archetypes)
    {
        for (ArchetypeBase a : archetypes)
        {
            acumulated.setAgility((byte) (acumulated.getAgility() - a.getAgility()));
            acumulated.setFinesse((byte) (acumulated.getFinesse() - a.getFinesse()));
            acumulated.setStrength((byte) (acumulated.getStrength() - a.getStrength()));
            acumulated.setWillpower((byte) (acumulated.getWillpower() - a.getWillpower()));

            for (DamageType dmg : DamageType.getValuesArray())
            {
                Resistance resA = a.getResistance(dmg);
                if (resA == null)
                    continue;
                Resistance resAcum = acumulated.getResistance(dmg);
                if (resAcum == null)
                    continue;
                resAcum.setRelative((byte) (resAcum.getRelative() - resA.getRelative()));
                resAcum.setThreshold((short) (resAcum.getThreshold() - resA.getThreshold()));
            }
        }
    }
}
