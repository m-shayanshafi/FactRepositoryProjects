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
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * Collection optimized for {@link #addAll(Collection)}
 * operation and iterating over so created collection. Other
 * operations may be not optimal.
 * <p>
 * <b>IMPORTANT</b> {@link #addAll(Collection)} takes
 * ownership.
 * @param <T>
 *            object stored in collection
 */
public class SuperSet<T> implements Collection<T>
{
    private LinkedList<Collection<T>> list = new LinkedList<Collection<T>>();

    private class SuperSetIterator implements Iterator<T>
    {
        private Iterator<Collection<T>> listIterator;
        private Iterator<T> colIterator;

        public SuperSetIterator(Iterator<Collection<T>> listIterator)
        {
            this.listIterator = listIterator;
            if (listIterator.hasNext())
                this.colIterator = listIterator.next().iterator();
            else
                this.colIterator = null;
        }

        /** {@inheritDoc} */
        @Override
        public boolean hasNext()
        {
            return colIterator != null && colIterator.hasNext()
                || listIterator.hasNext();
        }

        /** {@inheritDoc} */
        @Override
        public T next()
        {
            if (colIterator == null || !colIterator.hasNext())
            {
                if (!listIterator.hasNext())
                    throw new NoSuchElementException();
                colIterator = listIterator.next().iterator();
            }
            return colIterator.next();
        }

        /** {@inheritDoc} */
        @Override
        public void remove()
        {
            if (colIterator == null)
                throw new IllegalStateException();
            colIterator.remove();
        }
    }

    /**
     * Constructor.
     * @param initial
     *            initial contents
     */
    public SuperSet(Collection<T> initial)
    {
        this();
        addAll(initial);
    }

    /**
     * Constructor.
     */
    public SuperSet()
    {
    }

    /** {@inheritDoc} */
    @Override
    public boolean add(T e)
    {
        if (list.isEmpty())
        {
            Set<T> set = new HashSet<T>();
            set.add(e);
            return list.add(set);
        }

        Collection<T> collection = list.getFirst();
        return collection.add(e);
    }

    /**
     * {@inheritDoc}.
     * <p>
     * <b>IMPORTANT</b> Takes ownership of collection.
     */
    @SuppressWarnings("unchecked")
    @Override
    public boolean addAll(Collection<? extends T> c)
    {
        if (c == null || c.isEmpty())
            return false;

        return list.add((Collection<T>) c);
    }

    /** {@inheritDoc} */
    @Override
    public void clear()
    {
        list.clear();
    }

    /** {@inheritDoc} */
    @Override
    public boolean contains(Object o)
    {
        for (Collection<T> col : list)
            if (col.contains(o))
                return true;
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean containsAll(Collection<?> c)
    {
        for (Object obj : c)
            if (!contains(obj))
                return false;
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isEmpty()
    {
        return list.isEmpty();
    }

    /** {@inheritDoc} */
    @Override
    public Iterator<T> iterator()
    {
        return new SuperSetIterator(list.iterator());
    }

    /** {@inheritDoc} */
    @Override
    public boolean remove(Object o)
    {
        for (Collection<T> c : list)
            if (c.remove(o))
                return true;
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean removeAll(Collection<?> c)
    {
        boolean changed = false;
        for (Object o : c)
            changed = remove(o) || changed;
        return changed;
    }

    /** {@inheritDoc} */
    @Override
    public boolean retainAll(Collection<?> c)
    {
        boolean changed = false;
        for (Object o : c)
            if (!contains(o))
                changed = remove(o) || changed;
        return changed;
    }

    /** {@inheritDoc} */
    @Override
    public int size()
    {
        int ret = 0;
        for (Collection<T> c : list)
            ret += c.size();
        return ret;
    }

    /** {@inheritDoc} */
    @Override
    public Object[] toArray()
    {
        return toArray(new Object[size()]);
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    @Override
    public <E> E[] toArray(E[] a)
    {
        int size = size();
        if (a.length < size)
            a =
                    (E[]) java.lang.reflect.Array.newInstance(a.getClass()
                                                               .getComponentType(),
                                                              size);

        Object[] result = a;
        int i = 0;
        for (T obj : this)
            result[i++] = obj;

        if (a.length > size)
            a[size] = null;
        return a;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return list.toString();
    }
}
