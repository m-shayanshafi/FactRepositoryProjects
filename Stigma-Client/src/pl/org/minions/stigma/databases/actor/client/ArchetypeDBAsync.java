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
package pl.org.minions.stigma.databases.actor.client;

import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pl.org.minions.stigma.databases.actor.ArchetypeDB;
import pl.org.minions.stigma.databases.actor.wrappers.ArchetypeWrapper;
import pl.org.minions.stigma.databases.xml.client.XmlAsyncDB;
import pl.org.minions.stigma.game.actor.Archetype;
import pl.org.minions.stigma.game.actor.ArchetypeBuilder;
import pl.org.minions.utils.logger.Log;

/**
 * Asynchronous archetype database.
 * @see ArchetypeDB
 * @see XmlAsyncDB
 */
public class ArchetypeDBAsync extends XmlAsyncDB<Archetype, ArchetypeWrapper> implements
                                                                             ArchetypeDB
{
    private Set<Short> visitedArchetypes = new HashSet<Short>();

    private Map<Short, Set<Archetype>> waitingFor =
            new HashMap<Short, Set<Archetype>>();

    private Map<Short, Set<Short>> waitingForReverse =
            new HashMap<Short, Set<Short>>();

    /**
     * Constructs archetype database. It gets URI to the
     * root directory, where files to read are.
     * @param uri
     *            URI to the root directory with files to
     *            read
     */
    public ArchetypeDBAsync(URI uri)
    {
        super(uri,
              ArchetypeWrapper.class,
              new ArchetypeWrapper.DataConverter(),
              false);
    }

    private Archetype accumulate(Archetype archetype)
    {
        if (archetype.isAccumulated())
            return archetype;

        if (visitedArchetypes.contains(archetype.getId()))
        {
            Log.logger.error("Cycle detected with id: " + archetype.getId());
            return null;
        }

        visitedArchetypes.add(archetype.getId());
        List<Archetype> parents = new LinkedList<Archetype>();
        for (short id : archetype.getParentArchetypes())
        {
            Archetype a = getArchetype(id);
            if (a == null)
            {
                // not ready YET - return not complete instance - will be filled in future
                addWaitingFor(id, archetype);
                return archetype;
            }
            a = accumulate(a);
            if (a == null || !a.isAccumulated())
            {
                // not ready YET - return not complete instance - will be filled in future
                addWaitingFor(id, a);
                return archetype;
            }
            parents.add(a);
        }

        ArchetypeBuilder.accumulate(archetype, parents);
        return archetype;
    }

    private void addWaitingFor(short id, Archetype arch)
    {
        Set<Archetype> set = waitingFor.get(id);
        if (set == null)
        {
            set = new HashSet<Archetype>();
            waitingFor.put(id, set);
        }

        set.add(arch);

        Set<Short> setRev = waitingForReverse.get(arch.getId());
        if (setRev == null)
        {
            setRev = new HashSet<Short>();
            waitingForReverse.put(arch.getId(), setRev);
        }

        setRev.add(arch.getId());
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

    /** {@inheritDoc} */
    @Override
    protected void postParsing(Archetype obj)
    {
        accumulate(obj);
        if (obj.isAccumulated())
        {
            Set<Archetype> set = waitingFor.remove(obj.getId());
            if (set != null)
                for (Archetype a : set)
                {
                    Set<Short> setRev = waitingForReverse.get(a.getId());
                    if (setRev != null)
                    {
                        setRev.remove(obj.getId());
                        if (setRev.isEmpty())
                        {
                            waitingForReverse.remove(a.getId());
                            setRev = null;
                        }
                    }

                    // accumulates only when it does not wait for anything other
                    if (setRev == null)
                        accumulate(a);
                }

        }
    }

}
