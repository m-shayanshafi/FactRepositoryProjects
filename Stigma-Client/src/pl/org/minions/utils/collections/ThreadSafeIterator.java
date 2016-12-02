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

import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Thread-safe iterator. Does not allow to modify
 * collection.
 * @param <Y>
 *            type stored in collection
 */
public class ThreadSafeIterator<Y> implements Iterator<Y>
{
    private Iterator<Y> innerIterator;
    private AtomicInteger referenceCouter;

    /**
     * Default operator, hidden to scope of package -
     * Iterator creation by function iterator() of specific
     * collection.
     * @param collection
     *            collection for which iterator will be
     *            created
     */
    ThreadSafeIterator(ThreadSafeCollection<Y> collection)
    {
        this.innerIterator = collection.unsafeIterator();
        this.referenceCouter =
                collection.getInnerCollection().getReferenceCounterReference();
        checkHasNext();
    }

    /**
     * Default operator, hidden to scope of package -
     * Iterator creation by function iterator() of specific
     * collection.
     * @param set
     *            collection for which iterator will be
     *            created
     */
    ThreadSafeIterator(ThreadSafeSet<Y> set)
    {
        this.innerIterator = set.unsafeIterator();
        this.referenceCouter = set.getInnerSet().getReferenceCounterReference();
        checkHasNext();
    }

    private void checkHasNext()
    {
        if (!innerIterator.hasNext())
        {
            referenceCouter.decrementAndGet();
            innerIterator = null;
        }
    }

    /** {@inheritDoc} */
    @Override
    public boolean hasNext()
    {
        return innerIterator != null;
    }

    /** {@inheritDoc} */
    @Override
    /**
     * When iterator reaches end of collection it decrements
     * reference counter and closes all references to
     * collection. Next use of iterator will cause
     * UnsupportedOperationException
     */
    public Y next()
    {
        if (innerIterator == null)
        {
            throw new UnsupportedOperationException("Reached end of collection");
        }

        Y result = innerIterator.next();

        checkHasNext();

        return result;
    }

    /** {@inheritDoc} */
    @Override
    /**
     * ThreadSafeIterator does not allow to modify
     * collection - function always throws
     * UsupportedOperationException
     */
    public void remove()
    {
        throw new UnsupportedOperationException("Not allowed to modify collection");
    }

}
