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
package pl.org.minions.stigma.databases.item.wrappers;

import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import pl.org.minions.stigma.databases.xml.SimpleDataWrapper;
import pl.org.minions.stigma.game.item.modifier.ItemModifier;
import pl.org.minions.stigma.game.item.modifier.WeaponModifier;
import pl.org.minions.utils.Version;

/**
 * Class wrapping weapon modifier for simple XML database.
 */
@XmlRootElement
public class WeaponModifierWrapper extends SimpleDataWrapper<WeaponModifier>
{
    public static final int MAX_PRINT_AMOUNT = 10;

    private List<WeaponModifier> types;
    private String version;

    /**
     * Constructor needed by JAXB.
     */
    public WeaponModifierWrapper()
    {
        super();
        version = Version.FULL_VERSION;
        types = new LinkedList<WeaponModifier>();
    }

    /** {@inheritDoc} */
    @Override
    public String getVersion()
    {
        return version;
    }

    /**
     * Returns version.
     * @param version
     *            version of documents
     */
    @XmlAttribute
    public void setVersion(String version)
    {
        this.version = version;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isGood()
    {
        return types != null && !types.isEmpty();
    }

    /** {@inheritDoc} */
    @Override
    @XmlElementWrapper(name = "list")
    @XmlElement(name = "modifier")
    public List<WeaponModifier> getTypes()
    {
        return types;
    }

    /** {@inheritDoc} */
    @Override
    public void setTypes(List<WeaponModifier> types)
    {
        this.types = types;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        StringBuffer out = new StringBuffer();
        String newline = System.getProperty("line.separator");

        out.append("Weapon modifiers:").append(newline);
        if (types.size() <= MAX_PRINT_AMOUNT)
        {
            for (ItemModifier prof : types)
            {
                out.append(prof.toString()).append(newline);
            }
        }
        else
        {
            out.append("There are more than " + MAX_PRINT_AMOUNT
                + " ArmorModifiers");
        }

        return out.toString();
    }
}
