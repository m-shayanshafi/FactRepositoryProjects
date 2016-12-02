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

import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import pl.org.minions.stigma.databases.parsers.Parsable;
import pl.org.minions.stigma.databases.xml.Converter;
import pl.org.minions.stigma.game.actor.DamageType;
import pl.org.minions.stigma.game.actor.Resistance;
import pl.org.minions.stigma.game.item.LogicalSlotType;
import pl.org.minions.stigma.game.item.type.ArmorType;
import pl.org.minions.utils.Version;

/**
 * Class representing XML data for Armor item type.
 */
@XmlRootElement(name = "armor")
@XmlType(propOrder = {})
public class ArmorTypeData implements Parsable
{
    /**
     * Data converter between ArmorType and ArmorTypeData.
     */
    public static class DataConverter implements
                                     Converter<ArmorType, ArmorTypeData>
    {
        /** {@inheritDoc} */
        @Override
        public ArmorTypeData buildData(ArmorType object)
        {
            return new ArmorTypeData(object.getId(),
                                     object.getWeight(),
                                     object.getValue(),
                                     object.getName(),
                                     object.getDescription(),
                                     object.getOnGroundIcon(),
                                     object.getInventoryIcon(),
                                     object.getRequiredStrength(),
                                     object.getRequiredAgility(),
                                     object.getRequiredWillpower(),
                                     object.getRequiredFinesse(),
                                     object.getEquipementSlot(),
                                     object.getRequiredProficiency(),
                                     object.getResistanceMap(),
                                     object.getBaseEffects(),
                                     object.getBaseModifiers());
        }

        /** {@inheritDoc} */
        @Override
        public ArmorType buildObject(ArmorTypeData data)
        {
            return new ArmorType(data.getId(),
                                 data.getWeight() == null ? (short) 0
                                                         : data.getWeight(),
                                 data.getValue() == null ? 0 : data.getValue(),
                                 data.getName(),
                                 data.getDescription(),
                                 data.getOnGroundIcon(),
                                 data.getInventoryIcon(),
                                 data.getRequiredStrength() == null ? 0
                                                                   : data.getRequiredStrength(),
                                 data.getRequiredAgility() == null ? 0
                                                                  : data.getRequiredAgility(),
                                 data.getRequiredWillpower() == null ? 0
                                                                    : data.getRequiredWillpower(),
                                 data.getRequiredFinesse() == null ? 0
                                                                  : data.getRequiredFinesse(),
                                 data.getEquipementSlot(),
                                 data.getRequiredProficiencyIds(),
                                 data.getResistances(),
                                 data.getBaseEffectsIds(),
                                 data.baseModifiersIds);
        }
    }

    private short id;
    private Short weight;
    private Integer value;
    private String name;
    private String description;
    private String onGroundIcon;
    private String inventoryIcon;
    private String version = Version.FULL_VERSION;
    private Byte requiredStrength;
    private Byte requiredAgility;
    private Byte requiredWillpower;
    private Byte requiredFinesse;
    private LogicalSlotType equipementSlot;
    private List<Short> requiredProficiencyIds;
    private EnumMap<DamageType, Resistance> resistances;
    private List<Short> baseEffectsIds;
    private List<Short> baseModifiersIds;

    /**
     * Constructor used by JAXB.
     */
    public ArmorTypeData()
    {
        super();
        this.id = -1;
        this.weight = null;
        this.value = null;
        this.name = null;
        this.description = null;
        this.inventoryIcon = null;
        this.onGroundIcon = null;
        this.requiredStrength = null;
        this.requiredAgility = null;
        this.requiredWillpower = null;
        this.requiredFinesse = null;
        this.equipementSlot = LogicalSlotType.NO_SLOT_TYPE;
        this.requiredProficiencyIds = new LinkedList<Short>();
        this.resistances =
                new EnumMap<DamageType, Resistance>(DamageType.class);
        this.baseEffectsIds = new LinkedList<Short>();
        this.baseModifiersIds = new LinkedList<Short>();
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
     * @param requiredStrength
     *            strength required to wear item
     * @param requiredAgility
     *            agility required to wear item
     * @param requiredWillpower
     *            willpower required to wear item
     * @param requiredFinesse
     *            finesse required to wear item
     * @param equipementSlot
     *            slot type in equipment where actor is
     *            allowed to put item
     * @param requiredProficiencyIds
     *            list of proficiencies required to equip
     *            item
     * @param resistances
     *            map containing information about
     *            resistances of armor
     * @param baseEffectsIds
     *            list of effects added to weapon
     * @param baseModifiersIds
     *            list of modifiers added to weapon
     */
    public ArmorTypeData(short id,
                         short weight,
                         int value,
                         String name,
                         String description,
                         String onGroundIcon,
                         String inventoryIcon,
                         byte requiredStrength,
                         byte requiredAgility,
                         byte requiredWillpower,
                         byte requiredFinesse,
                         LogicalSlotType equipementSlot,
                         List<Short> requiredProficiencyIds,
                         EnumMap<DamageType, Resistance> resistances,
                         List<Short> baseEffectsIds,
                         List<Short> baseModifiersIds)
    {
        super();
        this.id = id;
        this.weight = weight;
        this.value = value;
        this.name = name;
        this.description = description;
        this.onGroundIcon = onGroundIcon;
        this.inventoryIcon = inventoryIcon;
        this.requiredStrength = requiredStrength;
        this.requiredAgility = requiredAgility;
        this.requiredWillpower = requiredWillpower;
        this.requiredFinesse = requiredFinesse;
        this.equipementSlot = equipementSlot;
        this.requiredProficiencyIds = requiredProficiencyIds;
        this.resistances = resistances;
        this.baseEffectsIds = baseEffectsIds;
        this.baseModifiersIds = baseModifiersIds;
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
    public Short getWeight()
    {
        return weight;
    }

    /**
     * Returns value.
     * @return value
     */
    public Integer getValue()
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
     * Returns resistances.
     * @return resistances
     */
    public EnumMap<DamageType, Resistance> getResistances()
    {
        return resistances;
    }

    /**
     * Returns requiredStrength.
     * @return requiredStrength
     */
    public Byte getRequiredStrength()
    {
        return requiredStrength;
    }

    /**
     * Returns requiredAgility.
     * @return requiredAgility
     */
    public Byte getRequiredAgility()
    {
        return requiredAgility;
    }

    /**
     * Returns requiredWillpower.
     * @return requiredWillpower
     */
    public Byte getRequiredWillpower()
    {
        return requiredWillpower;
    }

    /**
     * Returns requiredFinesse.
     * @return requiredFinesse
     */
    public Byte getRequiredFinesse()
    {
        return requiredFinesse;
    }

    /**
     * Returns equipementSlot.
     * @return equipementSlot
     */
    public LogicalSlotType getEquipementSlot()
    {
        return equipementSlot;
    }

    /**
     * Returns requiredProficiencyIds.
     * @return requiredProficiencyIds
     */
    public List<Short> getRequiredProficiencyIds()
    {
        return requiredProficiencyIds;
    }

    /**
     * Returns baseEffectsIds.
     * @return baseEffectsIds
     */
    public List<Short> getBaseEffectsIds()
    {
        return baseEffectsIds;
    }

    /**
     * Returns baseModifiersIds.
     * @return baseModifiersIds
     */
    public List<Short> getBaseModifiersIds()
    {
        return baseModifiersIds;
    }

    /**
     * Sets new value of requiredStrength.
     * @param requiredStrength
     *            the requiredStrength to set
     */
    @XmlElement(name = "reqstr", required = false)
    public void setRequiredStrength(Byte requiredStrength)
    {
        this.requiredStrength = requiredStrength;
    }

    /**
     * Sets new value of requiredAgility.
     * @param requiredAgility
     *            the requiredAgility to set
     */
    @XmlElement(name = "reqagl", required = false)
    public void setRequiredAgility(Byte requiredAgility)
    {
        this.requiredAgility = requiredAgility;
    }

    /**
     * Sets new value of requiredWillpower.
     * @param requiredWillpower
     *            the requiredWillpower to set
     */
    @XmlElement(name = "reqwp", required = false)
    public void setRequiredWillpower(Byte requiredWillpower)
    {
        this.requiredWillpower = requiredWillpower;
    }

    /**
     * Sets new value of requiredFinesse.
     * @param requiredFinesse
     *            the requiredFinesse to set
     */
    @XmlElement(name = "reqfn", required = false)
    public void setRequiredFinesse(Byte requiredFinesse)
    {
        this.requiredFinesse = requiredFinesse;
    }

    /**
     * Sets new value of equipementSlot.
     * @param equipementSlot
     *            the equipementSlot to set
     */
    @XmlElement(name = "slot", required = true)
    public void setEquipementSlot(LogicalSlotType equipementSlot)
    {
        this.equipementSlot = equipementSlot;
    }

    /**
     * Sets new value of requiredProficiencyIds.
     * @param requiredProficiencyIds
     *            the requiredProficiencyIds to set
     */
    @XmlElementWrapper(name = "profList")
    @XmlElement(name = "reqprof", required = false)
    public void setRequiredProficiencyIds(List<Short> requiredProficiencyIds)
    {
        this.requiredProficiencyIds = requiredProficiencyIds;
    }

    /**
     * Sets new value of baseEffectsIds.
     * @param baseEffectsIds
     *            the baseEffectsIds to set
     */
    @XmlElementWrapper
    @XmlElements(value = @XmlElement(name = "baseeffect", type = Short.class))
    public void setBaseEffectsIds(List<Short> baseEffectsIds)
    {
        this.baseEffectsIds = baseEffectsIds;
    }

    /**
     * Sets new value of baseModifiersIds.
     * @param baseModifiersIds
     *            the baseModifiersIds to set
     */
    @XmlElementWrapper
    @XmlElements(value = @XmlElement(name = "basemodifier", type = Short.class))
    public void setBaseModifiersIds(List<Short> baseModifiersIds)
    {
        this.baseModifiersIds = baseModifiersIds;
    }

    /**
     * Sets new value of resistances.
     * @param resistances
     *            the resistances to set
     */
    @XmlElementWrapper
    public void setResistances(EnumMap<DamageType, Resistance> resistances)
    {
        this.resistances = resistances;
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
    @XmlElement(required = false)
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
    public void setValue(Integer value)
    {
        this.value = value;
    }

    /**
     * Sets new value of weight.
     * @param weight
     *            the weight to set
     */
    @XmlAttribute(required = false)
    public void setWeight(Short weight)
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
        String space = " ";

        out.append("id: ").append(id).append(newline);
        out.append("name: ").append(name).append(newline);
        out.append("description: ").append(description).append(newline);
        out.append("onGroundIcon: ").append(onGroundIcon).append(newline);
        out.append("inventoryIcon: ").append(inventoryIcon).append(newline);
        out.append("weight: ").append(weight).append(newline);
        out.append("value: ").append(value).append(newline);
        out.append("requiredStrength: ")
           .append(requiredStrength)
           .append(newline);
        out.append("requiredAgility: ").append(requiredAgility).append(newline);
        out.append("requiredWillpower: ")
           .append(requiredWillpower)
           .append(newline);
        out.append("requiredFinesse: ").append(requiredFinesse).append(newline);
        out.append("equipementSlot: ")
           .append(equipementSlot.name())
           .append(newline);

        if (requiredProficiencyIds != null && requiredProficiencyIds.size() > 0)
        {
            out.append("requiredProficiencies:" + newline);
            for (Short p : requiredProficiencyIds)
            {
                out.append(p).append(space);
            }
            out.append(newline);
        }

        if (resistances != null && resistances.size() > 0)
        {
            out.append("resistances:\n");
            for (DamageType type : DamageType.getValuesArray())
            {
                if (resistances.containsKey(type))
                    out.append(type.name())
                       .append(" - (")
                       .append(resistances.get(type).getRelative())
                       .append("/")
                       .append(resistances.get(type).getThreshold())
                       .append(")")
                       .append(newline);
            }
        }

        if (baseEffectsIds != null && baseEffectsIds.size() > 0)
        {
            out.append("base effects:").append(newline);
            for (Short effect : baseEffectsIds)
            {
                out.append(effect.toString()).append(space);
            }
            out.append(newline);
        }

        if (baseModifiersIds != null && baseModifiersIds.size() > 0)
        {
            out.append("base modifiers:").append(newline);
            for (Short modifier : baseModifiersIds)
            {
                out.append(modifier.toString()).append(space);
            }
            out.append(newline);
        }

        return out.toString();
    }
}
