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
package pl.org.minions.stigma.server.npc;

import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlList;

import pl.org.minions.stigma.databases.xml.Modifiable;
import pl.org.minions.stigma.server.ai.AiDescription;

/**
 * Class describing NPC parameters.
 */
public class NpcDescription implements Modifiable
{
    private String name;
    private AiDescription aiDescription = new AiDescription();
    private List<Short> archetypesList = new LinkedList<Short>();
    private boolean modified;

    /**
     * Constructor.
     */
    public NpcDescription()
    {
    }

    /** {@inheritDoc} */
    @Override
    public void clearModified()
    {
        this.modified = false;
        aiDescription.clearModified();
    }

    /**
     * Returns AI script description used by this NPC.
     * @return AI script description
     */
    @XmlElement(name = "aiScript", required = false, nillable = true)
    public AiDescription getAiDescription()
    {
        return aiDescription;
    }

    /**
     * List of archetypes used by this NPC. May be empty -
     * simple "almost dead" critter will be created.
     * @return list of archetypes identifiers used by this
     *         NPC
     */
    @XmlList
    @XmlElement(name = "archetypes", required = true, nillable = false)
    public List<Short> getArchetypes()
    {
        return archetypesList;
    }

    /**
     * Returns name of described NPC.
     * @return name of described NPC.
     */
    public String getName()
    {
        return name;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isModified()
    {
        return modified || aiDescription.isModified();
    }

    /**
     * Sets new AI script description.
     * @param aiDescription
     *            new AI script description.
     */
    public void setAiDescription(AiDescription aiDescription)
    {
        this.aiDescription = aiDescription;
    }

    /** {@inheritDoc} */
    @Override
    public void setModified()
    {
        this.modified = true;
    }

    /**
     * Sets new name of described NPC.
     * @param name
     *            new name
     */
    @XmlAttribute(name = "name", required = false)
    public void setName(String name)
    {
        this.name = name;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "[NpcDescription: name=" + name + " aiDescription="
            + aiDescription + "]";
    }
}
