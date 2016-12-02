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
package pl.org.minions.stigma.game.item.type.data;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import pl.org.minions.stigma.databases.parsers.Parsable;
import pl.org.minions.stigma.databases.xml.Converter;
import pl.org.minions.stigma.game.item.type.OtherType;
import pl.org.minions.utils.Version;

/**
 * Class representing XML data for non-wearable item type.
 */
@XmlRootElement(name = "item")
@XmlType(propOrder = {})
public class OtherTypeData implements Parsable
{
    /**
     * Data converter between ArmorType and ArmorTypeData.
     */
    public static class DataConverter implements
                                     Converter<OtherType, OtherTypeData>
    {
        /** {@inheritDoc} */
        @Override
        public OtherTypeData buildData(OtherType object)
        {
            return new OtherTypeData(object.getId(),
                                     object.getWeight(),
                                     object.getValue(),
                                     object.getName(),
                                     object.getDescription(),
                                     object.getOnGroundIcon(),
                                     object.getInventoryIcon());
        }

        /** {@inheritDoc} */
        @Override
        public OtherType buildObject(OtherTypeData data)
        {
            return new OtherType(data.getId(),
                                 data.getWeight(),
                                 data.getValue(),
                                 data.getName(),
                                 data.getDescription(),
                                 data.getOnGroundIcon(),
                                 data.getInventoryIcon());
        }
    }

    private short id;
    private short weight;
    private int value;
    private String name;
    private String description;
    private String onGroundIcon;
    private String inventoryIcon;
    private String version;

    /**
     * Constructor used by JAXB.
     */
    public OtherTypeData()
    {
        super();
        this.id = -1;
        this.weight = 0;
        this.value = 0;
        this.name = null;
        this.description = null;
        this.inventoryIcon = null;
        this.onGroundIcon = null;
    }

    /**
     * Default constructor for wearable item type.
     * @param id
     *            id of item type
     * @param weight
     *            base weight of item
     * @param value
     *            base value of item
     * @param name
     *            base name of item
     * @param description
     *            base description of item type
     * @param onGroundIcon
     *            path to icon representing item laying on
     *            ground
     * @param inventoryIcon
     *            path to icon representing item in
     *            inventory
     */
    public OtherTypeData(short id,
                         short weight,
                         int value,
                         String name,
                         String description,
                         String onGroundIcon,
                         String inventoryIcon)
    {
        super();
        this.id = id;
        this.weight = weight;
        this.value = value;
        this.name = name;
        this.description = description;
        this.onGroundIcon = onGroundIcon;
        this.inventoryIcon = inventoryIcon;
        this.version = Version.FULL_VERSION;
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
        return id != -1 && name != null && !name.isEmpty();
    }

    /**
     * Returns id.
     * @return id
     */
    public short getId()
    {
        return id;
    }

    /**
     * Returns weight.
     * @return weight
     */
    public short getWeight()
    {
        return weight;
    }

    /**
     * Returns value.
     * @return value
     */
    public int getValue()
    {
        return value;
    }

    /**
     * Returns name.
     * @return name
     */
    public String getName()
    {
        return name;
    }

    /**
     * Returns description.
     * @return description
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * Sets new value of description.
     * @param description
     *            the description to set
     */
    @XmlElement(required = false)
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * Sets new value of version.
     * @param version
     *            the version to set
     */
    @XmlAttribute(required = true)
    public void setVersion(String version)
    {
        this.version = version;
    }

    /**
     * Sets new value of name.
     * @param name
     *            the name to set
     */
    @XmlElement(required = true)
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Sets new value of value.
     * @param value
     *            the value to set
     */
    @XmlAttribute(required = false)
    public void setValue(int value)
    {
        this.value = value;
    }

    /**
     * Sets new value of weight.
     * @param weight
     *            the weight to set
     */
    @XmlAttribute(required = false)
    public void setWeight(short weight)
    {
        this.weight = weight;
    }

    /**
     * Sets new value of id.
     * @param id
     *            the id to set
     */
    @XmlAttribute(required = true)
    public void setId(short id)
    {
        this.id = id;
    }

    /**
     * Returns onGroundIcon.
     * @return onGroundIcon
     */
    public String getOnGroundIcon()
    {
        return onGroundIcon;
    }

    /**
     * Sets new value of onGroundIcon.
     * @param onGroundIcon
     *            the onGroundIcon to set
     */
    @XmlElement(required = false)
    public void setOnGroundIcon(String onGroundIcon)
    {
        this.onGroundIcon = onGroundIcon;
    }

    /**
     * Returns inventoryIcon.
     * @return inventoryIcon
     */
    public String getInventoryIcon()
    {
        return inventoryIcon;
    }

    /**
     * Sets new value of inventoryIcon.
     * @param inventoryIcon
     *            the inventoryIcon to set
     */
    @XmlElement(required = false)
    public void setInventoryIcon(String inventoryIcon)
    {
        this.inventoryIcon = inventoryIcon;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        StringBuffer out = new StringBuffer();
        String newline = System.getProperty("line.separator");

        out.append("id: ").append(id).append(newline);
        out.append("name: ").append(name).append(newline);
        out.append("description: ").append(description).append(newline);
        out.append("onGroundIcon: ").append(onGroundIcon).append(newline);
        out.append("inventoryIcon: ").append(inventoryIcon).append(newline);
        out.append("weight: ").append(weight).append(newline);
        out.append("value: ").append(value).append(newline);

        return out.toString();
    }
}
