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
package pl.org.minions.utils.collections;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ThreadSafeMap is synchronized wrapper for Map<T,K>. All
 * function that modifies map are synchronized. All
 * collections that are created from map (like set of map's
 * keys) are Thread-Safe.
 * @param <T>
 *            key type stored in collection
 * @param <K>
 *            value type stored in collection
 */
public class ThreadSafeMap<T, K> implements Map<T, K>
{
    private Map<T, K> innerMap = new HashMap<T, K>();
    private AtomicInteger refCounter = new AtomicInteger(0);

    private synchronized void checkForCopy()
    {
        if (refCounter.get() > 0)
        {
            Map<T, K> newMap = new HashMap<T, K>();
            newMap.putAll(innerMap);
            innerMap = newMap;
        }
    }

    /** {@inheritDoc} */
    @Override
    public void clear()
    {
        checkForCopy();
        innerMap.clear();
    }

    /** {@inheritDoc} */
    @Override
    public boolean containsKey(Object key)
    {
        return innerMap.containsKey(key);
    }

    /** {@inheritDoc} */
    @Override
    public boolean containsValue(Object value)
    {
        return innerMap.containsValue(value);
    }

    /**
     * Returned collection is thread-safe. {@inheritDoc}
     */
    @Override
    public ThreadSafeSet<java.util.Map.Entry<T, K>> entrySet()
    {
        refCounter.incrementAndGet();
        return new ThreadSafeSet<java.util.Map.Entry<T, K>>(innerMap.entrySet());
    }

    /** {@inheritDoc} */
    @Override
    public K get(Object key)
    {
        return innerMap.get(key);
    }

    /** {@inheritDoc} */
    @Override
    public boolean isEmpty()
    {
        return innerMap.isEmpty();
    }

    /**
     * Returned collection is thread-safe. {@inheritDoc}
     */
    @Override
    public ThreadSafeSet<T> keySet()
    {
        refCounter.incrementAndGet();
        return new ThreadSafeSet<T>(innerMap.keySet());
    }

    /** {@inheritDoc} */
    @Override
    public K put(T key, K value)
    {
        checkForCopy();
        return innerMap.put(key, value);
    }

    /** {@inheritDoc} */
    @Override
    public void putAll(Map<? extends T, ? extends K> t)
    {
        checkForCopy();
        innerMap.putAll(t);
    }

    /** {@inheritDoc} */
    @Override
    public K remove(Object key)
    {
        checkForCopy();
        return innerMap.remove(key);
    }

    /** {@inheritDoc} */
    @Override
    public int size()
    {
        return innerMap.size();
    }

    /** Returned collection is thread-safe. {@inheritDoc} */
    @Override
    public ThreadSafeCollection<K> values()
    {
        refCounter.incrementAndGet();
        return new ThreadSafeCollection<K>(innerMap.values());
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "ThreadSafe: " + innerMap.toString();
    }
}
