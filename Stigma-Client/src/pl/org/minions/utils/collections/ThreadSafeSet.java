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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ThreadSefeSet is extension to normal set, the way it is
 * created do not allow to modify instance of set that is
 * already referenced (by ThreadSafeIterator). If access to
 * referenced set is needed, set makes copy of itself (old
 * references are not changed) and all changes are made to
 * that copy.
 * @param <T>
 *            type stored in collection
 */
public class ThreadSafeSet<T> implements Set<T>
{
    /**
     * Internal set, keeps normal Set&lt;T&gt; instance and
     * reference counter.
     */
    class InnerThreadSafeSet<Z>
    {
        private AtomicInteger referenceCounter = new AtomicInteger(0);
        private Set<Z> set;

        /**
         * Constructor. Creates empty set.
         */
        public InnerThreadSafeSet()
        {
            set = new HashSet<Z>();
        }

        /**
         * Wrapping constructor.Wraps given constructor.
         * @param c
         *            set to wrap
         */
        public InnerThreadSafeSet(Set<Z> c)
        {
            set = c;
        }

        /**
         * Clones set.
         * @return copy of the collection
         */
        public InnerThreadSafeSet<Z> copy()
        {
            InnerThreadSafeSet<Z> copy = new InnerThreadSafeSet<Z>();
            copy.set = new HashSet<Z>();
            copy.set.addAll(set);
            return copy;
        }

        /**
         * Decrements counter of references.
         */
        public void decrementReferenceCounter()
        {
            referenceCounter.decrementAndGet();
        }

        /**
         * Returns "reference" to reference counter. (One
         * element in table).
         * @return "reference" to reference counter
         */
        public AtomicInteger getReferenceCounterReference()
        {
            return referenceCounter;
        }

        /**
         * Returns reference counter.
         * @return reference counter
         */
        public int getReferenceCounterValue()
        {
            return referenceCounter.get();
        }

        /**
         * Returns wrapped set.
         * @return wrapped set
         */
        public Set<Z> getSet()
        {
            return set;
        }

        /**
         * Increments counter of references.
         */
        public void incrementReferenceCounter()
        {
            referenceCounter.incrementAndGet();
        }
    };

    private InnerThreadSafeSet<T> innerSet;

    /**
     * Default constructor.
     */
    public ThreadSafeSet()
    {
        innerSet = new InnerThreadSafeSet<T>();
    }

    /**
     * Constructor which makes thread-safe set from other
     * set.
     * @param set
     *            base set
     */
    public ThreadSafeSet(Set<T> set)
    {
        innerSet = new InnerThreadSafeSet<T>(set);
    }

    /** {@inheritDoc} */
    @Override
    public synchronized boolean add(T o)
    {
        checkForCopy();
        return innerSet.set.add(o);
    }

    /** {@inheritDoc} */
    @Override
    public synchronized boolean addAll(Collection<? extends T> c)
    {
        checkForCopy();
        return innerSet.set.addAll(c);
    }

    private void checkForCopy()
    {
        if (innerSet.getReferenceCounterValue() > 0)
        {
            innerSet = innerSet.copy();
        }
    }

    /** {@inheritDoc} */
    @Override
    public synchronized void clear()
    {
        checkForCopy();
        innerSet.set.clear();
    }

    /** {@inheritDoc} */
    @Override
    public boolean contains(Object o)
    {
        return innerSet.set.contains(o);
    }

    /** {@inheritDoc} */
    @Override
    public boolean containsAll(Collection<?> c)
    {
        return innerSet.set.containsAll(c);
    }

    /**
     * Gets inner set - visible only in package scope, used
     * by ThreadSafeIterator.
     * @return inner collection
     */
    InnerThreadSafeSet<T> getInnerSet()
    {
        return innerSet;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isEmpty()
    {
        return innerSet.set.isEmpty();
    }

    /** {@inheritDoc} */
    @Override
    /**
     * Returns thread safe iterator which does not allow to
     * modify collection
     * @return ThreadSafeIterator
     */
    public synchronized Iterator<T> iterator()
    {
        innerSet.incrementReferenceCounter();
        return new ThreadSafeIterator<T>(this);
    }

    /** {@inheritDoc} */
    @Override
    public synchronized boolean remove(Object o)
    {
        checkForCopy();
        return innerSet.set.remove(o);
    }

    /** {@inheritDoc} */
    @Override
    public synchronized boolean removeAll(Collection<?> c)
    {
        checkForCopy();
        return innerSet.set.removeAll(c);
    }

    /** {@inheritDoc} */
    @Override
    public synchronized boolean retainAll(Collection<?> c)
    {
        checkForCopy();
        return innerSet.set.retainAll(c);
    }

    /** {@inheritDoc} */
    @Override
    public synchronized int size()
    {
        return innerSet.set.size();
    }

    /** {@inheritDoc} */
    @Override
    public Object[] toArray()
    {
        return innerSet.set.toArray();
    }

    /** {@inheritDoc} */
    @Override
    public <Z> Z[] toArray(Z[] a)
    {
        return innerSet.set.toArray(a);
    }

    /**
     * Returns normal iterator for set. Use with extreme
     * caution.
     * @return unsafe, classic, Iterator.
     */
    public Iterator<T> unsafeIterator()
    {
        return innerSet.set.iterator();
    }
}
