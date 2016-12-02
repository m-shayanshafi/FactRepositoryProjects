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
package pl.org.minions.stigma.databases.actor.server;

import java.net.URI;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import pl.org.minions.stigma.databases.actor.ArchetypeDB;
import pl.org.minions.stigma.databases.actor.wrappers.ArchetypeWrapper;
import pl.org.minions.stigma.databases.xml.server.XmlSyncDB;
import pl.org.minions.stigma.game.actor.Archetype;
import pl.org.minions.stigma.game.actor.ArchetypeBuilder;
import pl.org.minions.utils.logger.Log;

/**
 * Synchronous archetype database.
 * @see ArchetypeDB
 * @see XmlSyncDB
 */
public class ArchetypeDBSync extends XmlSyncDB<Archetype, ArchetypeWrapper> implements
                                                                           ArchetypeDB
{
    private Set<Short> visitedArchetypes = new HashSet<Short>();

    /**
     * Constructor. Will try to load all archetypes from
     * given directory.
     * @param uri
     *            resources root
     */
    public ArchetypeDBSync(URI uri)
    {
        super(uri,
              ArchetypeWrapper.class,
              new ArchetypeWrapper.DataConverter(),
              true);
        accumulateAll();
    }

    private void accumulate(Archetype archetype)
    {
        if (archetype.isAccumulated())
            return;

        if (visitedArchetypes.contains(archetype.getId()))
        {
            Log.logger.error("Cycle detected with id: " + archetype.getId());
            return;
        }

        visitedArchetypes.add(archetype.getId());
        List<Archetype> parents = new LinkedList<Archetype>();
        for (short id : archetype.getParentArchetypes())
        {
            Archetype a = getArchetype(id);
            if (a == null)
            {
                Log.logger.warn("Skipping null parent id: " + id);
                continue;
            }
            accumulate(a);
            parents.add(a);
        }

        ArchetypeBuilder.accumulate(archetype, parents);
    }

    private void accumulateAll()
    {
        for (Short id : getKeys())
        {
            accumulate(getArchetype(id));
        }
        // no longer needed
        visitedArchetypes = null;
    }

    /** {@inheritDoc} */
    @Override
    public Archetype getArchetype(short id)
    {
        return getById(id);
    }

    /** {@inheritDoc} */
    @Override
    public String getDbDir()
    {
        return ArchetypeDB.DB_DIR;
    }

    /** {@inheritDoc} */
    @Override
    public String getFilePrefix()
    {
        return ArchetypeDB.FILE_PREFIX;
    }
}
