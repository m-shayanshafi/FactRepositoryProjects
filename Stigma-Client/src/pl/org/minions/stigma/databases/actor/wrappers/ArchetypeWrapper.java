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
package pl.org.minions.stigma.databases.actor.wrappers;

import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import pl.org.minions.stigma.databases.parsers.Parsable;
import pl.org.minions.stigma.databases.xml.Converter;
import pl.org.minions.stigma.game.actor.Archetype;
import pl.org.minions.stigma.game.actor.DamageType;
import pl.org.minions.stigma.game.actor.Gender;
import pl.org.minions.stigma.game.actor.Resistance;
import pl.org.minions.utils.Version;
import pl.org.minions.utils.logger.Log;

/**
 * Class which wraps Archetype class to make it's properties
 * nullable.
 */
@XmlRootElement(name = "archetype")
@XmlType(propOrder = {})
public class ArchetypeWrapper implements Parsable
{
    /**
     * Converter between {@link ArchetypeWrapper} and
     * {@link Archetype}.
     */
    public static class DataConverter implements
                                     Converter<Archetype, ArchetypeWrapper>
    {
        /** {@inheritDoc} */
        @Override
        public ArchetypeWrapper buildData(Archetype object)
        {
            return new ArchetypeWrapper(object);
        }

        /** {@inheritDoc} */
        @Override
        public Archetype buildObject(ArchetypeWrapper data)
        {
            return data.archetype;
        }
    }

    private String version;

    private Archetype archetype;

    /**
     * Default constructor.
     */
    public ArchetypeWrapper()
    {
        this.archetype = new Archetype(null);
    }

    /**
     * Wraps given archetype.
     * @param archetype
     *            archetype to wrap.
     */
    public ArchetypeWrapper(Archetype archetype)
    {
        this.archetype = archetype;
        this.version = Version.FULL_VERSION;
    }

    /**
     * Gets agility of archetype.
     * @return the agility
     */
    @XmlElement(name = "agl")
    public Byte getAgility()
    {
        if (archetype == null)
        {
            Log.logger.fatal("Trying to get agility from null.");
            return null;
        }
        if (archetype.getAgility() != 0)
            return new Byte(archetype.getAgility());
        else
            return null;
    }

    /**
     * Returns wrapped archetype.
     * @return wrapped archetype
     */
    public Archetype getArchetype()
    {
        return this.archetype;
    }

    /**
     * Gets finesse of archetype.
     * @return the finesse
     */
    @XmlElement(name = "fns", nillable = true)
    public Byte getFinesse()
    {
        if (archetype == null)
        {
            Log.logger.fatal("Trying to get finesse from null.");
            return null;
        }
        if (archetype.getFinesse() != 0)
            return new Byte(archetype.getFinesse());
        else
            return null;
    }

    /**
     * Gets id of archetype.
     * @return id of archetype
     */
    @XmlAttribute(name = "id")
    public short getId()
    {
        if (archetype == null)
        {
            Log.logger.fatal("Trying to get id from null.");
            return 0;
        }
        return archetype.getId();
    }

    /**
     * Gets name of archetype.
     * @return name of archetype
     */
    @XmlAttribute(name = "name", required = true)
    public String getName()
    {
        if (archetype == null)
        {
            Log.logger.fatal("Trying to get name from null.");
            return null;
        }
        return archetype.getName();
    }

    /**
     * Return list of parents.
     * @return list of parents.
     */
    @XmlList
    public List<Short> getParentArchetypes()
    {
        if (archetype == null)
        {
            Log.logger.fatal("Trying to get parent archetypes from null");
            return null;
        }
        else
            return this.archetype.getParentArchetypes();
    }

    /**
     * Gets map of resistances.
     * @return map (resistance per damage type) of
     *         resistances
     */
    @XmlElementWrapper
    public EnumMap<DamageType, Resistance> getResMap()
    {
        if (archetype == null)
        {
            Log.logger.fatal("Trying to get resistances from null.");
            return null;
        }
        return archetype.getResistanceMap();
    }

    /**
     * Gets strength of archetype.
     * @return the strength
     */
    @XmlElement(name = "str")
    public Byte getStrength()
    {
        if (archetype == null)
        {
            Log.logger.fatal("Trying to get strength from null.");
            return null;
        }
        if (archetype.getStrength() != 0)
            return new Byte(archetype.getStrength());
        else
            return null;
    }

    /** {@inheritDoc} */
    @Override
    @XmlAttribute(name = "ver")
    public String getVersion()
    {
        return version;
    }

    /**
     * Gets willpower of archetype.
     * @return the willpower
     */
    @XmlElement(name = "wlp")
    public Byte getWillpower()
    {
        if (archetype == null)
        {
            Log.logger.fatal("Trying to get willpower from null.");
            return null;
        }
        if (archetype.getWillpower() != 0)
            return new Byte(archetype.getWillpower());
        else
            return null;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isGood()
    {
        return archetype != null && archetype.getId() != 0
            && archetype.getName() != null && !archetype.getName().isEmpty();
    }

    /**
     * Gets "persistent" option from archetype.
     * @return "persistent" option of archetype
     */
    @XmlAttribute(name = "persistent", required = false)
    public boolean isPersistent()
    {
        if (archetype == null)
        {
            Log.logger.fatal("Trying to get persistent from null.");
            return false;
        }
        return archetype.isPersistent();
    }

    /**
     * Sets agility of archetype.
     * @param agility
     *            the agility to set
     */
    public void setAgility(Byte agility)
    {
        if (archetype == null)
        {
            Log.logger.fatal("Trying to set agility to null.");
        }
        else
            this.archetype.setAgility(agility.byteValue());
    }

    /**
     * Sets finesse of archetype.
     * @param finesse
     *            the finesse to set
     */
    public void setFinesse(Byte finesse)
    {
        if (archetype == null)
        {
            Log.logger.fatal("Trying to set finesse to null.");
        }
        else
            this.archetype.setFinesse(finesse.byteValue());
    }

    /**
     * Sets id of archetype.
     * @param id
     *            id to set
     */
    public void setId(short id)
    {
        if (archetype == null)
        {
            Log.logger.fatal("Trying to set id to null.");
            return;
        }
        archetype.setId(id);
    }

    /**
     * Sets name of archetype.
     * @param name
     *            archetype name
     */
    public void setName(String name)
    {
        if (archetype == null)
        {
            Log.logger.fatal("Trying to set name to null.");
            return;
        }
        archetype.setName(name);
    }

    /**
     * Sets "persistent" option of archetype.
     * @param persistent
     *            new option state
     */
    public void setPersistent(boolean persistent)
    {
        if (archetype == null)
        {
            Log.logger.fatal("Trying to set persistent to null.");
            return;
        }
        archetype.setPersistent(persistent);
    }

    /**
     * Sets map of resistances.
     * @param resistances
     *            map (resistance per damage type) of
     *            resistances
     */
    public void setResMap(EnumMap<DamageType, Resistance> resistances)
    {
        if (archetype == null)
        {
            Log.logger.fatal("Trying to set resistances to null.");
        }
        else
            this.archetype.setResistanceMap(resistances);
    }

    /**
     * Sets strength of archetype.
     * @param strength
     *            the strength to set
     */
    public void setStrength(Byte strength)
    {
        if (archetype == null)
        {
            Log.logger.fatal("Trying to set strength to null.");
        }
        else
            this.archetype.setStrength(strength.byteValue());
    }

    /**
     * Sets database version.
     * @param ver
     *            version of database
     */
    public void setVersion(String ver)
    {
        this.version = ver;
    }

    /**
     * Sets willpower of archetype.
     * @param willpower
     *            the willpower to set
     */
    public void setWillpower(Byte willpower)
    {
        if (archetype == null)
        {
            Log.logger.fatal("Trying to set willpower to null.");
        }
        else
            this.archetype.setWillpower(willpower.byteValue());
    }

    /**
     * Returns proficiencies.
     * @return proficiencies
     */
    @XmlList
    public List<Short> getProficiencies()
    {
        if (archetype == null)
        {
            Log.logger.fatal("Trying to get proficiencies from null.");
            return null;
        }

        return new LinkedList<Short>(this.archetype.getProficiencies());
    }

    /**
     * Sets proficiencies.
     * @param proficiencies
     *            proficiencies
     */
    public void setProficiencies(List<Short> proficiencies)
    {
        if (archetype == null)
        {
            Log.logger.fatal("Trying to set proficiencies to null.");
        }
        else
            this.archetype.getProficiencies().addAll(proficiencies);
    }

    /**
     * Returns gender.
     * @return gender
     */
    @XmlElement(required = false, nillable = true)
    public Gender getGender()
    {
        if (archetype == null)
        {
            Log.logger.fatal("Trying to get gender form null");
            return null;
        }
        return archetype.getGender();
    }

    /**
     * Sets gender.
     * @param gender
     *            new gender
     */
    public void setGender(Gender gender)
    {
        if (archetype == null)
            Log.logger.fatal("Trying to set gender from null");
        else
            archetype.setGender(gender);
    }

    /**
     * Sets new value of shortDescription.
     * @param shortDescription
     *            the shortDescription to set
     */
    public void setShortDescription(String shortDescription)
    {
        if (archetype == null)
            Log.logger.fatal("Trying to set short description to null");
        else
            archetype.setShortDescription(shortDescription);
    }

    /**
     * Returns shortDescription.
     * @return shortDescription
     */
    @XmlElement(nillable = true, required = false)
    public final String getShortDescription()
    {
        if (archetype == null)
        {
            Log.logger.fatal("Trying to get short description from null");
            return null;
        }
        return archetype.getShortDescription();
    }

    /**
     * Sets new value of description.
     * @param description
     *            the description to set
     */
    public final void setDescription(String description)
    {
        if (archetype == null)
            Log.logger.fatal("Trying to set description to null");
        else
            archetype.setDescription(description);
    }

    /**
     * Returns description.
     * @return description
     */
    @XmlElement(nillable = true, required = false)
    public final String getDescription()
    {
        if (archetype == null)
        {
            Log.logger.fatal("Trying to get description from null");
            return null;
        }
        return archetype.getDescription();
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        if (archetype != null)
            return archetype.toString();
        else
            return null;
    }
}
