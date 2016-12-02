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
package pl.org.minions.stigma.game.map.server;

import java.util.HashMap;
import java.util.Map;

import pl.org.minions.stigma.game.actor.Actor;
import pl.org.minions.utils.logger.Log;

/**
 * This class represents virtual space where dead actors are
 * moved.
 */
public class Purgatory
{
    private Map<Integer, Actor> actorMap;

    /**
     * Constructor.
     */
    public Purgatory()
    {
        actorMap = new HashMap<Integer, Actor>();
    }

    /**
     * Returns map of actors
     * @return map containing Actors
     */
    /*
     * public Map<Integer,Actor> getNPCList() { return actorMap; }
     */

    /**
     * Adds actor to purgatory.
     * @param actor
     *            actor to add
     */
    public void addActor(Actor actor)
    {
        this.actorMap.put(actor.getId(), actor);
        if (Log.isTraceEnabled())
        {
            Log.logger.trace("ADDED: Actor with id: " + actor.getId()
                + "added to hell");
        }
    }

    /**
     * Removes actor from purgatory.
     * @param actor
     *            actor to remove
     */
    public void removeActor(Actor actor)
    {
        this.actorMap.remove(actor.getId());
        if (Log.isTraceEnabled())
        {
            Log.logger.trace("DELETED: Actor with id: " + actor.getId()
                + "removed from hell");
        }
    }
}
