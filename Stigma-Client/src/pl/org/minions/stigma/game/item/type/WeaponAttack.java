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
package pl.org.minions.stigma.game.item.type;

import pl.org.minions.stigma.game.actor.DamageType;

/**
 * Class representing weapon damage.
 */
public class WeaponAttack
{
    private DamageType damageType;
    private short range;
    private short cooldown;

    private short baseDamage;

    private short attack;

    private short criticalChance;

    private short strenghtBonusStep;
    private short agilityBonusStep;
    private short willpowerBonusStep;
    private short finesseBonusStep;

    private short strenghtBonusMin;
    private short agilityBonusMin;
    private short willpowerBonusMin;
    private short finesseBonusMin;

    private short strenghtBonusMax;
    private short agilityBonusMax;
    private short willpowerBonusMax;
    private short finesseBonusMax;

    /**
     * Default constructor.
     * @param damageType
     *            type of damage done by weapon
     * @param range
     *            range of weapon
     * @param cooldown
     *            cooldown time for weapon (indicates how
     *            often actor can attack with this weapon)
     * @param baseDamage
     *            base damage of weapon
     * @param attack
     *            weapon attack
     * @param criticalChance
     *            change for critical hit
     * @param strenghtBonusStep
     *            how fast weapon damage increases with
     *            increase of strength, calculated from
     *            equation: damage +
     *            floor(actor_strength/strenghtBonusStep)
     * @param agilityBonusStep
     *            how fast weapon damage increases with
     *            increase of agility, calculated from
     *            equation: damage +
     *            floor(actor_agility/agilityBonusStep)
     * @param willpowerBonusStep
     *            how fast weapon damage increases with
     *            increase of willpower, calculated from
     *            equation: damage +
     *            floor(actor_willpower/willpowerBonusStep)
     * @param finesseBonusStep
     *            how fast weapon damage increases with
     *            increase of finesse, calculated from
     *            equation: damage +
     *            floor(actor_finesse/finesseBonusStep)
     * @param strenghtBonusMin
     *            minimal strength needed to receive bonus
     * @param agilityBonusMin
     *            minimal agility needed to receive bonus
     * @param willpowerBonusMin
     *            minimal willpower needed to receive bonus
     * @param finesseBonusMin
     *            minimal finesse needed to receive bonus
     * @param strenghtBonusMax
     *            maximal strength value from which bonus
     *            will be calculated
     * @param agilityBonusMax
     *            maximal agility value from which bonus
     *            will be calculated
     * @param willpowerBonusMax
     *            maximal willpower value from which bonus
     *            will be calculated
     * @param finesseBonusMax
     *            maximal finesse value from which bonus
     *            will be calculated
     */
    public WeaponAttack(DamageType damageType,
                        short range,
                        short cooldown,
                        short baseDamage,
                        short attack,
                        short criticalChance,
                        short strenghtBonusStep,
                        short agilityBonusStep,
                        short willpowerBonusStep,
                        short finesseBonusStep,
                        short strenghtBonusMin,
                        short agilityBonusMin,
                        short willpowerBonusMin,
                        short finesseBonusMin,
                        short strenghtBonusMax,
                        short agilityBonusMax,
                        short willpowerBonusMax,
                        short finesseBonusMax)
    {
        super();
        this.damageType = damageType;
        this.range = range;
        this.cooldown = cooldown;
        this.baseDamage = baseDamage;
        this.attack = attack;
        this.criticalChance = criticalChance;
        this.strenghtBonusStep = strenghtBonusStep;
        this.agilityBonusStep = agilityBonusStep;
        this.willpowerBonusStep = willpowerBonusStep;
        this.finesseBonusStep = finesseBonusStep;
        this.strenghtBonusMin = strenghtBonusMin;
        this.agilityBonusMin = agilityBonusMin;
        this.willpowerBonusMin = willpowerBonusMin;
        this.finesseBonusMin = finesseBonusMin;
        this.strenghtBonusMax = strenghtBonusMax;
        this.agilityBonusMax = agilityBonusMax;
        this.willpowerBonusMax = willpowerBonusMax;
        this.finesseBonusMax = finesseBonusMax;
    }

    /**
     * Returns damageType.
     * @return damageType
     */
    public DamageType getDamageType()
    {
        return damageType;
    }

    /**
     * Returns range.
     * @return range
     */
    public short getRange()
    {
        return range;
    }

    /**
     * Returns cooldown in turns.
     * @return cooldown
     */
    public short getCooldown()
    {
        return cooldown;
    }

    /**
     * Returns baseDamage.
     * @return baseDamage
     */
    public short getBaseDamage()
    {
        return baseDamage;
    }

    /**
     * Returns strenghtBonusMin.
     * @return strenghtBonusMin
     */
    public short getStrenghtBonusMin()
    {
        return strenghtBonusMin;
    }

    /**
     * Returns agilityBonusMin.
     * @return agilityBonusMin
     */
    public short getAgilityBonusMin()
    {
        return agilityBonusMin;
    }

    /**
     * Returns willpowerBonusMin.
     * @return willpowerBonusMin
     */
    public short getWillpowerBonusMin()
    {
        return willpowerBonusMin;
    }

    /**
     * Returns finesseBonusMin.
     * @return finesseBonusMin
     */
    public short getFinesseBonusMin()
    {
        return finesseBonusMin;
    }

    /**
     * Returns strenghtBonusMax.
     * @return strenghtBonusMax
     */
    public short getStrenghtBonusMax()
    {
        return strenghtBonusMax;
    }

    /**
     * Returns agilityBonusMax.
     * @return agilityBonusMax
     */
    public short getAgilityBonusMax()
    {
        return agilityBonusMax;
    }

    /**
     * Returns willpowerBonusMax.
     * @return willpowerBonusMax
     */
    public short getWillpowerBonusMax()
    {
        return willpowerBonusMax;
    }

    /**
     * Returns finesseBonusMax.
     * @return finesseBonusMax
     */
    public short getFinesseBonusMax()
    {
        return finesseBonusMax;
    }

    /**
     * Returns strenghtBonusStep.
     * @return strenghtBonusStep
     */
    public short getStrenghtBonusStep()
    {
        return strenghtBonusStep;
    }

    /**
     * Returns agilityBonusStep.
     * @return agilityBonusStep
     */
    public short getAgilityBonusStep()
    {
        return agilityBonusStep;
    }

    /**
     * Returns willpowerBonusStep.
     * @return willpowerBonusStep
     */
    public short getWillpowerBonusStep()
    {
        return willpowerBonusStep;
    }

    /**
     * Returns finesseBonusStep.
     * @return finesseBonusStep
     */
    public short getFinesseBonusStep()
    {
        return finesseBonusStep;
    }

    /**
     * Returns attack.
     * @return attack
     */
    public short getAttack()
    {
        return attack;
    }

    /**
     * Sets new value of baseDamage.
     * @param baseDamage
     *            the baseDamage to set
     */
    public void setBaseDamage(short baseDamage)
    {
        this.baseDamage = baseDamage;
    }

    /**
     * Returns criticalChance.
     * @return criticalChance
     */
    public short getCriticalChance()
    {
        return criticalChance;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        StringBuffer out = new StringBuffer();
        String newline = System.getProperty("line.separator");

        out.append("damage type: ").append(damageType).append(newline);
        out.append("base damage: ").append(baseDamage).append(newline);
        out.append("attack: ").append(attack).append(newline);
        out.append("critical: ").append(criticalChance).append(newline);
        if (agilityBonusStep != 0)
        {
            out.append("agilityBonusStep: ")
               .append(agilityBonusStep)
               .append(newline);
        }
        if (strenghtBonusStep != 0)
        {
            out.append("strenghtBonusStep: ")
               .append(strenghtBonusStep)
               .append(newline);
        }
        if (finesseBonusStep != 0)
        {
            out.append("finesseBonusStep: ")
               .append(finesseBonusStep)
               .append(newline);
        }
        if (willpowerBonusStep != 0)
        {
            out.append("willpowerBonusStep: ")
               .append(willpowerBonusStep)
               .append(newline);
        }
        if (agilityBonusMin != 0)
        {
            out.append("agilityBonusMin: ")
               .append(agilityBonusMin)
               .append(newline);
        }
        if (strenghtBonusMin != 0)
        {
            out.append("strenghtBonusMin: ")
               .append(strenghtBonusMin)
               .append(newline);
        }
        if (finesseBonusMin != 0)
        {
            out.append("finesseBonusMin: ")
               .append(finesseBonusMin)
               .append(newline);
        }
        if (willpowerBonusMin != 0)
        {
            out.append("willpowerBonusMin: ")
               .append(willpowerBonusMin)
               .append(newline);
        }
        if (agilityBonusMax != 0)
        {
            out.append("agilityBonusMax: ")
               .append(agilityBonusMax)
               .append(newline);
        }
        if (strenghtBonusMax != 0)
        {
            out.append("strenghtBonusMax: ")
               .append(strenghtBonusMax)
               .append(newline);
        }
        if (finesseBonusMax != 0)
        {
            out.append("finesseBonusMax: ")
               .append(finesseBonusMax)
               .append(newline);
        }
        if (willpowerBonusMax != 0)
        {
            out.append("willpowerBonusMax: ")
               .append(willpowerBonusMax)
               .append(newline);
        }

        return out.toString();
    }

}
