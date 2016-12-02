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
package pl.org.minions.stigma.server.collections;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import pl.org.minions.utils.collections.SuperSet;

/**
 * Special collection for queuing objects to be "polled"
 * when turn number is proper. Every object can be enqueued
 * only once, re-adding it to queue will move it to
 * different turn. All adding, re-adding and polling
 * operations will be made in (average) constant time -
 * collection works as hash map merged with linked list for
 * speeding up operations. Size is NOT optimized.
 * @param <T>
 *            queued object
 */
public class TurnScheduleQueue<T extends TurnSchedulable>
{
    private static class ListElement<T>
    {
        private ListElement<T> next;
        private int turnNumber;
        private Set<T> managerSet = new HashSet<T>();

        public ListElement(int turnNumber)
        {
            this.turnNumber = turnNumber;
        }

        public void add(T manager)
        {
            managerSet.add(manager);
        }

        public void detach()
        {
            next = null;
        }

        public Set<T> getObjectSet()
        {
            return managerSet;
        }

        public int getTurnNumber()
        {
            return turnNumber;
        }

        public ListElement<T> next()
        {
            return next;
        }

        public void remove(T manager)
        {
            managerSet.remove(manager);
        }

        public void setNext(ListElement<T> next)
        {
            this.next = next;
        }
    }

    private ListElement<T> head;
    private ListElement<T> tail;
    private Map<Integer, ListElement<T>> elementMap =
            new HashMap<Integer, ListElement<T>>();

    private ListElement<T> addTurn(int turnNumber)
    {
        if (head == null)
        {
            head = tail = new ListElement<T>(turnNumber);
            elementMap.put(turnNumber, tail);
            return head;
        }

        if (turnNumber < head.getTurnNumber())
        {
            // prepend

            for (int i = head.getTurnNumber() - 1; i >= turnNumber; --i)
            {
                ListElement<T> oldHead = head;
                head = new ListElement<T>(i);
                head.setNext(oldHead);
                elementMap.put(i, head);
            }

            return head;
        }

        // append
        for (int i = tail.getTurnNumber() + 1; i <= turnNumber; ++i)
        {
            ListElement<T> ret = new ListElement<T>(i);
            elementMap.put(i, ret);
            tail.setNext(ret);
            tail = ret;
        }

        return tail;
    }

    /**
     * Enqueues object for given turn number. Removes from
     * previous (if any) turn assignment.
     * @param turnNumber
     *            turn number for which manager should be
     *            enqueued
     * @param object
     *            object to enqueue
     */
    public void enqueue(int turnNumber, T object)
    {
        int lastQueuedTurn = object.getLastQueuedTurn();
        if (lastQueuedTurn > 0)
        {
            ListElement<T> el = elementMap.get(lastQueuedTurn);
            if (el != null)
                el.remove(object);
        }
        ListElement<T> el = elementMap.get(turnNumber);
        if (el == null)
            el = addTurn(turnNumber);
        el.add(object);
        object.setLastQueuedTurn(turnNumber);
    }

    /**
     * Polls for objects scheduled for given turn.
     * @param turnNumber
     *            turn in which objects should be ready
     * @return collection of ready objects
     */
    public Collection<T> poll(int turnNumber)
    {
        if (head == null)
            return null;

        SuperSet<T> ret = null;
        while (head != null && head.getTurnNumber() <= turnNumber)
        {
            if (ret == null)
                ret = new SuperSet<T>();
            ret.addAll(head.getObjectSet());

            ListElement<T> oldHead = head;
            head = head.next();
            oldHead.detach();
            elementMap.remove(oldHead.getTurnNumber());
        }

        if (head == null)
            tail = null;

        return ret;
    }

}
