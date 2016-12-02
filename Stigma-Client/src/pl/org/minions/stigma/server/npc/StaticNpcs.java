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

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import pl.org.minions.stigma.databases.parsers.Parsable;
import pl.org.minions.stigma.databases.xml.XmlDbElem;
import pl.org.minions.stigma.game.map.MapInstance;
import pl.org.minions.stigma.game.map.MapType;
import pl.org.minions.stigma.globals.Position;
import pl.org.minions.stigma.server.managers.ChatManager;
import pl.org.minions.stigma.server.managers.WorldManager;
import pl.org.minions.utils.Version;
import pl.org.minions.utils.logger.Log;

/**
 * Represents static NPC layer over map. NPC will be put on
 * given position. This layer type is rather not very
 * reusable.
 */
@XmlRootElement(name = "staticNpcs")
@XmlType(propOrder = {})
public class StaticNpcs implements XmlDbElem, Parsable, NpcGenerator
{
    private String version = Version.FULL_VERSION;
    private short id;

    private Map<Position, NpcDescription> npcMap =
            new HashMap<Position, NpcDescription>();

    private boolean modified;

    /**
     * Constructor.
     */
    public StaticNpcs()
    {
    }

    /**
     * Adds NPC on given position (overwrites existing).
     * @param p
     *            position to put NPC on
     * @param desc
     *            description of NPC
     */
    public void addNpc(Position p, NpcDescription desc)
    {
        npcMap.put(p, desc);
    }

    /** {@inheritDoc} */
    @Override
    public void clearModified()
    {
        this.modified = false;
        for (NpcDescription desc : npcMap.values())
            desc.clearModified();

    }

    /** {@inheritDoc} */
    @Override
    public final short getId()
    {
        return id;
    }

    /**
     * Returns NPC from given position, or {@code null} if
     * no such.
     * @param p
     *            requested position
     * @return NPC description from given position
     */
    public NpcDescription getNpc(Position p)
    {
        return npcMap.get(p);
    }

    /**
     * Returns map of NPCs positions to descriptions. Needed
     * for JAXB.
     * @return map of NPCS
     */
    public Map<Position, NpcDescription> getNpcMap()
    {
        return npcMap;
    }

    /** {@inheritDoc} */
    @Override
    public final String getVersion()
    {
        return version;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isGood()
    {
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isModified()
    {
        if (modified)
            return true;
        for (NpcDescription desc : npcMap.values())
            if (desc.isModified())
                return true;
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean putNpcs(WorldManager worldManager,
                           ChatManager chatManager,
                           MapInstance m)
    {
        MapType t = m.getType();
        if (t == null)
        {
            Log.logger.error("Not fully initialized map passed to StaticNpcLayer");
            return false;
        }

        for (Map.Entry<Position, NpcDescription> entry : npcMap.entrySet())
        {
            Position p = entry.getKey();
            if (t.getTile(p).isPassable())
                NpcFactory.buildNpc(worldManager,
                                    chatManager,
                                    entry.getValue(),
                                    t.getId(),
                                    m.getInstanceNo(),
                                    p);
            else
                Log.logger.warn("Bad position for NPC: " + p + " on map: "
                    + t.getId());
        }
        return true;
    }

    /**
     * Removes NPC from given position.
     * @param p
     *            requested position
     */
    public void removeNpc(Position p)
    {
        npcMap.remove(p);
    }

    /**
     * Sets id of this layer.
     * @param id
     *            new id
     */
    @XmlAttribute(name = "id", required = true)
    public final void setId(short id)
    {
        this.id = id;
    }

    /** {@inheritDoc} */
    @Override
    public void setModified()
    {
        this.modified = true;
    }

    /**
     * Sets new of NPCs positions to descriptions. Needed
     * for JAXB.
     * @param npcMap
     *            new map
     */
    public void setNpcMap(Map<Position, NpcDescription> npcMap)
    {
        this.npcMap = npcMap;
    }

    /**
     * Sets version - needed for verifying parsing.
     * @param version
     *            new version
     */
    @XmlAttribute(name = "version", required = true)
    public final void setVersion(String version)
    {
        this.version = version;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        String str = "[ StaticNpcLayer:\n";
        for (Map.Entry<Position, NpcDescription> entry : npcMap.entrySet())
        {
            str += entry.getKey();
            str += " -> ";
            str += entry.getValue();
            str += "\n";
        }
        return str + "]";
    }

}
