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
package pl.org.minions.stigma.game.actor;

import java.util.LinkedList;
import java.util.List;

import pl.org.minions.stigma.databases.xml.XmlDbElem;

/**
 * Archetype definition. Archetype describes some static
 * parameters of actor, also can have some "parent"
 * archetypes, which will be aggregated with it. Also
 * provides some interface helping storing it in database,
 * and caching accumulated results. Archetypes can be
 * persistent, which means they will be assigned to actor
 * for ever, or not-persistent what means they will modify
 * actor and disappear from it's history.
 */
public final class Archetype extends ArchetypeBase implements XmlDbElem
{
    private short id;
    private List<Short> parentArchetypes = new LinkedList<Short>();
    private boolean accumulated;
    private boolean persistent;
    private boolean modified;

    /**
     * Full constructor.
     * @param id
     *            id of archetype
     * @param name
     *            name of archetype
     */
    public Archetype(short id, String name)
    {
        super(name);
        this.id = id;
    }

    /**
     * Constructor for archetypes with no id.
     * @param name
     *            name of archetype
     */
    public Archetype(String name)
    {
        super(name);
        this.id = 0;
    }

    /** {@inheritDoc} */
    @Override
    public void clearModified()
    {
        this.modified = false;
    }

    /**
     * Gets id of archetype.
     * @return the id
     */
    @Override
    public short getId()
    {
        return id;
    }

    /**
     * Returns list of id of parent archetypes.
     * @return list of id of parent archetypes.
     */
    public List<Short> getParentArchetypes()
    {
        return parentArchetypes;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode()
    {
        return id;
    }

    /**
     * Returns {@code true} when archetype was accumulated
     * and is complete. It can be done only by calling
     * {@link ArchetypeBuilder#accumulate(Archetype, List)}.
     * @return {@code true} when archetype is complete
     */
    public boolean isAccumulated()
    {
        return accumulated;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isModified()
    {
        return modified;
    }

    /**
     * Returns whether or not this archetypes should be
     * added as persistent to actor.
     * @return {@code true} when archetypes is added as
     *         persistent to actor
     */
    public boolean isPersistent()
    {
        return persistent;
    }

    /**
     * Sets {@link #isAccumulated()} option.
     * @param accumulated
     *            new options state
     * @see ArchetypeBuilder#accumulate(Archetype, List)
     */
    void setAccumulated(boolean accumulated)
    {
        this.accumulated = accumulated;
    }

    /**
     * Sets new value of id. Used for unmarshaling.
     * @param id
     *            the id to set
     */
    public void setId(short id)
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
     * Sets {@link #isPersistent()} option.
     * @param persistent
     *            new option state
     * @see ArchetypeBuilder#addArchetypes(Actor,
     *      Archetype...)
     */
    public void setPersistent(boolean persistent)
    {
        this.persistent = persistent;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        // CHECKSTYLE:OFF
        StringBuilder out = new StringBuilder();
        out.append("ID = " + id + "\n");
        out.append(super.toString());

        return out.toString();
        // CHECKSTYLE:ON
    }
}
