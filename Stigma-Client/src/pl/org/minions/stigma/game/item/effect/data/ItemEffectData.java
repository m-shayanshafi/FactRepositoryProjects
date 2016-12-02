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
package pl.org.minions.stigma.game.item.effect.data;

import javax.xml.bind.annotation.XmlAttribute;

import pl.org.minions.stigma.databases.parsers.Parsable;
import pl.org.minions.stigma.databases.xml.Converter;
import pl.org.minions.stigma.game.item.effect.ItemEffect;
import pl.org.minions.utils.Version;

/**
 * Class representing XML database for item effects.
 */
public class ItemEffectData implements Parsable
{
    /**
     * Data converter between ItemEffect and ItemEffectData.
     */
    public static class DataConverter implements
                                     Converter<ItemEffect, ItemEffectData>
    {
        /** {@inheritDoc} */
        @Override
        public ItemEffectData buildData(ItemEffect object)
        {
            return new ItemEffectData(object.getId());
        }

        /** {@inheritDoc} */
        @Override
        public ItemEffect buildObject(ItemEffectData data)
        {
            return new ItemEffect(data.getId());
        }
    }

    private short id;
    private String version = Version.FULL_VERSION;

    /**
     * Constructor needed by JAXB.
     */
    public ItemEffectData()
    {
        this.id = -1;
    }

    /**
     * Default constructor.
     * @param id
     *            id of effect
     */
    public ItemEffectData(short id)
    {
        super();
        this.id = id;
    }

    /**
     * Returns id.
     * @return id
     */
    public short getId()
    {
        return id;
    }

    /** {@inheritDoc} */
    @Override
    public String getVersion()
    {
        return version;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isGood()
    {
        return id > 0;
    }

    /**
     * Sets new value of id.
     * @param id
     *            the id to set
     */
    @XmlAttribute(name = "id", required = true)
    public void setId(short id)
    {
        this.id = id;
    }

    /**
     * Sets new value of version.
     * @param version
     *            the version to set
     */
    @XmlAttribute(name = "version", required = true)
    public void setVersion(String version)
    {
        this.version = version;
    }
}
