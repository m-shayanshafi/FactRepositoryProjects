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
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

/**
 * Class which follows singleton design pattern (only one
 * instance of this class can exist). It is one interface to
 * produce two types of collections - normal and
 * thread-safe. The type of collection can be set only in by
 * init() method - if type is not set null will be returned
 * from getFactory() method
 */
public final class CollectionFactory
{
    /**
     * Enumerator indicating type of collection produced by
     * CollectonFactory.
     */
    public enum CollectionType
    {
        /**
         * Collection factory will generate normal
         * collections (remark that they may fail while
         * using in multithread environment).
         */
        Normal,
        /**
         * Collection factory will generate thread safe
         * collections (remark that they are slower than
         * normal ones).
         */
        ThreadSafe
    }

    private static final String WRONG_COLLECTION_TYPE = "Wrong collection type";

    private static CollectionFactory instance;

    private CollectionType mType;

    /**
     * Default constructor is private - see singleton
     * pattern
     * @param type
     *            type of collection
     */
    private CollectionFactory(CollectionType type)
    {
        this.mType = type;
    }

    /**
     * Static method used to get instance of factory.
     * @return instance of factory
     */
    public static CollectionFactory getFactory()
    {
        return instance;
    }

    /**
     * Static method used to initialize factory or change
     * it's type.
     * @param type
     *            type of factory
     * @return instance of factory
     */
    public static CollectionFactory init(CollectionType type)
    {
        if (instance == null || instance.mType != type)
        {
            instance = new CollectionFactory(type);
        }
        return instance;
    }

    /**
     * HashMap product of factory.
     * @param <K>
     *            type of map key element
     * @param <V>
     *            type of map value element
     * @return initialized hash map - thread-safe or not
     */
    public <K, V> Map<K, V> createHashMap()
    {
        if (mType == CollectionType.Normal)
        {
            return new HashMap<K, V>();
        }
        else if (mType == CollectionType.ThreadSafe)
        {
            return new ThreadSafeMap<K, V>();
        }
        else
        {
            throw new UnsupportedOperationException(WRONG_COLLECTION_TYPE);
        }
    }

    /**
     * Set based on HashSet product.
     * @param <T>
     *            type of set's elements
     * @return initialized set - thread-safe or not
     */
    public <T> Set<T> createHashSet()
    {
        if (mType == CollectionType.Normal)
        {
            return new HashSet<T>();
        }
        else if (mType == CollectionType.ThreadSafe)
        {
            return new ThreadSafeSet<T>();
        }
        else
        {
            throw new UnsupportedOperationException(WRONG_COLLECTION_TYPE);
        }
    }

    /**
     * Collection based on LinkedList product.
     * @param <T>
     *            type of collection's elements
     * @return initialized collection - thread-safe or not
     */
    public <T> Collection<T> createListCollection()
    {
        if (mType == CollectionType.Normal)
        {
            return new LinkedList<T>();
        }
        else if (mType == CollectionType.ThreadSafe)
        {
            return new ThreadSafeCollection<T>();
        }
        else
        {
            throw new UnsupportedOperationException(WRONG_COLLECTION_TYPE);
        }
    }

    /**
     * Gets type of collections produced by this factory.
     * @return type of collections produced by
     *         CollectionFactory
     */
    public CollectionType getType()
    {
        return mType;
    }
}
