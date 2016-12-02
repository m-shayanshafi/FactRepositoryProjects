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
package pl.org.minions.stigma.server.managers;

import java.util.HashMap;
import java.util.Map;

import pl.org.minions.stigma.game.command.Command;

// NOT synchronized (should be used by one thread only)
/**
 * Special collection of {@link ActorManager ActorManagers}
 * used by {@link WorldManager}. It provides some
 * functionality of map (for getting proper manager for
 * given id) and list (for iterating for next available
 * command). This class is not thread safe, and should be
 * used only by one thread. Also it may not provide any
 * useful functionality as collection - it should be used
 * only by {@link WorldManager}.
 */
public class ActorManagerCollection
{
    private static class ListElement
    {
        private ListElement next;
        private ListElement prev;
        private ActorManager value;

        public ListElement(ActorManager value,
                           ListElement next,
                           ListElement prev)
        {
            this.next = next;
            this.prev = prev;
            this.value = value;
        }

        public ListElement next()
        {
            return next;
        }

        public void remove()
        {
            if (prev != null)
            {
                prev.setNext(next);
            }
            if (next != null)
            {
                next.setPrev(prev);
            }
        }

        public void setNext(ListElement next)
        {
            this.next = next;
        }

        public void setPrev(ListElement prev)
        {
            this.prev = prev;
        }

        public ActorManager value()
        {
            return value;
        }
    }

    private ListElement root;
    private ListElement current;
    private ListElement start;

    private Map<Integer, ListElement> actorMap =
            new HashMap<Integer, ListElement>();

    /**
     * Adds actor manager to collection. This manager should
     * not be already in collection.
     * @param a
     *            actor manager to add
     */
    public void add(ActorManager a)
    {
        ListElement el = new ListElement(a, root, null);
        root = el;
        actorMap.put(a.getActor().getId(), el);
    }

    /**
     * Returns actor manager for given actor's id. Runs in
     * constant-time.
     * @param id
     *            id of actor which manager should be
     *            returned.
     * @return proper manager for given actor's id
     */
    public ActorManager get(int id)
    {
        ListElement el = actorMap.get(id);
        if (el == null)
            return null;
        return el.value();
    }

    /**
     * Marks currently selected for polling actor manager as
     * start of poll. If {@code poll} function reaches again
     * this actor it will stop. Should
     * be called at begging of <i>turn</i> in world manager.
     * @see #poll()
     */
    public void markPollStart()
    {
        if (current == null)
            current = root;
        start = current;
    }

    /**
     * Polls actors collection for next available command.
     * It treats collection as list. This function tries to
     * treat actors fairly, so it remembers which actor was
     * last polled, and never polls twice the same actor in
     * one <i>turn</i> (which are distinguished by
     * subsequent calls to {@link #markPollStart()}). If
     * polling in one turn would not manage to poll every
     * actor in list, next <i>turn</i> polling will start
     * from first not polled actor.<br/>
     * Method returns {@code null} if there are no more
     * available commands in whole collection. This means
     * any of reasons: every actor was already polled and/or
     * any unpolled actor has nothing to say.
     * @return next available command from managers stored
     *         in collection, or {@code null} if no such is
     *         available.
     */
    public Command poll()
    {
        if (current == null || start == null)
            return null;

        Command ret = null;
        do
        {
            ret = current.value().poll();
            current = current.next();
            if (current == start)
            {
                start = null;
                break;
            }
        } while (ret == null && current != null);

        return ret;
    }

    /**
     * Removes given actor manager from collection.
     * @param a
     *            actor manager to remove
     */
    public void remove(ActorManager a)
    {
        ListElement el = actorMap.remove(a.getActor().getId());
        if (el != null)
        {
            el.remove();
            if (current == el)
                current = el.next();
        }
    }
}
