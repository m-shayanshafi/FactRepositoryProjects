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

import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import pl.org.minions.stigma.databases.xml.SimpleDataWrapper;
import pl.org.minions.stigma.game.actor.Proficiency;
import pl.org.minions.utils.Version;

/**
 * Class wrapping Proficiencies.
 */
@XmlRootElement
public class ProficiencyWrapper extends SimpleDataWrapper<Proficiency>
{
    private List<Proficiency> types;
    private String version;

    /**
     * Constructor needed by JAXB.
     */
    public ProficiencyWrapper()
    {
        super();
        version = Version.FULL_VERSION;
        types = new LinkedList<Proficiency>();
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
    @XmlElement(name = "proficiency")
    public List<Proficiency> getTypes()
    {
        return types;
    }

    /** {@inheritDoc} */
    @Override
    public void setTypes(List<Proficiency> types)
    {
        this.types = types;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        StringBuffer out = new StringBuffer();
        String newline = System.getProperty("line.separator");

        out.append("Proficiencies:").append(newline);
        for (Proficiency prof : types)
        {
            out.append(prof.toString()).append(newline);
        }

        return out.toString();
    }
}
