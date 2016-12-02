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

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ThreadSefeCollection is extension to normal collection,
 * the way it is created do not allow to modify instance of
 * collection that is already referenced (by
 * ThreadSafeIterator). If access to referenced collection
 * is needed collection makes copy of itself (old references
 * are not changed) and all changes are made to that copy.
 * @param <T>
 *            type stored in collection
 */
public class ThreadSafeCollection<T> implements Collection<T>
{
    /**
     * Internal collection, keeps normal Collection&lt;T&gt;
     * instance and reference counter.
     */
    class InnerThreadSafeCollection<Z>
    {
        private AtomicInteger refCount = new AtomicInteger(0);
        private Collection<Z> collection;

        /**
         * Constructor. Creates empty collection.
         */
        public InnerThreadSafeCollection()
        {
            collection = new LinkedList<Z>();
        }

        /**
         * Constructor. Wraps given collection.
         * @param c
         *            collection to wrap
         */
        public InnerThreadSafeCollection(Collection<Z> c)
        {
            collection = c;
        }

        /**
         * Clones collection.
         * @return copy of the collection
         */
        public InnerThreadSafeCollection<Z> copy()
        {
            InnerThreadSafeCollection<Z> copy =
                    new InnerThreadSafeCollection<Z>();
            copy.collection = new LinkedList<Z>();
            copy.collection.addAll(collection);
            return copy;
        }

        /**
         * Decrements counter of references.
         */
        public void decrementReferenceCounter()
        {
            refCount.decrementAndGet();
        }

        /**
         * Returns wrapped collection.
         * @return wrapped collection
         */
        public Collection<Z> getCollection()
        {
            return collection;
        }

        /**
         * Returns reference counter object. For use by
         * iterators to decrement when detached.
         * @return reference counter object
         */
        public AtomicInteger getReferenceCounterReference()
        {
            return refCount;
        }

        /**
         * Returns reference counter.
         * @return reference counter
         */
        public int getReferenceCounterValue()
        {
            return refCount.get();
        }

        /**
         * Increments counter of references.
         */
        public void incrementReferenceCounter()
        {
            refCount.incrementAndGet();
        }
    };

    private InnerThreadSafeCollection<T> innerCollection;

    /**
     * Default constructor.
     */
    public ThreadSafeCollection()
    {
        innerCollection = new InnerThreadSafeCollection<T>();
    }

    /**
     * Constructor which makes thread-safe collection from
     * other collection.
     * @param collection
     *            base collection
     */
    public ThreadSafeCollection(Collection<T> collection)
    {
        innerCollection = new InnerThreadSafeCollection<T>(collection);
    }

    /** {@inheritDoc} */
    @Override
    public synchronized boolean add(T o)
    {
        checkForCopy();
        return innerCollection.collection.add(o);
    }

    /** {@inheritDoc} */
    @Override
    public synchronized boolean addAll(Collection<? extends T> c)
    {
        checkForCopy();
        return innerCollection.collection.addAll(c);
    }

    private synchronized void checkForCopy()
    {
        if (innerCollection.getReferenceCounterValue() > 0)
        {
            innerCollection = innerCollection.copy();
        }
    }

    /** {@inheritDoc} */
    @Override
    public synchronized void clear()
    {
        checkForCopy();
        innerCollection.collection.clear();
    }

    /** {@inheritDoc} */
    @Override
    public boolean contains(Object o)
    {
        return innerCollection.collection.contains(o);
    }

    /** {@inheritDoc} */
    @Override
    public boolean containsAll(Collection<?> c)
    {
        return innerCollection.collection.containsAll(c);
    }

    /**
     * Gets inner collection - visible only in package
     * scope, used by ThreadSafeIterator.
     * @return inner collection
     */
    InnerThreadSafeCollection<T> getInnerCollection()
    {
        return innerCollection;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isEmpty()
    {
        return innerCollection.collection.isEmpty();
    }

    /**
     * Returns thread safe iterator which does not allow to
     * modify collection.
     * @return {@link ThreadSafeIterator}
     */
    @Override
    public synchronized Iterator<T> iterator()
    {
        innerCollection.incrementReferenceCounter();
        return new ThreadSafeIterator<T>(this);
    }

    /** {@inheritDoc} */
    @Override
    public synchronized boolean remove(Object o)
    {
        checkForCopy();
        return innerCollection.collection.remove(o);
    }

    /** {@inheritDoc} */
    @Override
    public synchronized boolean removeAll(Collection<?> c)
    {
        checkForCopy();
        return innerCollection.collection.removeAll(c);
    }

    /** {@inheritDoc} */
    @Override
    public synchronized boolean retainAll(Collection<?> c)
    {
        checkForCopy();
        return innerCollection.collection.retainAll(c);
    }

    /** {@inheritDoc} */
    @Override
    public synchronized int size()
    {
        return innerCollection.collection.size();
    }

    /** {@inheritDoc} */
    @Override
    public Object[] toArray()
    {
        return innerCollection.collection.toArray();
    }

    /** {@inheritDoc} */
    @Override
    public <Z> Z[] toArray(Z[] a)
    {
        return innerCollection.collection.toArray(a);
    }

    /**
     * Returns normal iterator for collection. Use with
     * extreme caution.
     * @return unsafe, classic, Iterator.
     */
    public Iterator<T> unsafeIterator()
    {
        return innerCollection.collection.iterator();
    }
}
